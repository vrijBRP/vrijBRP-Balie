/*
 * Copyright 2021 - 2022 Procura B.V.
 *
 * In licentie gegeven krachtens de EUPL, versie 1.2
 * U mag dit werk niet gebruiken, behalve onder de voorwaarden van de licentie.
 * U kunt een kopie van de licentie vinden op:
 *
 *   https://github.com/vrijBRP/vrijBRP/blob/master/LICENSE.md
 *
 * Deze bevat zowel de Nederlandse als de Engelse tekst
 *
 * Tenzij dit op grond van toepasselijk recht vereist is of schriftelijk
 * is overeengekomen, wordt software krachtens deze licentie verspreid
 * "zoals deze is", ZONDER ENIGE GARANTIES OF VOORWAARDEN, noch expliciet
 * noch impliciet.
 * Zie de licentie voor de specifieke bepalingen voor toestemmingen en
 * beperkingen op grond van de licentie.
 */

package nl.procura.gba.jpa.personen.dao;

import static nl.procura.standard.exceptions.ProExceptionType.SELECT;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.jpa.personen.db.Sel;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.standard.exceptions.ProException;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SelDao extends GenericDao {

  /**
   * Find the sel records
   */
  public static final List<Sel> findSels() {
    return createQuery("select sel from Sel sel order by sel.cSel", Sel.class).getResultList();
  }

  /**
   * Returns the result from the readonly query
   */
  public static Result getFromStatement(String statement) {

    RuntimeException exception = null;
    EntityManager em = GbaJpa.createManager(GbaConfig.getProperties());
    Result result = new Result();

    if (em.isOpen()) {
      em.getTransaction().begin();
      Connection conn = em.unwrap(Connection.class);
      try {
        try {
          conn.setReadOnly(true);
          // No updates or changes allowed
          try (PreparedStatement ps = conn.prepareStatement(statement)) {
            try (ResultSet rs = ps.executeQuery()) {
              ResultSetMetaData rsmd = rs.getMetaData();
              for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                result.getColumns().add(rsmd.getColumnName(i));
              }
              while (rs.next()) {
                Row row = new Row();
                result.getRows().add(row);
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                  Value value = new Value();
                  value.setName(rsmd.getColumnName(i));
                  value.setValue(rs.getString(i));
                  row.getValues().add(value);
                }
              }
            }
          }
        } catch (SQLException e) {
          exception = new ProException(SELECT, "Fout bij zoeken in de database", e);
        } finally {
          try {
            em.getTransaction().rollback();
          } finally {
            em.close();
          }
        }
      } finally {
        try {
          conn.setReadOnly(false);
        } catch (SQLException e) {
          exception = new ProException(SELECT, "Fout bij zoeken in de database", e);
        }
      }
    }
    if (exception != null) {
      throw exception;
    }
    return result;
  }

  public static List<Sel> findByName(String name) {
    TypedQuery<Sel> query = createQuery("select s from Sel s where s.sel = :selname", Sel.class);
    query.setParameter("selname", name);
    return query.getResultList();
  }

  @Data
  public static class Result {

    private List<String> columns = new ArrayList<>();
    private List<Row>    rows    = new ArrayList<>();

    public boolean isColumn(String name) {
      return getColumns().stream().anyMatch(column -> column.equalsIgnoreCase(name));
    }
  }

  @Data
  public static class Row {

    private List<Value> values = new ArrayList<>();

    public boolean hasValue(String name) {
      return getValues().stream().anyMatch(val -> val.getName().equalsIgnoreCase(name));
    }

    public boolean hasValue(String name, String value) {
      return getValues().stream()
          .anyMatch(val -> val.getName().equalsIgnoreCase(name) && val.getValue().equalsIgnoreCase(value));
    }

    public Value getValue(String name) {
      return getValues().stream().filter(val -> val.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
  }

  @Data
  public static class Value {

    private String name  = "";
    private String value = "";
  }
}
