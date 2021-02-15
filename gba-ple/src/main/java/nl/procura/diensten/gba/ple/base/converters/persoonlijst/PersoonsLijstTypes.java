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

package nl.procura.diensten.gba.ple.base.converters.persoonlijst;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;

import java.util.LinkedHashMap;
import java.util.Map;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;

public class PersoonsLijstTypes {

  private static final Map<String, String> map = new LinkedHashMap<>();

  static {
    // Persoon
    add(PERSOON, ANR, "anummer");
    add(PERSOON, BSN, "burgerservicenummer");
    add(PERSOON, VOORNAMEN, "voornaam");
    add(PERSOON, TITEL_PREDIKAAT, "titel_predikaat");
    add(PERSOON, VOORV_GESLACHTSNAAM, "voorvoegsel");
    add(PERSOON, GESLACHTSNAAM, "geslachtsnaam");
    add(PERSOON, GEBOORTEDATUM, "geboortedatum");
    add(PERSOON, GEBOORTEPLAATS, "geboorteplaats");
    add(PERSOON, GEBOORTELAND, "geboorteland");
    add(PERSOON, GESLACHTSAAND, "geslacht");
    add(PERSOON, VORIG_A_NUMMER, "vorig_anummer");
    add(PERSOON, VOLGEND_A_NUMMER, "volgend_anummer");
    add(PERSOON, AANDUIDING_NAAMGEBRUIK, "naamgebruik");
    add(PERSOON, REGIST_GEM_AKTE, "registergemeente_akte");
    add(PERSOON, AKTENR, "aktenummer");
    add(PERSOON, GEMEENTE_DOC, "gemeente_document");
    add(PERSOON, DATUM_DOC, "datum_document");
    add(PERSOON, BESCHRIJVING_DOC, "beschrijving_document");
    add(PERSOON, AAND_GEG_IN_ONDERZ, "aanduiding_gegevens_in_onderzoek");
    add(PERSOON, DATUM_INGANG_ONDERZ, "datum_ingang_onderzoek");
    add(PERSOON, DATUM_EINDE_ONDERZ, "datum_einde_onderzoek");
    add(PERSOON, IND_ONJUIST, "indicatie_onjuist");
    add(PERSOON, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(PERSOON, VOLGCODE_GELDIG, "");
    add(PERSOON, DATUM_VAN_OPNEMING, "datum_opneming");
    add(PERSOON, GBAElem.RNI_DEELNEMER, "rni_deelnemer");
    add(PERSOON, GBAElem.OMSCHR_VERDRAG, "omschrijving_verdrag");

    // Ouder 1
    add(OUDER_1, ANR, "anummer");
    add(OUDER_1, BSN, "burgerservicenummer");
    add(OUDER_1, VOORNAMEN, "voornaam");
    add(OUDER_1, TITEL_PREDIKAAT, "titel_predikaat");
    add(OUDER_1, VOORV_GESLACHTSNAAM, "voorvoegsel");
    add(OUDER_1, GESLACHTSNAAM, "geslachtsnaam");
    add(OUDER_1, GEBOORTEDATUM, "geboortedatum");
    add(OUDER_1, GEBOORTEPLAATS, "geboorteplaats");
    add(OUDER_1, GEBOORTELAND, "geboorteland");
    add(OUDER_1, GESLACHTSAAND, "geslacht");
    add(OUDER_1, DATUM_INGANG_FAM_RECHT_BETREK, "datum_familierechtelijke_betrekking");
    add(OUDER_1, REGIST_GEM_AKTE, "registergemeente_akte");
    add(OUDER_1, AKTENR, "aktenummer");
    add(OUDER_1, GEMEENTE_DOC, "gemeente_document");
    add(OUDER_1, DATUM_DOC, "datum_document");
    add(OUDER_1, BESCHRIJVING_DOC, "beschrijving_document");
    add(OUDER_1, AAND_GEG_IN_ONDERZ, "aanduiding_gegevens_in_onderzoek");
    add(OUDER_1, DATUM_INGANG_ONDERZ, "datum_ingang_onderzoek");
    add(OUDER_1, DATUM_EINDE_ONDERZ, "datum_einde_onderzoek");
    add(OUDER_1, IND_ONJUIST, "indicatie_onjuist");
    add(OUDER_1, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(OUDER_1, VOLGCODE_GELDIG, "");
    add(OUDER_1, DATUM_VAN_OPNEMING, "datum_opneming");

    // Ouder 2
    add(OUDER_2, ANR, "anummer");
    add(OUDER_2, BSN, "burgerservicenummer");
    add(OUDER_2, VOORNAMEN, "voornaam");
    add(OUDER_2, TITEL_PREDIKAAT, "titel_predikaat");
    add(OUDER_2, VOORV_GESLACHTSNAAM, "voorvoegsel");
    add(OUDER_2, GESLACHTSNAAM, "geslachtsnaam");
    add(OUDER_2, GEBOORTEDATUM, "geboortedatum");
    add(OUDER_2, GEBOORTEPLAATS, "geboorteplaats");
    add(OUDER_2, GEBOORTELAND, "geboorteland");
    add(OUDER_2, GESLACHTSAAND, "geslacht");
    add(OUDER_2, DATUM_INGANG_FAM_RECHT_BETREK, "datum_familierechtelijke_betrekking");
    add(OUDER_2, REGIST_GEM_AKTE, "registergemeente_akte");
    add(OUDER_2, AKTENR, "aktenummer");
    add(OUDER_2, GEMEENTE_DOC, "gemeente_document");
    add(OUDER_2, DATUM_DOC, "datum_document");
    add(OUDER_2, BESCHRIJVING_DOC, "beschrijving_document");
    add(OUDER_2, AAND_GEG_IN_ONDERZ, "aanduiding_gegevens_in_onderzoek");
    add(OUDER_2, DATUM_INGANG_ONDERZ, "datum_ingang_onderzoek");
    add(OUDER_2, DATUM_EINDE_ONDERZ, "datum_einde_onderzoek");
    add(OUDER_2, IND_ONJUIST, "indicatie_onjuist");
    add(OUDER_2, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(OUDER_2, VOLGCODE_GELDIG, "");
    add(OUDER_2, DATUM_VAN_OPNEMING, "datum_opneming");

    // Nationaliteit
    add(NATIO, GBAElem.NATIONALITEIT, "nationaliteit");
    add(NATIO, REDEN_OPN_NATIO, "reden_verkrijging_nederlanderschap");
    add(NATIO, REDEN_EINDE_NATIO, "reden_verlies_nederlanderschap");
    add(NATIO, AAND_BIJZ_NL_SCHAP, "aanduiding_bijzonder_nederlanderschap");
    add(NATIO, EU_PERS_NR, "eu_persoonsnummer");
    add(NATIO, GEMEENTE_DOC, "gemeente_document");
    add(NATIO, DATUM_DOC, "datum_document");
    add(NATIO, BESCHRIJVING_DOC, "beschrijving_document");
    add(NATIO, AAND_GEG_IN_ONDERZ, "aanduiding_gegevens_in_onderzoek");
    add(NATIO, DATUM_INGANG_ONDERZ, "datum_ingang_onderzoek");
    add(NATIO, DATUM_EINDE_ONDERZ, "datum_einde_onderzoek");
    add(NATIO, IND_ONJUIST, "indicatie_onjuist");
    add(NATIO, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(NATIO, VOLGCODE_GELDIG, "");
    add(NATIO, DATUM_VAN_OPNEMING, "datum_opneming");
    add(NATIO, GBAElem.RNI_DEELNEMER, "rni_deelnemer");
    add(NATIO, GBAElem.OMSCHR_VERDRAG, "omschrijving_verdrag");

    // Huwelijk
    add(HUW_GPS, ANR, "anummer");
    add(HUW_GPS, BSN, "burgerservicenummer");
    add(HUW_GPS, VOORNAMEN, "voornaam");
    add(HUW_GPS, TITEL_PREDIKAAT, "titel_predikaat");
    add(HUW_GPS, VOORV_GESLACHTSNAAM, "voorvoegsel");
    add(HUW_GPS, GESLACHTSNAAM, "geslachtsnaam");
    add(HUW_GPS, GEBOORTEDATUM, "geboortedatum");
    add(HUW_GPS, GEBOORTEPLAATS, "geboorteplaats");
    add(HUW_GPS, GEBOORTELAND, "geboorteland");
    add(HUW_GPS, GESLACHTSAAND, "geslacht");
    add(HUW_GPS, DATUM_VERBINTENIS, "huwelijksdatum");
    add(HUW_GPS, PLAATS_VERBINTENIS, "huwelijksplaats");
    add(HUW_GPS, LAND_VERBINTENIS, "huwelijksland");
    add(HUW_GPS, DATUM_ONTBINDING, "ontbindingsdatum");
    add(HUW_GPS, PLAATS_ONTBINDING, "ontbindingsplaats");
    add(HUW_GPS, LAND_ONTBINDING, "ontbindingsland");
    add(HUW_GPS, REDEN_ONTBINDING, "ontbindingsreden");
    add(HUW_GPS, SOORT_VERBINTENIS, "soort");
    add(HUW_GPS, REGIST_GEM_AKTE, "registergemeente_akte");
    add(HUW_GPS, AKTENR, "aktenummer");
    add(HUW_GPS, GEMEENTE_DOC, "gemeente_document");
    add(HUW_GPS, DATUM_DOC, "datum_document");
    add(HUW_GPS, BESCHRIJVING_DOC, "beschrijving_document");
    add(HUW_GPS, AAND_GEG_IN_ONDERZ, "aanduiding_gegevens_in_onderzoek");
    add(HUW_GPS, DATUM_INGANG_ONDERZ, "datum_ingang_onderzoek");
    add(HUW_GPS, DATUM_EINDE_ONDERZ, "datum_einde_onderzoek");
    add(HUW_GPS, IND_ONJUIST, "indicatie_onjuist");
    add(HUW_GPS, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(HUW_GPS, VOLGCODE_GELDIG, "");
    add(HUW_GPS, DATUM_VAN_OPNEMING, "datum_opneming");

    // Overlijden
    add(OVERL, DATUM_OVERL, "overlijdensdatum");
    add(OVERL, PLAATS_OVERL, "overlijdensplaats");
    add(OVERL, LAND_OVERL, "overlijdensland");
    add(OVERL, REGIST_GEM_AKTE, "registergemeente_akte");
    add(OVERL, AKTENR, "aktenummer");
    add(OVERL, GEMEENTE_DOC, "gemeente_document");
    add(OVERL, DATUM_DOC, "datum_document");
    add(OVERL, BESCHRIJVING_DOC, "beschrijving_document");
    add(OVERL, AAND_GEG_IN_ONDERZ, "aanduiding_gegevens_in_onderzoek");
    add(OVERL, DATUM_INGANG_ONDERZ, "datum_ingang_onderzoek");
    add(OVERL, DATUM_EINDE_ONDERZ, "datum_einde_onderzoek");
    add(OVERL, IND_ONJUIST, "indicatie_onjuist");
    add(OVERL, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(OVERL, VOLGCODE_GELDIG, "");
    add(OVERL, DATUM_VAN_OPNEMING, "datum_opneming");
    add(OVERL, GBAElem.RNI_DEELNEMER, "rni_deelnemer");
    add(OVERL, GBAElem.OMSCHR_VERDRAG, "omschrijving_verdrag");

    // Inschrijving
    add(INSCHR, GEM_BLOKK_PL, "gemeente_blokkering_pl");
    add(INSCHR, DATUM_INGANG_BLOK_PL, "datum_ingang_blokkering_pl");
    add(INSCHR, DATUM_OPSCH_BIJHOUD, "datum_opschorting_pl");
    add(INSCHR, OMSCHR_REDEN_OPSCH_BIJHOUD, "reden_opschorting_pl");
    add(INSCHR, DATUM_EERSTE_INSCHR_GBA, "datum_eerste_inschrijving_gba");
    add(INSCHR, GEMEENTE_VAN_PK, "gemeente_pk");
    add(INSCHR, IND_GEHEIM, "indicatie_geheim");
    add(INSCHR, DATUM_VERIFICATIE, "datum_verificatie");
    add(INSCHR, OMSCHR_VERIFICATIE, "omschrijving_verificatie");
    add(INSCHR, VERSIENR, "versienummer");
    add(INSCHR, DATUMTIJDSTEMPEL, "datumtijdstempel");
    add(INSCHR, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(INSCHR, VOLGCODE_GELDIG, "");
    add(INSCHR, DATUM_VAN_OPNEMING, "datum_opneming");
    add(INSCHR, PK_GEGEVENS_VOL_CONVERT, "pk_conversie");
    add(INSCHR, GBAElem.RNI_DEELNEMER, "rni_deelnemer");
    add(INSCHR, GBAElem.OMSCHR_VERDRAG, "omschrijving_verdrag");

    // Verblijfplaats
    add(VB, GEM_INSCHR, "gemeente_inschrijving");
    add(VB, GEM_INSCHR_CODE, "gemeente_inschrijving_code");
    add(VB, DATUM_INSCHR, "datum_inschrijving");
    add(VB, FUNCTIE_ADRES, "functie_adres");
    add(VB, GEM_DEEL, "gemeentedeel");
    add(VB, DATUM_AANVANG_ADRESH, "datum_aanvang_adreshouding");
    add(VB, STRAATNAAM, "straatnaam_boco");
    add(VB, STRAATNAAM_OFFIC, "straatnaam_officieel");
    add(VB, STRAATNAAM_NEN, "straatnaam_nen");
    add(VB, OPENB_RUIMTE, "naam_openbare_ruimte");
    add(VB, WPL_NAAM, "woonplaatsnaam");
    add(VB, ID_VERBLIJFPLAATS, "identificatie_verblijfplaats");
    add(VB, IDCODE_NUMMERAAND, "identificatiecode_nummeraanduiding");
    add(VB, HNR, "huisnummer");
    add(VB, HNR_L, "huisletter");
    add(VB, HNR_T, "toevoeging");
    add(VB, HNR_A, "aanduiding");
    add(VB, POSTCODE, "postcode");
    add(VB, LOCATIEBESCHR, "locatie");
    add(VB, LAND_VERTREK, "emigratieland");
    add(VB, DATUM_VERTREK_UIT_NL, "emigratiedatum");
    add(VB, ADRES_BUITENL_1, "adres_buitenland_waarnaar_vertrokken_1");
    add(VB, ADRES_BUITENL_2, "adres_buitenland_waarnaar_vertrokken_2");
    add(VB, ADRES_BUITENL_3, "adres_buitenland_waarnaar_vertrokken_3");
    add(VB, LAND_VESTIGING, "immigratieland");
    add(VB, DATUM_VESTIGING_IN_NL, "immigratiedatum");
    add(VB, OMSCHR_VAN_DE_AANGIFTE_ADRESH, "adresaangifte");
    add(VB, IND_DOC, "documentindicatie");
    add(VB, AAND_GEG_IN_ONDERZ, "aanduiding_gegevens_in_onderzoek");
    add(VB, DATUM_INGANG_ONDERZ, "datum_ingang_onderzoek");
    add(VB, DATUM_EINDE_ONDERZ, "datum_einde_onderzoek");
    add(VB, IND_ONJUIST, "indicatie_onjuist");
    add(VB, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(VB, VOLGCODE_GELDIG, "");
    add(VB, DATUM_VAN_OPNEMING, "datum_opneming");
    add(VB, GBAElem.RNI_DEELNEMER, "rni_deelnemer");
    add(VB, GBAElem.OMSCHR_VERDRAG, "omschrijving_verdrag");

    // Kinderen
    add(KINDEREN, ANR, "anummer");
    add(KINDEREN, BSN, "burgerservicenummer");
    add(KINDEREN, VOORNAMEN, "voornaam");
    add(KINDEREN, TITEL_PREDIKAAT, "titel_predikaat");
    add(KINDEREN, VOORV_GESLACHTSNAAM, "voorvoegsel");
    add(KINDEREN, GESLACHTSNAAM, "geslachtsnaam");
    add(KINDEREN, GEBOORTEDATUM, "geboortedatum");
    add(KINDEREN, GEBOORTEPLAATS, "geboorteplaats");
    add(KINDEREN, GEBOORTELAND, "geboorteland");
    add(KINDEREN, REGIST_GEM_AKTE, "registergemeente_akte");
    add(KINDEREN, AKTENR, "aktenummer");
    add(KINDEREN, GEMEENTE_DOC, "gemeente_document");
    add(KINDEREN, DATUM_DOC, "datum_document");
    add(KINDEREN, BESCHRIJVING_DOC, "beschrijving_document");
    add(KINDEREN, AAND_GEG_IN_ONDERZ, "aanduiding_gegevens_in_onderzoek");
    add(KINDEREN, DATUM_INGANG_ONDERZ, "datum_ingang_onderzoek");
    add(KINDEREN, DATUM_EINDE_ONDERZ, "datum_einde_onderzoek");
    add(KINDEREN, IND_ONJUIST, "indicatie_onjuist");
    add(KINDEREN, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(KINDEREN, VOLGCODE_GELDIG, "");
    add(KINDEREN, DATUM_VAN_OPNEMING, "datum_opneming");
    add(KINDEREN, REG_BETREKK, "registratie_betrekking");

    // Verblijfstitel
    add(VBTITEL, AAND_VBT, "aanduiding_verblijfstitel");
    add(VBTITEL, VBT_OMS, "verblijfstitel_omschrijving");
    add(VBTITEL, DATUM_EINDE_VBT, "datum_einde_verblijfstitel");
    add(VBTITEL, INGANGSDATUM_VBT, "ingangsdatum_verblijfstitel");
    add(VBTITEL, AAND_GEG_IN_ONDERZ, "aanduiding_gegevens_in_onderzoek");
    add(VBTITEL, DATUM_INGANG_ONDERZ, "datum_ingang_onderzoek");
    add(VBTITEL, DATUM_EINDE_ONDERZ, "datum_einde_onderzoek");
    add(VBTITEL, IND_ONJUIST, "indicatie_onjuist");
    add(VBTITEL, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(VBTITEL, VOLGCODE_GELDIG, "");
    add(VBTITEL, DATUM_VAN_OPNEMING, "datum_opneming");

    // Gezag
    add(GEZAG, IND_GEZAG_MINDERJ, "gezag_minderjarige");
    add(GEZAG, IND_CURATELE_REG, "curatele");
    add(GEZAG, GEMEENTE_DOC, "gemeente_document");
    add(GEZAG, DATUM_DOC, "datum_document");
    add(GEZAG, BESCHRIJVING_DOC, "beschrijving_document");
    add(GEZAG, AAND_GEG_IN_ONDERZ, "aanduiding_gegevens_in_onderzoek");
    add(GEZAG, DATUM_INGANG_ONDERZ, "datum_ingang_onderzoek");
    add(GEZAG, DATUM_EINDE_ONDERZ, "datum_einde_onderzoek");
    add(GEZAG, IND_ONJUIST, "indicatie_onjuist");
    add(GEZAG, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(GEZAG, VOLGCODE_GELDIG, "");
    add(GEZAG, DATUM_VAN_OPNEMING, "datum_opneming");

    // Reisdocumenten
    add(REISDOC, SOORT_NL_REISDOC, "nederlands_reisdocument");
    add(REISDOC, NR_NL_REISDOC, "nummer_document");
    add(REISDOC, DATUM_UITGIFTE_NL_REISDOC, "datum_uitgifte");
    add(REISDOC, AUTORIT_VAN_AFGIFTE_NL_REISDOC, "autoriteit_van_afgifte");
    add(REISDOC, DATUM_EINDE_GELDIG_NL_REISDOC, "datum_einde_geldigheid");
    add(REISDOC, DATUM_INH_VERMIS_NL_REISDOC, "datum_inhouding_vermissing");
    add(REISDOC, AAND_INH_VERMIS_NL_REISDOC, "aanduiding_inhouding_vermissing");
    add(REISDOC, LENGTE_HOUDER, "lengte_houder");
    add(REISDOC, SIG_MET_BETREK_TOT_VERSTREK_NL_REISDOC, "signalering");
    add(REISDOC, AAND_BEZIT_BUITENL_REISDOC, "buitenlands_reisdocument");
    add(REISDOC, GEMEENTE_DOC, "gemeente_document");
    add(REISDOC, DATUM_DOC, "datum_document");
    add(REISDOC, BESCHRIJVING_DOC, "beschrijving_document");
    add(REISDOC, AAND_GEG_IN_ONDERZ, "aanduiding_gegevens_in_onderzoek");
    add(REISDOC, DATUM_INGANG_ONDERZ, "datum_ingang_onderzoek");
    add(REISDOC, DATUM_EINDE_ONDERZ, "datum_einde_onderzoek");
    add(REISDOC, IND_ONJUIST, "indicatie_onjuist");
    add(REISDOC, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(REISDOC, VOLGCODE_GELDIG, "");
    add(REISDOC, DATUM_VAN_OPNEMING, "datum_opneming");

    // Kiesrecht
    add(KIESR, AANDUIDING_EURO_KIESR, "aanduiding_europees_kiesrecht");
    add(KIESR, DATUM_VERZ_OF_MED_EURO_KIESR,
        "datum_verzoek_of_mededeling_europees_kiesrecht");
    add(KIESR, EINDDATUM_UITSL_EURO_KIESR, "einddatum_uitsluiting_europees_kiesrecht");
    add(KIESR, AAND_UITGESLOTEN_KIESR, "aanduiding_uitsluiting_kiesrecht");
    add(KIESR, EINDDATUM_UITSLUIT_KIESR, "einddatum_uitsluiting_kiesrecht");
    add(KIESR, GEMEENTE_DOC, "gemeente_document");
    add(KIESR, DATUM_DOC, "datum_document");
    add(KIESR, BESCHRIJVING_DOC, "beschrijving_document");
    add(KIESR, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(KIESR, VOLGCODE_GELDIG, "");
    add(KIESR, DATUM_VAN_OPNEMING, "datum_opneming");

    // Afnemers
    add(AFNEMERS, AFNEMERSINDICATIE, "afnemersindicatie");
    add(AFNEMERS, AFN_IND_OMSCHR, "omschrijving");
    add(AFNEMERS, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(AFNEMERS, VOLGCODE_GELDIG, "");

    // Verwijzing
    add(VERW, ANR, "anummer");
    add(VERW, BSN, "burgerservicenummer");
    add(VERW, VOORNAMEN, "voornaam");
    add(VERW, TITEL_PREDIKAAT, "titel_predikaat");
    add(VERW, VOORV_GESLACHTSNAAM, "voorvoegsel");
    add(VERW, GESLACHTSNAAM, "geslachtsnaam");
    add(VERW, GEBOORTEDATUM, "geboortedatum");
    add(VERW, GEBOORTEPLAATS, "geboorteplaats");
    add(VERW, GEBOORTELAND, "geboorteland");
    add(VERW, GEM_INSCHR, "gemeente_inschrijving");
    add(VERW, DATUM_INSCHR, "datum_inschrijving");
    add(VERW, STRAATNAAM, "straatnaam_boco");
    add(VERW, STRAATNAAM_OFFIC, "straatnaam_officieel");
    add(VERW, STRAATNAAM_NEN, "straatnaam_nen");
    add(VERW, OPENB_RUIMTE, "openbare_ruimte");
    add(VERW, WPL_NAAM, "woonplaatsnaam");
    add(VERW, ID_VERBLIJFPLAATS, "identificatie_verblijfplaats");
    add(VERW, IDCODE_NUMMERAAND, "identificatiecode_nummeraanduiding");
    add(VERW, HNR, "huisnummer");
    add(VERW, HNR_L, "huisletter");
    add(VERW, HNR_T, "toevoeging");
    add(VERW, HNR_A, "aanduiding");
    add(VERW, POSTCODE, "postcode");
    add(VERW, LOCATIEBESCHR, "locatie");
    add(VERW, IND_GEHEIM, "indicatie_geheim");
    add(VERW, AAND_GEG_IN_ONDERZ, "aanduiding_gegevens_in_onderzoek");
    add(VERW, DATUM_INGANG_ONDERZ, "datum_ingang_onderzoek");
    add(VERW, DATUM_EINDE_ONDERZ, "datum_einde_onderzoek");
    add(VERW, IND_ONJUIST, "indicatie_onjuist");
    add(VERW, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(VERW, VOLGCODE_GELDIG, "");
    add(VERW, DATUM_VAN_OPNEMING, "datum_opneming");

    // Overige
    add(DIV, DIV_GEZINSVERH, "gezinsverhouding");
    add(DIV, DIV_GEZINSVERH_OMSCHR, "gezinsverhouding_omschrijving");
    add(DIV, DIV_IND_VUURW, "vuurwapen");
    add(DIV, DIV_KENMERK, "kenmerk");
    add(DIV, DIV_KENMERK_OMSCHR, "kenmerk_omschrijving");
    add(DIV, DIV_DATUM_PL_VERSTREKT, "datum_pl_verstrekt");
    add(DIV, DIV_OTS, "ots");
    add(DIV, DIV_SIGNAAL, "signaal");
    add(DIV, DIV_CORR_ADRES_1, "correspondentie_adres_1");
    add(DIV, DIV_CORR_ADRES_2, "correspondentie_adres_2");
    add(DIV, ANR, "anummer");
    add(DIV, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(DIV, VOLGCODE_GELDIG, "");

    // Woningkaart
    add(WK, WK_VOLGORDE_GEZIN, "volgorde_gezin");
    add(WK, WK_VOLGORDE_PERSOON, "volgorde_persoon");
    add(WK, WK_WONINGCODE, "woning_code");
    add(WK, WK_DATUM_AANVANG, "datum_aanvang_adreshouding");
    add(WK, WK_DATUM_EINDE, "datum_einde_adreshouding");

    // Kladblok
    add(KLADBLOK, KLADBLOK_REGEL, "regel");

    // Lokale afnemers indicaties
    add(LOK_AF_IND, LOK_AFN_AANT_SOORT, "aantekening");
    add(LOK_AF_IND, LOK_AFN_AANT_OMSCHR, "omschrijving");
    add(LOK_AF_IND, LOK_AFN_AANT_OPMERK, "opmerking");
  }

  private static void add(GBACat cat, GBAElem element, String plName) {
    map.put(cat.getCode() + "." + element.getCode(), plName);
  }

  public static String getName(int cat, int element) {
    return map.get(cat + "." + element);
  }
}
