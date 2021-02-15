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

package nl.procura.gba.web.services.beheer.gebruiker.info;

import static nl.procura.standard.Globalfunctions.eq;

import nl.procura.gba.jpa.personen.db.UsrInfo;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;

public class GebruikerInfo extends UsrInfo {

  private Gebruiker gebruiker    = null;
  private String    defaultValue = "";
  private String    userValue    = "";

  public GebruikerInfo() {
  }

  public GebruikerInfo(Object key) {
    setKey(key.toString());
  }

  @Override
  public boolean equals(Object other) {
    // self check
    if (this == other) {
      return true;
    }
    // null check
    if (other == null) {
      return false;
    }
    // type check and cast
    if (!(other instanceof GebruikerInfo)) {
      return false;
    }

    GebruikerInfo gebrInfoImpl = (GebruikerInfo) other;
    return eq(gebrInfoImpl.getKey(), getKey()) && (gebrInfoImpl.getUsr().getCUsr().equals(getUsr().getCUsr()));
  }

  public Gebruiker getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(Gebruiker gebruiker) {
    this.gebruiker = gebruiker;
  }

  public String getGebruikerWaarde() {
    return userValue;
  }

  public void setGebruikerWaarde(String userValue) {
    this.userValue = userValue;
  }

  public String getInfo() {
    return getKey();
  }

  public void setInfo(String info) {
    setKey(info);
  }

  public String getOmschrijving() {
    return getDescr();
  }

  public void setOmschrijving(String omschrijving) {
    setDescr(omschrijving);
  }

  public String getStandaardWaarde() {
    return defaultValue;
  }

  public void setStandaardWaarde(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public String getWaarde() {
    return getValue();
  }

  public void setWaarde(String waarde) {
    setValue(waarde);
  }

  @Override
  public int hashCode() {
    return defaultValue.length();
  }

  public boolean isIedereen() {
    return getUsr().getCUsr() <= 0;
  }

  @Override
  public String toString() {
    return "GebruikerInfoImpl [getDescr()=" + getOmschrijving() + ", getKey()=" + getKey() + ", getValue()="
        + getValue() + "]";
  }
}
