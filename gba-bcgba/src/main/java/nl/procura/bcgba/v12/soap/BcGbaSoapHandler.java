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

package nl.procura.bcgba.v12.soap;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import nl.bprbzk.bcgba.v12.*;
import nl.procura.soap.CustomSoapException;
import nl.procura.soap.SoapHandler;
import nl.procura.standard.Resource;

public class BcGbaSoapHandler extends SoapHandler {

  public BcGbaSoapHandler() {
  }

  public BcGbaSoapHandler(String endpoint) {
    super(endpoint);
  }

  public BerichtUitBase send(BerichtInBase berichtIn) {

    try {
      BeheercomponentGBASoap proxy = getStub().getBeheercomponentGBASoap();

      updateProxy((BindingProvider) proxy);

      if (berichtIn instanceof MatchIdenGegBI) {
        return proxy.matchIdenGeg((MatchIdenGegBI) berichtIn);
      }

      throw new Exception("Onbekend BerichtInBase: " + berichtIn.getClass());
    } catch (Exception ex) {
      throw new CustomSoapException(ex);
    }
  }

  private BeheercomponentGBA getStub() {

    QName qName = new QName("http://bcgba.bprbzk.nl/", "BeheercomponentGBA");

    BeheercomponentGBA service = new BeheercomponentGBA(Resource.getURL("wsdl/v1_2/bc-gba-service.wsdl"), qName);

    service.setHandlerResolver(new MyHandlerResolver(this));

    return service;
  }
}
