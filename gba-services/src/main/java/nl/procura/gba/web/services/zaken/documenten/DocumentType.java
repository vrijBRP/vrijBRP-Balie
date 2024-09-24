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
  LATERE_VERMELDING_ERK("lv", "zaak", "Latere vermelding erkenning"),
  LATERE_VERMELDING_NK("lv_nk", "zaak", "Latere vermelding naamskeuze"),
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

  public String getDoc() {
    return doc;
  }

  public String getOms() {
    return oms;
  }

  public int getOrder() {
    return order;
  }

  public String getType() {
    return type;
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
