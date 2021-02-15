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

package nl.procura.diensten.gba.ple.extensions.formats;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.trim;

import java.text.MessageFormat;

import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.gba.common.MiscUtils;

public class Adres {

  private BasePLElem datumAanvang;
  private BasePLElem straat;
  private BasePLElem huisnummer;
  private BasePLElem huisletter;
  private BasePLElem huisnummertoev;
  private BasePLElem huisnummeraand;
  private BasePLElem locatie;
  private BasePLElem postcode;
  private BasePLElem gemeentedeel;
  private BasePLElem woonplaats;
  private BasePLElem gemeente;
  private BasePLElem buitenland1;
  private BasePLElem buitenland2;
  private BasePLElem buitenland3;
  private BasePLElem emigratieland;
  private BasePLElem datumEmigratie;

  public Adres(BasePLElem straat, BasePLElem huisnummer, BasePLElem huisletter,
      BasePLElem huisnummertoev, BasePLElem huisnummeraand, BasePLElem locatie,
      BasePLElem postcode, BasePLElem gemeentedeel, BasePLElem woonplaats,
      BasePLElem gemeente, BasePLElem datumAanvang, BasePLElem emigratieland,
      BasePLElem datumEmigratie, BasePLElem buitenland1, BasePLElem buitenland2,
      BasePLElem buitenland3) {

    this.straat = straat;
    this.huisnummer = huisnummer;
    this.huisletter = huisletter;
    this.huisnummertoev = huisnummertoev;
    this.huisnummeraand = huisnummeraand;
    this.locatie = locatie;
    this.postcode = postcode;
    this.gemeentedeel = gemeentedeel;
    this.woonplaats = woonplaats;
    this.gemeente = gemeente;
    this.datumAanvang = datumAanvang;
    this.emigratieland = emigratieland;
    this.datumEmigratie = datumEmigratie;
    this.buitenland1 = buitenland1;
    this.buitenland2 = buitenland2;
    this.buitenland3 = buitenland3;
  }

  public String getDatumAanvang() {
    return fil(getOms(datumEmigratie)) ? getOms(datumEmigratie) : getOms(this.datumAanvang);
  }

  public void setDatumAanvang(BasePLElem datumAanvang) {
    this.datumAanvang = datumAanvang;
  }

  public boolean isBuitenland() {
    return (isGevuld(buitenland1) || isGevuld(buitenland2)) || isGevuld(buitenland3) || isGevuld(emigratieland);
  }

  public String getAdres() {
    String out;
    if (isBuitenland()) {
      out = check(trimM("{0} {1} {2}, {3}", getOms(buitenland1), getOms(buitenland2), getOms(buitenland3),
          getOms(emigratieland)));
    } else {

      out = check(trim(fil(getOms(locatie)) ? getOms(locatie)
          : (getVal(huisnummeraand) + " " + getStraatOms(
              straat) + " " + getOms(huisnummer) + " " + getOms(huisletter) + " " + getOms(huisnummertoev))));
    }

    return out;
  }

  public BasePLElem getPlaats() {
    BasePLElem wo = woonplaats;
    BasePLElem gdo = gemeentedeel;
    BasePLElem gem = gemeente;
    return fil(getOms(wo)) ? wo : (fil(getOms(gdo)) ? gdo : gem);
  }

  public BasePLElem getWoonplaats() {
    return woonplaats;
  }

  public void setWoonplaats(BasePLElem woonplaats) {
    this.woonplaats = woonplaats;
  }

  @Deprecated
  public String getAdres_pc_wpl() {
    return getAdresPcWpl();
  }

  public String getAdresPcWpl() {
    if (isBuitenland()) {
      return check(trim(getAdres()));
    }

    return check(trimM("{0}, {1}  {2}", getAdres(), getOms(getPostcode()), getOms(getWoonplaats())));
  }

  @Deprecated
  public String getAdres_pc_wpl_gem() {
    return getAdresPcWplGem();
  }

  public String getAdresPcWplGem() {

    StringBuilder adres = new StringBuilder(getAdres_pc_wpl());

    if (!isBuitenland()) {

      boolean isAdres = fil(adres.toString());

      if (!adres.toString().contains(getOms(getGemeente()))) {
        if (isAdres) {
          adres.append(" (gemeente ");
          adres.append(getOms(gemeente));
          adres.append(")");
        } else {
          adres.append(getOms(gemeente));
        }
      }
    }

    return trim(adres.toString());
  }

  @Deprecated
  public String getAdres_pc() {
    return getAdresPc();
  }

  public String getAdresPc() {
    if (isBuitenland()) {
      return check(trim(getAdres()));
    }

    return check(trimM("{0}, {1}", getAdres(), getOms(getPostcode())));
  }

  @Deprecated
  public String getPc_wpl() {
    return getPcWpl();
  }

  public String getPcWpl() {
    return getOms(getPostcode()) + "  " + getOms(getPlaats());
  }

  @Deprecated
  public String getPc_wpl_gem() {
    return getPcWplGem();
  }

  public String getPcWplGem() {
    StringBuilder adres = new StringBuilder(getPc_wpl());
    boolean isAdres = fil(adres.toString());
    if (!adres.toString().contains(getOms(gemeente))) {
      if (isAdres) {
        adres.append(" (gemeente ");
        adres.append(gemeente);
        adres.append(")");

      } else {
        adres.append(gemeente);
      }
    }

    return trim(adres.toString());
  }

  public BasePLElem getStraat() {
    return straat;
  }

  public void setStraat(BasePLElem straat) {
    this.straat = straat;
  }

  public BasePLElem getHuisnummer() {
    return huisnummer;
  }

  public void setHuisnummer(BasePLElem huisnummer) {
    this.huisnummer = huisnummer;
  }

  public BasePLElem getHuisletter() {
    return huisletter;
  }

  public void setHuisletter(BasePLElem huisletter) {
    this.huisletter = huisletter;
  }

  public BasePLElem getHuisnummertoev() {
    return huisnummertoev;
  }

  public void setHuisnummertoev(BasePLElem huisnummertoev) {
    this.huisnummertoev = huisnummertoev;
  }

  public BasePLElem getHuisnummeraand() {
    return huisnummeraand;
  }

  public void setHuisnummeraand(BasePLElem huisnummeraand) {
    this.huisnummeraand = huisnummeraand;
  }

  public BasePLElem getLocatie() {
    return locatie;
  }

  public void setLocatie(BasePLElem locatie) {
    this.locatie = locatie;
  }

  public BasePLElem getGemeentedeel() {
    return gemeentedeel;
  }

  public void setGemeentedeel(BasePLElem gemeentedeel) {
    this.gemeentedeel = gemeentedeel;
  }

  public BasePLElem getGemeente() {
    return gemeente;
  }

  public void setGemeente(BasePLElem gemeente) {
    this.gemeente = gemeente;
  }

  public BasePLElem getBuitenland1() {
    return buitenland1;
  }

  public void setBuitenland1(BasePLElem buitenland1) {
    this.buitenland1 = buitenland1;
  }

  public BasePLElem getBuitenland2() {
    return buitenland2;
  }

  public void setBuitenland2(BasePLElem buitenland2) {
    this.buitenland2 = buitenland2;
  }

  public BasePLElem getBuitenland3() {
    return buitenland3;
  }

  public void setBuitenland3(BasePLElem buitenland3) {
    this.buitenland3 = buitenland3;
  }

  public BasePLElem getEmigratieland() {
    return emigratieland;
  }

  public void setEmigratieland(BasePLElem emigratieland) {
    this.emigratieland = emigratieland;
  }

  public BasePLElem getPostcode() {
    return postcode;
  }

  public void setPostcode(BasePLElem postcode) {
    this.postcode = postcode;
  }

  public BasePLElem getDatumEmigratie() {
    return datumEmigratie;
  }

  public void setDatumEmigratie(BasePLElem datumEmigratie) {
    this.datumEmigratie = datumEmigratie;
  }

  private String getStraatOms(BasePLElem element) {
    String oms = element.getValue().getDescr();
    return oms.equals(".") ? "puntadres" : oms;
  }

  private String check(String s) {
    return fil(s) ? s : "onbekend";
  }

  private String getVal(BasePLElem element) {
    return element.getValue().getVal();
  }

  private boolean isGevuld(BasePLElem element) {
    return element.getValue().isNotBlank();
  }

  private String getOms(BasePLElem element) {
    return element.getValue().getDescr();
  }

  private String trimM(String pattern, Object... arguments) {
    return MiscUtils.trimAllowed(MessageFormat.format(pattern, arguments));
  }
}
