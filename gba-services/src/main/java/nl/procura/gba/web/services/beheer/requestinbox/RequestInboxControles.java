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

import nl.procura.burgerzaken.requestinbox.api.ListItemsRequest;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItemStatus;
import nl.procura.burgerzaken.requestinbox.api.model.InboxItemTypeName;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesTemplate;
import nl.procura.gba.web.services.zaken.algemeen.controle.StandaardControle;

public class RequestInboxControles extends ControlesTemplate<RequestInboxService> {

  public RequestInboxControles(RequestInboxService service) {
    super(service);
  }

  @Override
  public Controles getControles(ControlesListener listener) {
    Controles controles = new Controles();
    ListItemsRequest request = new ListItemsRequest()
        .status(InboxItemStatus.RECEIVED)
        .types(InboxItemTypeName.getAutomaticProcessingIds())
        .page(1)
        .itemsPerPage(30)
        .preferredChannel(getVrijBRPChannel());
    getService().getRequestInboxItems(request)
        .getItems()
        .forEach(inboxItem -> {
          if (!inboxItem.getType().isUnknown()) {
            processRequest(inboxItem, controles);
          }
        });

    return controles;
  }

  @Transactional
  private void processRequest(RequestInboxItem item, Controles controles) {
    if (!item.getType().isUnknown()) {
      RequestInboxProcessors.get(item, getService().getServices()).process();
      controles.addControle(new RequestInboxControle(item, "Nieuw verzoek"));
    }
  }

  public static class RequestInboxControle extends StandaardControle {

    public RequestInboxControle(RequestInboxItem inboxRecord, String onderwerp) {
      super(onderwerp, inboxRecord.getDescription());
      setId(inboxRecord.getId());
      setGewijzigd(true);
    }
  }
}
