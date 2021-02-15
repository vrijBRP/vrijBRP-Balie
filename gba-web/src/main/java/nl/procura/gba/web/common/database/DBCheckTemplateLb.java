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

package nl.procura.gba.web.common.database;

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

  private final static Logger LOGGER = LoggerFactory.getLogger(DBCheckTemplateLb.class.getName());
  private final EntityManager entityManager;
  private final Database      database;
  private final String        type;
  private int                 count  = 0;

  public DBCheckTemplateLb(EntityManager entityManager, Database database, String type, String oms) {

    this.entityManager = entityManager;
    this.database = database;
    this.type = type;

    long startTime = System.currentTimeMillis();
    try {
      entityManager.getTransaction().begin();
      init();
      entityManager.getTransaction().commit();
      database.commit();
    } catch (Exception e) {
      LOGGER.error("Error!", e);
      error("Database actie mislukt: " + e.getMessage());
    } finally {
      if (fil(type)) {
        long endTime = System.currentTimeMillis();
        formatInfo(type, getClass().getSimpleName(), oms, count, endTime - startTime);
      }
    }
  }

  public void count(int nr) {
    this.count += nr;
  }

  public void count(int count, String msg) {
    count(count);
    if (count > 0) {
      formatInfo(type, getClass().getSimpleName(), msg, count, 0);
    }
  }

  public Database getDatabase() {
    return database;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

  public abstract void init() throws SQLException;

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

  protected int update(String sql) throws SQLException {
    try (Statement st = LbConnectionUtils.getNativeSqlConnection(getDatabase()).createStatement()) {
      return st.executeUpdate(sql);
    }
  }

  protected int nativeUpdate(String sql) {
    return getEntityManager().createNativeQuery(sql).executeUpdate();
  }

  private void error(String info) {
    LOGGER.error(info);
  }

  private void formatInfo(String type, String className, String descr, int nr, long duration) {
    StringBuilder msg = new StringBuilder();
    msg.append(pad_right(type, " ", 4));
    msg.append(" - ");
    msg.append(pad_right(className, " ", 13));
    msg.append(" - ");
    msg.append(pad_right(descr, ".", 60));
    msg.append(String.format("%3d", nr));
    if (duration > 0) {
      msg.append(" (");
      msg.append(duration);
      msg.append(" ms.)");
    }

    info(msg.toString());
  }

  private void info(String info) {
    LOGGER.info(info);
  }
}
