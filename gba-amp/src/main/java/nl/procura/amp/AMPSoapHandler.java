/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.amp;

import java.net.URL;

import javax.xml.ws.BindingProvider;

import nl.amp.api.domain.BevestigingKoppelingOrderRequest;
import nl.amp.api.domain.BevestigingKoppelingOrderResult;
import nl.amp.api.domain.BevestigingVerwerkingAnnuleringRequest;
import nl.amp.api.domain.BevestigingVerwerkingAnnuleringResult;
import nl.amp.api.domain.BevestigingVerwerkingUitreikingRequest;
import nl.amp.api.domain.BevestigingVerwerkingUitreikingResult;
import nl.amp.api.domain.BlokkeringRequest;
import nl.amp.api.domain.BlokkeringResult;
import nl.amp.api.domain.OphalenAnnuleringenRequest;
import nl.amp.api.domain.OphalenAnnuleringenResult;
import nl.amp.api.domain.OphalenOrdersRequest;
import nl.amp.api.domain.OphalenOrdersResult;
import nl.amp.api.domain.OphalenUitreikingenRequest;
import nl.amp.api.domain.OphalenUitreikingenResult;
import nl.amp.api.domain.OrderUpdateRequest;
import nl.amp.api.domain.OrderUpdateResult;
import nl.amp.api.domain.ThuisbezorgenReisdocumentService;
import nl.amp.api.domain.ThuisbezorgenReisdocumentService_Service;
import nl.amp.api.domain.VoormeldingRequest;
import nl.amp.api.domain.VoormeldingResult;
import nl.procura.soap.CustomSoapException;
import nl.procura.soap.SoapHandler;
import nl.procura.standard.Resource;

public class AMPSoapHandler extends SoapHandler {

  public AMPSoapHandler(String endpoint) {
    super(endpoint);
  }

  public VoormeldingResult voormelding(VoormeldingRequest request) {
    return getService().voormelding(request);
  }

  public OphalenOrdersResult ophalenOrders(OphalenOrdersRequest request) {
    return getService().ophalenOrders(request);
  }

  public BevestigingKoppelingOrderResult bevestigingKoppelingOrder(BevestigingKoppelingOrderRequest request) {
    return getService().bevestigingKoppelingOrder(request);
  }

  public OrderUpdateResult orderUpdate(OrderUpdateRequest request) {
    return getService().orderUpdate(request);
  }

  public BlokkeringResult blokkeer(BlokkeringRequest request) {
    return getService().blokkering(request);
  }

  public OphalenUitreikingenResult ophalenUitreikingen(OphalenUitreikingenRequest request) {
    return getService().ophalenUitreikingen(request);
  }

  public BevestigingVerwerkingUitreikingResult bevestigUitreiking(BevestigingVerwerkingUitreikingRequest request) {
    return getService().bevestigingVerwerkingUitreiking(request);
  }

  public OphalenAnnuleringenResult ophalenAnnuleringen(OphalenAnnuleringenRequest request) {
    return getService().ophalenAnnuleringen(request);
  }

  public BevestigingVerwerkingAnnuleringResult bevestigingAnnulering(BevestigingVerwerkingAnnuleringRequest request) {
    return getService().bevestigingVerwerkingAnnulering(request);
  }

  private ThuisbezorgenReisdocumentService getService() {
    try {
      ThuisbezorgenReisdocumentService proxy = getStub().getBasicHttpBindingThuisbezorgenReisdocumentService();
      updateProxy((BindingProvider) proxy);
      return proxy;
    } catch (Exception ex) {
      throw new CustomSoapException(ex);
    }
  }

  private ThuisbezorgenReisdocumentService_Service getStub() {
    URL url = Resource.getURL("wsdl/ThuisbezorgenReisdocumentService.wsdl");
    ThuisbezorgenReisdocumentService_Service service = new ThuisbezorgenReisdocumentService_Service(url);
    service.setHandlerResolver(new MyHandlerResolver(this));
    return service;
  }
}
