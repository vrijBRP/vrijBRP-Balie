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

package nl.procura.tmv.soap;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import nl.bprbzk.gba.terugmeldvoorziening.version1.*;
import nl.procura.soap.CustomSoapException;
import nl.procura.soap.SoapHandler;
import nl.procura.standard.Resource;

public class TmvSoapHandler extends SoapHandler {

  public TmvSoapHandler() {
  }

  public TmvSoapHandler(String endpoint, String username, String password) {
    super(endpoint, username, password);
  }

  public Antwoord send(Object berichtIn) {

    try {
      Terugmeldvoorziening proxy = getStub().getTerugmeldvoorziening();

      updateProxy((BindingProvider) proxy);

      if (berichtIn instanceof Terugmelding) {
        return proxy.registrerenTerugmelding((Terugmelding) berichtIn);
      }

      if (berichtIn instanceof InzageVerzoek) {
        return proxy.opvragenDossierGegevens((InzageVerzoek) berichtIn);
      }

      throw new Exception("Onbekend BerichtInBase: " + berichtIn.getClass());
    } catch (Exception ex) {
      throw new CustomSoapException(ex);
    }
  }

  private TerugmeldvoorzieningService getStub() {

    QName qName = new QName("http://www.bprbzk.nl/GBA/Terugmeldvoorziening/version1.0",
        "TerugmeldvoorzieningService");

    TerugmeldvoorzieningService service = new TerugmeldvoorzieningService(Resource.getURL("wsdl/tmv1_0.wsdl"),
        qName);

    service.setHandlerResolver(new MyHandlerResolver(this));

    return service;
  }
}
