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
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.jpa.personen.db.PresVraag;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class PresentievraagDao extends GenericDao {

  public static final String C_PRESENTIEVRAAG = "cPresentievraag";
  public static final String D_INVOER_VANAF   = "dInvoerFrom";
  public static final String D_INVOER_TM      = "dInvoerTo";
  public static final String D_IN             = "dIn";
  public static final String ANTWOORD         = "antwoord";

  public static List<PresVraag> findByCode(String zaakId) {
    final TypedQuery<PresVraag> query = GbaJpa.getManager().createNamedQuery("PresVraag.findByZaakId", PresVraag.class);
    query.setParameter("zaakid", zaakId);
    return query.getResultList();
  }

  public static PresVraag findById(Long presVraagId) {
    return GenericDao.find(PresVraag.class, presVraagId);
  }

  public static final List<PresVraag> find(ConditionalMap map) {

    final EntityManager em = GbaJpa.getManager();
    final CriteriaBuilder builder = em.getCriteriaBuilder();
    final CriteriaQuery<PresVraag> cq = builder.createQuery(PresVraag.class);
    final Root<PresVraag> table = cq.from(PresVraag.class);

    find(builder, cq, table, map);

    return em.createQuery(cq).getResultList();
  }

  private static void find(CriteriaBuilder builder, CriteriaQuery<?> query, Root<?> table, ConditionalMap map) {

    final List<Predicate> where = new ArrayList<>();
    where.add(builder.greaterThan(table.get(C_PRESENTIEVRAAG), 0));

    ge(where, builder, map, table.<Long> get(D_IN), D_INVOER_VANAF);
    le(where, builder, map, table.<Long> get(D_IN), D_INVOER_TM);

    final String inhoudBericht = astr(map.get(ANTWOORD));
    if (fil(inhoudBericht)) {
      where.add(builder.like(table.get(ANTWOORD), "%" + inhoudBericht + "%"));
    }

    query.where(where.toArray(new Predicate[where.size()]));
  }
}
