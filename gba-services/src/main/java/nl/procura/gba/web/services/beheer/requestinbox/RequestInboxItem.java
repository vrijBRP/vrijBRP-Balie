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

import static nl.procura.burgerzaken.requestinbox.api.model.InboxItemCustomerRole.INITIATOR;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.time.ZoneId;
import java.util.Date;

import nl.procura.burgerzaken.requestinbox.api.model.InboxItem;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItemCustomerRole;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItemStatus;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItemTypeName;
import nl.procura.standard.ProcuraDate;
import nl.procura.validation.Bsn;

import lombok.Data;

@Data
public class RequestInboxItem {

  private String id;
  private String parentId;
  private byte[] content;

  private final InboxItem inboxItem;
  private String          handlerDescription;

  public RequestInboxItem(InboxItem inboxItem) {
    this.inboxItem = inboxItem;
  }

  public InboxItemTypeName getType() {
    return InboxItemTypeName.getByName(inboxItem.getType().getName());
  }

  public String getTypeName() {
    return inboxItem.getType().getName();
  }

  public String getDescription() {
    InboxItemTypeName type = InboxItemTypeName.getByName(inboxItem.getType().getName());
    return type.isUnknown()
        ? (defaultIfBlank(inboxItem.getDescription(), inboxItem.getType().getName()) + " (onbekend type)")
        : type.getDescr();
  }

  public int getDocumentsCount() {
    return inboxItem.getDocuments().size();
  }

  public InboxItemStatus getStatus() {
    return InboxItemStatus.getByName(inboxItem.getStatus());
  }

  public String getRequestHandler() {
    return inboxItem.getRequestHandler();
  }

  public String getHandlingChannel() {
    return inboxItem.getHandlingChannel();
  }

  public String getApplication() {
    return inboxItem.getHandlingChannel();
  }

  public ProcuraDate getRegisteredOn() {
    return new ProcuraDate(Date.from(inboxItem.getRegisteredOn().toLocalDateTime()
        .atZone(ZoneId.systemDefault()).toInstant()));
  }

  public Bsn getBsn() {
    return getBsnCustomerByRole(INITIATOR);
  }

  private Bsn getBsnCustomerByRole(InboxItemCustomerRole role) {
    return inboxItem.getCustomers().stream()
        .filter(customer -> InboxItemCustomerRole.getByName(customer.getRole()) == role)
        .findFirst()
        .map(customer -> getBsnFromCustomer(customer.getCustomer()))
        .filter(Bsn::isCorrect)
        .orElse(null);
  }

  public static Bsn getBsnFromCustomer(String url) {
    return new Bsn(url.substring(url.lastIndexOf("/") + 1));
  }
}
