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

package nl.procura.gbaws.web.rest.v1_0.gbav.account;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "account")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "code", "naam", "omschrijving", "type", "dagenGeldig", "datumVerloop", "datumGewijzigd",
    "geblokkeerd", "gebruikersnaam", "wachtwoord" })
public class GbaWsRestGbavAccount {

  private int      code           = 0;
  private String   naam           = "";
  private String   omschrijving   = "";
  private GbavType type           = GbavType.BEPERKT;
  private int      dagenGeldig    = 0;
  private String   datumVerloop   = "";
  private String   datumGewijzigd = "";
  private boolean  geblokkeerd    = false;
  private String   gebruikersnaam = "";
  private String   wachtwoord     = "";

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }

  public GbavType getType() {
    return type;
  }

  public void setType(GbavType type) {
    this.type = type;
  }

  public int getDagenGeldig() {
    return dagenGeldig;
  }

  public void setDagenGeldig(int dagenGeldig) {
    this.dagenGeldig = dagenGeldig;
  }

  public String getDatumVerloop() {
    return datumVerloop;
  }

  public void setDatumVerloop(String datumVerloop) {
    this.datumVerloop = datumVerloop;
  }

  public boolean isGeblokkeerd() {
    return geblokkeerd;
  }

  public void setGeblokkeerd(boolean geblokkeerd) {
    this.geblokkeerd = geblokkeerd;
  }

  public String getDatumGewijzigd() {
    return datumGewijzigd;
  }

  public void setDatumGewijzigd(String datumGewijzigd) {
    this.datumGewijzigd = datumGewijzigd;
  }

  public String getGebruikersnaam() {
    return gebruikersnaam;
  }

  public void setGebruikersnaam(String gebruikersnaam) {
    this.gebruikersnaam = gebruikersnaam;
  }

  public String getWachtwoord() {
    return wachtwoord;
  }

  public void setWachtwoord(String wachtwoord) {
    this.wachtwoord = wachtwoord;
  }
}
