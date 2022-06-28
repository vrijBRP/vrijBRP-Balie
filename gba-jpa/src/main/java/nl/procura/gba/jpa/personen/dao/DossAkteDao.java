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
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.SELECT;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.jpa.personen.db.DossAkte;
import nl.procura.gba.jpa.personen.db.DossAkteRd;
import nl.procura.gba.jpa.personen.db.DossAkteRdCat;
import nl.procura.gba.jpa.personen.db.DossAkte_;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.standard.ProcuraDate;
import nl.procura.standard.exceptions.ProException;

public class DossAkteDao extends ZaakDao {

  public static final String C_DOSS_AKTE   = "cDossAkte";
  public static final String VNR           = "vnr";
  public static final String DEEL          = "registerdeel";
  public static final String SOORT         = "registersoort";
  public static final String INVOER_TYPE   = "invType";
  public static final String DATUM_FEIT    = "dFeit";
  public static final String DATUM_AKTE    = "dIn";
  public static final String JAAR          = "jaar";
  public static final String NAAM          = "naam";
  public static final String VOORN         = "voorn";
  public static final String GEBOORTEDATUM = "gebooortedatum";
  public static final String OPMERKING     = "opmerking";
  public static final String LIMIT         = "limit";
  public static final String GROEP_ID      = "groepId";

  private static long getJaar(long datum) {
    return along(new ProcuraDate(astr(datum)).getYear());
  }

  public static Long findCount(ConditionalMap map) {
    EntityManager em = GbaJpa.getManager();
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Long> cq = builder.createQuery(Long.class);
    Root<DossAkte> table = cq.from(DossAkte.class);
    cq = cq.select(builder.count(table.get(DossAkte_.cDossAkte)));
    find(builder, cq, table, map);
    return em.createQuery(cq).getSingleResult();
  }

  public static List<DossAkte> find(ConditionalMap map) {
    EntityManager em = GbaJpa.getManager();
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<DossAkte> cq = builder.createQuery(DossAkte.class);
    Root<DossAkte> table = cq.from(DossAkte.class);
    find(builder, cq, table, map);
    TypedQuery<DossAkte> query = em.createQuery(cq);
    if (map.containsKey(LIMIT) && pos(map.get(LIMIT))) {
      query = query.setMaxResults(aval(map.get(LIMIT)));
    }
    return query.getResultList();
  }

  private static void find(CriteriaBuilder builder, CriteriaQuery<?> query, Root<?> table, ConditionalMap map) {
    List<Predicate> where = new ArrayList<>();
    where.add(builder.greaterThan(table.get(C_DOSS_AKTE), 0));
    where.add(builder.greaterThan(table.get(JAAR), 0));
    where.add(builder.greaterThan(table.get(VNR), 0));

    if (map.containsKey(INVOER_TYPE)) {
      where.add(builder.equal(table.get(INVOER_TYPE), map.get(INVOER_TYPE)));
    }

    if (map.containsKey(DATUM_FEIT)) {
      where.add(builder.equal(table.get(DATUM_FEIT), map.get(DATUM_FEIT)));
    }

    if (map.containsKey(DATUM_AKTE)) {
      where.add(builder.equal(table.get(DATUM_AKTE), map.get(DATUM_AKTE)));
    }
    if (map.containsKey(SOORT)) {
      where.add(table.get(SOORT).in((List<Long>) map.get(SOORT)));
    }

    if (map.containsKey(DEEL)) {
      where.add(builder.equal(table.get(DEEL), map.get(DEEL)));
    }

    if (map.containsKey(VNR)) {
      where.add(builder.equal(table.get(VNR), map.get(VNR)));
    }

    if (map.containsKey(BSN)) {
      Predicate bsn1 = builder.equal(table.get(BSN), map.get(BSN));
      Predicate bsn2 = builder.equal(table.get("pBsn"), map.get(BSN));
      where.add(builder.or(bsn1, bsn2));
    }

    if (map.containsKey(NAAM)) {
      Predicate naam1 = builder.like(builder.lower(table.get(DossAkte_.GESLACHTSNAAM)), getWildcard(map.get(NAAM)));
      Predicate naam2 = builder.like(builder.lower(table.get(DossAkte_.P_GESLACHTSNAAM)), getWildcard(map.get(NAAM)));
      where.add(builder.or(naam1, naam2));
    }

    if (map.containsKey(VOORN)) {
      Predicate naam1 = builder.like(builder.lower(builder.function("unaccent",
          String.class, table.get(DossAkte_.VOORN))),
          getWildcard(map.get(VOORN)));
      Predicate naam2 = builder.like(builder.lower(builder.function("unaccent",
          String.class, table.get(DossAkte_.P_VOORN))),
          getWildcard(map.get(VOORN)));
      where.add(builder.or(naam1, naam2));
    }

    if (map.containsKey(OPMERKING)) {
      where.add(builder.and(builder.like(builder.lower(table.get(DossAkte_.OPM)), getWildcard(map.get(OPMERKING)))));
    }

    if (map.containsKey(GEBOORTEDATUM)) {
      Predicate date1 = builder.equal(table.get(DossAkte_.D_GEB), along(map.get(GEBOORTEDATUM)));
      Predicate date2 = builder.equal(table.get(DossAkte_.P_DGEB), along(map.get(GEBOORTEDATUM)));
      where.add(builder.or(date1, date2));
    }

    if (map.containsKey(JAAR)) {
      where.add(builder.and(builder.equal(table.<Long> get(JAAR), along(map.get(JAAR)))));
    }

    if (map.containsKey(D_INVOER_VANAF)) {
      where.add(builder.and(builder.ge(table.<Long> get(DATUM_FEIT), along(map.get(D_INVOER_VANAF)))));
    }

    if (map.containsKey(D_INVOER_TM)) {
      where.add(builder.and(builder.le(table.<Long> get(DATUM_FEIT), along(map.get(D_INVOER_TM)))));
    }

    if (map.containsKey(GROEP_ID)) {
      where.add(builder.and(builder.like(table.get(DossAkte_.AKTE_GROEP_ID), astr(map.get(GROEP_ID)))));
    }

    query.where(where.toArray(new Predicate[where.size()]));
  }

  private static String getWildcard(Object type) {
    return "%" + type.toString().toLowerCase() + "%";
  }

  public static boolean exists(long cDoss, long datum, long registersoort, String registerdeel, long vnr) {
    StringBuilder sql = new StringBuilder();
    sql.append("select count (a) from DossAkte a ");
    sql.append("where a.jaar          = :jaar ");
    sql.append("  and a.registersoort = :registersoort ");
    sql.append("  and a.registerdeel  = :registerdeel ");
    sql.append("  and a.vnr           = :vnr ");
    sql.append("  and a.cDossAkte    != :cDoss");

    Query q = GenericDao.createQuery(sql.toString());
    q.setParameter("jaar", getJaar(datum));
    q.setParameter("registersoort", registersoort);
    q.setParameter("registerdeel", registerdeel);
    q.setParameter("vnr", vnr);
    q.setParameter("cDoss", cDoss);

    return pos(q.getResultList().get(0));
  }

  public static long getHoogsteVanVoorVandaag(long cDoss, long datum, long registersoort, String registerdeel) {

    StringBuilder sql = new StringBuilder();
    sql.append("select max (a.vnr) from DossAkte a ");
    sql.append(" where a.jaar          = :jaar");
    sql.append("   and a.registersoort = :registersoort");
    sql.append("   and a.registerdeel  = :registerdeel");
    sql.append("   and a.dIn           >  0");
    sql.append("   and a.dIn           < :dIn");
    sql.append("   and a.cDossAkte    != :cDoss");

    TypedQuery<BigDecimal> q = GenericDao.createQuery(sql.toString(), BigDecimal.class);
    q.setParameter("jaar", getJaar(datum));
    q.setParameter("registersoort", registersoort);
    q.setParameter("registerdeel", registerdeel);
    q.setParameter("dIn", datum);
    q.setParameter("cDoss", cDoss);

    BigDecimal vnr = q.getResultList().get(0);

    return pos(vnr) ? along(vnr) : 0;
  }

  public static long getLaagsteVanafMorgen(long cDoss, long datum, long registersoort, String registerdeel) {

    StringBuilder sql = new StringBuilder();
    sql.append("select min (a.vnr) from DossAkte a ");
    sql.append(" where a.jaar          = :jaar");
    sql.append("   and a.registersoort = :registersoort");
    sql.append("   and a.registerdeel  = :registerdeel");
    sql.append("   and a.dIn           >  0");
    sql.append("   and a.dIn           > :dIn");
    sql.append("   and a.cDossAkte    != :cDoss");

    TypedQuery<BigDecimal> q = GenericDao.createQuery(sql.toString(), BigDecimal.class);
    q.setParameter("jaar", getJaar(datum));
    q.setParameter("registersoort", registersoort);
    q.setParameter("registerdeel", registerdeel);
    q.setParameter("dIn", datum);
    q.setParameter("cDoss", cDoss);

    BigDecimal vnr = q.getResultList().get(0);

    return pos(vnr) ? along(vnr) : 0;
  }

  public static long getHuidigVolgnr(long codeAkte, long registersoort, String registerdeel) {

    StringBuilder sql = new StringBuilder();
    sql.append("select a.vnr from DossAkte a ");
    sql.append(" where a.cDossAkte     = :cDossAkte");
    sql.append("   and a.registersoort = :registersoort");
    sql.append("   and a.registerdeel  = :registerdeel");

    TypedQuery<BigDecimal> q = GenericDao.createQuery(sql.toString(), BigDecimal.class);
    q.setParameter("cDossAkte", codeAkte);
    q.setParameter("registersoort", registersoort);
    q.setParameter("registerdeel", registerdeel);

    for (BigDecimal bd : q.getResultList()) {
      return along(bd);
    }

    return 0;
  }

  public static void checkBruikbaarVolgnr(long vnr, long codeAkte, long datum, long registersoort,
      String registerdeel) {

    long gisteren = DossAkteDao.getHoogsteVanVoorVandaag(codeAkte, datum, registersoort, registerdeel);
    long morgen = DossAkteDao.getLaagsteVanafMorgen(codeAkte, datum, registersoort, registerdeel);

    if (DossAkteDao.teLaag(registersoort, registerdeel, vnr)) {
      String msg = "Volgnummer %d valt niet binnen het bereik van dit registerdeel,";
      throw new ProException(SELECT, WARNING, String.format(msg, vnr));
    }

    if (DossAkteDao.teHoog(registersoort, registerdeel, vnr)) {
      String msg = "Volgnummer %d valt buiten het bereik van dit registerdeel. Het registerdeel is waarschijnlijk vol.";
      throw new ProException(SELECT, WARNING, String.format(msg, vnr));
    }

    if (DossAkteDao.exists(codeAkte, datum, registersoort, registerdeel, vnr)) {
      throw new ProException(SELECT, WARNING, String.format("Volgnummer %d is al in gebruik.", vnr));
    }

    if (morgen > 0 && morgen < vnr) {
      String msg = "Volgnummer %d is hoger dan %d dat later is uitgegeven.";
      throw new ProException(SELECT, WARNING, String.format(msg, vnr, morgen));
    }

    if (gisteren > vnr) {
      String msg = "Volgnummer %d is lager dan %d dat eerder is uitgegeven.";
      throw new ProException(SELECT, WARNING, String.format(msg, vnr, gisteren));
    }
  }

  public static long getAkteVolgnr(long codeAkte, long datum, long registersoort, String registerdeel) {

    long vnr = getHuidigVolgnr(codeAkte, registersoort, registerdeel);

    if (pos(vnr)) {
      return vnr;
    }

    long gisteren = getHoogsteVanVoorVandaag(codeAkte, datum, registersoort, registerdeel);
    long morgen = getLaagsteVanafMorgen(codeAkte, datum, registersoort, registerdeel);

    if (gisteren > 0 && morgen > 0 && (morgen - gisteren) < 2) {
      throw new ProException(WARNING, "Geen tussenliggend volgnummer beschikbaar voor dit registerdeel op {0}.",
          date2str(datum));
    }

    if (gisteren > 0) {
      vnr = gisteren;
    }

    DossAkteRd minMax = getAkteDeel(registersoort, registerdeel);

    if (minMax != null) {

      long min = along(minMax.getMin());

      // Zet volgnr minimaal op min.
      vnr = (vnr < min) ? min : vnr;

      // Zoek naar vrije code
      while (exists(0, datum, registersoort, registerdeel, vnr)) {
        vnr++;
      }

      if (morgen > 0 && vnr > morgen) {
        throw new ProException(WARNING, "Geen volgnummer beschikbaar voor dit registerdeel op {0}.", date2str(datum));
      }
    }

    return vnr;
  }

  public static DossAkteRd getAkteDeel(long registerSoort, String registerDeel) {
    DossAkteRd rd = new DossAkteRd();
    rd.setRegistersoort(toBigDecimal(registerSoort));
    rd.setRegisterdeel(registerDeel);
    return findByExample(rd).stream().findFirst().orElse(null);
  }

  public static List<DossAkteRdCat> getAkteLocaties() {
    return findByExample(new DossAkteRdCat());
  }

  public static List<DossAkteRd> getAkteDelenAhvCategorie(Long categorieCode) {

    StringBuilder sql = new StringBuilder();
    sql.append("select r from DossAkteRd r ");
    sql.append("where r.dossAkteRdCat.cDossAkteRdCat = :code");
    TypedQuery<DossAkteRd> q = GenericDao.createQuery(sql.toString(), DossAkteRd.class);

    q.setParameter("code", categorieCode);
    return q.getResultList();
  }

  public static List<DossAkteRd> getAkteDelenAhvRegistersoort(long registerSoort) {

    StringBuilder sql = new StringBuilder();
    sql.append("select r from DossAkteRd r ");
    sql.append("where r.registersoort = :registersoort ");
    TypedQuery<DossAkteRd> q = GenericDao.createQuery(sql.toString(), DossAkteRd.class);

    q.setParameter("registersoort", registerSoort);
    return q.getResultList();
  }

  public static boolean teLaag(long registersoort, String registerdeel, long vnr) {
    DossAkteRd minMax = getAkteDeel(registersoort, registerdeel);
    return minMax == null || vnr < aval(minMax.getMin());
  }

  public static boolean teHoog(long registersoort, String registerdeel, long vnr) {
    DossAkteRd minMax = getAkteDeel(registersoort, registerdeel);
    return minMax != null && vnr > aval(minMax.getMax());
  }

  public static boolean eersteVolgendeVolgnr(long codeAkte, long dIn, long registersoort, String registerdeel,
      long vnr) {
    return vnr == getAkteVolgnr(codeAkte, dIn, registersoort, registerdeel);
  }

  public static List<BigDecimal> getJaren() {

    StringBuilder sql = new StringBuilder();
    sql.append("select distinct (a.jaar) from DossAkte a ");
    sql.append("where a.jaar > 0 ");
    sql.append("order by a.jaar desc ");

    return GenericDao.createQuery(sql.toString(), BigDecimal.class).getResultList();
  }
}
