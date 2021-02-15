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

import static nl.procura.standard.Globalfunctions.pos;

import java.sql.SQLException;

import javax.persistence.EntityManager;

import nl.procura.gba.jpa.personen.db.AantekeningInd;
import nl.procura.gba.web.common.database.DBCheckTemplateLb;

import liquibase.database.Database;

/**
 * Locatie update
 */
public class DBCheckPre3 extends DBCheckTemplateLb {

  public DBCheckPre3(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "Aantekening tabel corrigeren 1/2");
  }

  @Override
  public void init() throws SQLException {
    checkAantekeningIndicatie();
  }

  private void checkAantekeningIndicatie() throws SQLException {

    String sql = "select count(*) from aantekening_ind where c_aantekening_ind = 1000";

    if (pos(count(sql))) {

      AantekeningInd a = new AantekeningInd();
      a.setCAantekeningInd(1000L);
      a.setProbev("");
      a.setButton("Aantekening");
      a.setOms("Vrije aantekening Proweb");
      getEntityManager().merge(a);
      count(1);
    }
  }
}
