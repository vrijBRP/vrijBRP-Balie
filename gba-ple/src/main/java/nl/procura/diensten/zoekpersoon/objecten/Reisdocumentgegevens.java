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

package nl.procura.diensten.zoekpersoon.objecten;

public class Reisdocumentgegevens {

  private String buitenlands_reisdocument         = "";
  private String gemeente_document                = "";
  private String datum_document                   = "";
  private String beschrijving_document            = "";
  private String datum_geldigheid                 = "";
  private String datum_opneming                   = "";
  private String Nederlands_reisdocument          = "";
  private String nummer_document                  = "";
  private String datum_uitgifte                   = "";
  private String datum_einde_geldigheid           = "";
  private String autoriteit_van_afgifte           = "";
  private String datum_inhouding_vermissing       = "";
  private String aanduiding_inhouding_vermissing  = "";
  private String lengte_houder                    = "";
  private String aanduiding_gegevens_in_onderzoek = "";
  private String datum_ingang_onderzoek           = "";
  private String signalering                      = "";
  private String datum_einde_onderzoek            = "";

  public Reisdocumentgegevens() {
  }

  public String getAanduiding_gegevens_in_onderzoek() {
    return aanduiding_gegevens_in_onderzoek;
  }

  public void setAanduiding_gegevens_in_onderzoek(String aanduiding_gegevens_in_onderzoek) {
    this.aanduiding_gegevens_in_onderzoek = aanduiding_gegevens_in_onderzoek;
  }

  public String getAanduiding_inhouding_vermissing() {
    return aanduiding_inhouding_vermissing;
  }

  public void setAanduiding_inhouding_vermissing(String aanduiding_inhouding_vermissing) {
    this.aanduiding_inhouding_vermissing = aanduiding_inhouding_vermissing;
  }

  public String getAutoriteit_van_afgifte() {
    return autoriteit_van_afgifte;
  }

  public void setAutoriteit_van_afgifte(String autoriteit_van_afgifte) {
    this.autoriteit_van_afgifte = autoriteit_van_afgifte;
  }

  public String getDatum_einde_geldigheid() {
    return datum_einde_geldigheid;
  }

  public void setDatum_einde_geldigheid(String datum_einde_geldigheid) {
    this.datum_einde_geldigheid = datum_einde_geldigheid;
  }

  public String getDatum_ingang_onderzoek() {
    return datum_ingang_onderzoek;
  }

  public void setDatum_ingang_onderzoek(String datum_ingang_onderzoek) {
    this.datum_ingang_onderzoek = datum_ingang_onderzoek;
  }

  public String getDatum_inhouding_vermissing() {
    return datum_inhouding_vermissing;
  }

  public void setDatum_inhouding_vermissing(String datum_inhouding_vermissing) {
    this.datum_inhouding_vermissing = datum_inhouding_vermissing;
  }

  public String getDatum_uitgifte() {
    return datum_uitgifte;
  }

  public void setDatum_uitgifte(String datum_uitgifte) {
    this.datum_uitgifte = datum_uitgifte;
  }

  public String getLengte_houder() {
    return lengte_houder;
  }

  public void setLengte_houder(String lengte_houder) {
    this.lengte_houder = lengte_houder;
  }

  public String getNederlands_reisdocument() {
    return Nederlands_reisdocument;
  }

  public void setNederlands_reisdocument(String nederlands_reisdocument) {
    Nederlands_reisdocument = nederlands_reisdocument;
  }

  public String getNummer_document() {
    return nummer_document;
  }

  public void setNummer_document(String nummer_document) {
    this.nummer_document = nummer_document;
  }

  public String getBeschrijving_document() {
    return beschrijving_document;
  }

  public void setBeschrijving_document(String beschrijving_document) {
    this.beschrijving_document = beschrijving_document;
  }

  public String getBuitenlands_reisdocument() {
    return buitenlands_reisdocument;
  }

  public void setBuitenlands_reisdocument(String buitenlands_reisdocument) {
    this.buitenlands_reisdocument = buitenlands_reisdocument;
  }

  public String getDatum_document() {
    return datum_document;
  }

  public void setDatum_document(String datum_document) {
    this.datum_document = datum_document;
  }

  public String getDatum_geldigheid() {
    return datum_geldigheid;
  }

  public void setDatum_geldigheid(String datum_geldigheid) {
    this.datum_geldigheid = datum_geldigheid;
  }

  public String getDatum_opneming() {
    return datum_opneming;
  }

  public void setDatum_opneming(String datum_opneming) {
    this.datum_opneming = datum_opneming;
  }

  public String getGemeente_document() {
    return gemeente_document;
  }

  public void setGemeente_document(String gemeente_document) {
    this.gemeente_document = gemeente_document;
  }

  public String getDatum_einde_onderzoek() {
    return datum_einde_onderzoek;
  }

  public void setDatum_einde_onderzoek(String datum_einde_onderzoek) {
    this.datum_einde_onderzoek = datum_einde_onderzoek;
  }

  public String getSignalering() {
    return signalering;
  }

  public void setSignalering(String signalering) {
    this.signalering = signalering;
  }
}
