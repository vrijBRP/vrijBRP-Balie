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

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personen.db.VogDoelTab;
import nl.procura.gba.web.common.database.DBCheckTemplateLb;

import liquibase.database.Database;

/**
 * Check waarbij de ontbrekende COVOG tabellen worden toegevoegd
 * <p>
 * Doel van de aanvraag
 */
public class DBCheckPost2 extends DBCheckTemplateLb {

  public DBCheckPost2(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "ontbrekende covog tabellen (doelen)");
  }

  @Override
  public void init() {

    if (isOracle()) {
      add(" ", " ", -1, -1);
    } else {
      add("", "", -1, -1);
    }

    add("IVB", "Integriteitsverklaring Beroepsvervoer", 20090501, 20111203);
    add("OVG", "Overig", 20030101, -1);
    add("VIS", "Visumaanvragen of emigratie naar niet-EU lidstaten", 20030101, 20090501);
    add("WRE", "Werkrelatie", 20030101, -1);
  }

  private void add(String code, String oms, int dIn, int dEnd) {

    TypedQuery<VogDoelTab> query = getEntityManager().createQuery("select d from VogDoelTab d", VogDoelTab.class);
    for (VogDoelTab rd : query.getResultList()) {
      if (astr(rd.getCVogDoelTab()).trim().equals(astr(code).trim())) {
        rd.setOms(oms);
        rd.setDIn(toBigDecimal(dIn));
        rd.setDEnd(toBigDecimal(dEnd));
        getEntityManager().merge(rd);
        return;
      }
    }

    VogDoelTab rd = new VogDoelTab();
    rd.setCVogDoelTab(code);
    rd.setOms(oms);
    rd.setDIn(toBigDecimal(dIn));
    rd.setDEnd(toBigDecimal(dEnd));
    getEntityManager().merge(rd);
    count(1);
  }
}
