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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personen.db.UsrInfo;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class UsrInfoDao extends GenericDao {

  public static final List<Object[]> findDistinctInfo() {
    return GbaJpa.getManager().createQuery("select distinct (p.key), p.descr from UsrInfo p",
        Object[].class).getResultList();
  }

  public static final List<UsrInfo> findUsrInfo(long cUsr) {

    EntityManager em = GbaJpa.getManager();

    StringBuilder sb = new StringBuilder();
    sb.append("select p from UsrInfo p where p.usr.cUsr = 0 or p.usr.cUsr = :c_usr ");
    TypedQuery<UsrInfo> q = em.createQuery(sb.toString(), UsrInfo.class);
    q.setParameter("c_usr", cUsr);

    return q.getResultList();
  }
}
