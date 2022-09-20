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

package nl.procura.gba.web.services.beheer.parameter;

import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ALGEMENE_INSTELLINGEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_BSM;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_CONNECT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_CONTACT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_COVOG;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_CURATELE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_DOCUMENTEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_EMAIL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_GEO;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_GPK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_GV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_HANDLEIDINGEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_INTERNAL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_KASSA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_KENNISBANK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_MIDOFFICE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_MIJN_OVERHEID;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ONDERZOEK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ONTBINDING;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_PORTAAL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_PRESENTIEVRAAG;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_PRINT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_PROBEV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_PROTOCOLLERING;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_RAAS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_REISDOCUMENTEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_RIJBEWIJZEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_RISKANALYSIS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_SMS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_SYSTEM;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_TERUGMELDINGEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_VERHUIZING;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_VERIFICATIEVRAAG;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_WERKPROCES;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZAKEN_ALGEMEEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZAKEN_BEHANDELEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZAKEN_DMS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZAKEN_DMS_TYPES;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZAKEN_STATUS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZOEKEN_ALGEMEEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZOEKEN_PROFIEL_GBAV_PLUS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZOEKEN_PROFIEL_STANDAARD;

public enum ParameterConstant implements ParameterType {

  TEST_OMGEVING("testomgeving", "Testomgeving",
      GROUP_ALGEMENE_INSTELLINGEN, true, false, false, ""),

  ZOEK_PROFIEL("zoek_profiel", "Aanvullend zoekprofiel",
      GROUP_ZOEKEN_ALGEMEEN, false, true, true, ""),

  ZOEK_PLE_JAVA_SERVER_URL("java_ple_server_url", "Endpoint opvragen persoonsgegevens",
      GROUP_ZOEKEN_ALGEMEEN, true, false, false, ""),

  ZAKEN_TAB_VOLGORDE("zaken_tab_volgorde", "Volgorde tabbladen van een zaak",
      GROUP_ZAKEN_ALGEMEEN, true, true, true, "0"),

  ZAKEN_EINDSTATUS("zaken_eindstatus", "Eindstatus wijzigen",
      GROUP_ZAKEN_ALGEMEEN, true, true, true, ""),

  ZAKEN_STATUS_EIGEN_ZAAK("zaken_status_eigen_zaak", "Status van zelf-ingevoerde zaak wijzigen",
      GROUP_ZAKEN_ALGEMEEN, true, true, true, "1"),

  ZAKEN_MAX_STATUS_ZAAK_WIJZIGEN("zaken_max_status_zaak_wijzigen", "Maximale status voor wijzigen zaak",
      GROUP_ZAKEN_ALGEMEEN, true, true, true, "5"),

  ZAKEN_INIT_STATUS_AFSTAM_GEB("zaken_init_afstam_geb", "Afstamming (geboorte)",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_DMS_AFSTAM_GEB("zaken_dms_afstam_geb", "Afstamming (geboorte)",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_AFSTAM_ERK("zaken_init_afstam_erk", "Afstamming (erkenning)",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_DMS_AFSTAM_ERK("zaken_dms_afstam_erk", "Afstamming (erkenning)",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_AFSTAM_NK("zaken_init_afstam_nk", "Afstamming (naamskeuze)",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_DMS_AFSTAM_NK("zaken_dms_afstam_nk", "Afstamming (naamskeuze)",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_CORRES("zaken_init_corres", "Correspondentie",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_INIT_STATUS_COVOG("zaken_init_covog", "COVOG",
      GROUP_ZAKEN_STATUS, true, true, true, "2"),

  ZAKEN_DMS_COVOG("zaken_dms_covog", "COVOG",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_VERSTREK("zaken_init_verstrek", "Verstrekkingsbeperking",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_DMS_VERSTREK("zaken_dms_verstrek", "Verstrekkingsbeperking",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_GPK("zaken_init_gpk", "GPK",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_INIT_STATUS_INBOX("zaken_init_inbox", "Inbox",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_INIT_STATUS_INDICATIE("zaken_init_indicatie", "Indicatie",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_INIT_STATUS_HUW_GPS("zaken_init_huw_gps", "Huwelijk / GPS",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_DMS_HUW_GPS("zaken_dms_huw_gps", "Huwelijk / GPS",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_OMZET_GPS("zaken_init_omzet_gps", "Omzetting GPS in huwelijk",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_DMS_OMZET_GPS("zaken_dms_omzet_gps", "Omzetting GPS in huwelijk",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_DMS_ONDERZOEK("zaken_dms_onderzoek", "Onderzoek",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_ONDERZOEK("zaken_init_onderzoek", "Onderzoek",
      GROUP_ZAKEN_STATUS, true, true, true, "1"),

  ZAKEN_DMS_RISK_ANALYSIS("zaken_dms_risicoanalyse", "Risicoanalyse",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_RISK_ANALYSIS("zaken_init_risicoanalyse", "Risicoanalyse",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_DMS_REGISTRATION("zaken_dms_registration", "Eerste inschrijving",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_DMS_ONTB_HUW_GPS("zaken_dms_ontb_huw_gps", "Ontbinding/einde Huwelijk/GPS",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_ONTBINDING("zaken_init_ontbinding", "Ontbinding/einde huwelijk/GPS in gemeente",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_INIT_STATUS_INH_VERMIS("zaken_init_inh_vermis", "Inhouding / vermissing reisdocument",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_DMS_INH_VERMIS("zaken_dms_inh_vermis", "Inhouding/vermissing",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_INH_VERMIS_RYB("zaken_init_inh_vermis_ryb", "Inhouding / vermissing rijbewijs",
      GROUP_ZAKEN_STATUS, true, true, true, "2"),

  ZAKEN_INIT_STATUS_OVERL_GEM("zaken_init_overl_gem", "Overlijden (in gemeente)",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_DMS_OVERL_GEM("zaken_dms_overl_gem", "Overlijden (in gemeente)",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_OVERL_LIJKV("zaken_init_overl_lijkv", "Overlijden (lijkvinding)",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_DMS_OVERL_LIJKV("zaken_dms_overl_lijkv", "Overlijden (lijkvinding)",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_OVERL_LEVENLOOS("zaken_init_overl_levenloos", "Overlijden (levenloos geboren kind)",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_DMS_OVERL_LEVENLOOS("zaken_dms_overl_levenloos", "Overlijden (levenloos geboren kind)",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_GV("zaken_init_gv", "Gegevensverstrekking",
      GROUP_ZAKEN_STATUS, true, true, true, "2"),

  ZAKEN_DMS_GV("zaken_dms_gv", "Gegevensverstrekking",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_REISDOC("zaken_init_reisdoc", "Reisdocument",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_DMS_REISDOC("zaken_dms_reisdoc", "Reisdocument",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_RIJBEWIJS("zaken_init_rijbewijs", "Rijbewijs",
      GROUP_ZAKEN_STATUS, true, true, true, "1"),

  ZAKEN_DMS_RIJBEWIJS("zaken_dms_rijbewijs", "Rijbewijs",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_TMV("zaken_init_tmv", "Terugmelding",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_INIT_STATUS_UITTREKSEL("zaken_init_uittreksel", "Uittreksel",
      GROUP_ZAKEN_STATUS, true, true, true, "2"),

  ZAKEN_DMS_UITTREKSEL("zaken_dms_uittreksel", "Uittreksel",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_VERH_BINNEN_WA("zaken_init_verh_binnen_wa", "Binnengem. verhuizing (woonadres)",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_INIT_STATUS_VERH_BINNEN_BA("zaken_init_verh_binnen_ba", "Binnengem. verhuizing (briefadres)",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_INIT_STATUS_VERH_BUITEN_WA("zaken_init_verh_buiten_wa", "Intergem. verhuizing (woonadres)",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_INIT_STATUS_VERH_BUITEN_BA("zaken_init_verh_buiten_ba", "Intergem. verhuizing (briefadres)",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_INIT_STATUS_VERH_HERV_WA("zaken_init_verh_herv_wa", "Hervestiging (woonadres)",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_INIT_STATUS_VERH_HERV_BA("zaken_init_verh_herv_ba", "Hervestiging (briefadres)",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_INIT_STATUS_VERH_EMIGR_WA("zaken_init_emigr_wa", "Emigratie (woonadres)",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_INIT_STATUS_VERH_EMIGR_BA("zaken_init_emigr_ba", "Emigratie (briefadres)",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_INIT_STATUS_REGISTRATION_WA("zaken_init_registration_ba", "Eerste inschrijving (woonadres)",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_INIT_STATUS_REGISTRATION_BA("zaken_init_registration_wa", "Eerste inschrijving (briefadres)",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_DMS_VERHUIZING("zaken_init_verhuizing", "Verhuizing",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_NAAMGEBRUIK("zaken_init_naamgebruik", "Naamgebruik",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_DMS_NAAMGEBRUIK("zaken_dms_naamgebruik", "Naamgebruik",
      GROUP_ZAKEN_DMS_TYPES, true, true, true, ""),

  ZAKEN_INIT_STATUS_PL_MUTATIE("zaken_init_pl_mutatie", "PL Mutatie",
      GROUP_ZAKEN_STATUS, true, true, true, "0"),

  ZAKEN_NIEUW_BRONNEN("zaken_nieuw_bron", "Bronnen",
          GROUP_ZAKEN_BEHANDELEN, true, true, true, ""),

  ZAKEN_NIEUW_LEVERANCIERS("zaken_nieuw_leveranciers", "Leveranciers",
          GROUP_ZAKEN_BEHANDELEN, true, true, true, ""),

  ZAKEN_NIEUW_ZAAKTYPES("zaken_nieuw_zaaktypes", "Zaaktypes",
          GROUP_ZAKEN_BEHANDELEN, true, true, true, ""),

  COVOG_ID_CODE("covog_idcode", "COVOG identificatiecode",
      GROUP_COVOG, true, false, false, ""),

  COVOG_RELATIE_ID("covog_relatiecode", "COVOG relatiecode",
      GROUP_COVOG, true, false, false, ""),

  COVOG_URL("covog_url", "COVOG URL",
      GROUP_COVOG, true, false, false, ""),

  SSL_PROXY_URL("ssl_proxy_url", "SSL Proxy (URL)",
      GROUP_ALGEMENE_INSTELLINGEN, true, false, false, ""),

  BSM_ENABLED("bsm_enabled", "Taakplanner inschakelen",
      GROUP_BSM, true, false, false, "1"),

  BSM_INTERNAL_URL("bsm_internal_url", "Taakplanner (Interne URL)",
      GROUP_BSM, true, false, false, ""),

  BSM_EXTERNAL_URL("bsm_external_url", "Taakplanner (Externe URL)",
      GROUP_BSM, true, false, false, ""),

  BSM_ZAKEN_DMS("bsm_zaken_dms", "Zaken DMS inschakelen",
      GROUP_ZAKEN_DMS, true, false, false, "0"),

  BSM_ZAKEN_DMS_ZAAKTYPE("bsm_zaken_dms_zaaktype", "Zaken DMS Zaak-id type",
      GROUP_ZAKEN_DMS, true, false, false, ""),

  BSM_ZAKEN_DMS_VARIANT("bsm_zaken_dms_variant", "Zaakgegevens opvragen variant",
      GROUP_ZAKEN_DMS, true, false, false, "1"),

  LOCATIE_OPSLAG("locatie_opslag", "Laatste locatie automatisch selecteren",
      GROUP_ALGEMENE_INSTELLINGEN, true, true, true, "1"),

  HANDLEIDING_RAADPLEGER("handleiding_raadpleger", "Handleiding voor raadplegers",
      GROUP_HANDLEIDINGEN, true, true, true, ""),

  HANDLEIDING_GEBRUIKER("handleiding_gebruiker", "Handleiding voor gebruikers",
      GROUP_HANDLEIDINGEN, true, true, true, ""),

  HANDLEIDING_BEHEERDER("handleiding_beheerder", "Handleiding voor beheerders",
      GROUP_HANDLEIDINGEN, true, true, true, ""),

  HANDLEIDING_INSCHRIJVING("handleiding_inschrijving", "Handleiding voor 1e inschrijving",
      GROUP_HANDLEIDINGEN, true, true, true, ""),

  HANDLEIDING_HUP("handleiding_uitvoeringsprocedures", "Handleiding uitvoeringsprocedures (HUP)",
      GROUP_HANDLEIDINGEN, true, true, true, ""),

  DOC_VERWIJDEREN("doc_verwijderen", "Opgeslagen documenten verwijderen",
      GROUP_DOCUMENTEN, true, true, true, ""),

  DOC_TOON_BESTAND("doc_toon_bestand", "Bestand tonen bij afdrukken",
      GROUP_DOCUMENTEN, true, false, false, ""),

  DOC_TEMPLATE_PAD("doc_template_pad", "Documenten sjabloonmap",
      GROUP_DOCUMENTEN, true, false, false, ""),

  DOC_DMS_TYPE("doc_dms_type", "Type documentenopslag",
      GROUP_DOCUMENTEN, true, false, false, "personen"),

  DOC_OUTPUT_PAD("doc_output_pad", "Proweb Personen documentenmap",
      GROUP_DOCUMENTEN, true, false, false, ""),

  DOC_CONFIDENTIALITY("doc_confidentiality", "Standaard mate van vertrouwelijkheid",
      GROUP_DOCUMENTEN, true, false, false, ""),

  DOC_OBJECT_STORAGE_ENABLED("doc_object_storage_enabled", "VrijBRP ObjectStore inschakelen",
      GROUP_DOCUMENTEN, true, false, false, ""),

  DOC_OBJECT_STORAGE_URL("doc_object_storage_url", "VrijBRP ObjectStore endpoint",
      GROUP_DOCUMENTEN, true, false, false, ""),

  DOC_OBJECT_STORAGE_USERNAME("doc_object_storage_username", "VrijBRP ObjectStore gebruikersnaam",
      GROUP_DOCUMENTEN, true, false, false, ""),

  DOC_OBJECT_STORAGE_PW("doc_object_storage_pw", "VrijBRP ObjectStore wachtwoord",
      GROUP_DOCUMENTEN, true, false, false, ""),

  GEMEENTE_CODES("gemeente_codes", "Gemeentecode(s)",
      GROUP_ALGEMENE_INSTELLINGEN, true, false, false, ""),

  GMAPKEY("gmapkey", "Google Maps sleutel",
      GROUP_ALGEMENE_INSTELLINGEN, true, false, false, ""),

  GPK_AFGEVER("gpk_afgever", "De afgever van GP-kaarten",
      GROUP_GPK, true, false, false, ""),

  ID_VERPLICHT("id_verplicht", "Identificering verplicht",
      GROUP_WERKPROCES, true, true, true, ""),

  FS_REISDOC("fs_reisdocument", "Functiescheiding reisdocumenten",
      GROUP_WERKPROCES, true, true, true, ""),

  FS_RIJBEWIJS("fs_rijbewijs", "Functiescheiding rijbewijzen",
      GROUP_WERKPROCES, true, true, true, ""),

  TOON_AANTEKENING("toon_aantekening", "Tonen aantekeningen",
      GROUP_WERKPROCES, true, true, true, ""),

  ACCESS_RISK_PROFILE_SIGNALS("access_risk_profile_signals", "Toegang tot markeringen",
      GROUP_WERKPROCES, false, true, true, ""),

  CONTACT_VERPLICHT("contact_verplicht", "Contactgegevens verplicht",
      GROUP_CONTACT, true, true, true, ""),

  REMEMBER_ME("remember_me", "Onthoud-mij-optie",
      GROUP_ALGEMENE_INSTELLINGEN, true, false, false, ""),

  SCHERMOPBOUWTYPE("schermopbouwtype", "Schermopbouwtype",
      GROUP_ALGEMENE_INSTELLINGEN, true, false, true, "0"),

  INLOGOPMERKING("inlogopmerking", "Inlogopmerking",
      GROUP_ALGEMENE_INSTELLINGEN, true, false, false, ""),

  KASSA_CLEAR_LIST("kassa_clear_file", "Kassalijst opschonen na betaling",
      GROUP_KASSA, true, true, true, ""),

  KASSA_FTP_PW("kassa_ftp_password", "Kassa FTP wachtwoord",
      GROUP_KASSA, true, false, false, ""),

  KASSA_FTP_URL("kassa_ftp_url", "Kassa FTP site",
      GROUP_KASSA, true, false, false, ""),

  KASSA_FTP_USERNAME("kassa_ftp_username", "Kassa FTP gebruikersnaam",
      GROUP_KASSA, true, false, false, ""),

  KASSA_FILENAME("kassa_filename", "Kassa uitvoerbestand",
      GROUP_KASSA, true, false, false, ""),

  KASSA_ID("kassa_id", "Kassa ID",
      GROUP_KASSA, true, false, false, ""),

  LOG("log", "Toegang tot de log",
      GROUP_ALGEMENE_INSTELLINGEN, false, true, true, ""),

  MIDOFFICE_BVH_OPNEMEN("midoffice_bvh_opnemen", "Binnenverhuizing naar midoffice sturen?",
      GROUP_MIDOFFICE, false, true, true, ""),

  MIDOFFICE_DASHBOARD_BRONNEN("dashboard_mo_bron", "Midoffice bronnen (dashboard)",
      GROUP_MIDOFFICE, true, false, false, ""),

  MIDOFFICE_DASHBOARD_LEVERANCIERS("dashboard_mo_leverancier", "Midoffice leveranciers (dashboard)",
      GROUP_MIDOFFICE, true, false, false, ""),

  OPENOFFICE_HOSTNAME("openoffice_hostname", "OpenOffice Host", GROUP_PRINT, true, false, false, ""),

  OPENOFFICE_PORT("openoffice_port", "OpenOffice Poort", GROUP_PRINT, true, false, false, ""),

  OPENOFFICE_CONVERT("openoffice_convert", "OpenOffice installatie", GROUP_PRINT, true, false, false, ""),

  BZ_CONNECT_PRINTING_ENABLED("bz_connect_printing_enabled", "Connect inschakelen voor printen", GROUP_PRINT, true,
      false, false, ""),

  BZ_CONNECT_ENABLED("bz_connect_enabled", "Connect inschakelen", GROUP_CONNECT, true, false, false,
      "0"),

  BZ_CONNECT_URL("bz_connect_url", "Connect - url", GROUP_CONNECT, true, false, false, ""),

  BZ_CONNECT_USERNAME("bz_connect_username", "Connect - gebruikersnaam", GROUP_CONNECT, true, false, false,
      ""),

  BZ_CONNECT_PW("bz_connect_pw", "Connect - wachtwoord", GROUP_CONNECT, true, false, false,
      ""),

  GEBR_PPD("c_ppd", "PPD code",
      GROUP_ALGEMENE_INSTELLINGEN, false, true, true, ""),

  PRESENTIE_VRAAG_ENDPOINT("presentie_vraag_endpoint", "URL van de presentievraag",
      GROUP_PRESENTIEVRAAG, true, false, false, ""),

  KENNISBANK_URL("kennisbank_url", "URL van de vindburgerzaken homepage ",
      GROUP_KENNISBANK, true, false, false, "http://portal.vindburgerzaken.nl"),

  PROT_STORE_PL("prot_store_level", "Mate van protocollering",
      GROUP_PROTOCOLLERING, true, false, false, ""),

  PROT_IGNORE_SEARCH("prot_ignore_search", "Uitsluiten van protocolleren zoekopdrachten",
      GROUP_PROTOCOLLERING, false, false, true, ""),

  PROT_IGNORE_LOGIN("prot_ignore_login", "Uitsluiten van protocolleren inlogpogingen",
      GROUP_PROTOCOLLERING, false, false, true, ""),

  RYB_VERGELIJKSCHERM("ryb_vergelijkscherm", "Vergelijkingsscherm tonen",
      GROUP_RIJBEWIJZEN, true, true, true, ""),

  RYB_TEST("ryb_test", "Gebruik testberichten",
      GROUP_RIJBEWIJZEN, true, true, true, ""),

  RYB_AANPASSINGEN("ryb_wijz", "RDW aanpassingen",
      GROUP_RIJBEWIJZEN, true, false, false, ""),

  RYB_ENABLED("ryb_enabled", "Rijbewijzen service inschakelen",
      GROUP_RIJBEWIJZEN, true, true, true, "1"),

  RYB_URL("ryb_url", "URL van de rijbewijzen",
      GROUP_RIJBEWIJZEN, true, false, false, ""),

  RYB_GEBRUIKERSNAAM("ryb_gebr_ident", "RDW account-id",
      GROUP_RIJBEWIJZEN, true, false, false, ""),

  RYB_PV_NR("ryb_pv_nr", "Standaard proces-verbaalnummer",
      GROUP_RIJBEWIJZEN, true, false, false, ""),

  RYB_VERVALTERMIJN_DAGEN("ryb_vervaltermijn_dagen", "Wachtwoord vervaltermijn (in dagen)",
      GROUP_RIJBEWIJZEN, true, false, false, "30"),

  REISD_PV_NR("reisd_pv_nr", "Standaard proces-verbaalnummer",
      GROUP_REISDOCUMENTEN, true, false, false, ""),

  REISD_TERMIJN_WIJZIGING("reisd_termijn_wijziging", "Ingangsdatum nieuwe reglementen",
      GROUP_REISDOCUMENTEN, true, false, false, "20140309"),

  REISD_SIGNAL_INFO("reisd_signal_info", "Toelichting bij signalering",
      GROUP_REISDOCUMENTEN, true, false, false,
      "Neem contact op met de Rijksdienst voor Identiteitsgegevens van het ministerie van " +
          "Binnenlandse Zaken en Koninkrijksrelaties via het nummer 088-9001000."),

  VRS_SERVICE_URL("reisd_vrs_url", "URL van VRS services application API",
      GROUP_REISDOCUMENTEN, true, false, false, ""),

  VRS_IDP_SERVICE_URL("reisd_vrs_idp_url", "URL van VRS services IDP API",
      GROUP_REISDOCUMENTEN, true, false, false, ""),

  VRS_SERVICE_TIMEOUT("reisd_vrs_timeout", "Timeout in seconden van VRS services application API",
      GROUP_REISDOCUMENTEN, true, false, false, "10"),

  VRS_START_DATE("reisd_vrs_start_date", "Ingangsdatum VRS",
      GROUP_REISDOCUMENTEN, true, false, false, "20230101"),

  VRS_CLIENT_ID("reisd_vrs_client_id", "VRS client-ID",
      GROUP_REISDOCUMENTEN, true, false, false, ""),

  VRS_CLIENT_SECRET("reisd_vrs_client_secret", "VRS client-secret",
      GROUP_REISDOCUMENTEN, true, false, false, ""),

  VRS_CLIENT_SCOPE("reisd_vrs_client_scope", "VRS scope",
      GROUP_REISDOCUMENTEN, true, false, false, ""),

  VRS_CLIENT_RESOURCE_SERVER("reisd_vrs_client_resource_server", "VRS resource server",
      GROUP_REISDOCUMENTEN, true, false, false, ""),

  VRS_INSTANTIE_CODE("reisd_vrs_instantie_code", "VRS instantie-code",
      GROUP_REISDOCUMENTEN, true, false, false, ""),

  TERUGMBEHEER("terugmbeheer", "Beheerder van de terugmeldingen",
      GROUP_TERUGMELDINGEN, false, true, true, ""),

  CURATELE_URL("curatele_url", "URL van de curatele webservice",
      GROUP_CURATELE, true, false, false, ""),

  CURATELE_GEBRUIKERSNAAM("curatele_gebr", "Gebruikersnaam",
      GROUP_CURATELE, true, false, false, ""),

  CURATELE_WACHTWOORD("curatele_ww", "Wachtwoord",
      GROUP_CURATELE, true, false, false, ""),

  TEST("test", "Code debuggen (Veel logging)",
      GROUP_ALGEMENE_INSTELLINGEN, true, false, true, ""),

  MIJN_OVERHEID_BULK_UITTREKSELS("mijn_overheid_bulk_uittreksels", "Mijn-overheid bij bulk uittreksels",
      GROUP_MIJN_OVERHEID, true, false, false, ""),

  TMV_URL("tmv_url", "Terugmeldingvoorziening URL",
      GROUP_TERUGMELDINGEN, true, false, false, ""),

  GV_KB_URL("gv_kb_url", "Schema URL",
      GROUP_GV, true, false, false,
      "http://www.kennisbankburgerzaken.nl/doc/285796/schema-voor-schriftelijke-verzoeken-om-gegevensverstrekking-uit-de-brp"),

  GV_DATUM_KENBAAR_MAKEN("gv_datum_kenbaar_maken", "Termijn in dagen (kenbaar maken)",
      GROUP_GV, true, false, false, "28"),

  GV_DATUM_VERSTREKKEN_GEEN_BEZW("gv_datum_verstrekk_geen_bezw", "Termijn in dagen (verstrekken zonder bezwaar)",
      GROUP_GV, true, false, false, "28"),

  GV_DATUM_VERSTREKKEN("gv_datum_verstrekk", "Termijn in dagen (verstrekken met bezwaar of geen reactie)",
      GROUP_GV, true, false, false, "28"),

  ONTBINDING_KB_URL("ontbinding_kb_url", "Beslisboom kennisbank",
      GROUP_ONTBINDING, true, false, false, ""),

  ONDERZ_FASE1_TERMIJN("onderz_fase1_termijn", "Termijn in dagen 1e fase",
      GROUP_ONDERZOEK, true, false, false, "14"),

  ONDERZ_FASE2_TERMIJN("onderz_fase2_termijn", "Termijn in dagen 2e fase",
      GROUP_ONDERZOEK, true, false, false, "14"),

  ONDERZ_EXTRA_TERMIJN("onderz_extra_termijn", "Termijn in dagen extra aanschrijving",
      GROUP_ONDERZOEK, true, false, false, "14"),

  ONDERZ_VOORNEMEN_TERMIJN("onderz_voornemen_termijn", "Termijn in dagen voornemen",
      GROUP_ONDERZOEK, true, false, false, "14"),

  ONDERZ_DEFAULT_AAND("onderz_default_aand", "Standaard aanduiding gegevens in onderzoek",
      GROUP_ONDERZOEK, true, false, false, ""),

  ONDERZ_5_DAGEN_TERM("onderz_5_dagen_term", "Onderzoek binnen 5 dagen af te handelen",
      GROUP_ONDERZOEK, true, false, false, ""),

  ONDERZ_ANDER_ORGAAN("onderz_ander_orgaan", "Gedegen onderzoek door ander overheidsorgaan en beschikbaar?",
      GROUP_ONDERZOEK, true, false, false, ""),

  ONDERZ_REDEN_OVERSLAAN("onderz_reden_overslaan", "Voldoende reden om stap(pen) over te slaan?",
      GROUP_ONDERZOEK, true, false, false, ""),

  VERHUIS_DATUM_LIMIET_TOEKOMST("verhuis_datum_limiet", "Verhuistermijn in dagen (toekomst)",
      GROUP_VERHUIZING, true, true, true, ""),

  VERHUIS_DATUM_LIMIET_VERLEDEN("verhuis_datum_limiet_verleden", "Verhuistermijn in dagen (verleden)",
      GROUP_VERHUIZING, true, true, true, ""),

  RISKANALYSIS_RELOCATION_IND("riskanalysis_relocation_ind", "Verhuizingindicatie",
      GROUP_RISKANALYSIS, true, false, false, ""),

  VER_VRAAG_ENDPOINT("ver_vraag_endpoint", "URL van de verificatievraag",
      GROUP_VERIFICATIEVRAAG, true, false, false, ""),

  WACHTWOORD_VERLOOP("ww_verloop", "Wachtw. verloop in dagen",
      GROUP_ALGEMENE_INSTELLINGEN, true, true, true, ""),

  SESSIE_TIMEOUT("sessiontimeout", "Sessie timeout (in minuten)",
      GROUP_ALGEMENE_INSTELLINGEN, true, true, true, ""),

  X_UA_COMPATIBLE("x_ua_compatible", "IE 10/11 legacy mode (x-ua-compatible)",
      GROUP_ALGEMENE_INSTELLINGEN, true, false, false, ""),

  ZOEK_MAX_FOUND_COUNT("zoek_max_find_count", "Maximaal aantal zoekresultaten",
      GROUP_ZOEKEN_ALGEMEEN, true, true, true, ""),

  ZOEK_ALL_RECORDS("zoek_all_records", "Mag alle zoekresultaten tonen",
      GROUP_ZOEKEN_ALGEMEEN, true, true, true, ""),

  ZOEK_PLE_JAVA_SERVER_USERNAME("java_ple_server_username", "Gebruikersnaam profiel",
      GROUP_ZOEKEN_PROFIEL_STANDAARD, true, false, true, ""),

  ZOEK_PLE_JAVA_SERVER_PW("java_ple_server_password", "Wachtwoord profiel",
      GROUP_ZOEKEN_PROFIEL_STANDAARD, true, false, true, ""),

  ZOEK_PERSONEN_PROFIEL2_USERNAME("zoek_personen_profiel2_username", "Gebruikersnaam profiel",
      GROUP_ZOEKEN_PROFIEL_GBAV_PLUS, true, false, true, ""),

  ZOEK_PERSONEN_PROFIEL2_PW("zoek_personen_profiel2_password", "Wachtwoord profiel",
      GROUP_ZOEKEN_PROFIEL_GBAV_PLUS, true, false, true, ""),

  ZOEK_PLE_BRON_GEMEENTE("zoek_ple_bron_gemeente", "Zoeken in gemeente database",
      GROUP_ZOEKEN_ALGEMEEN, true, true, true, ""),

  ZOEK_PLE_BRON_GBAV("zoek_ple_bron_gbav", "Zoeken in landelijke database",
      GROUP_ZOEKEN_ALGEMEEN, true, true, true, ""),

  KASSA_TYPE("kassa_type", "Kassatype",
      GROUP_KASSA, true, false, false, ""),

  KASSA_SEND_TYPE("kassa_send_type", "Versturen via",
      GROUP_KASSA, true, false, false, ""),

  PORTAAL_ROLLEN("portaal_rollen", "Rollen in VrijBRP Portaal",
      GROUP_PORTAAL, true, true, true, ""),

  ZOEK_PLE_NAAMGEBRUIK("zoek_ple_naamgebruik", "PL gegevens naar naamgebruik",
      GROUP_ZOEKEN_ALGEMEEN, true, true, true, ""),

  ZOEK_PLE_ADMIN_HIST("zoek_ple_admin_hist", "Toon administratieve historie",
      GROUP_ZOEKEN_ALGEMEEN, true, true, true, ""),

  EMAIL_HOST("email_host", "E-mail server",
      GROUP_EMAIL, true, false, false, ""),

  EMAIL_PORT("email_port", "Poort",
      GROUP_EMAIL, true, false, false, ""),

  EMAIL_USERNAME("email_username", "Gebruikersnaam",
      GROUP_EMAIL, true, false, false, ""),

  EMAIL_PW("email_pw", "Wachtwoord",
      GROUP_EMAIL, true, false, false, ""),

  EMAIL_EIGENSCHAPPEN("email_eigenschappen", "Overige e-mail eigenschappen",
      GROUP_EMAIL, true, false, false, ""),

  PROBEV_GEBR_CODE("probev_gebr_code", "PROBEV gebruiker code",
      GROUP_PROBEV, true, false, true, ""),

  SMS_ENABLED("SMS_enabled", "SMS service inschakelen",
      GROUP_SMS, true, true, true, ""),

  SMS_ENDPOINT("SMS_endpoint", "SMS service endpoint",
      GROUP_SMS, true, false, false, ""),

  SMS_USERNAME("SMS_username", "SMS gebruikersnaam",
      GROUP_SMS, true, false, false, ""),

  SMS_PW("SMS_password", "SMS wachtwoord",
      GROUP_SMS, true, false, false, ""),

  GEO_ENABLED("Geo_enabled", "Geo / BAG service inschakelen",
      GROUP_GEO, true, true, true, ""),

  GEO_ENDPOINT("Geo_endpoint", "Geo / BAG service endpoint",
      GROUP_GEO, true, false, false, ""),

  GEO_SEARCH_DEFAULT("Geo_search_default", "Veld 'Bron gegevens standaard' op BAG ",
      GROUP_GEO, true, true, true, ""),

  RAAS_ENDPOINT("RAAS_endpoint", "RAAS service endpoint",
      GROUP_RAAS, true, false, false, ""),

  RAAS_ENABLED("RAAS_enabled", "RAAS service inschakelen",
      GROUP_RAAS, true, true, true, ""),

  RAAS_IDENT_BEWIJS("RAAS_ident_bewijs", "Omschrijving bewijsstukken identiteit",
      GROUP_RAAS, true, false, false, "ID-document/vragen"),

  SYSTEM_MIN_HD_SIZE("system_min_hd_size", "Minimale grootte vrije ruimte in MB",
      GROUP_SYSTEM, true, false, false, "1000"),
  //
  // Internal Parameters. These cannot be changed by the user
  //
  PL_TOON_RECORDS("pl_toon_records", GROUP_INTERNAL, false, false, false, "1000"),
  SHOW_MESSAGES("show_messages", GROUP_INTERNAL, false, false, false, ""),
  SHOW_USER_MESSAGES("show_user_messages", GROUP_INTERNAL, false, false, false, ""),
  MIN_VOORRAAD("min_voorraad", GROUP_INTERNAL, false, false, false, ""),
  ZAKEN_SORT("zaken_sort", GROUP_INTERNAL, false, false, false, ""),
  COVOG_VOLGNR("covog_volgnr", GROUP_INTERNAL, false, false, false, ""),
  PROCURA_WS("procura_ws", GROUP_INTERNAL, false, false, false, ""),
  RYB_WACHTWOORD("ryb_gebr_ww", GROUP_INTERNAL, false, false, false, ""),
  RYB_DATUM_GEWIJZIGD("ryb_datum_gewijzigd", GROUP_INTERNAL, false, false, false, ""),
  RYB_GEBLOKKEERD("ryb_geblokkeerd", GROUP_INTERNAL, false, false, false, ""),
  ZAKENREGISTER_STATUS("zakenregister_status", GROUP_INTERNAL, false, false, false, ""),
  EMAIL_ONTVANGER_TEST("email_ontvanger_test", GROUP_INTERNAL, false, false, false, ""),
  EMAIL_AFZENDER_TEST("email_afzender_test", GROUP_INTERNAL, false, false, false, ""),
  EMAIL_BEANTWOORDER_TEST("email_beantwoorder_test", GROUP_INTERNAL, false, false, false, ""),
  OTHER("other", GROUP_INTERNAL, false, false, false, "");

  private final String         key;
  private final String         descr;
  private final ParameterGroup category;
  private final boolean        showApplication;
  private final boolean        showProfile;
  private final boolean        showUser;
  private final String         defaultValue;

  ParameterConstant(String key, ParameterGroup category, boolean showApplication, boolean showProfile,
      boolean showUser, String defaultValue) {
    this(key, "", category, showApplication, showProfile, showUser, defaultValue);
  }

  ParameterConstant(String key, String descr, ParameterGroup category, boolean showApplication,
      boolean showProfile, boolean showUser,
      String defaultValue) {
    this.key = key;
    this.descr = descr;
    this.category = category;
    this.showApplication = showApplication;
    this.showProfile = showProfile;
    this.showUser = showUser;
    this.defaultValue = defaultValue;
  }

  @Override
  public boolean isKey(String key) {
    return getKey().equals(key);
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public String getDescr() {
    return descr;
  }

  @Override
  public ParameterGroup getCategory() {
    return category;
  }

  @Override
  public boolean isShowApplication() {
    return showApplication;
  }

  @Override
  public boolean isShowProfile() {
    return showProfile;
  }

  @Override
  public boolean isShowUser() {
    return showUser;
  }

  @Override
  public String getDefaultValue() {
    return defaultValue;
  }

  @Override
  public String toString() {
    return getDescr() + " (" + getCategory().getCaption() + ")";
  }
}
