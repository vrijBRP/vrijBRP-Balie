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

package nl.procura.gba.web.rest.v1_0.zaak.bestand;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "zaak")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    propOrder = { "zaakId", "titel", "bestandsnaam", "gebruiker", "datum", "tijd", "dmsNaam", "vertrouwelijkheid" })
public class GbaRestZaakBestand {

  private String zaakId            = "";
  private String titel             = "";
  private String bestandsnaam      = "";
  private String gebruiker         = "";
  private String dmsNaam           = "";
  private String vertrouwelijkheid = "";
  private long   datum             = 0;
  private long   tijd              = 0;

  public GbaRestZaakBestand() {
  }

  public String getBestandsnaam() {
    return bestandsnaam;
  }

  public void setBestandsnaam(String bestandsnaam) {
    this.bestandsnaam = bestandsnaam;
  }

  public String getTitel() {
    return titel;
  }

  public void setTitel(String titel) {
    this.titel = titel;
  }

  public String getZaakId() {
    return zaakId;
  }

  public void setZaakId(String zaakId) {
    this.zaakId = zaakId;
  }

  public long getDatum() {
    return datum;
  }

  public void setDatum(long datum) {
    this.datum = datum;
  }

  public long getTijd() {
    return tijd;
  }

  public void setTijd(long tijd) {
    this.tijd = tijd;
  }

  public String getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(String gebruiker) {
    this.gebruiker = gebruiker;
  }

  public String getDmsNaam() {
    return dmsNaam;
  }

  public void setDmsNaam(String dmsNaam) {
    this.dmsNaam = dmsNaam;
  }

  public String getVertrouwelijkheid() {
    return vertrouwelijkheid;
  }

  public void setVertrouwelijkheid(String vertrouwelijkheid) {
    this.vertrouwelijkheid = vertrouwelijkheid;
  }
}
