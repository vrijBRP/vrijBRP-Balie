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

package nl.procura.gba.web.services.beheer.requestinbox;

import static nl.procura.burgerzaken.requestinbox.api.RequestInboxUtils.getVrijBRPChannel;
import static nl.procura.burgerzaken.requestinbox.api.model.InboxItemStatus.HANDLED;
import static nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutType.REQUEST_INBOX;

import java.text.MessageFormat;

import nl.procura.burgerzaken.requestinbox.api.UpdateItemRequest;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuut;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSResult;

import lombok.Getter;

public abstract class RequestInboxBodyProcessor {

  @Getter
  private final StringBuilder        log    = new StringBuilder();
  @Getter
  private final CaseProcessingResult result = new CaseProcessingResult();
  @Getter
  private final RequestInboxItem     record;
  @Getter
  private final Services             services;

  private Object inboxData;

  public RequestInboxBodyProcessor(RequestInboxItem record, Services services) {
    this.record = record;
    this.services = services;
  }

  abstract public RequestInboxBody getBody();

  abstract public CaseProcessingResult process();

  protected void addRequestBoxToZaak(Zaak zaak, RequestInboxItem item) {
    // Add verzoek-id to zaak
    ZaakAttribuut attribuut = new ZaakAttribuut(zaak.getZaakId(), REQUEST_INBOX, item.getId());
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
        doc.setZaakId(zaak.getZaakId());
        getServices().getDmsService().save(doc);
      });
    }
  }

  protected <T> T getInboxData(Class<T> clazz) {
    if (inboxData == null) {
      inboxData = RequestInboxBody.fromJson(getRecord(), clazz);
    }
    return clazz.cast(inboxData);
  }

  public void log(ProExceptionSeverity severity, String message, Object... args) {
    String line = MessageFormat.format(message, args);
    result.getInfo().add(new ProException(severity, line));
    log.append(severity.getCode() + ": " + line);
    log.append("\n");
  }
}
