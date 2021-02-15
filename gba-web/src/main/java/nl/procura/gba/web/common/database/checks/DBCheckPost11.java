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

import nl.procura.gba.jpa.personen.db.DossPer;
import nl.procura.gba.web.common.database.DBCheckTemplateLb;

import liquibase.database.Database;

public class DBCheckPost11 extends DBCheckTemplateLb {

  public DBCheckPost11(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "Naamgebruik bijwerken bij huwelijkspartners");
  }

  @Override
  public void init() {
    String sql = "select d from DossPer d where d.bsn > 0 and d.typePersoon in (70,71) and (d.ng is null or d.ng = '')";
    TypedQuery<DossPer> query = getEntityManager().createQuery(sql, DossPer.class);

    List<DossPer> resultList = query.getResultList();
    for (DossPer dossPer : resultList) {
      dossPer.setNg("E");
      getEntityManager().merge(dossPer);
    }
    count(resultList.size());
  }
}
