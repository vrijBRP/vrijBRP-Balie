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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox;

import java.util.List;
import java.util.function.Supplier;

import com.vaadin.ui.Button;

import nl.procura.burgerzaken.requestinbox.api.UpdateItemRequest;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxService;

public class RequestInboxStatusButton extends Button {

  public RequestInboxStatusButton(Supplier<List<RequestInboxItem>> itemsSupplier, Runnable postChangeListener) {
    super("Status wijzigen");
    addListener((ClickListener) event -> {
      List<RequestInboxItem> selectedValues = itemsSupplier.get();
      RequestInboxService service = getGbaApplication().getServices().getRequestInboxService();
      getGbaApplication().getParentWindow().addWindow(new RequestInboxStatusWindow(status -> {
        UpdateItemRequest request = new UpdateItemRequest().status(status);
        selectedValues.forEach(inboxItem -> service.updateItem(inboxItem, request));
        postChangeListener.run();
      }));
    });
  }

  public GbaApplication getGbaApplication() {
    return (GbaApplication) super.getApplication();
  }
}
