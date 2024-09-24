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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox.sidelayout;

import static java.util.Collections.singletonList;
import static nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource.STANDAARD;

import java.util.Map;

import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.ModuleRequestInboxParams;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.RequestInboxDocumentsButton;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.RequestInboxStatusButton;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.RequestInboxWindow;
import nl.procura.gba.web.modules.zaken.inbox.overzicht.InboxOverzichtCaseLayout;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBodyProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxProcessors;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxService;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSResult;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;

public class RequestInboxSideLayout extends GbaVerticalLayout {

  private final RequestInboxItem inboxItem;

  public RequestInboxSideLayout(RequestInboxItem inboxItem) {
    this.inboxItem = inboxItem;
    setStyleName("right-side-inbox-layout");
    setSizeFull();
    setWidth("300px");
    setMargin(true);
  }

  @Override
  public void attach() {
    if (getComponentCount() == 0) {
      addComponent(getTableLayout(inboxItem));
    }
    super.attach();
  }

  private Layout getTableLayout(RequestInboxItem inboxItem) {
    VLayout layout = new VLayout().spacing(true);
    layout.add(new Fieldset(inboxItem.getDescription()));
    layout.add(new HLayout(addStatusButton(), addRequestButton(inboxItem)).width("100%"));
    layout.add(new HLayout(addDocumentButton(), addCloseButton()).width("100%"));
    layout.add(new Fieldset("Gegevens"));

    // Process body
    RequestInboxBodyProcessor processor = RequestInboxProcessors.get(inboxItem, getApplication().getServices());
    Map<String, Object> data = processor.getBody().getData();
    layout.addExpandComponent(InboxOverzichtCaseLayout.builder()
        .numberOfColumns(1)
        .idListener(id -> getApplication().goToPl(getWindow(), "#pl", STANDAARD, id))
        .data(data)
        .build()
        .getLayout());

    return layout;
  }

  private Button addRequestButton(RequestInboxItem inboxItem) {
    Button button = new Button("Toon meer", event -> {
      RequestInboxWindow window = new RequestInboxWindow(ModuleRequestInboxParams.builder()
          .item(inboxItem)
          .build());
      getApplication().getParentWindow().addWindow(window);
    });
    button.setWidth("100%");
    return button;
  }

  private Button addStatusButton() {
    Button button = new RequestInboxStatusButton(() -> singletonList(inboxItem), () -> {});
    button.setWidth("100%");
    return button;
  }

  private Button addDocumentButton() {
    Button button = new RequestInboxDocumentsButton(this::getDocuments);
    button.setWidth("100%");
    return button;
  }

  private DMSResult getDocuments() {
    return getApplication().getServices().getRequestInboxService().getDocuments(inboxItem);
  }

  private Button addCloseButton() {
    RequestInboxService requestInboxService = getApplication().getServices().getRequestInboxService();
    Button button = new Button("Sluiten", event -> requestInboxService.openCurrentInboxItem(null));
    button.setEnabled(requestInboxService.getCurrentInboxItem() != null);
    button.setWidth("100%");
    return button;
  }
}
