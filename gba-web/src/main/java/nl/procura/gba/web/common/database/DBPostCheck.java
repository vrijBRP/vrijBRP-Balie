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

import javax.persistence.EntityManager;

import nl.procura.gba.web.common.database.checks.DBCheckPost1;
import nl.procura.gba.web.common.database.checks.DBCheckPost2;
import nl.procura.gba.web.common.database.checks.DBCheckPost3;
import nl.procura.gba.web.common.database.checks.DBCheckPost4;
import nl.procura.gba.web.common.database.checks.DBCheckPost5;
import nl.procura.gba.web.common.database.checks.DBCheckPost6;
import nl.procura.gba.web.common.database.checks.DBCheckPost7;
import nl.procura.gba.web.common.database.checks.DBCheckPost8;

import liquibase.database.Database;

public class DBPostCheck {

  private static final String TYPE = "  ná";

  public DBPostCheck(EntityManager entityManager, Database database) {
    new DBCheckPost1(entityManager, database, TYPE).execute();
    new DBCheckPost2(entityManager, database, TYPE).execute();
    new DBCheckPost3(entityManager, database, TYPE).execute();
    new DBCheckPost4(entityManager, database, TYPE).execute();
    new DBCheckPost5(entityManager, database, TYPE).execute();
    new DBCheckPost6(entityManager, database, TYPE).execute();
    new DBCheckPost7(entityManager, database, TYPE).execute();
    new DBCheckPost8(entityManager, database, TYPE).execute();
    // new DBCheckPost9(entityManager, database, TYPE).execute(); // Disabled for now. Too slow
  }
}
