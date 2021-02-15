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

package nl.procura.gba.jpa.personen.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personen.db.Koppelenum;

public class KoppelenumDao extends GenericDao {

  public static final List<Koppelenum> find() {
    return createQuery("select x from Koppelenum x order by x.cKoppelenum", Koppelenum.class).getResultList();
  }

  public static Collection<?> findKoppelenumen(long cDocument) {

    StringBuilder sql = new StringBuilder();
    sql.append("select x from Koppelenum x ");
    sql.append("left join x.documents d where (d.cDocument = :cDocument) ");
    sql.append("order by x.cKoppelenum");

    TypedQuery<Koppelenum> query = createQuery(sql.toString(), Koppelenum.class);
    query.setParameter("cDocument", cDocument);

    return query.getResultList();
  }
}
