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

package nl.procura.gba.web.services.zaken.documenten.printen;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;

public class PrintActie {

  private List<PrintListener> listeners   = new ArrayList<>();
  private ConditionalMap      model       = new ConditionalMap();
  private DocumentRecord      document    = null;
  private PrintOptie          printOptie  = null;
  private Zaak                zaak        = null;
  private String              vervolgblad = "";

  public void addListener(PrintListener printListener) {
    getListeners().add(printListener);
  }

  public DocumentRecord getDocument() {
    return document;
  }

  public void setDocument(DocumentRecord document) {
    this.document = document;
  }

  public List<PrintListener> getListeners() {
    return listeners;
  }

  public void setListeners(List<PrintListener> listeners) {
    this.listeners = listeners;
  }

  public ConditionalMap getModel() {
    return model;
  }

  public void setModel(ConditionalMap model) {
    this.model = model;
  }

  public PrintOptie getPrintOptie() {
    return printOptie;
  }

  public void setPrintOptie(PrintOptie printOptie) {
    this.printOptie = printOptie;
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

  @Override
  public String toString() {
    return "Printactie: " + document.getDocument() + ", bestand: " + document.getBestand() + ", printoptie: "
        + printOptie.getOms();
  }
}
