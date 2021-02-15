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

import static nl.procura.standard.Globalfunctions.astr;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.UniqueList;
import nl.procura.gba.jpa.personen.db.ZaakId;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class ZaakIdDao extends GenericDao {

  public static final String INTERN_ID = "internId";
  public static final String EXTERN_ID = "externId";
  public static final String TYPE      = "type";

  public static final List<ZaakId> find(ConditionalMap map) {

    if (map.isEmpty()) {
      return new ArrayList<>();
    }

    EntityManager em = GbaJpa.getManager();
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<ZaakId> query = builder.createQuery(ZaakId.class);
    Root<ZaakId> table = query.from(ZaakId.class);

    List<Predicate> where = new ArrayList();
    Path<Object> id = table.get("id");

    if (map.containsKey(INTERN_ID)) {
      where.add(builder.equal(id.get(INTERN_ID), astr(map.get(INTERN_ID))));
    }

    if (map.containsKey(EXTERN_ID)) {
      Object externId = map.get(EXTERN_ID);
      if (externId instanceof List) {
        where.add(table.get(EXTERN_ID).in((List<String>) externId));
      } else {
        where.add(builder.equal(table.get(EXTERN_ID), astr(map.get(EXTERN_ID))));
      }
    }

    if (map.containsKey(TYPE)) {
      where.add(builder.equal(id.get("type"), astr(map.get(TYPE))));
    }

    List<ZaakId> list = new UniqueList();
    if (!where.isEmpty()) {
      query.where(where.toArray(new Predicate[where.size()]));
      list.addAll(em.createQuery(query).getResultList());
    }
    return list;
  }
}
