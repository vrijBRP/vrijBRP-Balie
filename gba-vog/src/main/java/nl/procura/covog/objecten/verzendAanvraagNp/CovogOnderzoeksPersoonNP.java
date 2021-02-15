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

import static java.util.Arrays.copyOf;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "onderzoeksPersoonNP")
public class CovogOnderzoeksPersoonNP {

  @XmlElementWrapper(name = "functieaspecten")
  @XmlElement(name = "item")
  protected CovogFunctieaspect[] functieaspecten;
  @XmlElementWrapper(name = "histories")
  @XmlElement(name = "item")
  protected CovogHistorie[]      histories;
  private String                 geslachtsnaam;
  private String                 functie;
  private String                 voorvoegsel;
  private String                 voornamen;
  private String                 geboortedatum;
  private String                 gemeenteCodeGeboren;
  private String                 geboorteplaatsBuitenland;
  private String                 landCodeGeboren;
  @XmlElementWrapper(name = "nationaliteiten")
  @XmlElement(name = "code")
  private String[]               nationaliteiten = new String[0];
  private String                 burgerServiceNummer;
  private String                 geslacht;
  private String                 aanschrijfnaam;
  private String                 straat;
  private String                 huisnummer;
  private String                 huisnummerToevoeging;
  private String                 postcode;
  private String                 plaats;
  private String                 landCode;
  private String                 screeningsprofiel;

  public CovogOnderzoeksPersoonNP() {
  }

  public CovogOnderzoeksPersoonNP(String geslachtsnaam, String functie, String voorvoegsel, String voornamen,
      String geboortedatum, String gemeenteCodeGeboren, String geboorteplaatsBuitenland,
      String landCodeGeboren, String[] nationaliteiten, String burgerServiceNummer,
      String geslacht, String aanschrijfnaam, String straat, String huisnummer,
      String huisnummerToevoeging, String postcode, String plaats, String landCode,
      String screeningsprofiel, CovogFunctieaspect[] functieaspecten,
      CovogHistorie[] histories) {
    this.geslachtsnaam = geslachtsnaam;
    this.functie = functie;
    this.voorvoegsel = voorvoegsel;
    this.voornamen = voornamen;
    this.geboortedatum = geboortedatum;
    this.gemeenteCodeGeboren = gemeenteCodeGeboren;
    this.geboorteplaatsBuitenland = geboorteplaatsBuitenland;
    this.landCodeGeboren = landCodeGeboren;
    this.nationaliteiten = nationaliteiten;
    this.burgerServiceNummer = burgerServiceNummer;
    this.geslacht = geslacht;
    this.aanschrijfnaam = aanschrijfnaam;
    this.straat = straat;
    this.huisnummer = huisnummer;
    this.huisnummerToevoeging = huisnummerToevoeging;
    this.postcode = postcode;
    this.plaats = plaats;
    this.landCode = landCode;
    this.screeningsprofiel = screeningsprofiel;
    this.functieaspecten = functieaspecten;
    this.histories = copyOf(histories, histories.length);
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    this.geslachtsnaam = geslachtsnaam;
  }

  public String getFunctie() {
    return functie;
  }

  public void setFunctie(String functie) {
    this.functie = functie;
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

  public String[] getNationaliteiten() {
    return nationaliteiten;
  }

  public void setNationaliteiten(String[] nationaliteiten) {
    this.nationaliteiten = copyOf(nationaliteiten, nationaliteiten.length);
  }

  public String getBurgerServiceNummer() {
    return burgerServiceNummer;
  }

  public void setBurgerServiceNummer(String burgerServiceNummer) {
    this.burgerServiceNummer = burgerServiceNummer;
  }

  public String getGeslacht() {
    return geslacht;
  }

  public void setGeslacht(String geslacht) {
    this.geslacht = geslacht;
  }

  public String getAanschrijfnaam() {
    return aanschrijfnaam;
  }

  public void setAanschrijfnaam(String aanschrijfnaam) {
    this.aanschrijfnaam = aanschrijfnaam;
  }

  public String getStraat() {
    return straat;
  }

  public void setStraat(String straat) {
    this.straat = straat;
  }

  public String getHuisnummer() {
    return huisnummer;
  }

  public void setHuisnummer(String huisnummer) {
    this.huisnummer = huisnummer;
  }

  public String getHuisnummerToevoeging() {
    return huisnummerToevoeging;
  }

  public void setHuisnummerToevoeging(String huisnummerToevoeging) {
    this.huisnummerToevoeging = huisnummerToevoeging;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public String getPlaats() {
    return plaats;
  }

  public void setPlaats(String plaats) {
    this.plaats = plaats;
  }

  public String getLandCode() {
    return landCode;
  }

  public void setLandCode(String landCode) {
    this.landCode = landCode;
  }

  public String getScreeningsprofiel() {
    return screeningsprofiel;
  }

  public void setScreeningsprofiel(String screeningsprofiel) {
    this.screeningsprofiel = screeningsprofiel;
  }

  public CovogFunctieaspect[] getFunctieaspecten() {
    return functieaspecten;
  }

  public void setFunctieaspecten(CovogFunctieaspect[] functieaspecten) {
    this.functieaspecten = functieaspecten;
  }

  public CovogHistorie[] getHistories() {
    return histories;
  }

  public void setHistories(CovogHistorie[] histories) {
    this.histories = copyOf(histories, histories.length);
  }
}
