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

import static nl.procura.gba.common.ZaakType.RIJBEWIJS;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.aval;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.jpa.personen.db.Nrd;
import nl.procura.gba.jpa.personen.db.NrdStatus;
import nl.procura.gba.jpa.personen.utils.CriteriaWrapper;

public class NrdDao extends ZaakDao {

  public static final String  AANVR_NR                  = "aanvrNr";
  public static final String  RBW_NR                    = "rbwNr";
  public static final String  RBW_NR_VERV               = "rbwNrVerv";
  private static final int    CODE_RIJBEWIJS_UITGEREIKT = 90;
  private static final String C_AANVR                   = "cAanvr";
  private static final String D_AANVR                   = "dAanvr";
  private static final String T_AANVR                   = "tAanvr";
  private static final String C_STAT                    = "cStat";
  private static final String D_STAT                    = "dStat";

  public static final List<ZaakKey> findZaakKeys(ConditionalMap map) {

    CriteriaWrapper<Nrd, ZaakKey> w = new CriteriaWrapper<>(Nrd.class, ZaakKey.class, map);

    Path<Object> zaakId = w.getTable().get(ZAAK_ID);
    Path<Object> dAanv = w.getTable().get(D_AANVR);
    Path<Object> dOpn = w.getTable().get(D_AANVR);
    Path<Object> tOpn = w.getTable().get(T_AANVR);

    w.getCq().select(w.getBuilder().construct(ZaakKey.class, zaakId, dAanv, dOpn, tOpn)).orderBy(
        getOrder(w, D_AANVR, T_AANVR, C_AANVR));

    find(w);

    return addZaakType(w.getResultList(), RIJBEWIJS);
  }

  public static final int findCount(ConditionalMap map) {

    CriteriaWrapper<Nrd, Long> w = new CriteriaWrapper<>(Nrd.class, Long.class, map);

    w.getCq().select(w.getBuilder().count(w.getTable().get(ZAAK_ID)));

    find(w);

    List<?> list = w.getEm().createQuery(w.getCq()).getResultList();

    return aval(list.get(0));
  }

  public static final List<Nrd> find(ConditionalMap map) {

    CriteriaWrapper<Nrd, Nrd> w = new CriteriaWrapper<>(Nrd.class, Nrd.class, map);

    find(w);

    w.getCq().orderBy(getOrder(w, D_AANVR, T_AANVR, C_AANVR));

    TypedQuery<Nrd> query = w.getEm().createQuery(w.getCq());

    return get(query, map);
  }

  private static void find(CriteriaWrapper<Nrd, ?> w) {

    CriteriaBuilder builder = w.getBuilder();
    Root<Nrd> table = w.getTable();
    CriteriaQuery query = w.getCq();
    ConditionalMap map = w.getMap();

    List<Predicate> where = new ArrayList<>();

    where.add(builder.greaterThan(table.get(C_AANVR), 0));

    List<Predicate> ids = new ArrayList<>();

    if (map.containsKey(ANR)) {
      ids.add(builder.equal(table.get(ANR), map.get(ANR)));
    }

    if (map.containsKey(BSN)) {
      ids.add(builder.equal(table.get(BSN), map.get(BSN)));
    }

    if (ids.size() > 0) {
      where.add(builder.and(builder.or(ids.toArray(new Predicate[ids.size()]))));
    }

    if (map.containsKey(ZAAK_ID)) {

      Expression<Boolean> predicate1 = getZaakIds(builder, table, map);
      Expression<Boolean> predicate2 = getZaakIds(builder, table, map, AANVR_NR);

      where.add(builder.and(builder.or(predicate1, predicate2)));
    }

    if (map.containsKey(AANVR_NR)) {

      Expression<Boolean> predicate1 = builder.equal(table.get(ZAAK_ID), map.get(AANVR_NR));
      Expression<Boolean> predicate2 = builder.equal(table.get(AANVR_NR), map.get(AANVR_NR));

      where.add(builder.and(builder.or(predicate1, predicate2)));
    }

    if (map.containsKey(IND_VERWERKT)) {
      where.add(getStatussen(table, map));
    }

    if (map.containsKey(NIET_IND_VERWERKT)) {
      where.add(builder.not(getNegeerStatussen(table, map)));
    }

    ge(where, builder, map, table.<Long> get(D_AANVR), D_INVOER_VANAF, D_INGANG_VANAF);
    le(where, builder, map, table.<Long> get(D_AANVR), D_INVOER_TM, D_INGANG_TM);

    if (map.containsKey(RBW_NR)) {

      // Alleen juist als RBW_NR != RBW_NR_VERV

      Expression<Boolean> predicate1 = builder.notEqual(table.get(RBW_NR_VERV), map.get(RBW_NR));
      Expression<Boolean> predicate2 = builder.equal(table.get(RBW_NR), map.get(RBW_NR));

      where.add(builder.and(predicate1, predicate2));
    }

    // Uitreiking zoeken in nrd_status tabel
    if (map.containsKeys(D_AFHAAL_VANAF, D_AFHAAL_TM)) {

      Subquery<NrdStatus> nrdStatusQuery = query.subquery(NrdStatus.class);
      Root nrdStatus = query.from(NrdStatus.class);
      nrdStatusQuery.select(nrdStatus.get(ID).get(C_AANVR));

      List<Predicate> subWhere = new ArrayList<>();
      subWhere.add(nrdStatus.get(ID).get(C_STAT).in(CODE_RIJBEWIJS_UITGEREIKT));

      if (map.containsKey(D_AFHAAL_VANAF)) {
        subWhere.add(builder.ge(nrdStatus.get(ID).get(D_STAT), along(map.get(D_AFHAAL_VANAF))));
      }

      if (map.containsKey(D_AFHAAL_TM)) {
        subWhere.add(builder.le(nrdStatus.get(ID).get(D_STAT), along(map.get(D_AFHAAL_TM))));
      }

      nrdStatusQuery.where(subWhere.toArray(new Predicate[subWhere.size()]));
      where.add(builder.in(table.get(C_AANVR)).value(nrdStatusQuery));
    }

    // Niet mogelijk voor deze zaak
    if (map.containsKey(D_END_TERMIJN)) {
      where.add(builder.lessThan(table.get(C_AANVR), 0));
    }

    getAttributes(USR, query, where, table, builder, map);

    query.where(where.toArray(new Predicate[where.size()]));
  }

  public static Nrd findByAanvraagNummer(String aanvraagnummer) {
    return NrdDao.findFirstByExample(new Nrd(aanvraagnummer));
  }

  public static Long findAanvraagNummer(String aanvraagnummer) {
    Nrd nr = findByAanvraagNummer(aanvraagnummer);
    return nr != null ? nr.getCAanvr() : null;
  }
}
