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

import static nl.procura.gba.common.ZaakType.*;
import static nl.procura.gba.jpa.personen.db.QBvhPark.bvhPark;
import static nl.procura.gba.jpa.personen.db.QDocInh.docInh;
import static nl.procura.gba.jpa.personen.db.QDoss.doss;
import static nl.procura.gba.jpa.personen.db.QGeheimhouding.geheimhouding1;
import static nl.procura.gba.jpa.personen.db.QIndVerwerkt.indVerwerkt1;
import static nl.procura.gba.jpa.personen.db.QNaamgebruik.naamgebruik1;
import static nl.procura.gba.jpa.personen.db.QNrd.nrd;
import static nl.procura.gba.jpa.personen.db.QNrdStatus.nrdStatus;
import static nl.procura.gba.jpa.personen.db.QRdm01.rdm01;
import static nl.procura.gba.jpa.personen.db.QTerugmTmv.terugmTmv;
import static nl.procura.gba.jpa.personen.db.QTerugmelding.terugmelding1;
import static nl.procura.gba.jpa.personen.db.QUittAanvr.uittAanvr;
import static nl.procura.gba.jpa.personen.db.QVogAanvr.vogAanvr;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;

public class OfficialDashboardDao extends DashboardDao {

  // Soorten verhuizingen
  public static final int VERHUIS_BINNEN       = 1;
  public static final int VERHUIS_BUITEN       = 2;
  public static final int VERHUIS_EMIGRATIE    = 3;
  public static final int VERHUIS_HERVESTIGING = 4;

  // Redenen aanvraag rijbewijzen
  private static final int WEGENS_VERLIES_OF_DIEFSTAL = 1;
  // Status rijbewijs uitgereikt
  private static final int RIJBEWIJS_UITGEREIKT = 90;

  /**
   * Geboortes op datum invoer
   */
  public static List<ZaakKey> getGeboorten12(DashboardPeriode periode) {

    List<String> results = getJpaQueryFactory().select(doss.zaakId)
        .from(doss)
        .where(doss.typeDoss.eq(BigDecimal.valueOf(GEBOORTE.getCode()))
            .and(period(doss.dAanvr, periode)))
        .fetch();

    return toZaakKeys(results, GEBOORTE);
  }

  /**
   * Erkenningen op datum invoer
   */
  public static List<ZaakKey> getErkenningen13(DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(doss.zaakId)
        .from(doss)
        .where(doss.typeDoss.in(typeDoss(ERKENNING))
            .and(period(doss.dAanvr, periode)))
        .fetch();

    return toZaakKeys(results, ERKENNING);
  }

  /**
   * Overlijdens op datum invoer
   */
  public static List<ZaakKey> getOverlijden14(DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(doss.zaakId)
        .from(doss)
        .where(doss.typeDoss.in(typeDoss(
            OVERLIJDEN_IN_BUITENLAND,
            OVERLIJDEN_IN_GEMEENTE,
            LIJKVINDING,
            LEVENLOOS))
            .and(period(doss.dAanvr, periode)))
        .fetch();

    return toZaakKeys(results, ONBEKEND);
  }

  /**
   * Huwelijken/GPS op datum invoer
   */
  public static List<ZaakKey> getHuwelijken21(DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(doss.zaakId)
        .from(doss)
        .where(doss.typeDoss.in(typeDoss(
            HUWELIJK_GPS_GEMEENTE,
            OMZETTING_GPS))
            .and(period(doss.dAanvr, periode)))
        .fetch();

    return toZaakKeys(results, ONBEKEND);
  }

  /**
   * Ontbinding/einde huwelijk/gps
   */
  public static List<ZaakKey> getOntbindingen22(DashboardPeriode periode) {

    return toZaakKeys(getJpaQueryFactory().select(doss.zaakId)
        .from(doss)
        .where(doss.typeDoss.in(typeDoss(ONTBINDING_GEMEENTE))
            .and(period(doss.dAanvr, periode)))
        .fetch(), ONBEKEND);
  }

  /**
   * Uittreksels met type pl
   */
  public static List<ZaakKey> getUittreksels31(boolean digitaal, DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(uittAanvr.zaakId)
        .from(uittAanvr)
        .where(uittAanvr.cUittAanvr.gt(0)
            .and(period(uittAanvr.dAanvr, periode))
            .and(uittAanvr.document.type.eq("pl"))
            .and(getBronEnLeverancier(uittAanvr.bron, uittAanvr.leverancier, digitaal, periode)))
        .fetch();

    return toZaakKeys(results, UITTREKSEL);
  }

  /**
   * Attestaties de Vita + in lezen zijn
   */
  public static List<ZaakKey> getBewijsInLeven32(boolean digitaal, DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(uittAanvr.zaakId)
        .from(uittAanvr)
        .where(new BooleanBuilder(uittAanvr.cUittAanvr.gt(0)
            .and(period(uittAanvr.dAanvr, periode))
            .and(uittAanvr.document.type.eq("pl"))
            .and(uittAanvr.document.document.like("%Attestatie%")
                .or(uittAanvr.document.document.like("%in%leven%zijn%")))
            .and(getBronEnLeverancier(uittAanvr.bron, uittAanvr.leverancier, digitaal, periode))))
        .fetch();

    return toZaakKeys(results, UITTREKSEL);
  }

  /**
   * COVOG aanvragen
   */
  public static List<ZaakKey> getVog33(DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(vogAanvr.zaakId)
        .from(vogAanvr)
        .where(vogAanvr.cVogAanvr.gt(0)
            .and(period(vogAanvr.dAanvr, periode)))
        .fetch();

    return toZaakKeys(results, COVOG);
  }

  /**
   * Naamgebruik aanvragen
   */
  public static List<ZaakKey> getNaamgebruik34(boolean digitaal, DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(naamgebruik1.zaakId)
        .from(naamgebruik1)
        .where(naamgebruik1.cNaamgebruik.gt(0)
            .and(period(naamgebruik1.dIn, periode))
            .and(getBronEnLeverancier(naamgebruik1.bron, naamgebruik1.leverancier, digitaal, periode)))
        .fetch();

    return toZaakKeys(results, NAAMGEBRUIK);
  }

  /**
   * Geheimhoudingsverzoeken
   */
  public static List<ZaakKey> getGeheimhouding35(boolean digitaal, DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(geheimhouding1.zaakId)
        .from(geheimhouding1)
        .where(geheimhouding1.cGeheimhouding.gt(0)
            .and(period(geheimhouding1.dIn, periode))
            .and(getBronEnLeverancier(geheimhouding1.bron, geheimhouding1.leverancier, digitaal, periode)))
        .fetch();

    return toZaakKeys(results, VERSTREKKINGSBEPERKING);
  }

  /**
   * Binnen- / Intergemeentelijke verhuizingen
   */
  public static List<ZaakKey> getVerhuizingen41(boolean digitaal, DashboardPeriode periode) {
    return getVerhuizingen(digitaal, periode, VERHUIS_BINNEN, VERHUIS_BUITEN);
  }

  /**
   * Hervestigingen
   */
  public static List<ZaakKey> getVerhuizingen42(DashboardPeriode periode) {
    List<ZaakKey> verhuizingen = getVerhuizingen(false, periode, VERHUIS_HERVESTIGING);
    verhuizingen.addAll(getVestigingen(periode));
    return verhuizingen;
  }

  /**
   * Emigraties
   */
  public static List<ZaakKey> getVerhuizingen43(boolean digitaal, DashboardPeriode periode) {
    return getVerhuizingen(digitaal, periode, VERHUIS_EMIGRATIE);
  }

  private static List<ZaakKey> getVerhuizingen(boolean digitaal, DashboardPeriode periode, long... types) {
    List<String> results = getJpaQueryFactory().select(bvhPark.zaakId)
        .from(bvhPark)
        .where(bvhPark.cBvhPark.gt(0)
            .and(period(bvhPark.dOpn, periode))
            .and(bvhPark.verhuisType.in(Arrays.stream(types)
                .mapToObj(BigDecimal::valueOf)
                .collect(Collectors.toList())))
            .and(getBronEnLeverancier(bvhPark.bron, bvhPark.leverancier, digitaal, periode)))
        .fetch();

    return toZaakKeys(results, VERHUIZING);
  }

  /**
   * Eerste inschrijvingen
   */
  private static List<ZaakKey> getVestigingen(DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(doss.zaakId)
        .from(doss)
        .where(doss.typeDoss.eq(BigDecimal.valueOf(REGISTRATION.getCode()))
            .and(period(doss.dAanvr, periode)))
        .fetch();

    return toZaakKeys(results, REGISTRATION);
  }

  /**
   * Aangevraagde reisdocumenten
   */
  public static List<ZaakKey> getReisdocumenten61(DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(rdm01.zaakId)
        .from(rdm01)
        .where(rdm01.rdm01Id.gt(0)
            .and(period(rdm01.dAanvr, periode)))
        .fetch();

    return toZaakKeys(results, REISDOCUMENT);
  }

  /**
   * Afgehaalde reisdocumenten
   */
  public static List<ZaakKey> getReisdocumenten62(DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(rdm01.zaakId)
        .from(rdm01)
        .where(rdm01.rdm01Id.gt(0)
            .and(period(rdm01.dAfsl, periode))
            .and(rdm01.statAfsl.in(1, 3))) // Document uitgereikt / Document uitgereikt door andere instantie
        .fetch();

    return toZaakKeys(results, REISDOCUMENT);
  }

  /**
   * Vermiste reisdocumenten
   */
  public static List<ZaakKey> getReisdocumenten63(DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(docInh.zaakId)
        .from(docInh)
        .where(docInh.dInneming.gt(0)
            .and(docInh.indRijbewijs.lt(1))
            .and(period(docInh.dInneming, periode))
            .and(docInh.aandVi.eq("V")))
        .fetch();

    return toZaakKeys(results, INHOUD_VERMIS);
  }

  /**
   * Aangevraagde rijbewijzen
   */
  public static List<ZaakKey> getRijbewijzen71(DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(nrd.zaakId)
        .from(nrd)
        .where(nrd.cAanvr.gt(0)
            .and(period(nrd.dAanvr, periode)))
        .fetch();

    return toZaakKeys(results, RIJBEWIJS);
  }

  /**
   * Afgehaalde rijbewijzen
   */
  public static List<ZaakKey> getRijbewijzen72(DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(nrd.zaakId)
        .from(nrd)
        .innerJoin(nrdStatus)
        .on(nrdStatus.nrd.id.eq(nrd.id))
        .where(nrd.cAanvr.gt(0)
            .and(period(nrdStatus.rdwDStat, periode))
            .and(nrdStatus.id.cStat.eq(Long.valueOf(RIJBEWIJS_UITGEREIKT))))
        .fetch();

    return toZaakKeys(results, RIJBEWIJS);
  }

  /**
   * Vermiste rijbewijzen
   */
  public static List<ZaakKey> getRijbewijzen73(DashboardPeriode periode) {
    JPAQueryFactory queryFactory = getJpaQueryFactory();

    // zoeken in NRD tabel, tenzij de vermissing ook als aparte vermissing
    // in doc_inh is opgeslagen
    List<String> results = queryFactory.select(nrd.zaakId)
        .from(nrd)
        .where(nrd.cAanvr.gt(0)
            .and(nrd.rdnAanvr.eq(BigDecimal.valueOf(WEGENS_VERLIES_OF_DIEFSTAL)))
            .and(nrd.rbwNrVerv.notIn(JPAExpressions.select(docInh.id.nrNlDoc).from(docInh)))
            .and(period(nrd.dAanvr, periode)))
        .fetch();

    List<ZaakKey> zaakKeys = toZaakKeys(results, RIJBEWIJS);

    List<String> results2 = queryFactory.select(docInh.zaakId)
        .from(docInh)
        .where(docInh.dInneming.gt(0)
            .and(docInh.indRijbewijs.gt(0))
            .and(period(docInh.dInneming, periode))
            .and(docInh.aandVi.eq("V")))
        .fetch();

    zaakKeys.addAll(toZaakKeys(results2, INHOUD_VERMIS));

    return zaakKeys;
  }

  /**
   * Aangevraagde onderzoeken
   */
  public static List<ZaakKey> getOnderzoeken102(DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(doss.zaakId)
        .from(doss)
        .where(doss.typeDoss.eq(BigDecimal.valueOf(ONDERZOEK.getCode()))
            .and(period(doss.dAanvr, periode)))
        .fetch();

    return toZaakKeys(results, ONDERZOEK);
  }

  /**
   * Uitgevoerde onderzoeken
   */
  public static List<ZaakKey> getOnderzoeken103(DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(doss.zaakId)
        .from(doss)
        .where(doss.typeDoss.eq(BigDecimal.valueOf(ONDERZOEK.getCode()))
            .and(period(doss.dAanvr, periode))
            .and(doss.zaakId.in(JPAExpressions.select(indVerwerkt1.zaakId)
                .from(indVerwerkt1)
                .where(indVerwerkt1.indVerwerkt.eq(ZaakStatusType.VERWERKT.getCode())))))
        .fetch();

    return toZaakKeys(results, ONDERZOEK);
  }

  /**
   * Verzonden terugmeldingen BRP
   */
  public static List<ZaakKey> getTerugmeldingen105(DashboardPeriode periode) {
    List<String> results = getJpaQueryFactory().select(terugmelding1.zaakId)
        .from(terugmelding1)
        .where(terugmelding1.cTerugmelding.gt(0)
            .and(period(terugmelding1.dIn, periode))
            .and(terugmelding1.in(JPAExpressions.select(terugmelding1)
                .from(terugmTmv)
                .where(terugmTmv.berichtcode.like("ONBV")))))
        .fetch();

    return toZaakKeys(results, TERUGMELDING);
  }
}
