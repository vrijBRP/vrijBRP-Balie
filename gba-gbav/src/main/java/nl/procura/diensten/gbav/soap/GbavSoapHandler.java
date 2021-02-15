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

package nl.procura.diensten.gbav.soap;

import javax.xml.ws.BindingProvider;

import nl.bprbzk.gba.gba_v.vraag_v0.VraagAIRequest;
import nl.bprbzk.gba.gba_v.vraag_v0.VraagPLRequest;
import nl.bprbzk.gba.gba_v.vraag_v0.VraagResponse;
import nl.bprbzk.gba.gba_v.vraagai_v0.VraagAIPortType;
import nl.bprbzk.gba.gba_v.vraagai_v0.VraagAIService;
import nl.bprbzk.gba.gba_v.vraagpl_v0.VraagPLPortType;
import nl.bprbzk.gba.gba_v.vraagpl_v0.VraagPLService;
import nl.bprbzk.gba.lrdplus.version1.*;
import nl.procura.soap.CustomSoapException;
import nl.procura.soap.SoapHandler;
import nl.procura.standard.Resource;

public class GbavSoapHandler extends SoapHandler {

  public GbavSoapHandler(String endpoint, String gebruikersnaam, String wachtwoord) {
    super(endpoint, gebruikersnaam, wachtwoord);
  }

  public Resultaat wijzigWachtwoord(String wachtwoord) {
    return getLrdProxy().changePassword(wachtwoord);
  }

  public Antwoord vraag(Vraag2 vraag) {
    return getLrdProxy().vraag(vraag);
  }

  public VraagResponse vraag(VraagPLRequest vraag) {
    return getVraagPlProxy().vraagPL(vraag);
  }

  public VraagResponse vraag(VraagAIRequest vraag) {
    return getVraagAIProxy().vraagAI(vraag);
  }

  private LrdPlus getLrdProxy() {

    try {
      LrdPlus proxy = getLrdPlusService().getLrdPlus();
      updateProxy((BindingProvider) proxy);
      ((BindingProvider) proxy).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getEndpoint());

      return proxy;
    } catch (Exception ex) {
      throw new CustomSoapException(ex);
    }
  }

  private VraagPLPortType getVraagPlProxy() {

    try {
      VraagPLPortType proxy = getVraagPlService().getVraagPLPortType();
      updateProxy((BindingProvider) proxy);
      ((BindingProvider) proxy).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getEndpoint());
      return proxy;
    } catch (Exception ex) {
      throw new CustomSoapException(ex);
    }
  }

  private VraagAIPortType getVraagAIProxy() {

    try {
      VraagAIPortType proxy = getVraagAIService().getVraagAIPortType();
      updateProxy((BindingProvider) proxy);
      ((BindingProvider) proxy).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getEndpoint());
      return proxy;
    } catch (Exception ex) {
      throw new CustomSoapException(ex);
    }
  }

  private LrdPlusService getLrdPlusService() {

    LrdPlusService service = new LrdPlusService(Resource.getURL("wsdl/lrdplus.wsdl"));
    service.setHandlerResolver(new MyHandlerResolver(this));
    return service;
  }

  private VraagPLService getVraagPlService() {

    VraagPLService service = new VraagPLService(Resource.getURL("wsdl/vraagPL-v0.1.wsdl"));
    service.setHandlerResolver(new MyHandlerResolver(this));
    return service;
  }

  private VraagAIService getVraagAIService() {

    VraagAIService service = new VraagAIService(Resource.getURL("wsdl/vraagAI-v0.1.wsdl"));
    service.setHandlerResolver(new MyHandlerResolver(this));
    return service;
  }
}
