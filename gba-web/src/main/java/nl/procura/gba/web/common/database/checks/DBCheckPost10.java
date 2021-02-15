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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personen.db.DossAkte;
import nl.procura.gba.web.common.database.DBCheckTemplateLb;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort;

import liquibase.database.Database;

/**
 * Update datum feit van de erkenningen in de klapper
 */
public class DBCheckPost10 extends DBCheckTemplateLb {

  public DBCheckPost10(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "Klappers bijwerken");
  }

  @Override
  public void init() {
    String sql = "select d from DossAkte d where d.registersoort = :rs and d.dIn > 0 and d.dFeit <= 0";
    TypedQuery<DossAkte> query = getEntityManager().createQuery(sql, DossAkte.class);
    query.setParameter("rs", DossierAkteRegistersoort.AKTE_ERKENNING_NAAMSKEUZE.getCode());

    List<DossAkte> resultList = query.getResultList();
    for (DossAkte dossAkte : resultList) {
      dossAkte.setdFeit(dossAkte.getdIn());
      getEntityManager().merge(dossAkte);
    }
    count(resultList.size());
  }
}
