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
import static nl.procura.standard.Globalfunctions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.In;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.jpa.personen.utils.CriteriaWrapper;

public class DossDao extends ZaakDao {

  public static final String C_DOSS          = "cDoss";
  public static final String DOSS_ID         = "dossiernummer";
  public static final String D_IN            = "dIn";
  public static final String D_AANVR         = "dAanvr";
  public static final String T_AANVR         = "tAanvr";
  public static final String TYPE            = "typeDoss";
  public static final String AKTE_VNR        = "akteVnr";
  public static final String VNR             = "vnr";
  public static final String DOSS            = "doss";
  public static final String PARENT_DOSS_PER = "parentDossPer";
  public static final String DOSS_PER        = "dossPer";
  public static final String TYPE_PERSOON    = "typePersoon";

  private static final BigDecimal TYPE_AANGEVER         = toBigDecimal(10);
  private static final BigDecimal TYPE_OVERLEDENE       = toBigDecimal(15);
  private static final BigDecimal TYPE_VADER_DUO_MOEDER = toBigDecimal(30);
  private static final BigDecimal TYPE_MOEDER           = toBigDecimal(40);
  private static final BigDecimal TYPE_PARTNER          = toBigDecimal(50);
  private static final BigDecimal TYPE_ERKENNER         = toBigDecimal(60);
  private static final BigDecimal TYPE_PARTNER1         = toBigDecimal(70);
  private static final BigDecimal TYPE_PARTNER2         = toBigDecimal(71);
  private static final BigDecimal TYPE_BETROKKENE       = toBigDecimal(75);
  private static final BigDecimal TYPE_INSCHRIJVER      = toBigDecimal(80);
  private static final BigDecimal TYPE_GERELATEERDE_BRP = toBigDecimal(81);

  private static final long TYPE_GEBOORTE      = ZaakType.GEBOORTE.getCode();
  private static final long TYPE_ERKENNING     = ZaakType.ERKENNING.getCode();
  private static final long TYPE_HUWELIJK      = ZaakType.HUWELIJK_GPS_GEMEENTE.getCode();
  private static final long TYPE_OVERLIJDEN    = ZaakType.OVERLIJDEN_IN_GEMEENTE.getCode();
  private static final long TYPE_OVERLIJDEN_B  = ZaakType.OVERLIJDEN_IN_BUITENLAND.getCode();
  private static final long TYPE_LIJKVINDING   = ZaakType.LIJKVINDING.getCode();
  private static final long TYPE_LEVENLOOS     = ZaakType.LEVENLOOS.getCode();
  private static final long TYPE_OMZETTING     = ZaakType.OMZETTING_GPS.getCode();
  private static final long TYPE_ONTBINDING    = ZaakType.ONTBINDING_GEMEENTE.getCode();
  private static final long TYPE_NAAMSKEUZE    = ZaakType.NAAMSKEUZE.getCode();
  private static final long TYPE_ONDERZOEK     = ZaakType.ONDERZOEK.getCode();
  private static final long TYPE_RISK_ANALYSIS = ZaakType.RISK_ANALYSIS.getCode();
  private static final long TYPE_REGISTRATION  = ZaakType.REGISTRATION.getCode();

  public static List<ZaakKey> findZaakKeys(ConditionalMap map, QueryListener queryListener) {

    CriteriaWrapper<Doss, ZaakKey> w = new CriteriaWrapper<>(Doss.class, ZaakKey.class, map, queryListener);
    Path<Object> zaakId = w.getTable().get(ZAAK_ID);
    Path<Object> dAanv = w.getTable().get(D_IN);
    Path<Object> dOpn = w.getTable().get(D_AANVR);
    Path<Object> tOpn = w.getTable().get(T_AANVR);
    Path<Object> typeDoss = w.getTable().get(TYPE_DOSS);

    w.getCq()
        .select(w.getBuilder().construct(ZaakKey.class, zaakId, dAanv, dOpn, tOpn, typeDoss))
        .orderBy(getOrder(w, D_IN, D_AANVR, T_AANVR, C_DOSS));

    find(w);

    return w.getResultList();
  }

  public static int findCount(ConditionalMap map, QueryListener queryListener) {

    CriteriaWrapper<Doss, Long> w = new CriteriaWrapper<>(Doss.class, Long.class, map, queryListener);

    w.getCq().select(w.getBuilder().count(w.getTable().get(ZAAK_ID)));

    find(w);

    List<?> list = w.getEm().createQuery(w.getCq()).getResultList();

    return aval(list.get(0));
  }

  public static List<Doss> find(ConditionalMap map, QueryListener queryListener) {

    CriteriaWrapper<Doss, Doss> w = new CriteriaWrapper<>(Doss.class, Doss.class, map, queryListener);

    find(w);

    w.getCq().orderBy(getOrder(w, D_IN, D_AANVR, T_AANVR, C_DOSS));
    TypedQuery<Doss> query = w.getEm().createQuery(w.getCq());
    return get(query, map);
  }

  private static void find(CriteriaWrapper<Doss, ?> w) {

    CriteriaBuilder builder = w.getBuilder();
    Root<Doss> table = w.getTable();
    CriteriaQuery query = w.getCq();
    ConditionalMap map = w.getMap();
    QueryListener queryListener = w.getQueryListener();

    List<Predicate> where = new ArrayList<>();
    where.add(builder.greaterThan(table.get(C_DOSS), 0));

    List<Predicate> zakenPredicates = new ArrayList<>();

    // Moet een verwijzing hebben naar minimaal één van deze tabellen
    if (isType(map, TYPE_GEBOORTE)) {
      zakenPredicates.add(getSubZaak(builder, query, table, DossGeb.class, TYPE_GEBOORTE));
    }

    if (isType(map, TYPE_ERKENNING)) {
      zakenPredicates.add(getSubZaak(builder, query, table, DossErk.class, TYPE_ERKENNING));
    }

    if (isType(map, TYPE_NAAMSKEUZE)) {
      zakenPredicates.add(getSubZaak(builder, query, table, DossNk.class, TYPE_NAAMSKEUZE));
    }

    if (isType(map, TYPE_HUWELIJK)) {
      zakenPredicates.add(getSubZaak(builder, query, table, DossHuw.class, TYPE_HUWELIJK));
    }

    if (isType(map, TYPE_OVERLIJDEN)) {
      zakenPredicates.add(getSubZaak(builder, query, table, DossOverl.class, TYPE_OVERLIJDEN));
    }

    if (isType(map, TYPE_OVERLIJDEN_B)) {
      zakenPredicates.add(getSubZaak(builder, query, table, DossOverl.class, TYPE_OVERLIJDEN_B));
    }

    if (isType(map, TYPE_LIJKVINDING)) {
      zakenPredicates.add(getSubZaak(builder, query, table, DossOverl.class, TYPE_LIJKVINDING));
    }

    if (isType(map, TYPE_LEVENLOOS)) {
      zakenPredicates.add(getSubZaak(builder, query, table, DossGeb.class, TYPE_LEVENLOOS));
    }

    if (isType(map, TYPE_OMZETTING)) {
      zakenPredicates.add(getSubZaak(builder, query, table, DossOmzet.class, TYPE_OMZETTING));
    }

    if (isType(map, TYPE_ONTBINDING)) {
      zakenPredicates.add(getSubZaak(builder, query, table, DossOntb.class, TYPE_ONTBINDING));
    }

    if (isType(map, TYPE_ONDERZOEK)) {
      zakenPredicates.add(getSubZaak(builder, query, table, DossOnderz.class, TYPE_ONDERZOEK));
    }

    if (isType(map, TYPE_RISK_ANALYSIS)) {
      zakenPredicates.add(getSubZaak(builder, query, table, DossRiskAnalysis.class, TYPE_RISK_ANALYSIS));
    }

    if (isType(map, TYPE_REGISTRATION)) {
      zakenPredicates.add(getSubZaak(builder, query, table, DossRegistration.class, TYPE_REGISTRATION));
    }

    if (!zakenPredicates.isEmpty()) {
      where.add(builder.or(zakenPredicates.toArray(new Predicate[0])));
    }

    if (!map.containsKey(BSN) && map.containsKey(ANR)) {
      // Workaround because some PL's dont have a BSN.
      // This caused the application the to return no records
      map.put(BSN, "123456789");
    }

    if (map.containsKey(BSN)) {

      List<In<Object>> persoonPredicates = new ArrayList<>();

      if (isType(map, TYPE_GEBOORTE)) {
        persoonPredicates.add(getPersoon(builder, query, table, map, TYPE_GEBOORTE, TYPE_AANGEVER, TYPE_MOEDER,
            TYPE_VADER_DUO_MOEDER));
      }

      if (isType(map, TYPE_ERKENNING)) {
        persoonPredicates.add(getPersoon(builder, query, table, map, TYPE_ERKENNING, TYPE_AANGEVER, TYPE_MOEDER,
            TYPE_ERKENNER));
      }

      if (isType(map, TYPE_HUWELIJK)) {
        persoonPredicates.add(getPersoon(builder, query, table, map,
            TYPE_HUWELIJK, TYPE_PARTNER1, TYPE_PARTNER2));
      }

      if (isType(map, TYPE_OMZETTING)) {
        persoonPredicates.add(getPersoon(builder, query, table, map,
            TYPE_OMZETTING, TYPE_PARTNER1, TYPE_PARTNER2));
      }

      if (isType(map, TYPE_ONTBINDING)) {
        persoonPredicates.add(getPersoon(builder, query, table, map,
            TYPE_ONTBINDING, TYPE_PARTNER1, TYPE_PARTNER2));
      }

      if (isType(map, TYPE_OVERLIJDEN)) {
        persoonPredicates.add(getPersoon(builder, query, table, map,
            TYPE_OVERLIJDEN, TYPE_AANGEVER, TYPE_OVERLEDENE));
      }

      if (isType(map, TYPE_OVERLIJDEN_B)) {
        persoonPredicates.add(getPersoon(builder, query, table, map,
            TYPE_OVERLIJDEN_B, TYPE_AANGEVER, TYPE_OVERLEDENE));
      }

      if (isType(map, TYPE_LIJKVINDING)) {
        persoonPredicates.add(getPersoon(builder, query, table, map,
            TYPE_LIJKVINDING, TYPE_OVERLEDENE));
      }

      if (isType(map, TYPE_LEVENLOOS)) {
        persoonPredicates.add(getPersoon(builder, query, table, map,
            TYPE_LEVENLOOS, TYPE_AANGEVER));
      }

      if (isType(map, TYPE_NAAMSKEUZE)) {
        persoonPredicates.add(getPersoon(builder, query, table, map,
            TYPE_NAAMSKEUZE, TYPE_MOEDER, TYPE_PARTNER));
      }

      if (isType(map, TYPE_ONDERZOEK)) {
        persoonPredicates.add(getPersoon(builder, query, table, map,
            TYPE_ONDERZOEK, TYPE_AANGEVER, TYPE_BETROKKENE));
      }

      if (isType(map, TYPE_RISK_ANALYSIS)) {
        persoonPredicates.add(getPersoon(builder, query, table, map,
            TYPE_RISK_ANALYSIS, TYPE_BETROKKENE));
      }

      if (isType(map, TYPE_REGISTRATION)) {
        persoonPredicates.add(getPersoon(builder, query, table, map,
            TYPE_REGISTRATION, TYPE_INSCHRIJVER, TYPE_GERELATEERDE_BRP));
      }

      where.add(builder.or(persoonPredicates.toArray(new In[zakenPredicates.size()])));
    }

    if (map.containsKey(DOSS_ID)) {
      Expression<Boolean> predicate1 = getZaakIds(builder, table, map);
      Expression<Boolean> predicate2 = getZaakIds(builder, table, map, DOSS_ID);

      where.add(builder.and(builder.or(predicate1, predicate2)));
    }

    if (map.containsKey(C_DOSS)) {
      where.add(builder.equal(table.get(C_DOSS), map.get(C_DOSS)));
    }

    if (map.containsKey(IND_VERWERKT)) {
      where.add(getStatussen(table, map));
    }

    if (map.containsKey(NIET_IND_VERWERKT)) {
      where.add(builder.not(getNegeerStatussen(table, map)));
    }

    ge(where, builder, map, table.<Long> get(D_AANVR), D_INVOER_VANAF);
    le(where, builder, map, table.<Long> get(D_AANVR), D_INVOER_TM);

    ge(where, builder, map, table.<Long> get(D_IN), D_INGANG_VANAF);
    le(where, builder, map, table.<Long> get(D_IN), D_INGANG_TM);

    if (map.containsKey(AKTE_VNR)) {

      Subquery<DossAkte> dossAkteQuery = query.subquery(DossAkte.class);
      Root<DossAkte> dossAkte = dossAkteQuery.from(DossAkte.class);
      dossAkteQuery.select(dossAkte.get(DOSS).get(C_DOSS));
      dossAkteQuery.where(builder.equal(dossAkte.get(VNR), astr(map.get(AKTE_VNR)).toUpperCase()));
      where.add(builder.in(table.get(C_DOSS)).value(dossAkteQuery));
    }

    // Niet mogelijk voor deze zaak
    if (map.containsKeys(D_END_TERMIJN, D_AFHAAL_VANAF, D_AFHAAL_TM)) {
      where.add(builder.lessThan(table.get(C_DOSS), 0));
    }

    getMutDate(where, table, query, builder, map);
    getUsr(ZaakDao.USR, where, table, builder, map);
    getProfile(ZaakDao.USR, where, table, builder, map);
    getAttribute(query, where, table, builder, map);

    if (queryListener != null) {
      queryListener.addToQuery(query, table, where, builder);
    }

    query.where(where.toArray(new Predicate[0]));
  }

  @SuppressWarnings("unchecked")
  private static boolean isType(ConditionalMap map, long... codes) {
    if (map.containsKey(TYPE_DOSS)) {
      List<Long> typeCodes = (List<Long>) map.get(TYPE_DOSS);
      for (long typeCode : typeCodes) {
        for (long code : codes) {
          if (typeCode == code) {
            return true;
          }
        }
      }
      return false;
    }

    return true;
  }

  @SuppressWarnings("unchecked")
  protected static Predicate getSubZaak(CriteriaBuilder b, CriteriaQuery<?> q, Root<Doss> t, Class c, Long... types) {
    Subquery subQuery = q.subquery(c);
    Root root = subQuery.from(c);
    subQuery.select(root.get(DOSS).get(C_DOSS));
    return b.and(t.get(TYPE_DOSS).in(asList(types)), b.in(t.get(C_DOSS)).value(subQuery));
  }

  private static In<Object> getPersoon(CriteriaBuilder b, CriteriaQuery<?> q, Root<Doss> t, ConditionalMap m,
      long typeZaak, BigDecimal... typePersonen) {

    Subquery<DossPer> persQuery = q.subquery(DossPer.class);
    Root pers = persQuery.from(DossPer.class);
    persQuery.select(pers.get(DOSS).get(C_DOSS));

    Predicate isTypeZaak = b.equal(pers.get(DOSS).get(TYPE), typeZaak);
    Predicate isTypePersoon = pers.get(TYPE_PERSOON).in(asList(typePersonen));
    Predicate isBsn = b.equal(pers.get(BSN), m.get(BSN));
    Predicate isAnr = b.equal(pers.get(ANR), m.get(ANR));

    persQuery.where(b.and(isTypeZaak, isTypePersoon, b.or(isBsn, isAnr)));
    return b.in(t.get(C_DOSS)).value(persQuery);
  }
}
