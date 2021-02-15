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

package nl.procura.gba.web.components.layouts.form.document;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentSoort;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintActie;

public class PrintRecord {

  private byte[] previewArray = null;

  private RuntimeException exception  = null;
  private PrintActie       printActie = null;

  private Status          status   = Status.DEFAULT;
  private DocumentSoort   soort    = null;
  private DocumentRecord  document = null;
  private GbaNativeSelect uitvoer  = null;
  private Object          model    = null;

  private Zaak   zaak        = null;
  private String vervolgblad = "";

  public DocumentRecord getDocument() {
    return document;
  }

  public void setDocument(DocumentRecord document) {
    this.document = document;
  }

  public RuntimeException getException() {
    return exception;
  }

  public void setException(RuntimeException exception) {
    this.exception = exception;
  }

  public Object getModel() {
    return model;
  }

  public void setModel(Object model) {
    this.model = model;
  }

  public byte[] getPreviewArray() {
    return previewArray;
  }

  public void setPreviewArray(byte[] previewArray) {
    this.previewArray = previewArray;
  }

  public PrintActie getPrintActie() {
    return printActie;
  }

  public void setPrintActie(PrintActie printActie) {
    this.printActie = printActie;
  }

  public DocumentSoort getSoort() {
    return soort;
  }

  public void setSoort(DocumentSoort soort) {
    this.soort = soort;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public GbaNativeSelect getUitvoer() {
    return uitvoer;
  }

  public void setUitvoer(GbaNativeSelect uitvoer) {
    this.uitvoer = uitvoer;
  }

  public String getVervolgblad() {
    return vervolgblad;
  }

  public void setVervolgblad(String vervolgblad) {
    this.vervolgblad = vervolgblad;
  }

  public Zaak getZaak() {
    return zaak;
  }

  public void setZaak(Zaak zaak) {
    this.zaak = zaak;
  }

  public enum Status {
    DEFAULT,
    PRINTED,
    ERROR
  }
}
