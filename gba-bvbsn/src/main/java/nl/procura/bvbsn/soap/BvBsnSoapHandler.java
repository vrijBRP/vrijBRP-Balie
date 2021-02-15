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

package nl.procura.bvbsn.soap;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import nl.ictu.bsn.*;
import nl.procura.soap.CustomSoapException;
import nl.procura.soap.SoapHandler;
import nl.procura.standard.Resource;

public class BvBsnSoapHandler extends SoapHandler {

  public BvBsnSoapHandler() {
  }

  public BvBsnSoapHandler(String endpoint) {
    super(endpoint);
  }

  public BerichtUitBase send(BerichtInBase berichtIn) {

    try {
      GebruikerSoap proxy = getStub().getGebruikerSoap();

      updateProxy((BindingProvider) proxy);

      if (berichtIn instanceof ZoekNrBI) {
        return proxy.zoekNr((ZoekNrBI) berichtIn);
      } else if (berichtIn instanceof VerificatieIdenDocumentenBI) {
        return proxy.verificatieIdentiteitsDocument((VerificatieIdenDocumentenBI) berichtIn);
      } else if (berichtIn instanceof OpvrBsnIdenGegBI) {
        return proxy.opvrBsnIdenGeg((OpvrBsnIdenGegBI) berichtIn);
      } else if (berichtIn instanceof HaalOpIdenGegBI) {
        return proxy.haalOpIdenGeg((HaalOpIdenGegBI) berichtIn);
      } else if (berichtIn instanceof ContrBsnIdenGegBI) {
        return proxy.contrBsnIdenGeg((ContrBsnIdenGegBI) berichtIn);
      } else {
        throw new Exception("Onbekend BerichtInBase: " + berichtIn.getClass());
      }
    } catch (Exception ex) {
      throw new CustomSoapException(ex);
    }
  }

  private Gebruiker getStub() {

    QName qName = new QName("http://bsn.ictu.nl/", "Gebruiker");

    Gebruiker service = new Gebruiker(Resource.getURL("wsdl/WSDL-BSN-Verificatie.wsdl"), qName);

    service.setHandlerResolver(new MyHandlerResolver(this));

    return service;
  }
}
