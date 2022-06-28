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

import static java.util.Arrays.asList;
import static nl.procura.gba.common.ZaakType.REISDOCUMENT;
import static nl.procura.standard.Globalfunctions.aval;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.apache.commons.lang3.StringUtils.removePattern;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.jpa.personen.db.Rdm01;
import nl.procura.gba.jpa.personen.utils.CriteriaWrapper;

public class Rdm01Dao extends ZaakDao {

  public static final String AANVR_NR  = "aanvrNr";
  public static final String RDM01_ID  = "rdm01Id";
  public static final String D_AANVR   = "dAanvr";
  public static final String D_AFSL    = "dAfsl";
  public static final String T_AANVR   = "tAanvr";
  public static final String USR1      = "usr1";
  public static final String NR_NL_DOC = "nrNlDoc";
  public static final String STAT_AFSL = "statAfsl";

  public static final int CODE_REISD_UITGEREIKT          = 1;
  public static final int CODE_REIS_UITGEREIKT_INSTANTIE = 3;

  public static final List<ZaakKey> findZaakKeys(ConditionalMap map) {

    CriteriaWrapper<Rdm01, ZaakKey> w = new CriteriaWrapper<>(Rdm01.class, ZaakKey.class, map);

    Path<Object> zaakId = w.getTable().get(ZAAK_ID);
    Path<Object> dAanv = w.getTable().get(D_AANVR);
    Path<Object> dOpn = w.getTable().get(D_AANVR);
    Path<Object> tOpn = w.getTable().get(T_AANVR);

    w.getCq().select(w.getBuilder().construct(ZaakKey.class, zaakId, dAanv, dOpn, tOpn)).orderBy(
        getOrder(w, D_AANVR, T_AANVR, RDM01_ID));

    find(w);

    return addZaakType(w.getResultList(), REISDOCUMENT);
  }

  public static final int findCount(ConditionalMap map) {

    CriteriaWrapper<Rdm01, Long> w = new CriteriaWrapper<>(Rdm01.class, Long.class, map);

    w.getCq().select(w.getBuilder().count(w.getTable().get(ZAAK_ID)));

    find(w);

    List<?> list = w.getEm().createQuery(w.getCq()).getResultList();

    return aval(list.get(0));
  }

  public static final List<Rdm01> find(ConditionalMap map) {

    CriteriaWrapper<Rdm01, Rdm01> w = new CriteriaWrapper<>(Rdm01.class, Rdm01.class, map);

    find(w);

    w.getCq().orderBy(getOrder(w, D_AANVR, T_AANVR, RDM01_ID));

    TypedQuery<Rdm01> query = w.getEm().createQuery(w.getCq());

    return get(query, map);
  }

  private static void find(CriteriaWrapper<Rdm01, ?> w) {

    CriteriaBuilder builder = w.getBuilder();
    Root<Rdm01> table = w.getTable();
    CriteriaQuery query = w.getCq();
    ConditionalMap map = w.getMap();

    List<Predicate> where = new ArrayList<>();

    where.add(builder.greaterThan(table.get(RDM01_ID), 0));

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

      Predicate predicate1 = getZaakIds(builder, table, map);
      Predicate predicate2 = getZaakIds(builder, table, map, AANVR_NR);
      Predicate predicate3 = table.get(AANVR_NR).in(formattedAanvrNr(getZaakIdSet(map)));

      where.add(builder.and(builder.or(predicate1, predicate2, predicate3)));
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

    if (map.containsKey(NR_NL_DOC)) {
      where.add(builder.and(builder.equal(table.get(NR_NL_DOC), map.get(NR_NL_DOC))));
    }

    if (map.containsKeys(D_AFHAAL_VANAF, D_AFHAAL_TM)) {
      where.add(table.get(STAT_AFSL).in(asList(CODE_REISD_UITGEREIKT, CODE_REIS_UITGEREIKT_INSTANTIE)));
      ge(where, builder, map, table.<Long> get(D_AFSL), D_AFHAAL_VANAF);
      le(where, builder, map, table.<Long> get(D_AFSL), D_AFHAAL_TM);
    }

    // Niet mogelijk voor deze zaak
    if (map.containsKey(D_END_TERMIJN)) {
      where.add(builder.lessThan(table.get(D_AANVR), 0));
    }

    getAttributes(USR1, query, where, table, builder, map);

    query.where(where.toArray(new Predicate[where.size()]));
  }

  /**
   * Remove non-digit characters and left padding to 9
   */
  private static Set<String> formattedAanvrNr(Set<String> zaakIds) {
    HashSet<String> set = new HashSet<>();
    zaakIds.forEach(id -> set.add(leftPad(removePattern(id, "\\D"), 9, "0")));
    return set;
  }
}
