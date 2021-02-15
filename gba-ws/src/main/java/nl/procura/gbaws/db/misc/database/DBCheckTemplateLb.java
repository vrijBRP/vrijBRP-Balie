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

package nl.procura.gbaws.db.misc.database;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pad_right;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.commons.liquibase.utils.LbConnectionUtils;

import liquibase.database.Database;

public abstract class DBCheckTemplateLb {

  protected static final String DATABASECHANGELOGLOCK = "DATABASECHANGELOGLOCK";
  protected static final String DATABASECHANGELOG     = "DATABASECHANGELOG";
  private final static Logger   LOGGER                = LoggerFactory.getLogger(DBCheckTemplateLb.class.getName());
  private final EntityManager   entityManager;
  private final Database        database;
  private int                   count                 = 0;
  private String                type;

  public DBCheckTemplateLb(EntityManager entityManager, Database database, String type, String oms) {

    this.entityManager = entityManager;
    this.database = database;
    this.type = type;

    try {
      entityManager.getTransaction().begin();
      init();
      entityManager.getTransaction().commit();
      database.commit();
    } catch (Exception e) {
      e.printStackTrace();
      error("Database actie mislukt: " + e.getMessage());
    } finally {
      if (fil(type)) {
        formatInfo(type, getClass().getSimpleName(), oms, count);
      }
    }
  }

  protected int update(String sql) throws SQLException {
    try (Statement st = LbConnectionUtils.getNativeSqlConnection(getDatabase()).createStatement()) {
      return st.executeUpdate(sql);
    }
  }

  protected int count(String sql) throws SQLException {
    Connection connection = LbConnectionUtils.getNativeSqlConnection(getDatabase());
    try (Statement st = connection.createStatement()) {
      try (ResultSet rs = st.executeQuery(sql)) {
        if (rs.next()) {
          return rs.getInt(1);
        }

        return 0;
      }
    }
  }

  protected boolean isOracle() {
    return "oracle".equalsIgnoreCase(getDatabase().getDatabaseProductName());
  }

  public void count(int count, String msg) {
    count(count);
    if (count > 0) {
      formatInfo(type, getClass().getSimpleName(), msg, count);
    }
  }

  private void formatInfo(String type, String className, String descr, int nr) {
    info(String.format("%s - %s - %s %3d", pad_right(type, " ", 4), pad_right(className, " ", 12),
        pad_right(descr, ".", 60), nr));
  }

  public void count(int nr) {
    this.count += nr;
  }

  public abstract void init();

  private void info(String info) {
    LOGGER.info(info);
  }

  private void error(String info) {
    LOGGER.error(info);
  }

  public Database getDatabase() {
    return database;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }
}
