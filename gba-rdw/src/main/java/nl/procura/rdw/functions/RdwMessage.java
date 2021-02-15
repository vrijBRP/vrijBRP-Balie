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

package nl.procura.rdw.functions;

public class RdwMessage {

  private boolean ignoreBlock = false;
  private Proces  request     = null;
  private Proces  response    = null;

  public RdwMessage() {
  }

  public RdwMessage(Proces request) {
    this.request = request;
  }

  public Proces getRequest() {
    return request;
  }

  public void setRequest(Proces request) {
    this.request = request;
  }

  public Proces getResponse() {
    return response;
  }

  public void setResponse(Proces response) {
    this.response = response;
  }

  public void setRequest(RdwProces pf, ProcesObject p) {
    Proces proces = new Proces();
    proces.setObject(p);
    proces.setProces(pf.p);
    proces.setFunctie(pf.f);
  }

  public boolean isIgnoreBlock() {
    return ignoreBlock;
  }

  public void setIgnoreBlock(boolean ignoreBlock) {
    this.ignoreBlock = ignoreBlock;
  }
}
