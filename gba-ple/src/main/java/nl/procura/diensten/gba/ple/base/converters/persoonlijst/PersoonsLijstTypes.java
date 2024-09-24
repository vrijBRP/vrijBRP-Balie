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

import static nl.procura.burgerzaken.gba.core.enums.GBACat.AFNEMERS;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.CONTACT;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.DIV;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.GEZAG;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KINDEREN;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KLADBLOK;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.LOK_AF_IND;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.NATIO;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_1;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_2;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OVERL;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.PERSOON;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.TIJD_VBA;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VB;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VBTITEL;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VERW;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.WK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AANDUIDING_EURO_KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AANDUIDING_NAAMGEBRUIK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_BEZIT_BUITENL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_BIJZ_NL_SCHAP;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_GEG_IN_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_INH_VERMIS_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_UITGESLOTEN_KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_VBT;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ADRES_BUITENL_1;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ADRES_BUITENL_2;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ADRES_BUITENL_3;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ADRES_EU_LIDSTAAT_VAN_HERKOMST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AFNEMERSINDICATIE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AFN_IND_OMSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AKTENR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ANR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AUTORIT_VAN_AFGIFTE_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.BESCHRIJVING_DOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.BSN;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUMTIJDSTEMPEL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_AANVANG_ADRESH;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_DOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_EERSTE_INSCHR_GBA;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_EINDE_GELDIG_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_EINDE_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_EINDE_VBT;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_INGANG_BLOK_PL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_INGANG_FAM_RECHT_BETREK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_INGANG_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_INH_VERMIS_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_ONTBINDING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_OPSCH_BIJHOUD;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_OVERL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_UITGIFTE_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VAN_OPNEMING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VERBINTENIS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VERIFICATIE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VERTREK_UIT_NL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VERZ_OF_MED_EURO_KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VESTIGING_IN_NL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DIV_CORR_ADRES_1;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DIV_CORR_ADRES_2;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DIV_DATUM_PL_VERSTREKT;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DIV_GEZINSVERH;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DIV_GEZINSVERH_OMSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DIV_IND_VUURW;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DIV_KENMERK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DIV_KENMERK_OMSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DIV_OTS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DIV_SIGNAAL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.EINDDATUM_GELDIG;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.EINDDATUM_UITSLUIT_KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.EINDDATUM_UITSL_EURO_KIESR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.EMAILADRES;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.EMAIL_GELDIG_VANAF;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.EMAIL_VERIFICATIE_IND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.EU_PERS_NR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.FUNCTIE_ADRES;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTEDATUM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTELAND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTEPLAATS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEMEENTE_DOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEMEENTE_VAN_PK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEM_BLOKK_PL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEM_DEEL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEM_INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEM_INSCHR_CODE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GESLACHTSAAND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GESLACHTSNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR_A;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR_L;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR_T;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IDCODE_NUMMERAAND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ID_VERBLIJFPLAATS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_CURATELE_REG;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_DOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_GEHEIM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_GEZAG_MINDERJ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_ONJUIST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.INGANGSDATUM_VBT;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.INGANGSDAT_GELDIG;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.KLADBLOK_REGEL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_EU_LIDSTAAT_VAN_HERKOMST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_ONTBINDING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_OVERL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_VERBINTENIS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_VERTREK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_VESTIGING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LENGTE_HOUDER;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LOCATIEBESCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LOK_AFN_AANT_OMSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LOK_AFN_AANT_OPMERK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LOK_AFN_AANT_SOORT;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.NR_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.OMSCHR_REDEN_OPSCH_BIJHOUD;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.OMSCHR_VAN_DE_AANGIFTE_ADRESH;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.OMSCHR_VERIFICATIE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.OPENB_RUIMTE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.PK_GEGEVENS_VOL_CONVERT;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.PLAATS_EU_LIDSTAAT_VAN_HERKOMST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.PLAATS_ONTBINDING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.PLAATS_OVERL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.PLAATS_VERBINTENIS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.POSTCODE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.REDEN_EINDE_NATIO;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.REDEN_ONTBINDING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.REDEN_OPN_NATIO;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.REGIST_GEM_AKTE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.REG_BETREKK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.SIG_MET_BETREK_TOT_VERSTREK_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.SOORT_NL_REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.SOORT_VERBINTENIS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.STRAATNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.STRAATNAAM_NEN;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.STRAATNAAM_OFFIC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.TEL_GELDIG_VANAF;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.TEL_NR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.TEL_VERIFICATIE_IND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.TITEL_PREDIKAAT;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.TYPE_ADRES;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VBT_OMS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VERSIENR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOLGCODE_GELDIG;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOLGEND_A_NUMMER;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOORNAMEN;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOORV_GESLACHTSNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VORIG_A_NUMMER;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.WK_DATUM_AANVANG;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.WK_DATUM_EINDE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.WK_VOLGORDE_GEZIN;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.WK_VOLGORDE_PERSOON;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.WK_WONINGCODE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.WPL_NAAM;

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

    add(KIESR, ADRES_EU_LIDSTAAT_VAN_HERKOMST, "adres_eu_lidstaat_van_herkomst");
    add(KIESR, PLAATS_EU_LIDSTAAT_VAN_HERKOMST, "plaats_eu_lidstaat_van_herkomst");
    add(KIESR, LAND_EU_LIDSTAAT_VAN_HERKOMST, "land_eu_lidstaat_van_herkomst");

    add(KIESR, AAND_UITGESLOTEN_KIESR, "aanduiding_uitsluiting_kiesrecht");
    add(KIESR, EINDDATUM_UITSLUIT_KIESR, "einddatum_uitsluiting_kiesrecht");
    add(KIESR, GEMEENTE_DOC, "gemeente_document");
    add(KIESR, DATUM_DOC, "datum_document");
    add(KIESR, BESCHRIJVING_DOC, "beschrijving_document");
    add(KIESR, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(KIESR, VOLGCODE_GELDIG, "");
    add(KIESR, DATUM_VAN_OPNEMING, "datum_opneming");

    // Tijdelijk verblijfsadres
    add(TIJD_VBA, GEM_INSCHR, "gemeente_inschrijving");
    add(TIJD_VBA, GEM_INSCHR_CODE, "gemeente_inschrijving_code");
    add(TIJD_VBA, DATUM_INSCHR, "datum_inschrijving");
    add(TIJD_VBA, STRAATNAAM, "straatnaam_boco");
    add(TIJD_VBA, STRAATNAAM_OFFIC, "straatnaam_officieel");
    add(TIJD_VBA, STRAATNAAM_NEN, "straatnaam_nen");
    add(TIJD_VBA, OPENB_RUIMTE, "naam_openbare_ruimte");
    add(TIJD_VBA, WPL_NAAM, "woonplaatsnaam");
    add(TIJD_VBA, ID_VERBLIJFPLAATS, "identificatie_verblijfplaats");
    add(TIJD_VBA, IDCODE_NUMMERAAND, "identificatiecode_nummeraanduiding");
    add(TIJD_VBA, HNR, "huisnummer");
    add(TIJD_VBA, HNR_L, "huisletter");
    add(TIJD_VBA, HNR_T, "toevoeging");
    add(TIJD_VBA, HNR_A, "aanduiding");
    add(TIJD_VBA, POSTCODE, "postcode");
    add(TIJD_VBA, EINDDATUM_GELDIG, "einddatum_geldigheid");
    add(TIJD_VBA, TYPE_ADRES, "type_adres");
    add(TIJD_VBA, OMSCHR_VAN_DE_AANGIFTE_ADRESH, "adresaangifte");
    add(TIJD_VBA, AAND_GEG_IN_ONDERZ, "aanduiding_gegevens_in_onderzoek");
    add(TIJD_VBA, DATUM_INGANG_ONDERZ, "datum_ingang_onderzoek");
    add(TIJD_VBA, DATUM_EINDE_ONDERZ, "datum_einde_onderzoek");
    add(TIJD_VBA, IND_ONJUIST, "indicatie_onjuist");
    add(TIJD_VBA, INGANGSDAT_GELDIG, "datum_geldigheid");
    add(TIJD_VBA, VOLGCODE_GELDIG, "");
    add(TIJD_VBA, DATUM_VAN_OPNEMING, "datum_opneming");
    add(TIJD_VBA, GBAElem.RNI_DEELNEMER, "rni_deelnemer");
    add(TIJD_VBA, GBAElem.OMSCHR_VERDRAG, "omschrijving_verdrag");

    // Contactgegevens
    add(CONTACT, TEL_NR, "telefoonnummer");
    add(CONTACT, TEL_VERIFICATIE_IND, "verificatie-indicatie");
    add(CONTACT, TEL_GELDIG_VANAF, "geldig_vanaf");
    add(CONTACT, EMAILADRES, "emailadres");
    add(CONTACT, EMAIL_VERIFICATIE_IND, "verificatie-indicatie");
    add(CONTACT, EMAIL_GELDIG_VANAF, "geldig_vanaf");
    add(TIJD_VBA, GBAElem.RNI_DEELNEMER, "rni_deelnemer");
    add(TIJD_VBA, GBAElem.OMSCHR_VERDRAG, "omschrijving_verdrag");

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
