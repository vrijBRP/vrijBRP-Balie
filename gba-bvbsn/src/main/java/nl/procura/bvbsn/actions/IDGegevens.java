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

package nl.procura.bvbsn.actions;

public class IDGegevens {

  private String voornamen;
  private String voorvoegselGeslachtsnaam;
  private String geslachtsnaam;
  private String geboortedatum;
  private String geboorteplaats;
  private String geboorteland;
  private String geslachtsaanduiding;
  private String gemeenteVanInschrijving;
  private String straatnaam;
  private String huisnummer;
  private String huisnummertoevoeging;
  private String huisletter;
  private String aanduidingBijHuisnummer;
  private String postcode;
  private String locatiebeschrijving;
  private String landVanwaarIngeschreven;

  public String getVoornamen() {
    return voornamen;
  }

  public void setVoornamen(String voornamen) {
    this.voornamen = voornamen;
  }

  public String getVoorvoegselGeslachtsnaam() {
    return voorvoegselGeslachtsnaam;
  }

  public void setVoorvoegselGeslachtsnaam(String voorvoegselGeslachtsnaam) {
    this.voorvoegselGeslachtsnaam = voorvoegselGeslachtsnaam;
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    this.geslachtsnaam = geslachtsnaam;
  }

  public String getGeboortedatum() {
    return geboortedatum;
  }

  public void setGeboortedatum(String geboortedatum) {
    this.geboortedatum = geboortedatum;
  }

  public String getGeboorteplaats() {
    return geboorteplaats;
  }

  public void setGeboorteplaats(String geboorteplaats) {
    this.geboorteplaats = geboorteplaats;
  }

  public String getGeboorteland() {
    return geboorteland;
  }

  public void setGeboorteland(String geboorteland) {
    this.geboorteland = geboorteland;
  }

  public String getGeslachtsaanduiding() {
    return geslachtsaanduiding;
  }

  public void setGeslachtsaanduiding(String geslachtsaanduiding) {
    this.geslachtsaanduiding = geslachtsaanduiding;
  }

  public String getGemeenteVanInschrijving() {
    return gemeenteVanInschrijving;
  }

  public void setGemeenteVanInschrijving(String gemeenteVanInschrijving) {
    this.gemeenteVanInschrijving = gemeenteVanInschrijving;
  }

  public String getStraatnaam() {
    return straatnaam;
  }

  public void setStraatnaam(String straatnaam) {
    this.straatnaam = straatnaam;
  }

  public String getHuisnummer() {
    return huisnummer;
  }

  public void setHuisnummer(String huisnummer) {
    this.huisnummer = huisnummer;
  }

  public String getHuisnummertoevoeging() {
    return huisnummertoevoeging;
  }

  public void setHuisnummertoevoeging(String huisnummertoevoeging) {
    this.huisnummertoevoeging = huisnummertoevoeging;
  }

  public String getHuisletter() {
    return huisletter;
  }

  public void setHuisletter(String huisletter) {
    this.huisletter = huisletter;
  }

  public String getAanduidingBijHuisnummer() {
    return aanduidingBijHuisnummer;
  }

  public void setAanduidingBijHuisnummer(String aanduidingBijHuisnummer) {
    this.aanduidingBijHuisnummer = aanduidingBijHuisnummer;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode.replaceAll("\\s+", "").toUpperCase();
  }

  public String getLocatiebeschrijving() {
    return locatiebeschrijving;
  }

  public void setLocatiebeschrijving(String locatiebeschrijving) {
    this.locatiebeschrijving = locatiebeschrijving;
  }

  public String getLandVanwaarIngeschreven() {
    return landVanwaarIngeschreven;
  }

  public void setLandVanwaarIngeschreven(String landVanwaarIngeschreven) {
    this.landVanwaarIngeschreven = landVanwaarIngeschreven;
  }
}
