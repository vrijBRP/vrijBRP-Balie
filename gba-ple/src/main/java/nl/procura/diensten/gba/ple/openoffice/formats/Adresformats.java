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

package nl.procura.diensten.gba.ple.openoffice.formats;

import static nl.procura.standard.Globalfunctions.*;

public class Adresformats {

  public String  datum_aanvang   = "";
  private String straat          = "";
  private String huisnummer      = "";
  private String huisletter      = "";
  private String huisnummertoev  = "";
  private String huisnummeraand  = "";
  private String locatie         = "";
  private String postcode        = "";
  private String gemeentedeel    = "";
  private String woonplaats      = "";
  private String gemeente        = "";
  private String buitenland1     = "";
  private String buitenland2     = "";
  private String buitenland3     = "";
  private String emigratieland   = "";
  private String datum_emigratie = "";

  public Adresformats setValues(String straat, String huisnummer, String huisletter, String huisnummertoev,
      String huisnummeraand, String locatie, String postcode, String gemeentedeel,
      String woonplaats, String gemeente, String datum_aanvang, String emigratieland,
      String emigratiedatum, String buitenland1, String buitenland2, String buitenland3) {

    this.straat = astr(straat);
    this.huisnummer = huisnummer;
    this.huisletter = astr(huisletter);
    this.huisnummertoev = astr(huisnummertoev);
    this.huisnummeraand = astr(huisnummeraand);
    this.locatie = astr(locatie);
    this.postcode = astr(postcode);
    this.gemeentedeel = astr(gemeentedeel);
    this.woonplaats = astr(woonplaats);
    this.gemeente = astr(gemeente);
    this.datum_aanvang = astr(datum_aanvang);
    this.emigratieland = astr(emigratieland);
    this.datum_emigratie = astr(emigratiedatum);
    this.buitenland1 = astr(buitenland1);
    this.buitenland2 = astr(buitenland2);
    this.buitenland3 = astr(buitenland3);
    return this;
  }

  public String getAdres() {
    if (isBuitenland()) {
      return trim(buitenland1 + " " + buitenland2 + " " + buitenland3 + ", " + emigratieland);
    }

    return trim(fil(locatie) ? locatie
        : (huisnummeraand + " " + getStraatOms(astr(straat)) + " " + (pos(
            huisnummer) ? huisnummer : "") + " " + huisletter + " " + huisnummertoev));
  }

  public boolean isBuitenland() {
    return (fil(buitenland1) || fil(buitenland2) || fil(buitenland3) || fil(emigratieland));
  }

  private String getStraatOms(String straat) {
    return straat.equals(".") ? "puntadres" : straat;
  }

  public String getAdres_pc_wpl() {
    if (isBuitenland()) {
      return trim(getAdres());
    }

    return trim(getAdres() + ", " + getPostcode()) + "  " + getWoonplaats();
  }

  public String getAdres_pc_wpl_gem() {
    StringBuilder adres = new StringBuilder(getAdres_pc_wpl());
    if (!isBuitenland()) {
      boolean isAdres = fil(adres.toString());
      appendGemeente(adres, isAdres);
    }

    return trim(adres.toString());
  }

  public String getPostcode() {
    String s = this.postcode.replaceAll("\\s+", "");
    if (s.length() == 6) {
      return (s.substring(0, 4) + " " + s.substring(4, 6).toUpperCase());
    }

    return this.postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public String getWoonplaats() {
    return fil(woonplaats) ? woonplaats : (fil(gemeentedeel) ? gemeentedeel : gemeente);
  }

  public void setWoonplaats(String woonplaats) {
    this.woonplaats = woonplaats;
  }

  public String getPc_wpl() {
    return getPostcode() + "  " + getWoonplaats();
  }

  public String getPc_wpl_gem() {

    StringBuilder adres = new StringBuilder(getPc_wpl());
    boolean isAdres = fil(adres.toString());
    appendGemeente(adres, isAdres);
    return trim(adres.toString());
  }

  public String getDatum_aanvang() {
    return fil(datum_emigratie) ? datum_emigratie : this.datum_aanvang;
  }

  public void setDatum_aanvang(String datum_aanvang) {
    this.datum_aanvang = datum_aanvang;
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

  public String getHuisletter() {
    return huisletter;
  }

  public void setHuisletter(String huisletter) {
    this.huisletter = huisletter;
  }

  public String getHuisnummertoev() {
    return huisnummertoev;
  }

  public void setHuisnummertoev(String huisnummertoev) {
    this.huisnummertoev = huisnummertoev;
  }

  public String getHuisnummeraand() {
    return huisnummeraand;
  }

  public void setHuisnummeraand(String huisnummeraand) {
    this.huisnummeraand = huisnummeraand;
  }

  public String getLocatie() {
    return locatie;
  }

  public void setLocatie(String locatie) {
    this.locatie = locatie;
  }

  public String getGemeentedeel() {
    return gemeentedeel;
  }

  public void setGemeentedeel(String gemeentedeel) {
    this.gemeentedeel = gemeentedeel;
  }

  public String getGemeente() {
    return gemeente;
  }

  public void setGemeente(String gemeente) {
    this.gemeente = gemeente;
  }

  public String getBuitenland1() {
    return buitenland1;
  }

  public void setBuitenland1(String buitenland1) {
    this.buitenland1 = buitenland1;
  }

  public String getBuitenland2() {
    return buitenland2;
  }

  public void setBuitenland2(String buitenland2) {
    this.buitenland2 = buitenland2;
  }

  public String getBuitenland3() {
    return buitenland3;
  }

  public void setBuitenland3(String buitenland3) {
    this.buitenland3 = buitenland3;
  }

  public String getEmigratieland() {
    return emigratieland;
  }

  public void setEmigratieland(String emigratieland) {
    this.emigratieland = emigratieland;
  }

  public String getDatum_emigratie() {
    return datum_emigratie;
  }

  public void setDatum_emigratie(String datum_emigratie) {
    this.datum_emigratie = datum_emigratie;
  }

  private void appendGemeente(StringBuilder adres, boolean isAdres) {
    if (!adres.toString().contains(gemeente)) {
      if (isAdres) {
        adres.append(" (gemeente ");
        adres.append(gemeente);
        adres.append(")");
      } else {
        adres.append(gemeente);
      }
    }
  }
}
