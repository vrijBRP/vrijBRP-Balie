/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.parameters.bean;

import static nl.procura.commons.elements.codegeneration.GenerateReflectionUtils.getNewInstanceof;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ACCESS_RISK_PROFILE_SIGNALS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.BSM_ENABLED;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.BSM_EXTERNAL_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.BSM_INTERNAL_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.BSM_ZAKEN_DMS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.BSM_ZAKEN_DMS_VARIANT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.BSM_ZAKEN_DMS_ZAAKTYPE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.BZ_CONNECT_ENABLED;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.BZ_CONNECT_PRINTING_ENABLED;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.BZ_CONNECT_PW;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.BZ_CONNECT_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.BZ_CONNECT_USERNAME;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.CONTACT_VERPLICHT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.COVOG_ID_CODE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.COVOG_RELATIE_ID;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.COVOG_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.CURATELE_GEBRUIKERSNAAM;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.CURATELE_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.CURATELE_WACHTWOORD;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.DOC_CONFIDENTIALITY;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.DOC_DMS_TYPE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.DOC_OBJECT_STORAGE_ENABLED;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.DOC_OBJECT_STORAGE_PW;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.DOC_OBJECT_STORAGE_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.DOC_OBJECT_STORAGE_USERNAME;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.DOC_OUTPUT_PAD;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.DOC_TEMPLATE_PAD;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.DOC_TOON_BESTAND;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.DOC_VERWIJDEREN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.EMAIL_EIGENSCHAPPEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.EMAIL_HOST;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.EMAIL_PORT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.EMAIL_PW;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.EMAIL_USERNAME;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.FS_REISDOC;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.FS_RIJBEWIJS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GEBR_PPD;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GEMEENTE_CODES;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GEO_ENABLED;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GEO_ENDPOINT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GEO_PW;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GEO_SEARCH_DEFAULT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GEO_USERNAME;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GMAPKEY;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GPK_AFGEVER;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GV_DATUM_KENBAAR_MAKEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GV_DATUM_VERSTREKKEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GV_DATUM_VERSTREKKEN_GEEN_BEZW;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GV_KB_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.HANDLEIDING_BEHEERDER;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.HANDLEIDING_GEBRUIKER;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.HANDLEIDING_HUP;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.HANDLEIDING_INSCHRIJVING;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ID_VERPLICHT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.INLOGOPMERKING;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.KASSA_CLEAR_LIST;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.KASSA_FILENAME;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.KASSA_FTP_PW;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.KASSA_FTP_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.KASSA_FTP_USERNAME;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.KASSA_ID;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.KASSA_SEND_TYPE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.KASSA_TYPE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.KENNISBANK_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.LOCATIE_OPSLAG;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.LOG;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.MIDOFFICE_BVH_OPNEMEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.MIDOFFICE_DASHBOARD_BRONNEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.MIDOFFICE_DASHBOARD_LEVERANCIERS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.MIJN_OVERHEID_BULK_UITTREKSELS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ONDERZ_5_DAGEN_TERM;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ONDERZ_ANDER_ORGAAN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ONDERZ_DEFAULT_AAND;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ONDERZ_EXTRA_TERMIJN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ONDERZ_FASE1_TERMIJN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ONDERZ_FASE2_TERMIJN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ONDERZ_REDEN_OVERSLAAN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ONDERZ_VOORNEMEN_TERMIJN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ONTBINDING_KB_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.OPENOFFICE_CONVERT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.OPENOFFICE_HOSTNAME;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.OPENOFFICE_PORT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.OPENOFFICE_REST_API;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.PORTAAL_ROLLEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.PRESENTIE_VRAAG_ENDPOINT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.PROBEV_GEBR_CODE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.PROT_IGNORE_LOGIN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.PROT_IGNORE_SEARCH;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.PROT_STORE_PL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.RAAS_ENABLED;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.RAAS_ENDPOINT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.RAAS_IDENT_BEWIJS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.REISD_PV_NR;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.REISD_SIGNAL_INFO;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.REISD_WIJZIGING_GEZAG;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.REMEMBER_ME;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.RISKANALYSIS_RELOCATION_IND;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.RYB_AANPASSINGEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.RYB_ENABLED;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.RYB_GEBRUIKERSNAAM;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.RYB_PV_NR;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.RYB_TEST;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.RYB_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.RYB_VERGELIJKSCHERM;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.RYB_VERVALTERMIJN_DAGEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.SCHERMOPBOUWTYPE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.SESSIE_TIMEOUT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.SMS_ENABLED;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.SMS_ENDPOINT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.SMS_PW;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.SMS_USERNAME;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.SSL_PROXY_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.SYSTEM_MIN_HD_SIZE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.TERUGMBEHEER;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.TEST;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.TEST_OMGEVING;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.TMV_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.TOON_AANTEKENING;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VERHUIS_DATUM_LIMIET_TOEKOMST;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VERHUIS_DATUM_LIMIET_VERLEDEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VER_VRAAG_ENDPOINT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_BASISREGISTER;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_CLIENT_ID;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_CLIENT_RESOURCE_SERVER;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_CLIENT_SCOPE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_CLIENT_SECRET;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_ENABLED;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_IDP_SERVICE_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_INSTANTIE_CODE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_SERVICE_TIMEOUT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.VRS_SERVICE_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.WACHTWOORD_VERLOOP;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.X_UA_COMPATIBLE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_AFSTAM_ERK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_AFSTAM_GEB;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_AFSTAM_LV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_AFSTAM_NK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_COVOG;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_GV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_HUW_GPS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_INH_VERMIS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_NAAMGEBRUIK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_NATURALISATIE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_OMZET_GPS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_ONDERZOEK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_ONTB_HUW_GPS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_OVERL_GEM;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_OVERL_LEVENLOOS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_OVERL_LIJKV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_REGISTRATION;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_REISDOC;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_RIJBEWIJS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_RISK_ANALYSIS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_UITTREKSEL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_VERHUIZING;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_DMS_VERSTREK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_EINDSTATUS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_AFSTAM_ERK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_AFSTAM_GEB;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_AFSTAM_LV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_AFSTAM_NK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_CORRES;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_COVOG;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_GPK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_GV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_HUW_GPS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_INBOX;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_INDICATIE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_INH_VERMIS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_INH_VERMIS_RYB;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_NAAMGEBRUIK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_NATURALISATIE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_OMZET_GPS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_ONDERZOEK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_ONTBINDING;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_OVERL_GEM;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_OVERL_LEVENLOOS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_OVERL_LIJKV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_PL_MUTATIE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_REGISTRATION_BA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_REGISTRATION_WA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_REISDOC;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_RIJBEWIJS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_RISK_ANALYSIS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_TMV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_UITTREKSEL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERH_BINNEN_BA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERH_BINNEN_WA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERH_BUITEN_BA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERH_BUITEN_WA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERH_EMIGR_BA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERH_EMIGR_WA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERH_HERV_BA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERH_HERV_WA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_INIT_STATUS_VERSTREK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_MAX_STATUS_ZAAK_WIJZIGEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_NIEUW_BRONNEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_NIEUW_LEVERANCIERS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_NIEUW_ZAAKTYPES;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_STATUS_EIGEN_ZAAK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZAKEN_TAB_VOLGORDE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZOEK_ALL_RECORDS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZOEK_MAX_FOUND_COUNT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZOEK_PERSONEN_PROFIEL2_PW;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZOEK_PERSONEN_PROFIEL2_USERNAME;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZOEK_PLE_ADMIN_HIST;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZOEK_PLE_BRON_GBAV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZOEK_PLE_BRON_GEMEENTE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZOEK_PLE_JAVA_SERVER_PW;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZOEK_PLE_JAVA_SERVER_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZOEK_PLE_JAVA_SERVER_USERNAME;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZOEK_PLE_NAAMGEBRUIK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZOEK_PROFIEL;
import static nl.procura.standard.Globalfunctions.astr;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.jpa.personen.db.Parm;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.SimpleMultiField;
import nl.procura.gba.web.components.fields.values.MultiFieldValue;
import nl.procura.gba.web.modules.beheer.parameters.container.ContactVerplichtContainer;
import nl.procura.gba.web.modules.beheer.parameters.container.DmsTypeContainer;
import nl.procura.gba.web.modules.beheer.parameters.container.DocumentVertrouwelijkheidParameterContainer;
import nl.procura.gba.web.modules.beheer.parameters.container.IdVerplichtContainer;
import nl.procura.gba.web.modules.beheer.parameters.container.KassaApplicationTypeContainer;
import nl.procura.gba.web.modules.beheer.parameters.container.KassaVerstuurTypeContainer;
import nl.procura.gba.web.modules.beheer.parameters.container.KassalijstOpschonenContainer;
import nl.procura.gba.web.modules.beheer.parameters.container.OpenOfficeContainer;
import nl.procura.gba.web.modules.beheer.parameters.container.ParmBooleanContainer;
import nl.procura.gba.web.modules.beheer.parameters.container.ParmEmptyBooleanContainer;
import nl.procura.gba.web.modules.beheer.parameters.container.ProtocolleringContainer;
import nl.procura.gba.web.modules.beheer.parameters.container.RdwAanpassingenContainer;
import nl.procura.gba.web.modules.beheer.parameters.container.SchermopbouwtypeContainer;
import nl.procura.gba.web.modules.beheer.parameters.container.TabVolgordeContainer;
import nl.procura.gba.web.modules.beheer.parameters.container.ZaakDmsVariantContainer;
import nl.procura.gba.web.modules.beheer.parameters.container.ZaakIdentificatieTypeParameterContainer;
import nl.procura.gba.web.modules.beheer.parameters.layout.ZakenBehandelingParametersLayout.toDatabaseConverter;
import nl.procura.gba.web.modules.beheer.parameters.layout.ZakenBehandelingParametersLayout.toFieldConverter;
import nl.procura.gba.web.modules.bs.onderzoek.page20.AanduidingOnderzoekContainer;
import nl.procura.gba.web.services.beheer.parameter.ParameterType;
import nl.procura.gba.web.services.beheer.parameter.Parameters;
import nl.procura.gba.web.services.beheer.parameter.annotations.ParameterAnnotation;
import nl.procura.gba.web.services.beheer.parameter.annotations.ParameterValueConverter;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.annotation.layout.FormBean;
import nl.procura.vaadin.annotation.layout.Position;
import nl.procura.vaadin.annotation.layout.Position.Direction;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.validator.GetalValidator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD,
    defaultWidth = "220px")
@FormBean(defaultCaptionWidth = "200px")
public class ParameterBean implements Serializable {

  public static final long    GEEN_GEBRUIKER             = 0;
  public static final long    GEEN_PROFIEL               = 0;
  private static final String CODE_STATUS_OPGENOMEN      = "0";
  private static final String CODE_STATUS_IN_BEHANDELING = "1";
  private static final String CODE_STATUS_VERWERKT       = "2";
  private static final String CODE_STATUS_WACHTKAMER     = "5";

  // Testomgeving
  @ParameterAnnotation(TEST_OMGEVING)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Testomgeving",
      description = "Is dit de testomgeving.")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String testOmgeving = "";

  // Testomgeving
  @ParameterAnnotation(ZOEK_PROFIEL)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Aanvullend zoekprofiel",
      description = "Welk zoekprofiel inschakelen?.")
  private String zoekProfiel = "";

  // PLE Url
  @ParameterAnnotation(ZOEK_PLE_JAVA_SERVER_URL)
  @Position(order = "0")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Endpoint opvragen persoonsgegevens",
      width = "400px")
  private String urlPersonenWs = "";

  @ParameterAnnotation(ZAKEN_TAB_VOLGORDE)
  @Position(order = "0.1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Volgorde tabbladen van een zaak")
  @Select(containerDataSource = TabVolgordeContainer.class,
      itemCaptionPropertyId = TabVolgordeContainer.OMSCHRIJVING)
  private String zakenTabVolgorde = "";

  @ParameterAnnotation(ZAKEN_EINDSTATUS)
  @Position(order = "0.2")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Eindstatus wijzigen",
      description = "Mag een zaak met een 'eindstatus' nog worden gewijzigd")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String zakenEindstatus = "";

  @ParameterAnnotation(ZAKEN_STATUS_EIGEN_ZAAK)
  @Position(order = "0.3")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Status van zelf-ingevoerde zaak wijzigen",
      description = "Mag de status van een zelf-ingevoerde-zaak gewijzigd")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String zakenEigenStatus = "";

  @ParameterAnnotation(ZAKEN_MAX_STATUS_ZAAK_WIJZIGEN)
  @Position(order = "0.4")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Maximale status voor wijzigen zaak")
  private String zakenMaxStatusZaakWijzigen = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_AFSTAM_GEB)
  @Position(order = "Afstamming (geboorte)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afstamming (geboorte)")
  private String initieleStatusAfstamGeb = "";

  @ParameterAnnotation(ZAKEN_DMS_AFSTAM_GEB)
  @Position(order = "Afstamming (geboorte)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afstamming (geboorte)")
  private String dmsAfstamGeb = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_AFSTAM_ERK)
  @Position(order = "Afstamming (geboorte")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afstamming (geboorte")
  private String initieleStatusAfstamErk = "";

  @ParameterAnnotation(ZAKEN_DMS_AFSTAM_ERK)
  @Position(order = "Afstamming (erkenning)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afstamming (erkenning)")
  private String dmsAfstamErk = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_AFSTAM_NK)
  @Position(order = "Afstamming (naamskeuze)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afstamming (naamskeuze)")
  private String initieleStatusAfstamNk = "";

  @ParameterAnnotation(ZAKEN_DMS_AFSTAM_NK)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afstamming (naamskeuze)")
  private String dmsAfstamNk = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_AFSTAM_LV)
  @Position(order = "Afstamming (latere vermelding)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afstamming (latere vermelding)")
  private String initieleStatusAfstamLv = "";

  @ParameterAnnotation(ZAKEN_DMS_AFSTAM_LV)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Afstamming (latere vermelding)")
  private String dmsAfstamLv = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_CORRES)
  @Position(order = "Correspondentie")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Correspondentie")
  private String initieleStatusCorres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_COVOG)
  @Position(order = "COVOG")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "COVOG")
  private String initieleStatusCovog = "";

  @ParameterAnnotation(ZAKEN_DMS_COVOG)
  @Position(order = "COVOG")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "COVOG")
  private String dmsCovog = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERSTREK)
  @Position(order = "Verstrekkingsbeperking")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Verstrekkingsbeperking")
  private String initieleStatusVerstrek = "";

  @ParameterAnnotation(ZAKEN_DMS_VERSTREK)
  @Position(order = "Verstrekkingsbeperking")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Verstrekkingsbeperking")
  private String dmsVerstrek = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_GPK)
  @Position(order = "GPK")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "GPK")
  private String initieleStatusGpk = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_INBOX)
  @Position(order = "Inbox")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Inbox")
  private String initieleStatusInbox = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_INDICATIE)
  @Position(order = "Inbox")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Indicatie")
  private String initieleStatusIndicatie = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_HUW_GPS)
  @Position(order = "Huwelijk / GPS")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Huwelijk / GPS")
  private String initieleStatusHuwGps = "";

  @ParameterAnnotation(ZAKEN_DMS_HUW_GPS)
  @Position(order = "Huwelijk / GPS")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Huwelijk / GPS")
  private String dmsHuwGps = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_OMZET_GPS)
  @Position(order = "Omzetting GPS in huwelijk")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Omzetting GPS in huwelijk")
  private String initieleStatusOmzettingGps = "";

  @ParameterAnnotation(ZAKEN_DMS_OMZET_GPS)
  @Position(order = "Omzetting GPS in huwelijk")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Omzetting GPS in huwelijk")
  private String dmsOmzetGps = "";

  @ParameterAnnotation(ZAKEN_DMS_ONTB_HUW_GPS)
  @Position(order = "Ontbinding/einde Huwelijk/GPS")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Ontbinding/einde Huwelijk/GPS")
  private String dmsOntbHuwGps = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_ONTBINDING)
  @Position(order = "Ontbinding/einde huwelijk/GPS in gemeente")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Ontbinding/einde huwelijk/GPS in gemeente")
  private String initieleStatusOntbinding = "";

  @ParameterAnnotation(ZAKEN_DMS_ONDERZOEK)
  @Position(order = "Onderzoek")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Onderzoek")
  private String dmsOnderzoek = "";

  @ParameterAnnotation(ZAKEN_DMS_NATURALISATIE)
  @Position(order = "Nationaliteit")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Nationaliteit")
  private String dmsNaturalisatie = "";

  @ParameterAnnotation(ZAKEN_DMS_REGISTRATION)
  @Position(order = "Eerste inschrijving")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Eerste inschrijving")
  private String dmsRegistration = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_ONDERZOEK)
  @Position(order = "Onderzoek")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Onderzoek")
  private String initieleStatusOnderzoek = "";

  @ParameterAnnotation(ZAKEN_DMS_RISK_ANALYSIS)
  @Position(order = "Risicoanalyse")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Risicoanalyse")
  private String dmsRiskAnalysis = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_RISK_ANALYSIS)
  @Position(order = "Risicoanalyse")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Risicoanalyse")
  private String initialStatusRiskAnalysis = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_INH_VERMIS)
  @Position(order = "Inhouding / vermissing reisdocument")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Inhouding / vermissing reisdocument")
  private String initieleStatusInhVermis = "";

  @ParameterAnnotation(ZAKEN_DMS_INH_VERMIS)
  @Position(order = "Inhouding/vermissing")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Inhouding/vermissing")
  private String dmsInhVerm = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_INH_VERMIS_RYB)
  @Position(order = "Inhouding / vermissing rijbewijs")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Inhouding / vermissing rijbewijs")
  private String initieleStatusInhVermisRijb = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_OVERL_GEM)
  @Position(order = "Overlijden (in gemeente)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Overlijden (in gemeente)")
  private String initieleStatusOverlGem = "";

  @ParameterAnnotation(ZAKEN_DMS_OVERL_GEM)
  @Position(order = "Overlijden (in gemeente)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Overlijden (in gemeente)")
  private String dmsOverlGem = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_OVERL_LIJKV)
  @Position(order = "Overlijden (lijkvinding)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Overlijden (lijkvinding)")
  private String initieleStatusOverlLijkv = "";

  @ParameterAnnotation(ZAKEN_DMS_OVERL_LIJKV)
  @Position(order = "Overlijden (lijkvinding)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Overlijden (lijkvinding)")
  private String dmsOverlLijkv = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_OVERL_LEVENLOOS)
  @Position(order = "Overlijden (levenloos geboren kind)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Overlijden (levenloos geboren kind)")
  private String initieleStatusOverlLevenloos = "";

  @ParameterAnnotation(ZAKEN_DMS_OVERL_LEVENLOOS)
  @Position(order = "Overlijden (levenloos geboren kind)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Overlijden (levenloos geboren kind)")
  private String dmsOverlLevenloos = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_GV)
  @Position(order = "Gegevensverstrekking")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Gegevensverstrekking")
  private String initieleStatusGv = "";

  @ParameterAnnotation(ZAKEN_DMS_GV)
  @Position(order = "Gegevensverstrekking")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Gegevensverstrekking")
  private String dmsGv = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_REISDOC)
  @Position(order = "Reisdocument")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Reisdocument")
  private String initieleStatusReisdoc = "";

  @ParameterAnnotation(ZAKEN_DMS_REISDOC)
  @Position(order = "Reisdocument")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Reisdocument")
  private String dmsReisdoc = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_RIJBEWIJS)
  @Position(order = "Rijbewijs")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Rijbewijs")
  private String initieleStatusRijbewijs = "";

  @ParameterAnnotation(ZAKEN_DMS_RIJBEWIJS)
  @Position(order = "Rijbewijs")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Rijbewijs")
  private String dmsRijbewijs = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_TMV)
  @Position(order = "Terugmelding")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Terugmelding")
  private String initieleStatusTmv = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_UITTREKSEL)
  @Position(order = "Uittreksel")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Uittreksel")
  private String initieleStatusUitt = "";

  @ParameterAnnotation(ZAKEN_DMS_UITTREKSEL)
  @Position(order = "Uittreksel")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Uittreksel")
  private String dmsUitt = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERH_BINNEN_WA)
  @Position(order = "Binnenverhuizing (woonadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Binnenverhuizing (woonadres)")
  private String initieleStatusBinnenVerhWoonAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERH_BINNEN_BA)
  @Position(order = "Binnenverhuizing (briefadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Binnenverhuizing (briefadres)")
  private String initieleStatusBinnenVerhBriefAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERH_BUITEN_WA)
  @Position(order = "Intergem. verhuizing (woonadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Intergem. verhuizing (woonadres)")
  private String initieleStatusBuitenVerhWoonAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERH_BUITEN_BA)
  @Position(order = "Intergem. verhuizing (briefadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Intergem. verhuizing (briefadres)")
  private String initieleStatusBuitenVerhBriefAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERH_HERV_WA)
  @Position(order = "Hervestiging (woonadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Hervestiging (woonadres)")
  private String initieleStatusHervestigingWoonAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERH_HERV_BA)
  @Position(order = "Hervestiging (briefadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Hervestiging (briefadres)")
  private String initieleStatusHervestigingBriefAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERH_EMIGR_WA)
  @Position(order = "Emigratie (woonadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Emigratie (woonadres)")
  private String initieleStatusEmigrWoonAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_VERH_EMIGR_BA)
  @Position(order = "Emigratie (briefadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Emigratie (briefadres)")
  private String initieleStatusEmigrBriefAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_REGISTRATION_WA)
  @Position(order = "Eerste inschrijving (Woonadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Eerste inschrijving (Woonadres)")
  private String initieleStatusRegWoonAdres = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_REGISTRATION_BA)
  @Position(order = "Eerste inschrijving (Briefadres)")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Eerste inschrijving (Briefadres)")
  private String initieleStatusRegBriefAdres = "";

  @ParameterAnnotation(ZAKEN_DMS_VERHUIZING)
  @Position(order = "Verhuizing")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Verhuizing")
  private String dmsVerhuizing = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_NAAMGEBRUIK)
  @Position(order = "Naamgebruik")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Naamgebruik")
  private String initieleStatusNaamgebruik = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_NATURALISATIE)
  @Position(order = "Nationaliteit")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Nationaliteit")
  private String initieleStatusNaturalisatie = "";

  @ParameterAnnotation(ZAKEN_INIT_STATUS_PL_MUTATIE)
  @Position(order = "PL mutatie")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "PL mutatie")
  private String initieleStatusPLMutatie = "";

  @ParameterAnnotation(ZAKEN_DMS_NAAMGEBRUIK)
  @Position(order = "Naamgebruik")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Naamgebruik")
  private String dmsNaamgebruik = "";

  // Nieuwe zaken
  @ParameterAnnotation(ZAKEN_NIEUW_BRONNEN)
  @Position(order = "Bronnen")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Bronnen")
  private String nieuweZakenBronnen = "";

  @ParameterAnnotation(ZAKEN_NIEUW_LEVERANCIERS)
  @Position(order = "Leveranciers")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Leveranciers")
  private String nieuweZakenLeveranciers = "";

  @ParameterValueConverter(
      toField = toFieldConverter.class,
      toDB = toDatabaseConverter.class)
  @ParameterAnnotation(ZAKEN_NIEUW_ZAAKTYPES)
  @Position(order = "Zaaktypes")
  @Field(customTypeClass = SimpleMultiField.class,
      caption = "Zaaktypes")
  private MultiFieldValue<FieldValue> nieuweZakenTypes = new MultiFieldValue<>();

  // COVOG ID Code
  @ParameterAnnotation(COVOG_ID_CODE)
  @Position(order = "3")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "COVOG identificatiecode")
  @TextField(secret = true)
  private String covogIdentificatiecode = "";

  // COVOG Relatie ID
  @ParameterAnnotation(COVOG_RELATIE_ID)
  @Position(order = "2")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "COVOG relatiecode")
  private String covogRelatiecode = "";

  // COVOG URL
  @ParameterAnnotation(COVOG_URL)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "COVOG URL",
      width = "400px")
  private String covogURL = "";

  // COVOG URL
  @ParameterAnnotation(SSL_PROXY_URL)
  @Position(order = "9")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "SSL Proxy (URL)",
      width = "400px")
  private String sslProxyUrl = "";

  @ParameterAnnotation(BSM_ENABLED)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Taakplanner inschakelen",
      width = "400px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String bsmEnabled = "";

  @ParameterAnnotation(BSM_INTERNAL_URL)
  @Position(order = "2")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Taakplanner (Interne URL)",
      width = "400px")
  private String bsmInternalUrl = "";

  @ParameterAnnotation(BSM_EXTERNAL_URL)
  @Position(order = "3")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Taakplanner (Externe URL)",
      width = "400px")
  private String bsmExternalUrl = "";

  @ParameterAnnotation(BSM_ZAKEN_DMS)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Zaken DMS inschakelen",
      width = "400px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String bsmZakenDmsQueryAan = "";

  @ParameterAnnotation(BSM_ZAKEN_DMS_ZAAKTYPE)
  @Position(order = "2")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Zaken DMS Zaak-id type",
      width = "400px")
  @Select(containerDataSource = ZaakIdentificatieTypeParameterContainer.class,
      itemCaptionPropertyId = ZaakIdentificatieTypeParameterContainer.OMSCHRIJVING)
  private String bsmZakenDmsQueryZaaktype = "";

  @ParameterAnnotation(BSM_ZAKEN_DMS_VARIANT)
  @Position(order = "3")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Zaakgegevens opvragen variant",
      width = "400px")
  @Select(containerDataSource = ZaakDmsVariantContainer.class,
      itemCaptionPropertyId = ZaakDmsVariantContainer.OMSCHRIJVING)
  private String bsmZakenDmsZaakdetailsApart = "";

  // Test Debug
  @ParameterAnnotation(LOCATIE_OPSLAG)
  @Position(order = "11")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Laatste locatie automatisch selecteren",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String locatieOpslaan;

  // Handleidingen  
  @ParameterAnnotation(HANDLEIDING_GEBRUIKER)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Handleiding voor gebruikers",
      width = "60px")
  @Position(order = "2")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String handleidingGebruiker = "";

  @ParameterAnnotation(HANDLEIDING_BEHEERDER)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Handleiding voor beheerders",
      width = "60px")
  @Position(order = "3")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String handleidingBeheerder = "";

  @ParameterAnnotation(HANDLEIDING_INSCHRIJVING)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Handleiding voor 1e inschrijving",
      width = "60px")
  @Position(order = "4")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String handleidingInschrijving = "";

  @ParameterAnnotation(HANDLEIDING_HUP)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Handleiding uitvoeringsprocedures (HUP)",
      width = "60px")
  @Position(order = "5")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String handleidingUitvoeringsprocedures = "";

  // Documenten Verwijderen
  @ParameterAnnotation(DOC_VERWIJDEREN)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Opgeslagen documenten verwijderen",
      description = "Mogen de documenten die zijn opgeslagen door de gebruiker worden verwijderd?",
      width = "60px")
  @Position(order = "1")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String documentenVerwijderen = "";

  // Documenten Verwijderen
  @ParameterAnnotation(DOC_TOON_BESTAND)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Bestand tonen bij afdrukken",
      description = "Toon bij het genereren ook het sjabloon?",
      width = "60px")
  @Position(order = "10")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String documentSjabloonTonen = "";

  // Documenten Template Path
  @ParameterAnnotation(DOC_TEMPLATE_PAD)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Documenten sjabloonmap",
      description = "Map waarin de sjabolen voor de documenten worden opgeslagen. Leeg laten voor de standaard map.",
      width = "300px")
  @Position(order = "20")
  private String documentenTemplatePath = "";

  // Documenten Output Path
  @ParameterAnnotation(DOC_OUTPUT_PAD)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Proweb Personen documentenmap",
      description = "Map waarin de documentuitvoer wordt opgeslagen. Leeg laten voor de standaard map.",
      width = "300px")
  @Position(order = "40")
  private String documentenOutputPath = "";

  // Documenten mate van vertrouwelijkheid
  @ParameterAnnotation(DOC_CONFIDENTIALITY)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Mate van vertrouwelijkheid documenten",
      width = "300px")
  @Position(order = "41")
  @Select(containerDataSource = DocumentVertrouwelijkheidParameterContainer.class,
      itemCaptionPropertyId = DocumentVertrouwelijkheidParameterContainer.OMSCHRIJVING)
  @Immediate
  private String documentConf = "";

  // Documenten Verwijderen
  @ParameterAnnotation(DOC_DMS_TYPE)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Opslag locatie",
      description = "De plek waarop de bestanden worden opgeslagen",
      width = "300px")
  @Position(order = "42")
  @Select(containerDataSource = DmsTypeContainer.class,
      itemCaptionPropertyId = DmsTypeContainer.OMSCHRIJVING)
  @Immediate
  private String documentDmsType = "";

  // Document Object Storage
  @ParameterAnnotation(DOC_OBJECT_STORAGE_ENABLED)
  @Position(order = "50")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Documenten service actief",
      width = "70px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String documentObjStorageEnabled = "";

  @ParameterAnnotation(DOC_OBJECT_STORAGE_URL)
  @Position(order = "51")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Documenten service endpoint",
      width = "400px")
  private String documentObjStorageUrl = "";

  @ParameterAnnotation(DOC_OBJECT_STORAGE_USERNAME)
  @Position(order = "52")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Documenten service gebruikersnaam",
      width = "200px")
  private String documentObjStorageUsername = "";

  @ParameterAnnotation(DOC_OBJECT_STORAGE_PW)
  @Position(order = "53")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Documenten service wachtwoord",
      width = "200px")
  @TextField(secret = true)
  private String documentObjStoragePw = "";

  // Gemeentecodes
  @ParameterAnnotation(GEMEENTE_CODES)
  @Position(order = "5")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gemeentecode(s)",
      description = "De code(s) van de gemeente om te bepalen of een PL of adres binnengemeentelijk is. " +
          "Als er meerdere codes zijn, dan moeten deze door een komma (,) worden gescheiden.")
  private String gemeenteCodes = "";

  // Google Maps Key
  @ParameterAnnotation(GMAPKEY)
  @Position(order = "2")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Google Maps sleutel",
      description = "De kaart bij de extra persoonsgegevens werkt alleen als hier een geldige sleutel is ingegeven. " +
          "Deze sleutel kan worden verkregen bij http://www.google.com/apis/maps",
      width = "400px")
  private String googleMapKey = "";

  //  GPK
  @ParameterAnnotation(GPK_AFGEVER)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "De afgever van GP-kaarten")
  private String gpk = "";

  // Identificering Overslaan
  @ParameterAnnotation(ID_VERPLICHT)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Identificering verplicht",
      width = "250px")
  @Select(containerDataSource = IdVerplichtContainer.class,
      itemCaptionPropertyId = IdVerplichtContainer.OMSCHRIJVING)
  private String identificeringVerplichtheid = "";

  // Functiescheiding bij reisdocumenten
  @ParameterAnnotation(FS_REISDOC)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Functiescheiding reisdocumenten",
      description = "Moet er functiescheiding zijn met betrekking tot reisdocumentaanvragen?",
      width = "60px")
  @Position(order = "1")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String functieScheidingReisdocumenten = "";

  // Functiescheiding bij reisdocumenten
  @ParameterAnnotation(FS_RIJBEWIJS)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Functiescheiding rijbewijzen",
      description = "Moet er functiescheiding zijn met betrekking tot rijbewijsaanvragen?",
      width = "60px")
  @Position(order = "1")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String functieScheidingRijbewijzen = "";

  @ParameterAnnotation(TOON_AANTEKENING)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Tonen aantekeningen",
      description = "Moeten de aantekeningen worden getoond zodra een persoonslijst wordt gezocht?",
      width = "60px")
  @Position(order = "1")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String tonenAantekeningen = "";

  @ParameterAnnotation(ACCESS_RISK_PROFILE_SIGNALS)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Toegang tot markeringen",
      description = "Heeft deze persoon of profiel toegang tot het markeren van personen en adressen voor risicoanalyses?",
      width = "60px")
  @Position(order = "1")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String accessRiskProfileSignal = "";

  // Contactgegevens overslaan
  @ParameterAnnotation(CONTACT_VERPLICHT)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Contactgegevens verplicht",
      width = "250px")
  @Select(containerDataSource = ContactVerplichtContainer.class,
      itemCaptionPropertyId = ContactVerplichtContainer.OMSCHRIJVING)
  private String contactgegevensVerplichtheid = "";

  // Inlog opmerking
  @ParameterAnnotation(REMEMBER_ME)
  @Position(order = "4")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Onthoud-mij-optie",
      description = "Toon de optie op het inlogscherm waarmee de gebruiker automatisch kan inloggen.")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String rememberMeOption = "";

  // Inlog opmerking
  @ParameterAnnotation(SCHERMOPBOUWTYPE)
  @Position(order = "4a")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Schermopbouwtype",
      description = "Dynamisch: tabellen worden verticaal vergroot naar 100% van de pagina. Dit werkt niet goed op kleine schermen en/of in Internet Explorer 11. <br/>Vast: tabellen worden niet verticaal vergroot naar 100%.")
  @Select(containerDataSource = SchermopbouwtypeContainer.class,
      itemCaptionPropertyId = SchermopbouwtypeContainer.OMSCHRIJVING)
  private String schermopbouwtype = "";

  // Inlog opmerking
  @ParameterAnnotation(INLOGOPMERKING)
  @Position(order = "4")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Inlogopmerking",
      description = "Een opmerking die wordt getoond na het inloggen.",
      width = "400px",
      height = "100px")
  @TextField(rows = 3)
  private String inlogOpmerking = "";

  // Kassa Clear List
  @ParameterAnnotation(KASSA_CLEAR_LIST)
  @Position(order = "7")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Kassalijst opschonen na verzending")
  @Select(containerDataSource = KassalijstOpschonenContainer.class,
      itemCaptionPropertyId = KassalijstOpschonenContainer.OMSCHRIJVING)
  private String kassaClearList = "";

  // Kassa FTP Password
  @ParameterAnnotation(KASSA_FTP_PW)
  @Position(order = "5")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Kassa FTP wachtwoord")
  @TextField(secret = true)
  private String kassaFTPPassword = "";

  // Kassa FTP Url
  @ParameterAnnotation(KASSA_FTP_URL)
  @Position(order = "3",
      direction = Direction.VERTICAL_WITH_RULER)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Kassa FTP site",
      width = "400px")
  private String kassaFTPURL = "";

  // Kassa FTP Username
  @ParameterAnnotation(KASSA_FTP_USERNAME)
  @Position(order = "4")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Kassa FTP gebruikersnaam")
  @TextField(secret = true)
  private String kassaFTPUsername = "";

  // Kassa Filename
  @ParameterAnnotation(KASSA_FILENAME)
  @Position(order = "2",
      direction = Direction.VERTICAL_WITH_RULER)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Kassa uitvoerbestand",
      width = "400px")
  private String kassaFilename = "";

  // Kassa ID
  @ParameterAnnotation(KASSA_ID)
  @Position(order = "6",
      direction = Direction.VERTICAL_WITH_RULER)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Kassa ID")
  private String kassaID = "";

  // Log
  @ParameterAnnotation(LOG)
  @Position(order = "3")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Toegang tot de log",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String log;

  // Midoffice Binnenverhuizing
  @ParameterAnnotation(MIDOFFICE_BVH_OPNEMEN)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Binnenverhuizing naar midoffice sturen?",
      description = "De applicatie moet de binnenverhuizingen van deze gebruiker naar het midoffice doorsturen.",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String midofficeBinnenverhuizingOpnemen = "";

  @ParameterAnnotation(MIDOFFICE_DASHBOARD_BRONNEN)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Midoffice bronnen (dashboard)",
      description = "Wat voor bronnen gelden al midoffice bron.",
      width = "400px")
  private String moDashboardBron = "";

  @ParameterAnnotation(MIDOFFICE_DASHBOARD_LEVERANCIERS)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Midoffice leveranciers (dashboard)",
      description = "Wat voor leveranciers gelden al midoffice leverancier.",
      width = "400px")
  private String moDashboardLeverancier = "";

  // Open Office REST API
  @ParameterAnnotation(OPENOFFICE_REST_API)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "OpenOffice REST API",
      description = "Gebruik een externe REST API voor OpenOffice conversies")
  private String openOfficeRestAPI = "";

  // Open Office Hostname
  @ParameterAnnotation(OPENOFFICE_HOSTNAME)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "OpenOffice host",
      description = "Om de volledige functionaliteit van de documenten te kunnen benutten moet er " +
          "een OpenOffice listener draaien op een bepaalde host en port die de sjablonen " +
          "kan converteren naar het gewenste formaat")
  private String openOfficeHostname = "";

  // Open Office Port
  @ParameterAnnotation(OPENOFFICE_PORT)
  @Position(order = "2")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "OpenOffice poort",
      description = "De poort waarop de OpenOffice listener draait",
      width = "60px",
      validators = GetalValidator.class)
  @TextField(maxLength = 5)
  private String openOfficePort;

  // Open Office installatie
  @ParameterAnnotation(OPENOFFICE_CONVERT)
  @Position(order = "3")
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "OpenOffice installatie")
  @Select(containerDataSource = OpenOfficeContainer.class,
      itemCaptionPropertyId = OpenOfficeContainer.OMSCHRIJVING)
  private String openOfficeInstallatie = "remote";

  @ParameterAnnotation(BZ_CONNECT_PRINTING_ENABLED)
  @Position(order = "4")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "VrijBRP Connect inschakelen voor printen",
      description = "VrijBRP Connect inschakelen voor printen")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String bzPrintConnectEnabled = "";

  // VrijBRP Connect
  @ParameterAnnotation(BZ_CONNECT_ENABLED)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Inschakelen",
      description = "Inschakelen.")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String bzConnectEnabled = "";

  @ParameterAnnotation(BZ_CONNECT_URL)
  @Position(order = "2")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "URL van de service",
      description = "Het endpoint waarop de service draait")
  @TextField
  private String bzConnectHost;

  @ParameterAnnotation(BZ_CONNECT_USERNAME)
  @Position(order = "3")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gebruikersnaam",
      description = "De gebruikersnaam waarmee deze applicatie zich identificeert")
  @TextField
  private String bzConnectUsername;

  @ParameterAnnotation(BZ_CONNECT_PW)
  @Position(order = "4")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Wachtwoord",
      description = "Het wachtwoord waarmee deze applicatie zich identificeert")
  @TextField(secret = true)
  private String bzConnectPw;

  // PPD code
  @ParameterAnnotation(GEBR_PPD)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "PPD code",
      validators = { GetalValidator.class },
      width = "30px")
  @TextField(maxLength = 2)
  private String ppdCode = "";

  // Presentievraag
  @ParameterAnnotation(PRESENTIE_VRAAG_ENDPOINT)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "URL van de presentievraag",
      width = "400px")
  private String presentievraagURL = "";

  // Kennisbank
  @ParameterAnnotation(KENNISBANK_URL)
  @Position(order = "0")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "URL van de vindburgerzaken homepage ",
      width = "400px")
  private String kennisbankHomepageURL = "";

  // Protocollering PL
  @ParameterAnnotation(PROT_STORE_PL)
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "Mate van protocollering",
      description = "De mate van protocollering bij het zoeken van een specifieke persoonslijst",
      width = "300px")
  @Position(order = "2")
  @Select(containerDataSource = ProtocolleringContainer.class,
      itemCaptionPropertyId = ProtocolleringContainer.OMSCHRIJVING)
  private String protocolleringPL = "";

  // Protocollering zoekopdrachten niet vastleggen
  @ParameterAnnotation(PROT_IGNORE_SEARCH)
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "Geen zoekopdrachten protocolleren",
      description = "Van dit account worden geen zoekopdrachten vastgelegd",
      width = "100px")
  @Position(order = "3")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String protIgnoreSearch = "";

  // Protocollering logingpogingen niet vastleggen
  @ParameterAnnotation(PROT_IGNORE_LOGIN)
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "Geen inlogpogingen protocolleren",
      description = "Van dit account worden geen inlogpogingen vastgelegd",
      width = "100px")
  @Position(order = "4")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String protIgnoreLogin = "";

  // Vergelijkscherm Tonen
  @ParameterAnnotation(RYB_VERGELIJKSCHERM)
  @Position(order = "11")
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "Vergelijkingsscherm tonen",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String rijbewijsVergelijkscherm = "";

  @ParameterAnnotation(RYB_TEST)
  @Position(order = "12")
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "Gebruik testberichten",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String rijbewijsTesten = "";

  @ParameterAnnotation(RYB_AANPASSINGEN)
  @Position(order = "13")
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "RDW aanpassingen",
      width = "200px")
  @Select(containerDataSource = RdwAanpassingenContainer.class,
      itemCaptionPropertyId = RdwAanpassingenContainer.OMSCHRIJVING)
  private String rijbewijsWijzigingen = "";

  @ParameterAnnotation(RYB_ENABLED)
  @Position(order = "01")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Rijbewijzen service actief",
      width = "70px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String rijbewijzenEnabled = "";

  @ParameterAnnotation(RYB_URL)
  @Position(order = "02",
      direction = Direction.VERTICAL_WITH_RULER)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "URL van de rijbewijzen",
      width = "400px")
  private String rijbewijsUrl = "";

  @ParameterAnnotation(RYB_GEBRUIKERSNAAM)
  @Position(order = "05",
      direction = Direction.VERTICAL_WITH_RULER)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "RDW account-id",
      width = "200px")
  private String rijbewijsGebrNaam = "";

  @ParameterAnnotation(RYB_PV_NR)
  @Position(order = "13")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Standaard proces-verbaalnummer",
      width = "400px")
  private String rijbewijsPvNr = "";

  @ParameterAnnotation(RYB_VERVALTERMIJN_DAGEN)
  @Position(order = "07")
  @Field(customTypeClass = NumberField.class,
      caption = "Wachtwoord vervaltermijn (in dagen)",
      width = "40px")
  private String rijbewijsVervaltermijnInDagen = "";

  @ParameterAnnotation(REISD_PV_NR)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Standaard proces-verbaalnummer",
      width = "400px")
  private String reisdocumentPvNr = "";

  @ParameterAnnotation(REISD_WIJZIGING_GEZAG)
  @Position(order = "2")
  @Field(customTypeClass = DatumVeld.class,
      caption = "Ingangsdatum wijziging afleiding gezag",
      width = "80px")
  private String reisdocumentGezagsRegels = "";

  @ParameterAnnotation(REISD_SIGNAL_INFO)
  @Position(order = "3")
  @Field(type = FieldType.TEXT_AREA,
      caption = "Toelichting bij signalering",
      width = "400px")
  @TextArea(rows = 5)
  private String reisdocumentToelSignalering = "";

  @ParameterAnnotation(VRS_ENABLED)
  @Position(order = "10")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "VRS inschakelen",
      width = "70px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String reisdocumentEnableVrs = "";

  @ParameterAnnotation(VRS_BASISREGISTER)
  @Position(order = "11")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "VRS Basisregister inschakelen",
      width = "70px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String reisdocumentVrsBasisregister = "";

  @ParameterAnnotation(VRS_SERVICE_URL)
  @Position(order = "12")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "URL van VRS API",
      width = "400px")
  private String reisdocumentVrsUrl = "";

  @ParameterAnnotation(VRS_IDP_SERVICE_URL)
  @Position(order = "13")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "URL van VRS Token API",
      width = "400px")
  private String reisdocumentVrsTokenUrl = "";

  @ParameterAnnotation(VRS_SERVICE_TIMEOUT)
  @Position(order = "14")
  @Field(customTypeClass = NumberField.class,
      caption = "VRS timeout",
      width = "80px")
  private String reisdocumentVrsTimeout = "";

  @ParameterAnnotation(VRS_CLIENT_ID)
  @Position(order = "15")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "VRS client-id",
      width = "400px")
  private String reisdocumentVrsClientId = "";

  @ParameterAnnotation(VRS_CLIENT_SECRET)
  @Position(order = "16")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "VRS client secret",
      width = "400px")
  @TextField(secret = true)
  private String reisdocumentVrsClientSecret = "";

  @ParameterAnnotation(VRS_CLIENT_SCOPE)
  @Position(order = "17")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "VRS scope",
      width = "400px")
  private String reisdocumentVrsScope = "";

  @ParameterAnnotation(VRS_CLIENT_RESOURCE_SERVER)
  @Position(order = "18")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "VRS resource server",
      width = "400px")
  private String reisdocumentVrsResourceServer = "";

  @ParameterAnnotation(VRS_INSTANTIE_CODE)
  @Position(order = "19")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "VRS instantie-code",
      width = "400px")
  private String reisdocumentVrsInstantiecode = "";

  // Terugmelding beheer
  @ParameterAnnotation(TERUGMBEHEER)
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Beheerder van de terugmeldingen",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String terugmeldingBeheer;
  // Curatele
  @ParameterAnnotation(CURATELE_URL)
  @Position(order = "01")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "URL van de curatele webservice",
      width = "400px")
  private String curateleURL = "";

  @ParameterAnnotation(CURATELE_GEBRUIKERSNAAM)
  @Position(order = "02",
      direction = Direction.VERTICAL_WITH_RULER)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gebruikersnaam",
      width = "250px")
  private String curateleGebrNaam = "";

  @ParameterAnnotation(CURATELE_WACHTWOORD)
  @TextField(secret = true)
  @Position(order = "03")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Wachtwoord",
      width = "250px")
  private String curateleWachtwoord = "";

  // Test Debug
  @ParameterAnnotation(TEST)
  @Position(order = "7")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Code debuggen (Veel logging)",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String test;

  // Test Debug
  @ParameterAnnotation(MIJN_OVERHEID_BULK_UITTREKSELS)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Mijn-overheid bij bulk uittreksels",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String mijnOverheid;
  // TMV URL
  @ParameterAnnotation(TMV_URL)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Terugmeldingvoorziening URL",
      width = "400px")
  private String tmvUrl = "";

  @ParameterAnnotation(GV_KB_URL)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Schema URL",
      description = "URL van het Schema van gegevensverstrekking",
      width = "400px")
  private String gvSchemaUrl = "";

  @ParameterAnnotation(GV_DATUM_KENBAAR_MAKEN)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Termijn in dagen (kenbaar maken)",
      description = "Datum van einde termijn",
      width = "50px")
  private String gvDatumTermijnKenbaar;

  @ParameterAnnotation(GV_DATUM_VERSTREKKEN_GEEN_BEZW)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Termijn in dagen (verstrekken zonder bezwaar)",
      description = "Datum van einde termijn",
      width = "50px")
  private String gvDatumVerstrekkenZonderBezwaar = "";

  @ParameterAnnotation(GV_DATUM_VERSTREKKEN)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Termijn in dagen (verstrekken met bezwaar of geen reactie)",
      description = "Datum van einde termijn",
      width = "50px")
  private String gvDatumVerstrekkenMetBezwaar = "";

  @ParameterAnnotation(ONTBINDING_KB_URL)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Beslisboom kennisbank",
      description = "URL van de beslisboom om te bepalen of een echtscheiding kan worden ingeschreven in Nederlandse registers",
      width = "400px")
  private String ontbindingKbUrl = "";

  // Onderzoek
  @ParameterAnnotation(ONDERZ_FASE1_TERMIJN)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Termijn in dagen 1e fase",
      width = "400px")
  private String onderzoekTermijnFase1 = "";

  @ParameterAnnotation(ONDERZ_FASE2_TERMIJN)
  @Position(order = "2")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Termijn in dagen 2e fase",
      width = "400px")
  private String onderzoekTermijnFase2 = "";

  @ParameterAnnotation(ONDERZ_EXTRA_TERMIJN)
  @Position(order = "3")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Termijn in dagen extra aanschrijving",
      width = "400px")
  private String onderzoekTermijnExtra = "";

  @ParameterAnnotation(ONDERZ_VOORNEMEN_TERMIJN)
  @Position(order = "4")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Termijn in dagen voornemen",
      width = "400px")
  private String onderzoekTermijnVoornemen = "";

  @ParameterAnnotation(ONDERZ_DEFAULT_AAND)
  @Position(order = "5")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Aanduiding gegevens in onderzoek",
      width = "400px")
  @Select(containerDataSource = AanduidingOnderzoekContainer.class,
      itemCaptionPropertyId = AanduidingOnderzoekContainer.OMSCHRIJVING)
  private String onderzoekAandGegevens = "";

  @ParameterAnnotation(ONDERZ_5_DAGEN_TERM)
  @Position(order = "6")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Binnen 5 dagen af te handelen",
      description = "Standaardwaarde voor \"Is het onderzoek binnen 5 dagen af te handelen?\"")
  @Select(containerDataSource = ParmEmptyBooleanContainer.class,
      itemCaptionPropertyId = ParmEmptyBooleanContainer.OMSCHRIJVING)
  private String afhanTerm5Dagen = "";

  @ParameterAnnotation(ONDERZ_ANDER_ORGAAN)
  @Position(order = "7")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Gedegen onderzoek ander orgaan",
      description = "Standaardwaarde voor \"Is er gedegen onderzoek door ander overheidsorgaan en beschikbaar?\"")
  @Select(containerDataSource = ParmEmptyBooleanContainer.class,
      itemCaptionPropertyId = ParmEmptyBooleanContainer.OMSCHRIJVING)
  private String onderzAnderOrgaan = "";

  @ParameterAnnotation(ONDERZ_REDEN_OVERSLAAN)
  @Position(order = "8")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Voldoende reden overslaan stappen",
      description = "Standaardwaarde voor vraag \"Is er voldoende reden om stap(pen) over te slaan?\"")
  @Select(containerDataSource = ParmEmptyBooleanContainer.class,
      itemCaptionPropertyId = ParmEmptyBooleanContainer.OMSCHRIJVING)
  private String onderzRedenOversl = "";

  // Verhuistermijn
  @ParameterAnnotation(VERHUIS_DATUM_LIMIET_TOEKOMST)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verhuistermijn in dagen (toekomst)",
      description = "Maximaal aantal dagen tussen vandaag en de verhuisdatum. (leeg = geen controle).",
      width = "50px")
  private String verhuizingDatumLimietToekomst = "";

  // Verhuistermijn
  @ParameterAnnotation(VERHUIS_DATUM_LIMIET_VERLEDEN)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verhuistermijn in dagen (verleden)",
      description = "Maximaal aantal dagen tussen verhuisdatum in het verleden en vandaag. (leeg = geen controle).",
      width = "50px")
  private String verhuizingDatumLimietVerleden = "";

  // Verhuistermijn
  @ParameterAnnotation(RISKANALYSIS_RELOCATION_IND)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verhuizingindicatie",
      description = "De indicatie die gebruikt wordt om een persoon te markeren.",
      width = "100px")
  private String riskanalysisRelocationInd = "";

  // Verificatievraag
  @ParameterAnnotation(VER_VRAAG_ENDPOINT)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "URL van de verificatievraag",
      width = "400px")
  private String verificatievraagURL = "";

  // Wachtwoord verloop
  @ParameterAnnotation(WACHTWOORD_VERLOOP)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Wachtw. verloop in dagen",
      description = "Het aantal dagen voor het verloop van het wachtwoord",
      validators = { GetalValidator.class },
      width = "40px")
  @TextField(maxLength = 3)
  private String wachtwoordVerloop = "";

  // Sessie timeout
  @ParameterAnnotation(SESSIE_TIMEOUT)

  @Position(order = "6")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Sessie timeout (in minuten)",
      validators = GetalValidator.class,
      width = "40px")
  @TextField(maxLength = 2)
  private String sessieTimeout = "15";

  // X-UA-Compatible
  @ParameterAnnotation(X_UA_COMPATIBLE)

  @Position(order = "7")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "IE 10/11 legacy mode (x-ua-compatible)")
  private String xUaCompatible = "";

  // Maximaal aantal zoekresultaten
  @ParameterAnnotation(ZOEK_MAX_FOUND_COUNT)
  @Position(order = "5")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Maximaal aantal zoekresultaten",
      description = "Het aantal persoonslijsten dat maximaal per zoekactie mag worden gezocht. " +
          "Dit geldt alleen voor zoeken in de gemeentelijke database.",
      validators = { GetalValidator.class },
      width = "60px")
  @TextField(maxLength = 4)
  private String zoekMaxAantalResultaten = "";

  @ParameterAnnotation(ZOEK_ALL_RECORDS)
  @Position(order = "6")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Mag alle zoekresultaten tonen",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String zoekAlleZoekresultaten;

  @ParameterAnnotation(ZOEK_PLE_JAVA_SERVER_USERNAME)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gebruikersnaam profiel")
  private String gebruikerPersonenWsProfiel1 = "";

  // PLE wachtwoord
  @ParameterAnnotation(ZOEK_PLE_JAVA_SERVER_PW)
  @Position(order = "10")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Wachtwoord profiel")
  @TextField(secret = true)
  private String wachtwoordPersonenWsProfiel1 = "";

  // PLE Username
  @ParameterAnnotation(ZOEK_PERSONEN_PROFIEL2_USERNAME)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gebruikersnaam profiel")
  private String gebruikerPersonenWsProfiel2 = "";

  // PLE wachtwoord
  @ParameterAnnotation(ZOEK_PERSONEN_PROFIEL2_PW)
  @Position(order = "10")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Wachtwoord profiel")
  @TextField(secret = true)
  private String wachtwoordPersonenWsProfiel2 = "";

  @ParameterAnnotation(ZOEK_PLE_BRON_GEMEENTE)
  @Position(order = "8",
      direction = Direction.VERTICAL_WITH_RULER)
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "Zoeken in gemeente database",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String zoekPleBronGemeente = "";

  @ParameterAnnotation(ZOEK_PLE_BRON_GBAV)
  @Position(order = "9",
      direction = Direction.VERTICAL_WITH_RULER)
  @Field(type = FieldType.NATIVE_SELECT,
      caption = "Zoeken in landelijke database",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String zoekPleBronGbav = "";

  // Kassa Clear List
  @ParameterAnnotation(KASSA_TYPE)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Kassatype")
  @Select(containerDataSource = KassaApplicationTypeContainer.class,
      itemCaptionPropertyId = KassaApplicationTypeContainer.OMSCHRIJVING)
  private String kassaType = "";

  // Kassa Send Type
  @ParameterAnnotation(KASSA_SEND_TYPE)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Versturen via")
  @Select(containerDataSource = KassaVerstuurTypeContainer.class,
      itemCaptionPropertyId = KassaVerstuurTypeContainer.OMSCHRIJVING)
  private String kassaSendType = "";

  // Portaal
  @ParameterAnnotation(PORTAAL_ROLLEN)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_AREA,
      caption = "Rollen in het Portaal",
      width = "300px")
  @TextArea(rows = 10)
  private String portaalRollen = "";

  // PLE Naamgebruik
  @ParameterAnnotation(ZOEK_PLE_NAAMGEBRUIK)
  @Position(order = "7")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "PL gegevens naar naamgebruik",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String zoekPLENaamgebruik;

  // PLE Tonen administratieve historie
  @ParameterAnnotation(ZOEK_PLE_ADMIN_HIST)
  @Position(order = "7.1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Toon administratieve historie",
      width = "60px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String zoekAdminHist;

  @ParameterAnnotation(EMAIL_HOST)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "E-mail server",
      width = "400px")
  private String emailServer = "";

  @ParameterAnnotation(EMAIL_PORT)
  @Position(order = "2")
  @Field(customTypeClass = NumberField.class,
      caption = "Poort",
      width = "400px")
  private String emailPort = "";

  @ParameterAnnotation(EMAIL_USERNAME)
  @Position(order = "3")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gebruikersnaam",
      width = "400px")
  private String emailGebruikersnaam = "";

  @ParameterAnnotation(EMAIL_PW)
  @Position(order = "4")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Wachtwoord",
      width = "400px")
  @TextField(secret = true)
  private String emailWachtwoord = "";

  @ParameterAnnotation(EMAIL_EIGENSCHAPPEN)
  @Position(order = "9")
  @Field(type = FieldType.TEXT_AREA,
      caption = "Overige e-mail eigenschappen",
      width = "400px")
  @TextArea(rows = 5)
  private String emailEigenschappen = "";

  // PROBEV
  @ParameterAnnotation(PROBEV_GEBR_CODE)
  @Position(order = "1")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "PROBEV gebruiker code",
      validators = { GetalValidator.class },
      width = "90px")
  @TextField(maxLength = 10)
  private String probevGebrCode = "";

  // SMS
  @ParameterAnnotation(SMS_ENABLED)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "SMS service actief",
      width = "70px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String smsEnabled = "";

  @ParameterAnnotation(SMS_ENDPOINT)
  @Position(order = "10")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "SMS service endpoint",
      width = "400px")
  private String smsEndpoint = "";

  @ParameterAnnotation(SMS_USERNAME)
  @Position(order = "20")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "SMS gebruikersnaam",
      width = "200px")
  private String smsUsername = "";

  @ParameterAnnotation(SMS_PW)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "SMS wachtwoord",
      width = "200px")
  @Position(order = "30")
  @TextField(secret = true)
  private String smsPassword = "";

  // GEO
  @ParameterAnnotation(GEO_ENABLED)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Geo / BAG service actief",
      width = "70px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String geoEnabled = "";

  @ParameterAnnotation(GEO_ENDPOINT)
  @Position(order = "2")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geo / BAG service endpoint",
      width = "400px")
  private String geoEndpoint = "";

  @ParameterAnnotation(GEO_USERNAME)
  @Position(order = "3")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geo / BAG service gebruikersnaam",
      description = "De gebruikersnaam waarmee deze applicatie zich identificeert")
  @TextField
  private String geoUsername;

  @ParameterAnnotation(GEO_PW)
  @Position(order = "4")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geo / BAG service wachtwoord",
      description = "Het wachtwoord waarmee deze applicatie zich identificeert")
  @TextField(secret = true)
  private String geoPw;

  @ParameterAnnotation(GEO_SEARCH_DEFAULT)
  @Position(order = "11")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Veld 'Bron gegevens' standaard op BAG",
      width = "70px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String geoSourceDefault = "";

  // RAAS
  @ParameterAnnotation(RAAS_ENDPOINT)
  @Position(order = "10")
  @Field(type = FieldType.TEXT_FIELD,
      caption = "RAAS service endpoint",
      width = "400px")
  private String raasEndpoint = "";

  @ParameterAnnotation(RAAS_ENABLED)
  @Position(order = "1")
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "RAAS service actief",
      width = "70px")
  @Select(containerDataSource = ParmBooleanContainer.class,
      itemCaptionPropertyId = ParmBooleanContainer.OMSCHRIJVING)
  private String raasEnabled = "";

  @ParameterAnnotation(RAAS_IDENT_BEWIJS)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Omschrijving bewijsstukken identiteit",
      width = "400px")
  @Position(order = "40")
  @TextField(maxLength = 80)
  private String raasBewijsIdentificatie = "";

  @ParameterAnnotation(SYSTEM_MIN_HD_SIZE)
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Minimale grootte vrije ruimte in MB",
      width = "100px",
      required = true)
  @Position(order = "1")
  private String systemMinHdSize = "";

  public ParameterBean() {
  }

  public static List<ParameterBeanField> getFields(FieldFilter filter) {
    List<ParameterBeanField> list = new ArrayList<>();
    for (java.lang.reflect.Field field : ParameterBean.class.getDeclaredFields()) {
      ParameterAnnotation parmAnn = field.getAnnotation(ParameterAnnotation.class);
      Field fieldAnn = field.getAnnotation(Field.class);
      if (parmAnn != null && fieldAnn != null) {
        ParameterValueConverter converter = field.getAnnotation(ParameterValueConverter.class);
        ParameterBeanField b = new ParameterBeanField(field, fieldAnn, parmAnn.value(), converter);
        if (filter.is(b)) {
          list.add(b);
        }
      }
    }
    return list;
  }

  /**
   * Vult de velden met de waarden uit de parameters
   */
  public void setFieldValues(Parameters parameters, long cUsr, long cProfile) throws IllegalAccessException {
    for (ParameterBeanField field : getFields((b) -> true)) {
      String value = astr(getValueFromParameter(parameters, field.getType(), cUsr, cProfile));
      if (field.getConverter() != null) {
        field.getField().set(this, getNewInstanceof(field.getConverter().toField()).apply(value));
      } else {
        field.getField().set(this, value);
      }
    }
  }

  /**
   * geeft de waarde van de parameter terug
   */
  private String getValueFromParameter(Parameters parameters,
      ParameterType type, long cUsr, long cProfile) {

    return parameters.getAlle().stream()
        .filter(parm -> type.isKey(parm.getParm()))
        .filter(parm -> parm.isUserCode(cUsr) && parm.isProfileCode(cProfile))
        .findFirst()
        .map(Parm::getValue)
        .orElse(null);
  }

  public interface FieldFilter {

    boolean is(ParameterBeanField b);
  }

  @Data
  @AllArgsConstructor
  public static class ParameterBeanField {

    private java.lang.reflect.Field field;
    private Field                   fieldAnn;
    private ParameterType           type;
    private ParameterValueConverter converter;

    public boolean isType(ParameterType type) {
      return this.type == type;
    }

    public boolean isFieldName(Object id) {
      return field.getName().equals(id);
    }

    public ParameterValueConverter getConverter() {
      return converter;
    }
  }
}
