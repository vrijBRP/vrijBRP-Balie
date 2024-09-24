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

package nl.procura.gba.web.services.beheer.requestinbox.zaken.reisdocument;

import static nl.procura.burgerzaken.requestinbox.api.RequestInboxUtils.getVrijBRPChannel;
import static nl.procura.burgerzaken.requestinbox.api.model.InboxItemStatus.HANDLED;
import static nl.procura.gba.web.services.beheer.requestinbox.zaken.betaling.InboxBetalingProcessor.BEDRAG;
import static nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType.REQUEST_INBOX;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;

import nl.procura.burgerzaken.requestinbox.api.UpdateItemRequest;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItemTypeName;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBody;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBodyProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.gba.web.services.beheer.requestinbox.zaken.betaling.InboxBetalingProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.zaken.verloren_reisdoc.TravelDocumentType;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuut;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSResult;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentService;
import nl.procura.gba.web.services.zaken.reisdocumenten.SpoedType;

import lombok.Data;

public class InboxReisdocumentProcessor extends RequestInboxBodyProcessor {

  public InboxReisdocumentProcessor(RequestInboxItem record, Services services) {
    super(record, services);
  }

  @Override
  public CaseProcessingResult process() {
    RequestInboxItem item = getRecord();
    log(INFO, "Omschrijving: {0}", item.getDescription());
    log(INFO, "Zaaknummer: {0}", item.getId());

    ReisdocumentInboxData data = RequestInboxBody.fromJson(item, ReisdocumentInboxData.class);
    ReisdocumentAanvraag zaakId = (ReisdocumentAanvraag) getServices().getReisdocumentService().getNewZaak();

    zaakId.setBsn(BigDecimal.valueOf(data.getBsn()));
    zaakId.setReisdocumentType(data.getTravelDocumentType().getType());
    zaakId.setLengte(BigDecimal.valueOf(data.getHeight()));
    zaakId.setSpoed(BooleanUtils.isTrue(data.getExpeditedProcessing())
        ? SpoedType.JA_OP_VERZOEK_AANVRAGER
        : SpoedType.NEE);

    ReisdocumentService service = getServices().getReisdocumentService();
    service.getZaakStatussen().setInitieleStatus(zaakId, ZaakStatusType.INCOMPLEET);
    service.save(zaakId);

    // Add verzoek-id to zaak
    ZaakAttribuut attribuut = new ZaakAttribuut(zaakId.getZaakId(), REQUEST_INBOX, item.getId());
    getServices().getZaakAttribuutService().save(attribuut);

    // Update status of inbox item
    UpdateItemRequest request = new UpdateItemRequest()
        .handlingChannel(getVrijBRPChannel())
        .status(HANDLED);
    getServices().getRequestInboxService().updateItem(item, request);

    // Add documents
    if (item.getDocumentsCount() > 0) {
      DMSResult dmsResult = getServices().getRequestInboxService().getDocuments(item);
      dmsResult.getDocuments().forEach(doc -> {
        doc.setZaakId(zaakId.getZaakId());
        getServices().getDmsService().save(doc);
      });
    }

    log(INFO, "Reisdocument opgeslagen", zaakId.getZaakId());

    return getResult();
  }

  @Override
  public RequestInboxBody getBody() {
    RequestInboxBody inboxBody = new RequestInboxBody();
    try {
      ReisdocumentInboxData data = RequestInboxBody.fromJson(getRecord(), ReisdocumentInboxData.class);
      inboxBody.addBsn("BSN", data.getBsn())
          .add("Reisdocument", data.getTravelDocumentType())
          .add("Lengte", data.getHeight())
          .add("Spoed", data.getExpeditedProcessing())
          .add("Betaling", getPaymentInfo());
    } catch (RuntimeException e) {
      log(ERROR, "Fout bij inlezen verzoek", e.getMessage());
      inboxBody.add("Fout bij inlezen verzoek", e.getMessage());
    }
    return inboxBody;
  }

  private String getPaymentInfo() {
    for (RequestInboxItem relatedRecord : getServices().getRequestInboxService().getRelatedItems(getRecord())) {
      if (relatedRecord.getType() == InboxItemTypeName.VRIJBRP_PAYMENT) {
        InboxBetalingProcessor processor = new InboxBetalingProcessor(relatedRecord, getServices());
        if (processor.isPaymentSuccess()) {
          Map<String, Object> data = processor.getBody().getData();
          return "Ja, " + data.get(BEDRAG);
        }
      }
    }
    return "Nee";
  }

  @Data
  private static class ReisdocumentInboxData {

    private Long               bsn;
    private TravelDocumentType travelDocumentType;
    private Integer            height;
    private Boolean            expeditedProcessing;
  }
}
