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

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.gba.web.services.beheer.requestinbox.zaken.betaling.InboxBetalingProcessor.BEDRAG;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;

import nl.procura.burgerzaken.requestinbox.api.model.InboxItemTypeName;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBody;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBodyProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.gba.web.services.beheer.requestinbox.zaken.betaling.InboxBetalingProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.zaken.verloren_reisdoc.TravelDocumentType;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;
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
    ReisdocumentAanvraag zaak = (ReisdocumentAanvraag) getServices().getReisdocumentService().getNewZaak();

    zaak.setBsn(BigDecimal.valueOf(data.getBsn()));
    zaak.setReisdocumentType(data.getTravelDocumentType().getType());
    zaak.setLengte(BigDecimal.valueOf(data.getHeight()));
    zaak.setSpoed(BooleanUtils.isTrue(data.getExpeditedProcessing())
        ? SpoedType.JA_OP_VERZOEK_AANVRAGER
        : SpoedType.NEE);

    ReisdocumentService service = getServices().getReisdocumentService();
    service.getZaakStatussen().setInitieleStatus(zaak, ZaakStatusType.INCOMPLEET);
    service.save(zaak);

    addRequestBoxToZaak(zaak, item);

    log(INFO, "Reisdocument opgeslagen", zaak.getZaakId());

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
