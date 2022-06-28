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

import static nl.procura.standard.Globalfunctions.*;

import java.util.*;

import javax.persistence.Query;
import javax.persistence.criteria.*;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.IndVerwerkt;
import nl.procura.gba.jpa.personen.db.ZaakAttr;
import nl.procura.gba.jpa.personen.db.ZaakId;
import nl.procura.gba.jpa.personen.utils.CriteriaWrapper;
import nl.procura.gba.jpa.personen.utils.GbaDaoUtils;

public class ZaakDao extends GenericDao {

  public static final String  ZAAK_ID_TYPE      = "zaakIdType";
  public static final String  TYPE              = "type";
  public static final String  TYPE_DOSS         = "typeDoss";
  public static final String  IND_VERWERKT      = "indVerwerkt";
  public static final String  NIET_IND_VERWERKT = "NietIndVerwerkt";
  public static final String  USR               = "usr";
  public static final String  C_USR             = "cUsr";
  public static final String  C_PROFILE         = "cProfile";
  public static final String  PROFILES          = "profiles";
  public static final String  BRONNEN           = "bronnen";
  public static final String  LEVERANCIER       = "leverancier";
  public static final String  LEVERANCIERS      = "leveranciers";
  public static final String  D_END_TERMIJN     = "dEndTermijn";
  public static final String  ATTRIBUTEN        = "attributen";
  public static final String  ONTB_ATTRIBUTEN   = "ontbAttributen";
  public static final String  ID                = "id";
  public static final String  D_INVOER_VANAF    = "dInvoerFrom";
  public static final String  D_INVOER_TM       = "dInvoerTo";
  public static final String  D_INGANG_VANAF    = "dIngangFrom";
  public static final String  D_INGANG_TM       = "dIngangTo";
  public static final String  D_AFHAAL_VANAF    = "dAfhaalFrom";
  public static final String  D_AFHAAL_TM       = "dAfhaalTo";
  public static final String  D_MUT_VANAF       = "dMutFrom";
  public static final String  D_MUT_TM          = "dMutTo";
  private static final String ZAAK_ATTR         = "zaakAttr";
  private static final String BRON              = "bron";

  protected static void getUsr(String cUsr, List<Predicate> where, Root table, CriteriaBuilder builder,
      ConditionalMap map) {

    if (map.containsKey(C_USR)) {
      where.add(builder.equal(table.get(cUsr).get(C_USR), map.get(C_USR)));
    }
  }

  protected static void getMutDate(List<Predicate> where, Root table, CriteriaQuery query, CriteriaBuilder builder,
      ConditionalMap map) {

    if (map.containsKey(D_MUT_VANAF) || map.containsKey(D_MUT_TM)) {
      Subquery<IndVerwerkt> indVerwerktQuery = query.subquery(IndVerwerkt.class);
      Root indVerwerkt = query.from(IndVerwerkt.class);

      indVerwerktQuery.select(indVerwerkt.get(ZAAK_ID));
      List<Predicate> preds = new ArrayList();
      if (map.containsKey(D_MUT_VANAF)) {
        preds.add(builder.ge(indVerwerkt.get(IndicatieVerwerktDao.D_IN), along(map.get(D_MUT_VANAF))));
      }

      if (map.containsKey(D_MUT_TM)) {
        preds.add(builder.le(indVerwerkt.get(IndicatieVerwerktDao.D_IN), along(map.get(D_MUT_TM))));
      }

      indVerwerktQuery.where(preds.toArray(new Predicate[preds.size()]));
      where.add(builder.in(table.get(ZAAK_ID)).value(indVerwerktQuery));
    }
  }

  protected static void getProfile(String cUsr, List<Predicate> where, Root table, CriteriaBuilder builder,
      ConditionalMap map) {

    if (map.containsKey(C_PROFILE)) {
      where.add(builder.equal(table.get(cUsr).get(PROFILES).get(C_PROFILE), map.get(C_PROFILE)));
    }
  }

  protected static void getAttribute(CriteriaQuery<?> q, List<Predicate> w, Root table, CriteriaBuilder builder,
      ConditionalMap map) {

    if (map.containsKey(ATTRIBUTEN)) {
      Subquery<ZaakAttr> zaakAttrQuery = q.subquery(ZaakAttr.class);
      Root zaakAttr = q.from(ZaakAttr.class);
      zaakAttrQuery.select(zaakAttr.get(ID).get(ZAAK_ID));
      zaakAttrQuery.where(zaakAttr.get(ID).get(ZAAK_ATTR).in((Collection<String>) map.get(ATTRIBUTEN)));
      w.add(builder.in(table.get(ZAAK_ID)).value(zaakAttrQuery));
    }

    if (map.containsKey(ONTB_ATTRIBUTEN)) {
      Subquery<ZaakAttr> zaakAttrQuery = q.subquery(ZaakAttr.class);
      Root zaakAttr = q.from(ZaakAttr.class);
      zaakAttrQuery.select(zaakAttr.get(ID).get(ZAAK_ID));
      zaakAttrQuery.where(zaakAttr.get(ID).get(ZAAK_ATTR).in((Collection<String>) map.get(ONTB_ATTRIBUTEN)));
      w.add(builder.in(table.get(ZAAK_ID)).value(zaakAttrQuery).not());
    }

    getBron(w, table, builder, map);
    getLeveranciers(w, table, builder, map);
    getZaakIdType(q, w, table, builder, map);
  }

  protected static void getBron(List<Predicate> where, Root table, CriteriaBuilder builder, ConditionalMap map) {

    if (map.containsKey(BRONNEN)) {
      List<Predicate> whereList = new ArrayList<>();
      for (String value : (Collection<String>) map.get(BRONNEN)) {
        whereList.add(builder.like(table.get(BRON), value));
      }
      where.add(builder.or(whereList.toArray(new Predicate[whereList.size()])));
    }
  }

  protected static void getZaakIdType(CriteriaQuery<?> q, List<Predicate> w, Root table, CriteriaBuilder builder,
      ConditionalMap map) {
    if (map.containsKey(ZAAK_ID_TYPE)) {
      Subquery<ZaakId> zaakIdQuery = q.subquery(ZaakId.class);
      Root zaakId = q.from(ZaakId.class);
      zaakIdQuery.select(zaakId.get(ID).get("internId"));
      zaakIdQuery.where(builder.equal(zaakId.get(ID).get(TYPE), map.get(ZAAK_ID_TYPE)));
      w.add(builder.in(table.get(ZAAK_ID)).value(zaakIdQuery));
    }
  }

  protected static void getLeveranciers(List<Predicate> where, Root table, CriteriaBuilder builder,
      ConditionalMap map) {

    if (map.containsKey(LEVERANCIERS)) {
      List<Predicate> whereList = new ArrayList<>();
      for (String value : (Collection<String>) map.get(LEVERANCIERS)) {
        whereList.add(builder.like(table.get(LEVERANCIER), value));
      }
      where.add(builder.or(whereList.toArray(new Predicate[whereList.size()])));
    }
  }

  @SuppressWarnings("unchecked")
  protected static Predicate getStatussen(Root table, ConditionalMap map) {
    return table.get(IND_VERWERKT).in((ArrayList<Long>) map.get(IND_VERWERKT));
  }

  /**
   * Vertaald ZAAK_ID value in map naar een Set
   */
  public static final Set<String> getZaakIdSet(ConditionalMap map) {

    Object value = map.get(ZAAK_ID);
    Set<String> zaakIds = new HashSet<>();

    if (map.get(ZAAK_ID) instanceof Set) {
      zaakIds = (Set<String>) map.get(ZAAK_ID);
    } else {
      zaakIds.add(astr(value));
    }

    return zaakIds;
  }

  public static final Predicate getZaakIds(CriteriaBuilder builder, Path table, ConditionalMap map) {
    return getZaakIds(builder, table, map, ZAAK_ID);
  }

  /**
   * Geeft juiste predicate terug al naargelang het aantal zaakIds
   */
  public static final Predicate getZaakIds(CriteriaBuilder builder, Path table, ConditionalMap map,
      String columnName) {

    Set zaakIds = getZaakIdSet(map);

    if (zaakIds.size() == 1) {
      return builder.equal(table.get(columnName), zaakIds.iterator().next());
    }

    return table.get(columnName).in(zaakIds);
  }

  @SuppressWarnings("unchecked")
  protected static Predicate getNegeerStatussen(Root table, ConditionalMap map) {
    return table.get(IND_VERWERKT).in((ArrayList<Long>) map.get(NIET_IND_VERWERKT));
  }

  public static boolean isExistingZaakId(Object zaak, String id) {

    Object entity = GbaDaoUtils.getEntity(zaak);
    Query query = createQuery(
        "select count(o) from " + entity.getClass().getSimpleName() + " o where o.zaakId = :zaakId");
    query.setParameter("zaakId", id);
    return pos(query.getResultList().get(0));
  }

  /**
   * Voeg het zaakType toe
   */
  protected static List<ZaakKey> addZaakType(List<ZaakKey> zaakKeys, ZaakType zaakType) {
    for (ZaakKey zaakKey : zaakKeys) {
      zaakKey.setZaakType(zaakType);
    }
    return zaakKeys;
  }

  protected static List<Order> getOrder(CriteriaWrapper<?, ?> w, String... fields) {
    List<Order> list = new ArrayList<>();
    for (String field : fields) {
      list.add(w.getBuilder().desc(w.getTable().get(field)));
    }
    return list;
  }
}
