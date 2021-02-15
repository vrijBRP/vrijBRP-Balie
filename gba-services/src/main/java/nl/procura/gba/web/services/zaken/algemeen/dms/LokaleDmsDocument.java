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

package nl.procura.gba.web.services.zaken.algemeen.dms;

import static nl.procura.standard.Globalfunctions.along;

import com.google.common.collect.ComparisonChain;

import nl.procura.gba.common.DateTime;

@Deprecated
public class LokaleDmsDocument implements Comparable<LokaleDmsDocument> {

  private String titel             = "";
  private String extensie          = "";
  private String pad               = "";
  private String aangemaaktDoor    = "";
  private String datum             = "";
  private String tijd              = "";
  private String datatype          = "";
  private String bestandsnaam      = "";
  private String zaakId            = "";
  private String dmsNaam           = "";
  private String vertrouwelijkheid = "";

  @Override
  public int compareTo(LokaleDmsDocument that) {
    return ComparisonChain.start()
        .compare(that.getDatumTijd().getLongDate(), this.getDatumTijd().getLongDate())
        .compare(that.getDatumTijd().getLongTime(), this.getDatumTijd().getLongTime())
        .result();
  }

  public String getAangemaaktDoor() {
    return aangemaaktDoor;
  }

  public void setAangemaaktDoor(String aangemaaktDoor) {
    this.aangemaaktDoor = aangemaaktDoor;
  }

  public String getBestandsnaam() {
    return bestandsnaam;
  }

  public void setBestandsnaam(String bestandsnaam) {
    this.bestandsnaam = bestandsnaam;
  }

  public String getDatatype() {
    return datatype;
  }

  public void setDatatype(String datatype) {
    this.datatype = datatype;
  }

  public String getDatum() {
    return datum;
  }

  public void setDatum(String datum) {
    this.datum = datum;
  }

  public DateTime getDatumTijd() {
    return new DateTime(along(getDatum()), along(getTijd()));
  }

  public String getDmsNaam() {
    return dmsNaam;
  }

  public void setDmsNaam(String dmsNaam) {
    this.dmsNaam = dmsNaam;
  }

  public String getExtensie() {
    return extensie;
  }

  public void setExtensie(String extensie) {
    this.extensie = extensie;
  }

  public String getPad() {
    return pad;
  }

  public void setPad(String pad) {
    this.pad = pad;
  }

  public String getTijd() {
    return tijd;
  }

  public void setTijd(String tijd) {
    this.tijd = tijd;
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

  public String getVertrouwelijkheid() {
    return vertrouwelijkheid;
  }

  public void setVertrouwelijkheid(String vertrouwelijkheid) {
    this.vertrouwelijkheid = vertrouwelijkheid;
  }
}
