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

import static nl.procura.standard.Globalfunctions.toBigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personen.db.Reisdoc;
import nl.procura.gba.web.common.database.DBCheckTemplateLb;

import liquibase.database.Database;

/**
 * Check waarbij de ontbrekende reisdocumentsoort automatisch worden toegevoegd.
 */
public class DBCheckPost1 extends DBCheckTemplateLb {

  public DBCheckPost1(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "ontbrekende reisdocumenten");
  }

  @Override
  public void init() {

    add("NI", "Nederlandse identiteitskaart", 12);
    add("PB", "Reisdocument voor vreemdelingen", 18);
    add("PN", "Nationaal paspoort", 18);
    add("PV", "Reisdocument voor vluchtelingen", 18);
    add("TE", "Tweede paspoort (zakenpaspoort)", 18);
    add("TN", "Tweede paspoort", 18);
    add("ZN", "Nationaal paspoort met 64 bladzijden (zakenpaspoort)", 18);
    add("PF", "Faciliteitenpaspoort", 0);
  }

  private void add(String zkarg, String reisdoc, int lt) {

    TypedQuery<Reisdoc> query = getEntityManager().createQuery("select d from Reisdoc d where d.zkarg = :zkarg",
        Reisdoc.class);
    query.setParameter("zkarg", zkarg);

    for (Reisdoc rd : query.getResultList()) {
      rd.setReisdoc(reisdoc);
      rd.setLtToest(toBigDecimal(lt));
      getEntityManager().merge(rd);
      return;
    }

    Reisdoc rd = new Reisdoc();
    rd.setZkarg(zkarg);
    rd.setReisdoc(reisdoc);
    rd.setLtToest(toBigDecimal(lt));
    getEntityManager().merge(rd);
    count(1);
  }
}
