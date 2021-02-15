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

package nl.procura.bvbsn;

import nl.ictu.bsn.AfzenderDE;
import nl.ictu.bsn.BerichtInBase;
import nl.ictu.bsn.BerichtUitBase;
import nl.procura.bvbsn.soap.BvBsnSoapHandler;

public class BvBsnActionTemplate {

  private String     indicatieEindgebruiker = "";
  private AfzenderDE afzender;

  private BerichtInBase    requestObject  = null;
  private BerichtUitBase   responseObject = null;
  private BvBsnSoapHandler soapHandler    = new BvBsnSoapHandler();

  public void send() {
    setResponseObject(getSoapHandler().send(getRequestObject()));
  }

  public int getVerwerkingSuccesCode() {
    return 0;
  }

  public Verwerking getVerwerking() {

    return new Verwerking(
        getResponseObject().getBvBsnBericht().getBerichtResultaatCode() == getVerwerkingSuccesCode(),
        getResponseObject().getBvBsnBericht().getBerichtResultaatCode(),
        getResponseObject().getBvBsnBericht().getBerichtResultaatOmschrijving());
  }

  public String getOutputMessage() {

    StringBuilder sb = new StringBuilder();

    try {
      int code = getResponseObject().getBvBsnBericht().getBerichtResultaatCode();
      String oms = getResponseObject().getBvBsnBericht().getBerichtResultaatOmschrijving();

      sb.append("Algemeen|afzender|" + getResponseObject().getBerichtNrAfzender() + "\n");
      sb.append("Algemeen|melding|" + oms + " (" + code + ")\n");
    } catch (Exception e) {
      // ignore
    }

    return sb.toString();
  }

  protected void append(StringBuilder sb, String id, Object value) {

    sb.append(id);
    sb.append(value);
    sb.append("\n");
  }

  public AfzenderDE getAfzender() {
    return afzender;
  }

  public void setAfzender(AfzenderDE afzender) {
    this.afzender = afzender;
  }

  public String getIndicatieEindgebruiker() {
    return indicatieEindgebruiker;
  }

  public void setIndicatieEindgebruiker(String indicatieEindgebruiker) {
    this.indicatieEindgebruiker = indicatieEindgebruiker;
  }

  public BerichtUitBase getResponseObject() {
    return responseObject;
  }

  public void setResponseObject(BerichtUitBase responseObject) {
    this.responseObject = responseObject;
  }

  public BvBsnSoapHandler getSoapHandler() {
    return soapHandler;
  }

  public void setSoapHandler(BvBsnSoapHandler soapHandler) {
    this.soapHandler = soapHandler;
  }

  public BerichtInBase getRequestObject() {
    return requestObject;
  }

  public void setRequestObject(BerichtInBase requestObject) {
    this.requestObject = requestObject;
  }

  public class Verwerking {

    private boolean succes       = false;
    private int     code         = 0;
    private String  omschrijving = "";

    public Verwerking(boolean succes, int code, String omschrijving) {

      setSucces(succes);
      setCode(code);
      setOmschrijving(omschrijving);
    }

    public boolean isSucces() {
      return succes;
    }

    public void setSucces(boolean succes) {
      this.succes = succes;
    }

    public int getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }

    public String getOmschrijving() {
      return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
      this.omschrijving = omschrijving;
    }
  }
}
