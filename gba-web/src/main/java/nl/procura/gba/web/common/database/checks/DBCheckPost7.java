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

package nl.procura.gba.web.common.database.checks;

import java.sql.SQLException;

import javax.persistence.EntityManager;

import nl.procura.gba.web.common.database.DBCheckTemplateLb;

import liquibase.database.Database;

/**
 * Niet-gebruikte klapperrecords
 */
public class DBCheckPost7 extends DBCheckTemplateLb {

  public DBCheckPost7(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "Niet gebruikte klapperrecords verwijderen");
  }

  @Override
  public void init() throws SQLException {
    String sql = "delete from doss_akte where c_doss is null and jaar <= 0 and vnr <= 0";
    count(update(sql));
  }
}
