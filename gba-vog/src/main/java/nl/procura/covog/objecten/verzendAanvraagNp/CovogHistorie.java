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

package nl.procura.covog.objecten.verzendAanvraagNp;

public class CovogHistorie {

  private String geslachtsnaam;
  private String voorvoegsel;
  private String voornamen;
  private String geslacht;
  private String geboortedatum;
  private String gemeenteCodeGeboren;
  private String geboorteplaatsBuitenland;
  private String landCodeGeboren;
  private String burgerServiceNummer;

  public CovogHistorie() {
  }

  public CovogHistorie(String geslachtsnaam, String voorvoegsel, String voornamen, String geslacht,
      String geboortedatum, String gemeenteCodeGeboren, String geboorteplaatsBuitenland,
      String landCodeGeboren, String burgerServiceNummer) {
    this.geslachtsnaam = geslachtsnaam;
    this.voorvoegsel = voorvoegsel;
    this.voornamen = voornamen;
    this.geslacht = geslacht;
    this.geboortedatum = geboortedatum;
    this.gemeenteCodeGeboren = gemeenteCodeGeboren;
    this.geboorteplaatsBuitenland = geboorteplaatsBuitenland;
    this.landCodeGeboren = landCodeGeboren;
    this.burgerServiceNummer = burgerServiceNummer;
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    this.geslachtsnaam = geslachtsnaam;
  }

  public String getVoorvoegsel() {
    return voorvoegsel;
  }

  public void setVoorvoegsel(String voorvoegsel) {
    this.voorvoegsel = voorvoegsel;
  }

  public String getVoornamen() {
    return voornamen;
  }

  public void setVoornamen(String voornamen) {
    this.voornamen = voornamen;
  }

  public String getGeslacht() {
    return geslacht;
  }

  public void setGeslacht(String geslacht) {
    this.geslacht = geslacht;
  }

  public String getGeboortedatum() {
    return geboortedatum;
  }

  public void setGeboortedatum(String geboortedatum) {
    this.geboortedatum = geboortedatum;
  }

  public String getGemeenteCodeGeboren() {
    return gemeenteCodeGeboren;
  }

  public void setGemeenteCodeGeboren(String gemeenteCodeGeboren) {
    this.gemeenteCodeGeboren = gemeenteCodeGeboren;
  }

  public String getGeboorteplaatsBuitenland() {
    return geboorteplaatsBuitenland;
  }

  public void setGeboorteplaatsBuitenland(String geboorteplaatsBuitenland) {
    this.geboorteplaatsBuitenland = geboorteplaatsBuitenland;
  }

  public String getLandCodeGeboren() {
    return landCodeGeboren;
  }

  public void setLandCodeGeboren(String landCodeGeboren) {
    this.landCodeGeboren = landCodeGeboren;
  }

  public String getBurgerServiceNummer() {
    return burgerServiceNummer;
  }

  public void setBurgerServiceNummer(String burgerServiceNummer) {
    this.burgerServiceNummer = burgerServiceNummer;
  }
}
