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

package nl.procura.gba.web.services.beheer.requestinbox.zaken.betaling;

import static nl.procura.burgerzaken.requestinbox.api.model.InboxItemStatus.HANDLED;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import nl.procura.burgerzaken.requestinbox.api.UpdateItemRequest;
import nl.procura.burgerzaken.requestinbox.api.model.InboxEnum;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBody;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBodyProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class InboxBetalingProcessor extends RequestInboxBodyProcessor {

  public static String BEDRAG    = "Bedrag";
  public static String AANBIEDER = "Aanbieder";

  public InboxBetalingProcessor(RequestInboxItem record, Services services) {
    super(record, services);
  }

  @Override
  public CaseProcessingResult process() {
    UpdateItemRequest request = new UpdateItemRequest().status(HANDLED);
    getServices().getRequestInboxService().updateItem(getRecord(), request);
    return getResult();
  }

  @Override
  public RequestInboxBody getBody() {
    RequestInboxBody inboxBody = new RequestInboxBody();
    try {
      BetalingInboxData data = getInboxData(BetalingInboxData.class);
      inboxBody.add(InboxBetalingProcessor.BEDRAG, toEuro(data.getAmountInCents()))
          .add("Transactie-Id", data.getTransactionId())
          .add(AANBIEDER, data.getPaymentProvider())
          .add("Aanbieder transactie-Id", data.getPaymentProviderTransactionId())
          .add("Status", data.getStatus());
    } catch (RuntimeException e) {
      log(ERROR, "Fout bij inlezen verzoek", e.getMessage());
      inboxBody.add("Fout bij inlezen verzoek", e.getMessage());
    }
    return inboxBody;
  }

  private String toEuro(Long amountInCents) {
    return "€ " + (amountInCents / 100.0);
  }

  public boolean isPaymentSuccess() {
    return getInboxData(BetalingInboxData.class).getStatus() == Status.SUCCESS;
  }

  @Data
  private static class BetalingInboxData {

    private Long   amountInCents;
    private Long   transactionId;
    private String paymentProvider;
    private Long   paymentProviderTransactionId;
    private Status status;
  }

  @Getter
  @AllArgsConstructor
  private enum Status implements InboxEnum<String> {

    SUCCESS("success", "Gelukt"),
    FAILURE("failure", "Mislukt"),
    UNKNOWN("unknown", "Onbekend");

    private final String id;
    private final String descr;

    @Override
    public String toString() {
      return descr;
    }
  }
}
