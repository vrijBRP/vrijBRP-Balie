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

import static java.util.Optional.ofNullable;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.jpa.personen.db.ZaakAttr;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.standard.Globalfunctions;

public class ZaakAttrDao extends GenericDao {

  public static final String ZAAK_ATTR = "zaakAttr";
  public static final String C_USR     = "cUsr";

  public static List<ZaakAttr> find(ConditionalMap map) {

    if (map.isEmpty()) {
      return new ArrayList<>();
    }

    EntityManager em = GbaJpa.getManager();
    StringBuilder sql = new StringBuilder();
    sql.append("select z from ZaakAttr z ");
    sql.append("where z.id.zaakId = :zaakId ");
    sql.append("and z.id.cUsr = :cUsr ");

    if (map.containsKey(ZAAK_ATTR)) {
      sql.append("and z.id.zaakAttr = :zaakAttr");
    }

    TypedQuery<ZaakAttr> q = em.createQuery(sql.toString(), ZaakAttr.class);
    q.setParameter("zaakId", astr(map.get(ZAAK_ID)));
    q.setParameter("cUsr", ofNullable(map.get(C_USR))
        .map(Globalfunctions::along)
        .orElse(0L));

    if (map.containsKey(ZAAK_ATTR)) {
      q.setParameter("zaakAttr", astr(map.get(ZAAK_ATTR)));
    }
    return q.getResultList();
  }
}
