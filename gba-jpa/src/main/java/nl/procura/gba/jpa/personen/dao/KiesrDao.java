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

import static org.apache.commons.lang3.math.NumberUtils.toLong;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import nl.procura.gba.jpa.personen.db.KiesrStem;
import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.gba.jpa.personen.db.KiesrVerkInfo;
import nl.procura.gba.jpa.personen.db.KiesrWijz;
import nl.procura.validation.Anummer;

import lombok.Data;

public class KiesrDao extends GenericDao {

  public static List<KiesrVerk> getVerkiezingen() {
    return createQuery("select v from KiesrVerk v order by v.dVerk desc", KiesrVerk.class).getResultList();
  }

  public static long getAantalStempassen(KiesrStemQuery query) {
    StringBuilder sql = new StringBuilder("select count(p) from KiesrStem p where p.cKiesrVerk = :cKiesrVerk ");
    Map<String, Object> params = getSearchParams(query, sql);
    TypedQuery<Long> sqlQuery = createQuery(sql.toString(), Long.class);
    params.forEach(sqlQuery::setParameter);
    return sqlQuery.getSingleResult();
  }

  public static KiesrStemResult getStempassen(KiesrStemQuery query) {
    StringBuilder sql = new StringBuilder("select p from KiesrStem p where p.cKiesrVerk = :cKiesrVerk ");
    Map<String, Object> params = getSearchParams(query, sql);
    sql.append(" order by p.vnr " + (query.desc ? "desc" : "asc"));
    TypedQuery<KiesrStem> sqlQuery = createQuery(sql.toString(), KiesrStem.class);
    params.forEach(sqlQuery::setParameter);
    if (query.getMax() > 0) {
      sqlQuery.setMaxResults(query.getMax());
    }
    KiesrStemResult result = new KiesrStemResult();
    result.setStempassen(sqlQuery.getResultList());
    result.setAantal(getAantalStempassen(query));
    KiesrStemQuery totaalQuery = new KiesrStemQuery();
    totaalQuery.setCKiesrVerk(query.getCKiesrVerk());
    result.setTotaal(getAantalStempassen(totaalQuery));
    return result;
  }

  private static Map<String, Object> getSearchParams(KiesrStemQuery query, StringBuilder sql) {
    Map<String, Object> params = new HashMap<>();
    params.put("cKiesrVerk", query.getCKiesrVerk());
    if (query.getAand() != null) {
      if (query.getAand() == 100L) { // Alle aanduidingen
        sql.append(" and p.aand > 0");
      } else {
        sql.append(" and p.aand = :aand");
        params.put("aand", query.getAand());
      }
    }
    if (query.getVnr() != null) {
      sql.append(" and p.vnr = :vnr");
      params.put("vnr", query.getVnr());
    }
    if (query.getDAandVan() != null) {
      sql.append(" and p.dAand >= :dAandVan");
      params.put("dAandVan", query.getDAandVan());
    }
    if (query.getAnr() != null) {
      sql.append(" and p.anr = :anr");
      params.put("anr", query.getAnr());
    }
    if (query.getAnrVolmacht() != null) {
      sql.append(" and p.anrVolmacht = :anrVolmacht");
      params.put("anrVolmacht", query.getAnrVolmacht());
    }
    if (query.getDAandTm() != null) {
      sql.append(" and p.dAand > 0 and p.dAand <= :dAandTm");
      params.put("dAandTm", query.getDAandTm());
    }
    return params;
  }

  public static List<KiesrWijz> getWijzigingen(KiesrVerk verk, Anummer anummer) {
    String sql = "select p from KiesrWijz p " +
        "where p.kiesrStem.cKiesrVerk = :cKiesrVerk " +
        "and p.kiesrStem.anr = :anr " +
        "order by p.dIn desc, p.tIn desc";
    TypedQuery<KiesrWijz> query = createQuery(sql, KiesrWijz.class);
    query.setParameter("cKiesrVerk", verk.getcKiesrVerk());
    query.setParameter("anr", anummer.getLongAnummer());
    return query.getResultList();
  }

  public static List<KiesrVerkInfo> getInfo(KiesrVerk verk) {
    String sql = "select p from KiesrVerkInfo p " +
        "where p.cKiesrVerk = :cKiesrVerk " +
        "order by p.cKiesrVerkInfo asc";
    TypedQuery<KiesrVerkInfo> query = createQuery(sql, KiesrVerkInfo.class);
    query.setParameter("cKiesrVerk", verk.getcKiesrVerk());
    return query.getResultList();
  }

  public static long getVnr(KiesrStem kiesrStem) {
    String sql = "select max(p.vnr) from KiesrStem p where p.cKiesrVerk = :cKiesrVerk";
    TypedQuery<Long> query = createQuery(sql, Long.class);
    query.setParameter("cKiesrVerk", kiesrStem.getcKiesrVerk());
    return toLong("" + query.getSingleResult(), 0) + 1;
  }

  @Data
  public static class KiesrStemQuery {

    private Long    vnr;
    private Long    cKiesrVerk;
    private Long    aand;
    private Long    dAandVan;
    private Long    dAandTm;
    private int     max;
    private Long    anr;
    private Long    anrVolmacht;
    private boolean desc = false;
  }

  @Data
  public static class KiesrStemResult {

    private List<KiesrStem> stempassen;
    private long            aantal;
    private long            totaal;
  }
}
