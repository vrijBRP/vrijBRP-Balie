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

package nl.procura.gba.web.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.jpa.personen.dao.views.verwijderzaken.VerwijderZaakType;
import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.jpa.personen.utils.GbaEclipseLinkUtil;
import nl.procura.gba.jpa.personen.utils.GbaJpaStorageWrapper;
import nl.procura.gba.web.application.GbaConfigMock;
import nl.procura.gba.web.common.database.checks.DBCheckPost7;
import nl.procura.standard.threadlocal.ThreadLocalStorage;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.changelog.ChangeSet;
import liquibase.database.Database;
import liquibase.database.core.HsqlDatabase;
import liquibase.database.jvm.HsqlConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

public final class TemporaryDatabase {

  private static final Logger        LOGGER = LoggerFactory.getLogger(TemporaryDatabase.class);
  private static TemporaryDatabase   instance;
  private final GbaJpaStorageWrapper gbaJpaStorageWrapper;

  private TemporaryDatabase() {
    gbaJpaStorageWrapper = newGbaJpaStorageWrapper();
    createDatabase();
    updateGeneratorTable(gbaJpaStorageWrapper);
  }

  public static TemporaryDatabase getInstance() {
    if (instance == null) {
      instance = new TemporaryDatabase();
    }

    return instance;
  }

  public static void ensureCleanMockDatabase() {
    GbaConfigMock.setGbaConfig();
    TemporaryDatabase database = TemporaryDatabase.getInstance();
    resetDatabase();
    ThreadLocalStorage.init(database.gbaJpaStorageWrapper);
  }

  private GbaJpaStorageWrapper newGbaJpaStorageWrapper() {
    Properties properties = new Properties();
    properties.setProperty(GbaEclipseLinkUtil.USERNAME, "SA");
    properties.setProperty(GbaEclipseLinkUtil.CUSTOM_URL, "jdbc:hsqldb:mem:tests");
    properties.setProperty(GbaEclipseLinkUtil.CUSTOM_DRIVER, "org.hsqldb.jdbcDriver");
    return new GbaJpaStorageWrapper(properties);
  }

  public static void resetDatabase() {
    LOGGER.info("Reset database");
    try (Connection connection = getConnection()) {
      deleteAllFromTable(connection, getTableName(DossAkte.class));
      deleteAllFromTable(connection, getTableName(ZaakId.class));
      deleteAllFromTable(connection, getTableName(ZaakAttr.class));
      deleteAllFromTable(connection, getTableName(ZaakRel.class));
      Arrays.asList(VerwijderZaakType.values())
          .forEach(type -> deleteAllFromTable(connection,
              getTableName(type.getEntity())));
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  private static String getTableName(Class<? extends BaseEntity> cl) {
    return cl.getAnnotation(Table.class).name();
  }

  // suppress SQL injection warning as the table name comes from the database itself
  @SuppressWarnings("squid:S2077")
  private static void deleteAllFromTable(Connection connection, String tableName) {
    try (Statement statement = connection.createStatement()) {
      statement.execute("delete from " + tableName);
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * To create a database, download Liquibase CLI
   * <p>
   * Create schema: {@code
   * liquibase --url="jdbc:postgresql://localhost:5432/personen" --username="postgres" --password="pw"
   * --changeLogFile=db.init.xml --changeSetAuthor="-" generateChangeLog
   * }
   * <p>
   * Replace:<ul>
   * <li>{@code defaultValueComputed="('-1'::integer)::numeric"} with {@code defaultValueNumeric="-1"}
   * <li>{@code defaultValueComputed="(0)::numeric"} with {@code defaultValueNumeric="-1"}
   * <li>{@code type="BYTEA"} with {@code type="VARBINARY(1000000)"}
   * </ul>
   * <p>
   * To export data from a single table:
   * <p>
   * {@code liquibase --url="jdbc:postgresql://localhost:5432/personen" --username="postgres" --password="pw"
   * --diffTypes="data" --changeLogFile=db.tablename.xml --changeSetAuthor="-"
   * generateChangeLog --includeObjects tablename
   * }
   *
   */
  private void createDatabase() {
    LOGGER.info("Create database");
    try (Connection connection = getConnection()) {
      HsqlDatabase hsqlDatabase = getHsqlDatabase(connection);
      // initial database for version 1.18
      updateDatabase(hsqlDatabase, "db.init.xml");
      // make liquibase think we've applied all updates until version 1.18
      syncDatabase(hsqlDatabase, "db.sync-1.18.xml");
      // update database with changes after version 1.18
      updateDatabase(hsqlDatabase, "liquibase/personen.changelog.xml");
      // add initial data
      updateDatabase(hsqlDatabase, "db.serial.xml");
      updateDatabase(hsqlDatabase, "db.usr.xml");
      updateDatabase(hsqlDatabase, "db.location.xml");
      updateDatabase(hsqlDatabase, "db.profile.xml");
      updateDatabase(hsqlDatabase, "db.parm.xml");
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  private static void updateGeneratorTable(GbaJpaStorageWrapper gbaJpaStorageWrapper) {
    LOGGER.info("Update generator table");
    try (Connection connection = getConnection()) {
      HsqlDatabase hsqlDatabase = getHsqlDatabase(connection);
      new DBCheckPost7(gbaJpaStorageWrapper.get().getManager(), hsqlDatabase, "");
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  public static HsqlDatabase getHsqlDatabase(Connection connection) {
    HsqlConnection hsqlConnection = new HsqlConnection(connection);
    HsqlDatabase hsqlDatabase = new HsqlDatabase();
    hsqlDatabase.setConnection(hsqlConnection);
    return hsqlDatabase;
  }

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:hsqldb:mem:tests", "SA", "");
  }

  public static List<ChangeSet> getChangeSets(Database hsqlDatabase, String changeLogFile) {
    try {
      Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), hsqlDatabase);
      return liquibase.getDatabaseChangeLog().getChangeSets();
    } catch (LiquibaseException e) {
      throw new IllegalStateException(e);
    }
  }

  private void updateDatabase(Database hsqlDatabase, String changeLogFile) {
    LOGGER.info("Update database with {}", changeLogFile);
    try {
      Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), hsqlDatabase);
      liquibase.update(new Contexts());
    } catch (LiquibaseException e) {
      throw new IllegalStateException(e);
    }
  }

  private void syncDatabase(Database hsqlDatabase, String changeLogFile) {
    try {
      Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), hsqlDatabase);
      liquibase.changeLogSync(new Contexts(), new LabelExpression());
    } catch (LiquibaseException e) {
      throw new IllegalStateException(e);
    }
  }
}
