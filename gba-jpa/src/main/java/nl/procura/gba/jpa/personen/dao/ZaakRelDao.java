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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.UniqueList;
import nl.procura.gba.jpa.personen.db.ZaakRel;
import nl.procura.gba.jpa.personen.db.ZaakRelPK;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class ZaakRelDao extends GenericDao {

  public static final String ZAAK_ID_REL = "zaakIdRel";

  public static final List<ZaakRel> findBoth(ConditionalMap map) {

    List<ZaakRel> list = new UniqueList();

    if (map.isEmpty()) {
      return list;
    }

    if (map.containsKey(ZAAK_ID)) {

      EntityManager em = GbaJpa.getManager();
      CriteriaBuilder builder = em.getCriteriaBuilder();
      CriteriaQuery<ZaakRel> query = builder.createQuery(ZaakRel.class);
      Root<ZaakRel> table = query.from(ZaakRel.class);

      List<Predicate> where = new ArrayList<>();

      Path<Object> id = table.get("id");
      Expression<Boolean> predicate1 = builder.equal(id.get(ZAAK_ID), map.get(ZAAK_ID));
      Expression<Boolean> predicate2 = builder.equal(id.get(ZAAK_ID_REL), map.get(ZAAK_ID));

      where.add(builder.or(predicate1, predicate2));

      if (map.containsKey(ZAAK_ID_REL)) {

        Expression<Boolean> predicate3 = builder.equal(id.get(ZAAK_ID_REL), map.get(ZAAK_ID_REL));
        Expression<Boolean> predicate4 = builder.equal(id.get(ZAAK_ID), map.get(ZAAK_ID_REL));

        where.add(builder.or(predicate3, predicate4));
      }

      query.where(where.toArray(new Predicate[where.size()]));

      list.addAll(em.createQuery(query).getResultList());
    }

    return list;
  }

  public static final List<ZaakRel> find(ConditionalMap map) {

    List<ZaakRel> list = new UniqueList();

    if (map.isEmpty()) {
      return new ArrayList<>();
    }

    if (map.containsKey(ZAAK_ID)) {

      Set<String> zaakIds = ZaakDao.getZaakIdSet(map);
      for (ZaakRel zr : findBoth(map)) {
        for (String zaakId : zaakIds) {
          if (zr.getId().getZaakIdRel().equals(zaakId)) {
            ZaakRel zaakRel = new ZaakRel(new ZaakRelPK(zr.getId().getZaakIdRel(), zr.getId().getZaakId()));
            zaakRel.setzType(zr.getzTypeRel());
            zaakRel.setzTypeRel(zr.getzType());
            list.add(zaakRel);
          } else {
            list.add(zr);
          }
        }
      }
    }

    return list;
  }
}
