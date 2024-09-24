/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gbaws.db.misc;

import static nl.procura.commons.liquibase.utils.LbConnectionUtils.getConnection;
import static nl.procura.commons.liquibase.utils.LbConnectionUtils.getDatabase;
import static nl.procura.standard.Globalfunctions.aval;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import nl.procura.commons.liquibase.connections.LbDatabaseType;
import nl.procura.commons.liquibase.parameters.LbConnectionParameters;
import nl.procura.commons.liquibase.utils.LbUtils;
import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.config.GbaConfigProperty;
import nl.procura.gbaws.db.misc.database.DBPostCheck;
import nl.procura.gbaws.db.misc.database.DBPreCheck;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.exception.LiquibaseException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseUpdate {

  public DatabaseUpdate(EntityManager entityManager) {

    String database = GbaConfig.get(GbaConfigProperty.DB_NAME);
    String server = GbaConfig.get(GbaConfigProperty.DB_SERVER);
    String port = GbaConfig.get(GbaConfigProperty.DB_PORT);
    String schema = GbaConfig.get(GbaConfigProperty.DB_SCHEMA);
    String sid = GbaConfig.get(GbaConfigProperty.DB_SID);
    String username = GbaConfig.get(GbaConfigProperty.DB_USERNAME);
    String password = GbaConfig.get(GbaConfigProperty.DB_PW);

    final LbConnectionParameters parameters = new LbConnectionParameters();
    parameters.setDatabaseType(LbDatabaseType.get(database));
    parameters.setServer(server);
    parameters.setPort(aval(port));
    parameters.setSchema(schema);
    parameters.setSid(sid);
    parameters.setUsername(username);
    parameters.setPassword(password);

    update(entityManager, getDatabase(getConnection(parameters)));
  }

  private void update(EntityManager entityManager, Database targetDatabase) {

    try {
      unlockLiquibase(entityManager);
      // Pre checks
      new DBPreCheck(entityManager, targetDatabase);

      final String changelog = "liquibase/personenws.changelog.xml";
      final Liquibase liquibase = new Liquibase(changelog, LbUtils.getDefaultAccessor(), targetDatabase);
      liquibase.update(new Contexts());

      // Post checks
      new DBPostCheck(entityManager, targetDatabase);
    } catch (LiquibaseException | RuntimeException e) {
      throw new RuntimeException("Fout tijdens bijwerken van de database.", e);
    }
  }

  private static void unlockLiquibase(EntityManager entityManager) {
    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();
    try {
      String sql = "delete from databasechangeloglock "
          + "where locked = true "
          + "and lockgranted is not null "
          + "and lockgranted < ?1";
      Query nativeQuery = entityManager.createNativeQuery(sql);
      LocalDateTime time = LocalDateTime.now().minusMinutes(5);
      nativeQuery.setParameter(1, time);
      int update = nativeQuery.executeUpdate();
      if (update > 0) {
        log.info("Oude liquibase lock verwijderd");
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
    }
  }
}
