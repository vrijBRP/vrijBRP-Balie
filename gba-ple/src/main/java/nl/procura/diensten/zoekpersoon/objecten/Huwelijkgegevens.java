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

public class Huwelijkgegevens {

  private String anummer                          = "";
  private String burgerservicenummer              = "";
  private String voornaam                         = "";
  private String titel_predikaat                  = "";
  private String voorvoegsel                      = "";
  private String geslachtsnaam                    = "";
  private String geboortedatum                    = "";
  private String geboorteplaats                   = "";
  private String geboorteland                     = "";
  private String geslacht                         = "";
  private String huwelijksdatum                   = "";
  private String huwelijksplaats                  = "";
  private String huwelijksland                    = "";
  private String ontbindingsdatum                 = "";
  private String ontbindingsplaats                = "";
  private String ontbindingsland                  = "";
  private String ontbindingsreden                 = "";
  private String soort                            = "";
  private String gemeente_document                = "";
  private String datum_document                   = "";
  private String beschrijving_document            = "";
  private String datum_geldigheid                 = "";
  private String datum_opneming                   = "";
  private String registergemeente_akte            = "";
  private String aktenummer                       = "";
  private String aanduiding_gegevens_in_onderzoek = "";
  private String datum_ingang_onderzoek           = "";
  private String indicatie_onjuist                = "";
  private String datum_einde_onderzoek            = "";
  private String aanduiding_recentste_verbintenis = "";

  public Huwelijkgegevens() {
  }

  public String getAanduiding_gegevens_in_onderzoek() {
    return aanduiding_gegevens_in_onderzoek;
  }

  public void setAanduiding_gegevens_in_onderzoek(String aanduiding_gegevens_in_onderzoek) {
    this.aanduiding_gegevens_in_onderzoek = aanduiding_gegevens_in_onderzoek;
  }

  public String getAktenummer() {
    return aktenummer;
  }

  public void setAktenummer(String aktenummer) {
    this.aktenummer = aktenummer;
  }

  public String getAnummer() {
    return anummer;
  }

  public void setAnummer(String anummer) {
    this.anummer = anummer;
  }

  public String getDatum_ingang_onderzoek() {
    return datum_ingang_onderzoek;
  }

  public void setDatum_ingang_onderzoek(String datum_ingang_onderzoek) {
    this.datum_ingang_onderzoek = datum_ingang_onderzoek;
  }

  public String getIndicatie_onjuist() {
    return indicatie_onjuist;
  }

  public void setIndicatie_onjuist(String indicatie_onjuist) {
    this.indicatie_onjuist = indicatie_onjuist;
  }

  public String getRegistergemeente_akte() {
    return registergemeente_akte;
  }

  public void setRegistergemeente_akte(String registergemeente_akte) {
    this.registergemeente_akte = registergemeente_akte;
  }

  public String getBurgerservicenummer() {
    return burgerservicenummer;
  }

  public void setBurgerservicenummer(String burgerservicenummer) {
    this.burgerservicenummer = burgerservicenummer;
  }

  public String getBeschrijving_document() {
    return beschrijving_document;
  }

  public void setBeschrijving_document(String beschrijving_document) {
    this.beschrijving_document = beschrijving_document;
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

  public String getGeboortedatum() {
    return geboortedatum;
  }

  public void setGeboortedatum(String geboortedatum) {
    this.geboortedatum = geboortedatum;
  }

  public String getGeboorteland() {
    return geboorteland;
  }

  public void setGeboorteland(String geboorteland) {
    this.geboorteland = geboorteland;
  }

  public String getGeboorteplaats() {
    return geboorteplaats;
  }

  public void setGeboorteplaats(String geboorteplaats) {
    this.geboorteplaats = geboorteplaats;
  }

  public String getGemeente_document() {
    return gemeente_document;
  }

  public void setGemeente_document(String gemeente_document) {
    this.gemeente_document = gemeente_document;
  }

  public String getGeslacht() {
    return geslacht;
  }

  public void setGeslacht(String geslacht) {
    this.geslacht = geslacht;
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    this.geslachtsnaam = geslachtsnaam;
  }

  public String getHuwelijksdatum() {
    return huwelijksdatum;
  }

  public void setHuwelijksdatum(String huwelijksdatum) {
    this.huwelijksdatum = huwelijksdatum;
  }

  public String getHuwelijksland() {
    return huwelijksland;
  }

  public void setHuwelijksland(String huwelijksland) {
    this.huwelijksland = huwelijksland;
  }

  public String getHuwelijksplaats() {
    return huwelijksplaats;
  }

  public void setHuwelijksplaats(String huwelijksplaats) {
    this.huwelijksplaats = huwelijksplaats;
  }

  public String getOntbindingsdatum() {
    return ontbindingsdatum;
  }

  public void setOntbindingsdatum(String ontbindingsdatum) {
    this.ontbindingsdatum = ontbindingsdatum;
  }

  public String getOntbindingsland() {
    return ontbindingsland;
  }

  public void setOntbindingsland(String ontbindingsland) {
    this.ontbindingsland = ontbindingsland;
  }

  public String getOntbindingsplaats() {
    return ontbindingsplaats;
  }

  public void setOntbindingsplaats(String ontbindingsplaats) {
    this.ontbindingsplaats = ontbindingsplaats;
  }

  public String getOntbindingsreden() {
    return ontbindingsreden;
  }

  public void setOntbindingsreden(String ontbindingsreden) {
    this.ontbindingsreden = ontbindingsreden;
  }

  public String getSoort() {
    return soort;
  }

  public void setSoort(String soort) {
    this.soort = soort;
  }

  public String getTitel_predikaat() {
    return titel_predikaat;
  }

  public void setTitel_predikaat(String titel_predikaat) {
    this.titel_predikaat = titel_predikaat;
  }

  public String getVoornaam() {
    return voornaam;
  }

  public void setVoornaam(String voornaam) {
    this.voornaam = voornaam;
  }

  public String getVoorvoegsel() {
    return voorvoegsel;
  }

  public void setVoorvoegsel(String voorvoegsel) {
    this.voorvoegsel = voorvoegsel;
  }

  public String getDatum_einde_onderzoek() {
    return datum_einde_onderzoek;
  }

  public void setDatum_einde_onderzoek(String datum_einde_onderzoek) {
    this.datum_einde_onderzoek = datum_einde_onderzoek;
  }

  // By use _ prefix this property is excluded from the WDSDL because it is no longer a getter
  public String _getAanduiding_recentste_verbintenis() {
    return aanduiding_recentste_verbintenis;
  }

  // By use _ prefix this property is excluded from the WDSDLbecause it is no longer a setter
  public void _setAanduiding_recentste_verbintenis(String aanduiding_recentste_verbintenis) {
    this.aanduiding_recentste_verbintenis = aanduiding_recentste_verbintenis;
  }
}
