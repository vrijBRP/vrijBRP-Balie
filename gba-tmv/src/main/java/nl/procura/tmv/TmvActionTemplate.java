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

package nl.procura.tmv;

import nl.bprbzk.gba.terugmeldvoorziening.version1.Antwoord;
import nl.bprbzk.gba.terugmeldvoorziening.version1.InzageVerzoek;
import nl.bprbzk.gba.terugmeldvoorziening.version1.Terugmelding;
import nl.procura.tmv.soap.TmvSoapHandler;

public class TmvActionTemplate {

  private Object         requestObject  = null;
  private Antwoord       responseObject = null;
  private TmvSoapHandler soapHandler    = new TmvSoapHandler();

  public void send() {
    setResponseObject(getSoapHandler().send(getRequestObject()));
  }

  public boolean isVerwerkingSuccess() {

    if (getRequestObject() instanceof InzageVerzoek) {
      return getResponseObject().getBerichtcode().matches("OZDO|OZDS");
    }

    if (getRequestObject() instanceof Terugmelding) {
      return getResponseObject().getBerichtcode().matches("ONBV");
    }

    return false;
  }

  public String getOutputMessage() {

    StringBuilder sb = new StringBuilder();

    try {
      sb.append("algemeen|berichtcode|" + getResponseObject().getBerichtcode() + "\n");
      sb.append("algemeen|berichtreferentie|" + getResponseObject().getBerichtreferentie() + "\n");
      sb.append("algemeen|verwerkingomschrijving|" + getResponseObject().getVerwerkingomschrijving() + "\n");
      sb.append("algemeen|berichtnummer|" + getResponseObject().getTmvBerichtnummer() + "\n");
      sb.append("algemeen|verwerkingcode|" + getResponseObject().getVerwerkingcode() + "\n");
    } catch (Exception e) {}

    return sb.toString();
  }

  public TmvSoapHandler getSoapHandler() {
    return soapHandler;
  }

  public void setSoapHandler(TmvSoapHandler soapHandler) {
    this.soapHandler = soapHandler;
  }

  public Object getRequestObject() {
    return requestObject;
  }

  public void setRequestObject(Object requestObject) {
    this.requestObject = requestObject;
  }

  public Antwoord getResponseObject() {
    return responseObject;
  }

  public void setResponseObject(Antwoord responseObject) {
    this.responseObject = responseObject;
  }
}
