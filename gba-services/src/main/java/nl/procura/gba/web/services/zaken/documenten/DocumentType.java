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

package nl.procura.gba.web.services.zaken.documenten;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

import lombok.Getter;

@Getter
public enum DocumentType {

  PL_UITTREKSEL(1, "pl", Constants.PERSOONSLIJSTEN, "Uittreksel"),
  PL_OPTIE(2, "pl_optie", Constants.PERSOONSLIJSTEN, "Optie (PL)"),
  PL_NATURALISATIE(3, "pl_nat", Constants.PERSOONSLIJSTEN, "Nationaliteit (PL)"),
  PL_FORMULIER(4, "pl_form", Constants.PERSOONSLIJSTEN, "Formulier"),
  PL_BEGELEIDENDE_BRIEF(5, "pl_beg", Constants.PERSOONSLIJSTEN, "Begeleidende brief"),
  PL_BEWONER_BRIEF(6, "pl_bew", Constants.PERSOONSLIJSTEN, "Brief aan de bewoners"),
  PL_ADRESONDERZOEK(7, "pl_adresonderzoek", Constants.PERSOONSLIJSTEN, "Adresonderzoek"),
  VERHUIZING_AANGIFTE("bvh", "aangifte", "Verhuizing (aangifte)"),
  GV_AANVRAAG("gv_aanvraag", Constants.AANVRAAG, "Gegevensverstrekking (aanvraag)"),
  RIJBEWIJS_AANVRAAG("rijb_aanvraag", Constants.AANVRAAG, "Rijbewijs (aanvraag)"),
  NAAMGEBRUIK_AANVRAAG("naamg_aanvraag", Constants.AANVRAAG, "Naamgebruik (aanvraag)"),
  INDICATIE_AANVRAAG("indicatie_aanvraag", Constants.AANVRAAG, "Indicatie (aanvraag)"),
  COVOG_AANVRAAG("covog_aanvraag", Constants.AANVRAAG, "COVOG (aanvraag)"),
  VERSTREKKINGSBEPERKING_AANVRAAG("verstrekk_aanvraag", Constants.AANVRAAG, "Verstrekkingsbeperking (aanvraag)"),
  GPK_AANVRAAG("gpk_aanvraag", Constants.AANVRAAG, "GehandicaptenParkeerKaart (aanvraag)"),
  CORRESPONDENTIE_ZAAK("correspondentie", "zaak", "Correspondentie (zaak)"),
  DOCUMENT_VERMISSING("reisd_vermissing", "vermissing", "Vermissing reisdocument / rijbewijs"),
  REISDOCUMENT_AANVRAAG("reisd_aanvraag", Constants.AANVRAAG, "Reisdocument (aanvraag)"),
  REISDOCUMENT_C1("reisd_c1", "c1", "Reisdocument (c1)"),
  TELLING("telling", "telling", "Telling"),
  KLAPPER("klapper", "klapper", "Klapper"),
  PRESENTIEVRAAG("presentievraag", "data", "Presentievraag"),
  TMV("tmv", "tmv", "Terugmelding"),
  ERKENNING("erkenning", "zaak", "Erkenning"),
  GEBOORTE("geboorte", "zaak", "Geboorte"),
  NAAMSKEUZE("naamskeuze", "zaak", "Naamskeuze"),
  LATERE_VERMELDING_ERK("lv", "zaak", "LV erkenning"),
  LATERE_VERMELDING_NK("lv_nk", "zaak", "LV naamskeuze"),
  LATERE_VERMELDING_AFST("lv_afst", "data", "LV afstamming"),
  LATERE_VERMELDING_AFST_1("lv_afst_1", "data", "LV afstamming - adoptie"),
  LATERE_VERMELDING_AFST_2("lv_afst_2", "data", "LV afstamming - ambtshalve verbetering akte"),
  LATERE_VERMELDING_AFST_3("lv_afst_3", "data", "LV afstamming - doorhaling akte"),
  LATERE_VERMELDING_AFST_4("lv_afst_4", "data", "LV afstamming - gerechtelijke vaststelling ouderschap"),
  LATERE_VERMELDING_AFST_5("lv_afst_5", "data", "LV afstamming - herroeping adoptie"),
  LATERE_VERMELDING_AFST_6("lv_afst_6", "data", "LV afstamming - ontkenning ouderschap"),
  LATERE_VERMELDING_AFST_7("lv_afst_7", "data", "LV afstamming - vaststelling namen bij KB"),
  LATERE_VERMELDING_AFST_8("lv_afst_8", "data", "LV afstamming - vaststelling namen bij optie"),
  LATERE_VERMELDING_AFST_9("lv_afst_9", "data", "LV afstamming - verbetering akte"),
  LATERE_VERMELDING_AFST_10("lv_afst_10", "data", "LV afstamming - vernietiging erkenning"),
  LATERE_VERMELDING_AFST_11("lv_afst_11", "data", "LV afstamming - wettiging"),
  LATERE_VERMELDING_AFST_12("lv_afst_12", "data", "LV afstamming - wijziging geslacht"),
  LATERE_VERMELDING_AFST_13("lv_afst_13", "data", "LV afstamming - wijziging geslachtsnaam bij KB"),
  LATERE_VERMELDING_AFST_14("lv_afst_14", "data",
      "LV afstamming - wijziging geslachtsnaam door rechterlijke uitspraak"),
  LATERE_VERMELDING_AFST_15("lv_afst_15", "data", "LV afstamming - wijziging geslachtsnaam t.g.v. huwelijk"),
  LATERE_VERMELDING_AFST_16("lv_afst_16", "data", "LV afstamming - wijziging geslachtsnaam t.g.v. echtscheiding"),
  LATERE_VERMELDING_AFST_17("lv_afst_17", "data", "LV afstamming - wijziging geslachtsnaam door erkenning ouder"),
  LATERE_VERMELDING_AFST_18("lv_afst_18", "data", "LV afstamming - wijziging voornamen"),
  LATERE_VERMELDING_AFST_19("lv_afst_19", "data", "LV afstamming - naamskeuze"),
  HUWELIJK("huwelijk", "zaak", "Huwelijk / GPS"),
  HUWELIJK_VOORBEREIDING("huwelijk_vn", "zaak", "Huwelijk/GPS (voorbereiding)"),
  GPS_OMZETTING("gps_omzetting", "zaak", "Omzetting GPS in Huwelijk"),
  ONTBINDING_GEMEENTE("ontbinding", "zaak", "Ontbinding/einde huwelijk/GPS in gemeente"),
  GPS_OMZETTING_VOORBEREIDING("gps_omzetting_vn", "zaak", "Omzetting GPS in Huwelijk (voorbereiding)"),
  OVERLIJDEN("overlijden", "zaak", "Overlijden"),
  LIJKVINDING("lijkvinding", "zaak", "Lijkvinding"),
  LEVENLOOS("levenloos", "zaak", "Levenloos geboren kind"),
  ONDERZOEK("onderzoek", "data", "Onderzoek"),
  ONDERZOEK_FASE1("onderzoek_fase1", "data", "Onderzoek (1e fase)"),
  ONDERZOEK_FASE2_EXTERNE_BRON("onderzoek_fase_bron", "data", "Onderzoek (2e fase) - externe bron"),
  ONDERZOEK_FASE_EXTRA("onderzoek_fase_extra", "data", "Onderzoek (extra aanschrijving)"),
  ONDERZOEK_VOORNEMEN("onderzoek_fase3", "data", "Onderzoek (voornemen ambtshalve wijziging)"),
  ONDERZOEK_BESLUIT("onderzoek_fase_besluit", "data", "Onderzoek (besluit)"),
  OPTIE("optie", "data", "Optie (Zaak)"),
  NATURALISATIE("naturalisatie", "data", "Naturalisatie (Zaak)"),
  REGISTRATION("eerste_registratie", "data", "Eerste inschrijving"),
  ZAAK("zaak", "zaak", "Zaak (algemeen)"),
  RISK_ANALYSE("risico_analyse", "data", "Risicoanalyse"),
  STEMPAS("stempas", "data", "Stempas"),
  ROS("ros", "data", "Register ongeldige stempassen (ROS)"),
  PL_CATEGORIE("pl_categorie", "data", "Persoonslijst categorie"),
  ONBEKEND("onbekend", "onbekend", "Onbekend");

  private final int    order;
  private final String type;
  private final String doc;
  private final String oms;

  DocumentType(int order, String type, String doc, String oms) {
    this.order = order;
    this.type = type;
    this.doc = doc;
    this.oms = oms;
  }

  DocumentType(String type, String doc, String oms) {
    this(0, type, doc, oms);
  }

  public static DocumentType getType(String id) {
    for (DocumentType var : DocumentType.values()) {
      if (equalsIgnoreCase(var.getType(), id)) {
        return var;
      }
    }

    return ONBEKEND;
  }

  @Override
  public String toString() {
    return getOms();
  }

  private static class Constants {

    private static final String PERSOONSLIJSTEN = "persoonslijsten";
    private static final String AANVRAAG        = "aanvraag";
  }
}
