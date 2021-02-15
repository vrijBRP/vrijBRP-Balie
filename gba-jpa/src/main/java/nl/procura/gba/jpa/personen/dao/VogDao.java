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

import static nl.procura.gba.common.ZaakType.COVOG;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.jpa.personen.utils.CriteriaWrapper;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class VogDao extends ZaakDao {

  public static final String BSN_A       = "bsnA";
  public static final String C_VOG_AANVR = "cVogAanvr";
  public static final String AANVR_ID    = "aanvrId";
  public static final String D_AANVR     = "dAanvr";
  public static final String T_AANVR     = "tAanvr";
  public static final String C_GEM       = "cGem";
  public static final String C_LOC       = "cLoc";
  public static final String V_AANVR     = "vAanvr";

  public static final List<ZaakKey> findZaakKeys(ConditionalMap map) {

    CriteriaWrapper<VogAanvr, ZaakKey> w = new CriteriaWrapper<>(VogAanvr.class, ZaakKey.class, map);

    Path<Object> zaakId = w.getTable().get(ZAAK_ID);
    Path<Object> dAanv = w.getTable().get(D_AANVR);
    Path<Object> dOpn = w.getTable().get(D_AANVR);
    Path<Object> tOpn = w.getTable().get(T_AANVR);

    w.getCq().select(w.getBuilder().construct(ZaakKey.class, zaakId, dAanv, dOpn, tOpn)).orderBy(
        getOrder(w, D_AANVR, T_AANVR, C_VOG_AANVR));

    find(w);

    return addZaakType(w.getResultList(), COVOG);
  }

  public static final int findCount(ConditionalMap map) {

    CriteriaWrapper<VogAanvr, Long> w = new CriteriaWrapper<>(VogAanvr.class, Long.class, map);

    w.getCq().select(w.getBuilder().count(w.getTable().get(ZAAK_ID)));

    find(w);

    List<?> list = w.getEm().createQuery(w.getCq()).getResultList();

    return aval(list.get(0));
  }

  public static final List<VogAanvr> find(ConditionalMap map) {

    CriteriaWrapper<VogAanvr, VogAanvr> w = new CriteriaWrapper<>(VogAanvr.class, VogAanvr.class, map);

    find(w);

    w.getCq().orderBy(getOrder(w, D_AANVR, T_AANVR, C_VOG_AANVR));

    TypedQuery<VogAanvr> query = w.getEm().createQuery(w.getCq());

    return get(query, map);
  }

  private static void find(CriteriaWrapper<VogAanvr, ?> w) {

    CriteriaBuilder builder = w.getBuilder();
    Root<VogAanvr> table = w.getTable();
    CriteriaQuery query = w.getCq();
    ConditionalMap map = w.getMap();

    List<Predicate> where = new ArrayList<>();

    where.add(builder.greaterThan(table.get(C_VOG_AANVR), 0));

    if (map.containsKey(C_VOG_AANVR)) {
      where.add(builder.equal(table.get(C_VOG_AANVR), map.get(C_VOG_AANVR)));
    }

    List<Predicate> ids = new ArrayList<>();

    if (map.containsKey(ANR)) {
      ids.add(builder.equal(table.get(ANR), map.get(ANR)));
    }

    if (map.containsKey(BSN)) {
      ids.add(builder.equal(table.get(BSN_A), map.get(BSN)));
    }

    if (ids.size() > 0) {
      where.add(builder.and(builder.or(ids.toArray(new Predicate[ids.size()]))));
    }

    if (map.containsKey(IND_VERWERKT)) {
      where.add(getStatussen(table, map));
    }

    if (map.containsKey(NIET_IND_VERWERKT)) {
      where.add(builder.not(getNegeerStatussen(table, map)));
    }

    if (map.containsKey(ZAAK_ID)) {

      List<Predicate> exp = new ArrayList<>();

      Set<String> zaakIds = ZaakDao.getZaakIdSet(map);

      for (String zaakId : zaakIds) {

        exp.add(builder.equal(table.get(ZAAK_ID), zaakId));
        exp.add(builder.equal(table.get(AANVR_ID), pos(zaakId) ? zaakId : 9999));

        ConditionalMap vogMap = getVogNr(zaakId);

        if (vogMap.size() > 0) {

          Predicate exp1 = builder.equal(table.get(C_GEM), vogMap.get(C_GEM));
          Predicate exp2 = builder.equal(table.get(C_LOC), vogMap.get(C_LOC));
          Predicate exp3 = builder.equal(table.get(D_AANVR), vogMap.get(D_AANVR));
          Predicate exp4 = builder.equal(table.get(V_AANVR), vogMap.get(V_AANVR));

          exp.add(builder.and(exp1, exp2, exp3, exp4));
        }
      }

      where.add(builder.or(exp.toArray(new Predicate[exp.size()])));
    }

    ge(where, builder, map, table.<Long> get(D_AANVR), D_INVOER_VANAF, D_INGANG_VANAF);
    le(where, builder, map, table.<Long> get(D_AANVR), D_INVOER_TM, D_INGANG_TM);

    // Niet mogelijk voor deze zaak
    if (map.containsKeys(D_END_TERMIJN, D_AFHAAL_VANAF, D_AFHAAL_TM)) {
      where.add(builder.lessThan(table.get(C_VOG_AANVR), 0));
    }

    getMutDate(where, table, query, builder, map);
    getUsr(ZaakDao.USR, where, table, builder, map);
    getProfile(ZaakDao.USR, where, table, builder, map);
    getAttribute(query, where, table, builder, map);

    query.where(where.toArray(new Predicate[where.size()]));
  }

  private static ConditionalMap getVogNr(String vogNummer) {

    Matcher m = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{8})-(\\d{4})").matcher(vogNummer);

    ConditionalMap map = new ConditionalMap();

    while (m.find()) {

      map.put(C_GEM, along(m.group(1)));
      map.put(C_LOC, along(m.group(2)));
      map.put(D_AANVR, along(m.group(3)));
      map.put(V_AANVR, along(m.group(4)));
    }

    return map;
  }

  public static final List<VogBelang> findBelanghebbenden() {
    return GbaJpa.getManager().createQuery("select x from VogBelang x order by x.instellingB, x.naamB",
        VogBelang.class).getResultList();
  }

  public static final List<VogDoelTab> findVogDoelen() {
    return GbaJpa.getManager().createQuery("select x from VogDoelTab x where x.dIn > 0",
        VogDoelTab.class).getResultList();
  }

  public static final List<VogProfTab> findVogProfielen() {
    return GbaJpa.getManager().createQuery("select x from VogProfTab x where x.dIn > 0",
        VogProfTab.class).getResultList();
  }

  public static final List<VogFuncTab> findVogFuncties() {
    return GbaJpa.getManager().createQuery("select x from VogFuncTab x where x.dIn > 0",
        VogFuncTab.class).getResultList();
  }
}
