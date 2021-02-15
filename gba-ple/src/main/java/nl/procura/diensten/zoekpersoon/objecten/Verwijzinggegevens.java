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

public class Verwijzinggegevens {

  private String anummer                            = "";
  private String burgerservicenummer                = "";
  private String datum_opneming                     = "";
  private String datum_geldigheid                   = "";
  private String indicatie_onjuist                  = "";
  private String datum_einde_onderzoek              = "";
  private String datum_ingang_onderzoek             = "";
  private String aanduiding_gegevens_in_onderzoek   = "";
  private String indicatie_geheim                   = "";
  private String locatie                            = "";
  private String toevoeging                         = "";
  private String huisletter                         = "";
  private String huisnummer                         = "";
  private String straatnaam_boco                    = "";
  private String datum_inschrijving                 = "";
  private String gemeente_inschrijving              = "";
  private String geboorteland                       = "";
  private String straatnaam_officieel               = "";
  private String straatnaam_nen                     = "";
  private String geslachtsnaam                      = "";
  private String aanduiding                         = "";
  private String postcode                           = "";
  private String geboorteplaats                     = "";
  private String geboortedatum                      = "";
  private String voorvoegsel                        = "";
  private String titel_predikaat                    = "";
  private String voornaam                           = "";
  private String naam_openbare_ruimte               = "";
  private String woonplaatsnaam                     = "";
  private String identificatiecode_verblijfplaats   = "";
  private String identificatiecode_nummeraanduiding = "";

  public Verwijzinggegevens() {
  }

  public String getAanduiding() {
    return aanduiding;
  }

  public void setAanduiding(String aanduiding) {
    this.aanduiding = aanduiding;
  }

  public String getAanduiding_gegevens_in_onderzoek() {
    return aanduiding_gegevens_in_onderzoek;
  }

  public void setAanduiding_gegevens_in_onderzoek(String aanduiding_gegevens_in_onderzoek) {
    this.aanduiding_gegevens_in_onderzoek = aanduiding_gegevens_in_onderzoek;
  }

  public String getAnummer() {
    return anummer;
  }

  public void setAnummer(String anummer) {
    this.anummer = anummer;
  }

  public String getDatum_einde_onderzoek() {
    return datum_einde_onderzoek;
  }

  public void setDatum_einde_onderzoek(String datum_einde_onderzoek) {
    this.datum_einde_onderzoek = datum_einde_onderzoek;
  }

  public String getDatum_geldigheid() {
    return datum_geldigheid;
  }

  public void setDatum_geldigheid(String datum_geldigheid) {
    this.datum_geldigheid = datum_geldigheid;
  }

  public String getDatum_ingang_onderzoek() {
    return datum_ingang_onderzoek;
  }

  public void setDatum_ingang_onderzoek(String datum_ingang_onderzoek) {
    this.datum_ingang_onderzoek = datum_ingang_onderzoek;
  }

  public String getDatum_inschrijving() {
    return datum_inschrijving;
  }

  public void setDatum_inschrijving(String datum_inschrijving) {
    this.datum_inschrijving = datum_inschrijving;
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

  public String getGemeente_inschrijving() {
    return gemeente_inschrijving;
  }

  public void setGemeente_inschrijving(String gemeente_inschrijving) {
    this.gemeente_inschrijving = gemeente_inschrijving;
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    this.geslachtsnaam = geslachtsnaam;
  }

  public String getHuisletter() {
    return huisletter;
  }

  public void setHuisletter(String huisletter) {
    this.huisletter = huisletter;
  }

  public String getHuisnummer() {
    return huisnummer;
  }

  public void setHuisnummer(String huisnummer) {
    this.huisnummer = huisnummer;
  }

  public String getIndicatie_geheim() {
    return indicatie_geheim;
  }

  public void setIndicatie_geheim(String indicatie_geheim) {
    this.indicatie_geheim = indicatie_geheim;
  }

  public String getIndicatie_onjuist() {
    return indicatie_onjuist;
  }

  public void setIndicatie_onjuist(String indicatie_onjuist) {
    this.indicatie_onjuist = indicatie_onjuist;
  }

  public String getLocatie() {
    return locatie;
  }

  public void setLocatie(String locatie) {
    this.locatie = locatie;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public String getBurgerservicenummer() {
    return burgerservicenummer;
  }

  public void setBurgerservicenummer(String burgerservicenummer) {
    this.burgerservicenummer = burgerservicenummer;
  }

  public String getStraatnaam_boco() {
    return straatnaam_boco;
  }

  public void setStraatnaam_boco(String straatnaam_BOCO) {
    this.straatnaam_boco = straatnaam_BOCO;
  }

  public String getStraatnaam_officieel() {
    return straatnaam_officieel;
  }

  public void setStraatnaam_officieel(String straatnaam_officieel) {
    this.straatnaam_officieel = straatnaam_officieel;
  }

  public String getTitel_predikaat() {
    return titel_predikaat;
  }

  public void setTitel_predikaat(String titel_predikaat) {
    this.titel_predikaat = titel_predikaat;
  }

  public String getToevoeging() {
    return toevoeging;
  }

  public void setToevoeging(String toevoeging) {
    this.toevoeging = toevoeging;
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

  public String getStraatnaam_nen() {
    return straatnaam_nen;
  }

  public void setStraatnaam_nen(String straatnaam_nen) {
    this.straatnaam_nen = straatnaam_nen;
  }

  public String getNaam_openbare_ruimte() {
    return naam_openbare_ruimte;
  }

  public void setNaam_openbare_ruimte(String naamOpenbareRuimte) {
    naam_openbare_ruimte = naamOpenbareRuimte;
  }

  public String getWoonplaatsnaam() {
    return woonplaatsnaam;
  }

  public void setWoonplaatsnaam(String woonplaatsnaam) {
    this.woonplaatsnaam = woonplaatsnaam;
  }

  public String getIdentificatiecode_verblijfplaats() {
    return identificatiecode_verblijfplaats;
  }

  public void setIdentificatiecode_verblijfplaats(String identificatiecodeVerblijfplaats) {
    identificatiecode_verblijfplaats = identificatiecodeVerblijfplaats;
  }

  public String getIdentificatiecode_nummeraanduiding() {
    return identificatiecode_nummeraanduiding;
  }

  public void setIdentificatiecode_nummeraanduiding(String identificatiecodeNummeraanduiding) {
    identificatiecode_nummeraanduiding = identificatiecodeNummeraanduiding;
  }
}
