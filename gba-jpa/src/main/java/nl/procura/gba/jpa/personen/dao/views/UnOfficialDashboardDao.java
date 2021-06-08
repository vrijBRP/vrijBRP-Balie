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

package nl.procura.gba.jpa.personen.dao.views;

import static nl.procura.gba.common.ZaakStatusType.VERWERKT;
import static nl.procura.gba.common.ZaakStatusType.VERWERKT_IN_GBA;
import static nl.procura.gba.common.ZaakType.*;
import static nl.procura.gba.jpa.personen.db.QDoss.doss;
import static nl.procura.gba.jpa.personen.db.QDossOnderz.dossOnderz;
import static nl.procura.gba.jpa.personen.db.QDossRiskAnalysis.dossRiskAnalysis;
import static nl.procura.gba.jpa.personen.db.QDossRiskAnalysisSubject.dossRiskAnalysisSubject;
import static nl.procura.gba.jpa.personen.db.QGv.gv;
import static nl.procura.gba.jpa.personen.db.QGvProce.gvProce;
import static nl.procura.gba.jpa.personen.db.QIndVerwerkt.indVerwerkt1;
import static nl.procura.gba.jpa.personen.db.QNrd.nrd;
import static nl.procura.gba.jpa.personen.db.QNrdStatus.nrdStatus;
import static nl.procura.gba.jpa.personen.db.QRiskProfile.riskProfile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.standard.ProcuraDate;

public class UnOfficialDashboardDao extends DashboardDao {

  // Toegekende gegevensverstrekking
  private static final int GV_TK_JA = 501;
  // Niet toegekende verstrekkking
  private static final int GV_TK_NEE = 502;
  // Belangenafweging
  private static final int GV_TK_BELANG = 503;

  //Overheidsorgaan, art. 3.5
  private static final int GRONDSLAG_3_5 = 101;
  //(Landelijk aangewezen) derde, art. 3.6
  private static final int GRONDSLAG_3_6 = 102;
  //(Gemeentelijk aangewezen) derde, art. 3.9
  private static final int GRONDSLAG_3_9 = 103;

  private static final int NIET_VERSTREKKEN = 202;
  private static final int NU_VERSTREKKEN   = 204;

  private static int RES_ONDERZ_IMMIGRATIE    = 2; //Vanuit het buitenland gevestigd in de gemeente
  private static int RES_ONDERZ_VERHUISD      = 3; //Verhuisd binnen de gemeente
  private static int RES_ONDERZ_EMIGRATIE     = 5; //Vertrokken naar het buitenland
  private static int RES_ONDERZ_EMIGRATIE_ONB = 6; //Vertrokken met onbekende bestemming

  /**
   * Gegevensverstrekking (totaal aantal aanvragen)
   */
  public static List<ZaakKey> getGv121(DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(gv.zaakId)
        .from(gv)
        .where(gv.cGv.gt(0)
            .and(period(gv.dIn, periode)))
        .fetch();

    return toZaakKeys(results, GEGEVENSVERSTREKKING);
  }

  /**
   * Aantal verstrekkingen totaal
   */
  public static List<ZaakKey> getGv122(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(gv.zaakId)
        .from(gv)
        .where(gv.cGv.gt(0)
            .and(period(gv.dIn, periode))
            .and(gv.cToek.in(GV_TK_JA)
                .or(gv.in(JPAExpressions.select(gvProce.gv)
                    .from(gvProce)
                    .where(gvProce.cProcesactie
                        .in(NU_VERSTREKKEN))))))
        .fetch();

    return toZaakKeys(results, GEGEVENSVERSTREKKING);
  }

  /**
   * Overheidsorgaan art. 3.5
   */
  public static List<ZaakKey> getGv123(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(gv.zaakId)
        .from(gv)
        .where(gv.cGv.gt(0)
            .and(period(gv.dIn, periode))
            .and(gv.cGrondslag.in(GRONDSLAG_3_5)))
        .fetch();

    return toZaakKeys(results, GEGEVENSVERSTREKKING);
  }

  /**
   * Derde art. 3.6
   */
  public static List<ZaakKey> getGv124(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(gv.zaakId)
        .from(gv)
        .where(gv.cGv.gt(0)
            .and(period(gv.dIn, periode))
            .and(gv.cGrondslag.in(GRONDSLAG_3_6)))
        .fetch();

    return toZaakKeys(results, GEGEVENSVERSTREKKING);
  }

  /**
   * Derde art. 3.9
   */
  public static List<ZaakKey> getGv125(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(gv.zaakId)
        .from(gv)
        .where(gv.cGv.gt(0)
            .and(period(gv.dIn, periode))
            .and(gv.cGrondslag.in(GRONDSLAG_3_9)))
        .fetch();

    return toZaakKeys(results, GEGEVENSVERSTREKKING);
  }

  /**
   * Aantal aanvragen met een belangenafweging
   */
  public static List<ZaakKey> getGv126(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(gv.zaakId)
        .from(gv)
        .where(gv.cGv.gt(0)
            .and(period(gv.dIn, periode))
            .and(gv.cToek.in(GV_TK_BELANG)))
        .fetch();

    return toZaakKeys(results, GEGEVENSVERSTREKKING);
  }

  /**
   * Aantal verstrekkingen na belangenafweging
   */
  public static List<ZaakKey> getGv127(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(gv.zaakId)
        .from(gv)
        .where(gv.cGv.gt(0)
            .and(period(gv.dIn, periode))
            .and(gv.cToek.in(GV_TK_BELANG))
            .and(gv.in(JPAExpressions.select(gvProce.gv)
                .from(gvProce)
                .where(gvProce.cProcesactie
                    .in(NU_VERSTREKKEN)))))
        .fetch();

    return toZaakKeys(results, GEGEVENSVERSTREKKING);
  }

  /**
   * Aantal weigeringen na belangenafweging
   */
  public static List<ZaakKey> getGv128(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(gv.zaakId)
        .from(gv)
        .where(gv.cGv.gt(0)
            .and(period(gv.dIn, periode))
            .and(gv.cToek.in(GV_TK_BELANG))
            .and(gv.in(JPAExpressions.select(gvProce.gv)
                .from(gvProce)
                .where(gvProce.cProcesactie
                    .in(NIET_VERSTREKKEN)))))
        .fetch();

    return toZaakKeys(results, GEGEVENSVERSTREKKING);
  }

  /**
   * Aantal directe weigeringen
   */
  public static List<ZaakKey> getGv129(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(gv.zaakId)
        .from(gv)
        .where(gv.cGv.gt(0)
            .and(period(gv.dIn, periode))
            .and(gv.cToek.in(GV_TK_NEE)))
        .fetch();

    return toZaakKeys(results, GEGEVENSVERSTREKKING);
  }

  /**
   * Aantal inschrijvingen als resultaat van adresonderzoek
   */
  public static List<ZaakKey> getAddressInv131(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(doss.zaakId)
        .from(doss)
        .where(doss.typeDoss.eq(BigDecimal.valueOf(ONDERZOEK.getCode()))
            .and(period(doss.dAanvr, periode))
            .and(doss.cDoss.in(JPAExpressions.select(dossOnderz.cDossOnderz)
                .from(dossOnderz)
                .where(dossOnderz.resOnderzBetrok
                    .in(RES_ONDERZ_IMMIGRATIE)))))
        .fetch();

    return toZaakKeys(results, ONDERZOEK);
  }

  /**
   * Aantal uitschrijvingen als resultaat van adresonderzoek
   */
  public static List<ZaakKey> getAddressInv132(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(doss.zaakId)
        .from(doss)
        .where(doss.typeDoss.eq(BigDecimal.valueOf(ONDERZOEK.getCode()))
            .and(period(doss.dAanvr, periode))
            .and(doss.cDoss.in(JPAExpressions.select(dossOnderz.cDossOnderz)
                .from(dossOnderz)
                .where(dossOnderz.resOnderzBetrok
                    .in(RES_ONDERZ_EMIGRATIE, RES_ONDERZ_EMIGRATIE_ONB)))))
        .fetch();

    return toZaakKeys(results, ONDERZOEK);
  }

  /**
   * Aantal verhuizingen als resultaat van adresonderzoek
   */
  public static List<ZaakKey> getAddressInv133(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(doss.zaakId)
        .from(doss)
        .where(doss.typeDoss.eq(BigDecimal.valueOf(ONDERZOEK.getCode()))
            .and(period(doss.dAanvr, periode))
            .and(doss.cDoss.in(JPAExpressions.select(dossOnderz.cDossOnderz)
                .from(dossOnderz)
                .where(dossOnderz.resOnderzBetrok
                    .in(RES_ONDERZ_VERHUISD)))))
        .fetch();

    return toZaakKeys(results, ONDERZOEK);
  }

  /**
   * Aantal uitgevoerde risicoanalyses
   */
  public static List<ZaakKey> getRiskAnalysis141(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(doss.zaakId)
        .from(doss)
        .where(doss.typeDoss.eq(BigDecimal.valueOf(RISK_ANALYSIS.getCode()))
            .and(period(doss.dAanvr, periode))
            .and(doss.cDoss.in(JPAExpressions.select(dossRiskAnalysis.cDossRa)
                .from(dossRiskAnalysis))))
        .fetch();

    return toZaakKeys(results, RISK_ANALYSIS);
  }

  /**
   * Aantal uitgevoerde risicoanalyses beneden drempelwaarde
   */
  public static List<ZaakKey> getRiskAnalysis142(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(doss.zaakId)
        .from(doss, dossRiskAnalysis, riskProfile)
        .where(doss.eq(dossRiskAnalysis.doss)
            .and(dossRiskAnalysis.riskProfile.eq(riskProfile))
            .and(doss.typeDoss.eq(BigDecimal.valueOf(RISK_ANALYSIS.getCode())))
            .and(period(doss.dAanvr, periode))
            .and(doss.cDoss.notIn(JPAExpressions.select(dossRiskAnalysisSubject.dossRiskAnalysis.cDossRa)
                .from(dossRiskAnalysisSubject)
                .where(dossRiskAnalysisSubject.score.gt(riskProfile.threshold)))))
        .fetch();

    return toZaakKeys(results, RISK_ANALYSIS);
  }

  /**
   * Aantal uitgevoerde risicoanalyses boven drempelwaarde
   */
  public static List<ZaakKey> getRiskAnalysis143(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(doss.zaakId)
        .from(doss, dossRiskAnalysis, riskProfile)
        .where(doss.eq(dossRiskAnalysis.doss)
            .and(dossRiskAnalysis.riskProfile.eq(riskProfile))
            .and(doss.typeDoss.eq(BigDecimal.valueOf(RISK_ANALYSIS.getCode())))
            .and(period(doss.dAanvr, periode))
            .and(doss.cDoss.in(JPAExpressions.select(dossRiskAnalysisSubject.dossRiskAnalysis.cDossRa)
                .from(dossRiskAnalysisSubject)
                .where(dossRiskAnalysisSubject.score.gt(riskProfile.threshold)))))
        .fetch();

    return toZaakKeys(results, RISK_ANALYSIS);
  }

  /**
   * Aantal uitgevoerde risicoanalyses boven drempelwaarde en gekoppelde zaak alsnog verwerkt
   */
  public static List<ZaakKey> getRiskAnalysis144(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(doss.zaakId)
        .from(doss, dossRiskAnalysis, riskProfile)
        .where(doss.eq(dossRiskAnalysis.doss)
            .and(dossRiskAnalysis.riskProfile.eq(riskProfile))
            .and(doss.typeDoss.eq(BigDecimal.valueOf(RISK_ANALYSIS.getCode())))
            .and(period(doss.dAanvr, periode))
            .and(doss.cDoss.in(JPAExpressions.select(dossRiskAnalysisSubject.dossRiskAnalysis.cDossRa)
                .from(dossRiskAnalysisSubject)
                .where(dossRiskAnalysisSubject.score.gt(riskProfile.threshold))))
            .and(doss.zaakId.in(JPAExpressions.select(indVerwerkt1.zaakId)
                .from(indVerwerkt1)
                .where(indVerwerkt1.indVerwerkt.in(VERWERKT.getCode())))))
        .fetch();

    return toZaakKeys(results, RISK_ANALYSIS);
  }

  /**
   * Aantal uitgevoerde risicoanalyses boven drempelwaarde en gekoppelde zaak niet verwerkt (geweigerd/geannuleerd)
   */
  public static List<ZaakKey> getRiskAnalysis145(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(doss.zaakId)
        .from(doss, dossRiskAnalysis, riskProfile)
        .where(doss.eq(dossRiskAnalysis.doss)
            .and(dossRiskAnalysis.riskProfile.eq(riskProfile))
            .and(doss.typeDoss.eq(BigDecimal.valueOf(RISK_ANALYSIS.getCode())))
            .and(period(doss.dAanvr, periode))
            .and(doss.cDoss.in(JPAExpressions.select(dossRiskAnalysisSubject.dossRiskAnalysis.cDossRa)
                .from(dossRiskAnalysisSubject)
                .where(dossRiskAnalysisSubject.score.gt(riskProfile.threshold))))
            .and(doss.zaakId.in(JPAExpressions.select(indVerwerkt1.zaakId)
                .from(indVerwerkt1)
                .where(indVerwerkt1.indVerwerkt.in(
                    ZaakStatusType.GEANNULEERD.getCode(),
                    ZaakStatusType.GEWEIGERD.getCode())))))
        .fetch();

    return toZaakKeys(results, RISK_ANALYSIS);
  }

  /**
   * Aantal onderzoeken binnen 10 weken verwerkt
   */
  public static List<ZaakKey> getRiskAnalysis15_1(DashboardPeriode periode) {
    return toZaakKeys(getOnderzoeken(periode).entrySet().stream()
        .filter(entry -> entry.getValue() <= 70)
        .map(Map.Entry::getKey).collect(Collectors.toList()), ONDERZOEK);
  }

  /**
   * Aantal onderzoeken na 10 weken verwerkt
   */
  public static List<ZaakKey> getRiskAnalysis15_2(DashboardPeriode periode) {
    return toZaakKeys(getOnderzoeken(periode).entrySet().stream()
        .filter(entry -> entry.getValue() > 70)
        .map(Map.Entry::getKey).collect(Collectors.toList()), ONDERZOEK);
  }

  private static Map<String, Integer> getOnderzoeken(DashboardPeriode periode) {
    List<Tuple> tuples = getJpaQueryFactory().select(doss.zaakId, dossOnderz.onderzDAanvang, indVerwerkt1.dIn)
        .from(doss)
        .innerJoin(indVerwerkt1)
        .innerJoin(dossOnderz)
        .on(doss.zaakId.eq(indVerwerkt1.zaakId))
        .on(doss.cDoss.eq(dossOnderz.cDossOnderz))
        .where(doss.typeDoss.eq(BigDecimal.valueOf(ONDERZOEK.getCode()))
            .and(period(doss.dAanvr, periode))
            .and(indVerwerkt1.indVerwerkt.in(VERWERKT.getCode(), VERWERKT_IN_GBA.getCode())))
        .fetch();

    Map<String, Integer> groups = new HashMap<>();
    for (Tuple tuple : tuples) {
      String zaakId = tuple.get(doss.zaakId);
      Date dAanv = tuple.get(dossOnderz.onderzDAanvang);
      if (dAanv != null) {
        BigDecimal dVerwerkt = tuple.get(indVerwerkt1.dIn);
        groups.put(zaakId, new ProcuraDate(dAanv).diffInDays(dVerwerkt.toString()));
      }
    }
    return groups;
  }
}
