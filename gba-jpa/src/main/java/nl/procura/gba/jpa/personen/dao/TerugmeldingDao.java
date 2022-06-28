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

import static nl.procura.gba.common.ZaakType.TERUGMELDING;
import static nl.procura.standard.Globalfunctions.aval;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.jpa.personen.utils.CriteriaWrapper;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class TerugmeldingDao extends ZaakDao {

  public static final String  SOFINR         = "snr";
  public static final String  IND_VERWERKT   = "indVerwerkt";
  private static final String USR_TOEV       = "usrToev";
  private static final String C_TERUGMELDING = "cTerugmelding";
  private static final String D_IN           = "dIn";
  private static final String T_IN           = "tIn";

  public static final List<ZaakKey> findZaakKeys(ConditionalMap map) {

    CriteriaWrapper<Terugmelding, ZaakKey> w = new CriteriaWrapper<>(Terugmelding.class, ZaakKey.class, map);

    Path<Object> zaakId = w.getTable().get(ZAAK_ID);
    Path<Object> dAanv = w.getTable().get(D_IN);
    Path<Object> dOpn = w.getTable().get(D_IN);
    Path<Object> tOpn = w.getTable().get(T_IN);

    w.getCq().select(w.getBuilder().construct(ZaakKey.class, zaakId, dAanv, dOpn, tOpn)).orderBy(
        getOrder(w, D_IN, T_IN, C_TERUGMELDING));

    find(w);

    return addZaakType(w.getResultList(), TERUGMELDING);
  }

  public static final int findCount(ConditionalMap map) {

    CriteriaWrapper<Terugmelding, Long> w = new CriteriaWrapper<>(Terugmelding.class, Long.class, map);

    w.getCq().select(w.getBuilder().count(w.getTable().get(C_TERUGMELDING)));

    find(w);

    List<?> list = w.getEm().createQuery(w.getCq()).getResultList();

    return aval(list.get(0));
  }

  public static final List<Terugmelding> find(ConditionalMap map) {

    CriteriaWrapper<Terugmelding, Terugmelding> w = new CriteriaWrapper<>(Terugmelding.class, Terugmelding.class,
        map);

    find(w);

    w.getCq().orderBy(getOrder(w, D_IN, T_IN, C_TERUGMELDING));

    TypedQuery<Terugmelding> query = w.getEm().createQuery(w.getCq());

    return get(query, map);
  }

  private static void find(CriteriaWrapper<Terugmelding, ?> w) {

    CriteriaBuilder builder = w.getBuilder();
    Root<Terugmelding> table = w.getTable();
    CriteriaQuery query = w.getCq();
    ConditionalMap map = w.getMap();

    List<Predicate> where = new ArrayList<>();

    where.add(builder.greaterThan(table.get(C_TERUGMELDING), 0));

    List<Predicate> ids = new ArrayList<>();

    if (map.containsKey(ANR)) {
      ids.add(builder.equal(table.get(ANR), map.get(ANR)));
    }

    if (map.containsKey(SOFINR)) {
      ids.add(builder.equal(table.get(SOFINR), map.get(SOFINR)));
    }

    if (map.containsKey(BSN)) {
      ids.add(builder.equal(table.get(SOFINR), map.get(BSN)));
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

    ge(where, builder, map, table.<Long> get(D_IN), D_INVOER_VANAF, D_INGANG_VANAF);
    le(where, builder, map, table.<Long> get(D_IN), D_INVOER_TM, D_INGANG_TM);

    if (map.containsKey(ZAAK_ID)) {
      where.add(getZaakIds(builder, table, map));
    }

    // Niet mogelijk voor deze zaak
    if (map.containsKeys(D_END_TERMIJN, D_AFHAAL_VANAF, D_AFHAAL_TM)) {
      where.add(builder.lessThan(table.get(C_TERUGMELDING), 0));
    }

    getAttributes(USR_TOEV, query, where, table, builder, map);

    query.where(where.toArray(new Predicate[where.size()]));
  }

  public static final List<TerugmTmv> findTmv(long dossierNummer) {

    String sql = "select x from TerugmTmv x where x.id.dossiernummer = :d order by x.id.dIn desc, x.id.tIn desc";
    TypedQuery<TerugmTmv> q = createQuery(sql, TerugmTmv.class);
    q.setParameter("d", dossierNummer);

    return q.getResultList();
  }

  public static final void removeTerugmelding(Terugmelding tm) {

    for (TerugmDetail d : tm.getTerugmDetails()) {
      removeEntity(d);
    }

    for (TerugmReactie r : tm.getTerugmReacties()) {
      removeEntity(r);
    }

    for (TerugmTmvRel r : tm.getTerugmTmvRels()) {
      removeEntity(r);
    }

    String sql = "delete from terugm_tmv where dossiernummer not in (select dossiernummer from terugm_tmv_rel)";
    GbaJpa.getManager().createNativeQuery(sql).executeUpdate();

    removeEntity(tm);
  }

  public static final boolean exists(long dossiernummer, long statusCode, String berichtcode) {

    Query q = createQuery(
        "select x from TerugmTmv x where x.id.dossiernummer = :dr and x.dossierstatus = :ds and x.berichtcode = :bc");
    q.setParameter("dr", dossiernummer);
    q.setParameter("ds", statusCode);
    q.setParameter("bc", berichtcode);

    return q.getResultList().size() > 0;
  }
}
