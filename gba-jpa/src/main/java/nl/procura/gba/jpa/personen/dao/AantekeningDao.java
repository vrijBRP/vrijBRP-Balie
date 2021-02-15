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

import static nl.procura.gba.jpa.personen.dao.ZaakDao.getZaakIds;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.jpa.personen.db.Aantekening;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class AantekeningDao extends GenericDao {

  private static final String C_AANTEKENING = "cAantekening";

  public static final List<Aantekening> find(ConditionalMap map) {

    if (map.isEmpty()) {
      return new ArrayList<>();
    }

    EntityManager em = GbaJpa.getManager();
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Aantekening> query = builder.createQuery(Aantekening.class);
    Root<Aantekening> gpk = query.from(Aantekening.class);
    List<Predicate> where = new ArrayList<>();

    where.add(builder.greaterThan(gpk.get(C_AANTEKENING), 0));

    if (map.containsKey(BSN)) {

      Predicate a = builder.equal(gpk.get(BSN), map.get(BSN));
      where.add(builder.or(a));
    }

    if (map.containsKey(ZAAK_ID)) {
      where.add(getZaakIds(builder, gpk, map));
    }

    query.where(where.toArray(new Predicate[where.size()]));

    return GenericDao.refreshEntities(em.createQuery(query).getResultList());
  }
}
