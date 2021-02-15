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

import static nl.procura.gba.common.ZaakType.VERHUIZING;
import static nl.procura.standard.Globalfunctions.aval;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.jpa.personen.db.BvhPark;
import nl.procura.gba.jpa.personen.utils.CriteriaWrapper;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class BvhParkDao extends ZaakDao {

  public static final String AANGIFTE_ANR = "aangifteAnr";
  public static final String AANGIFTE_BSN = "aangifteBsn";
  public static final String HOOFD_BSN    = "hoofdBsn";
  public static final String TOEST_BSN    = "toestBsn";
  public static final String C_BVH_PARK   = "cBvhPark";
  public static final String D_OPN        = "dOpn";
  public static final String T_OPN        = "tOpn";
  public static final String D_AANV       = "dAanv";

  public static final List<ZaakKey> findZaakKeys(ConditionalMap map) {

    CriteriaWrapper<BvhPark, ZaakKey> w = new CriteriaWrapper<>(BvhPark.class, ZaakKey.class, map);

    Path<Object> zaakId = w.getTable().get(ZAAK_ID);
    Path<Object> dAanv = w.getTable().get(D_AANV);
    Path<Object> dOpn = w.getTable().get(D_OPN);
    Path<Object> tOpn = w.getTable().get(T_OPN);

    w.getCq().select(w.getBuilder().construct(ZaakKey.class, zaakId, dAanv, dOpn, tOpn)).orderBy(
        getOrder(w, D_AANV, D_OPN, T_OPN, C_BVH_PARK)).distinct(true);

    find(w);

    return addZaakType(w.getResultList(), VERHUIZING);
  }

  public static final int findCount(ConditionalMap map) {

    CriteriaWrapper<BvhPark, Long> w = new CriteriaWrapper<>(BvhPark.class, Long.class, map);

    w.getCq().select(w.getBuilder().countDistinct(w.getTable().get(ZAAK_ID)));

    find(w);

    List<?> list = w.getEm().createQuery(w.getCq()).getResultList();

    return aval(list.get(0));
  }

  public static final List<BvhPark> find(ConditionalMap map) {

    CriteriaWrapper<BvhPark, BvhPark> w = new CriteriaWrapper<>(BvhPark.class, BvhPark.class, map);

    find(w);

    w.getCq().orderBy(getOrder(w, D_AANV, D_OPN, T_OPN, C_BVH_PARK));

    TypedQuery<BvhPark> query = w.getEm().createQuery(w.getCq());

    return get(query, map);
  }

  private static void find(CriteriaWrapper<BvhPark, ?> w) {

    CriteriaBuilder builder = w.getBuilder();
    Root<BvhPark> table = w.getTable();
    CriteriaQuery query = w.getCq();
    ConditionalMap map = w.getMap();

    List<Predicate> where = new ArrayList<>();

    where.add(builder.greaterThan(table.get(C_BVH_PARK), 0));

    List<Predicate> ids = new ArrayList<>();

    if (map.containsKey(ANR)) {
      ids.add(builder.equal(table.get(ANR), map.get(ANR)));
      ids.add(builder.equal(table.get(AANGIFTE_ANR), map.get(ANR)));
    }

    if (map.containsKey(BSN)) {
      ids.add(builder.equal(table.get(BSN), map.get(BSN)));
      ids.add(builder.equal(table.get(AANGIFTE_BSN), map.get(BSN)));
      ids.add(builder.equal(table.get(TOEST_BSN), map.get(BSN)));
      ids.add(builder.equal(table.get(HOOFD_BSN), map.get(BSN)));
    }

    if (ids.size() > 0) {
      where.add(builder.and(builder.or(ids.toArray(new Predicate[ids.size()]))));
    }

    if (map.containsKey(ZAAK_ID)) {
      where.add(getZaakIds(builder, table, map));
    }

    if (map.containsKey(IND_VERWERKT)) {
      where.add(getStatussen(table, map));
    }

    if (map.containsKey(NIET_IND_VERWERKT)) {
      where.add(builder.not(getNegeerStatussen(table, map)));
    }

    // Niet mogelijk voor deze zaak
    if (map.containsKeys(D_END_TERMIJN, D_AFHAAL_VANAF, D_AFHAAL_TM)) {
      where.add(builder.lessThan(table.get(C_BVH_PARK), 0));
    }

    ge(where, builder, map, table.<Long> get(D_OPN), D_INVOER_VANAF);
    le(where, builder, map, table.<Long> get(D_OPN), D_INVOER_TM);

    ge(where, builder, map, table.<Long> get(D_AANV), D_INGANG_VANAF);
    le(where, builder, map, table.<Long> get(D_AANV), D_INGANG_TM);

    getMutDate(where, table, query, builder, map);
    getUsr(ZaakDao.USR, where, table, builder, map);
    getProfile(ZaakDao.USR, where, table, builder, map);
    getAttribute(query, where, table, builder, map);

    query.where(where.toArray(new Predicate[where.size()]));
  }

  @SuppressWarnings("unchecked")
  public static final List<Object[]> findNrs(String zaakId) {
    EntityManager em = GbaJpa.getManager();

    StringBuilder sql = new StringBuilder();
    sql.append("select x.anr, x.bsn, x.cGemHerkomst, x.indVerwerkt, x.aangifte, x.cBvhPark, x.geenVerwerking ");
    sql.append("from BvhPark x where x.zaakId = :");
    sql.append(ZAAK_ID);

    Query q = em.createQuery(sql.toString());
    q.setParameter(ZAAK_ID, zaakId);
    return q.getResultList();
  }

  /**
   * Update alleen het veld geen_verwerking
   */
  public static int updateVerwerking(long cBvhPark, long geenVerwerking) {
    if (cBvhPark > 0) {
      Query query = createQuery("update BvhPark x set x.geenVerwerking = :gv where x.cBvhPark = :code");
      query.setParameter("gv", geenVerwerking);
      query.setParameter("code", cBvhPark);
      return query.executeUpdate();
    }
    return 0;
  }
}
