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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox.page2;

import static java.util.Collections.singletonList;
import static nl.procura.burgerzaken.requestinbox.api.model.InboxItemStatus.RECEIVED;
import static nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource.STANDAARD;

import java.util.Map;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.RequestInboxDocumentsButton;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.RequestInboxHandleNowButton;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.RequestInboxHandlerButton;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.RequestInboxLayoutForm;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.RequestInboxLayoutTable;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.RequestInboxStatusButton;
import nl.procura.gba.web.modules.zaken.inbox.overzicht.InboxOverzichtCaseLayout;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBodyProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxProcessors;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSResult;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.window.Message;
import nl.procura.vaadin.functies.VaadinUtils;

import lombok.Getter;

public class Page2RequestInboxLayout extends GbaVerticalLayout {

  private final Button buttonDocuments = new RequestInboxDocumentsButton(this::getDocuments);
  private final Button buttonProcess   = new Button("Verzoek verwerken");
  private final Button buttonStatus    = new RequestInboxStatusButton(() -> singletonList(getInboxItem()),
      this::setHandler);
  private final Button buttonHandle    = new RequestInboxHandleNowButton(this::getInboxItem, this::setHandler);
  private final Button buttonHandler   = new RequestInboxHandlerButton(this::getInboxItem, this::setHandler);

  @Getter
  private final RequestInboxItem inboxItem;

  public Page2RequestInboxLayout(RequestInboxItem inboxItem) {
    this.inboxItem = inboxItem;
    this.buttonDocuments.setWidth("100%");
    setSizeFull();
    setSpacing(true);
  }

  @Override
  public void attach() {
    buildLayout();
    super.attach();
  }

  private void setHandler() {
    removeAllComponents();
    buildLayout();
    new Message(getApplication().getParentWindow(), "De behandelaar is gewijzigd", Message.TYPE_SUCCESS);
  }

  private void buildLayout() {
    if (components.isEmpty()) {
      buttonDocuments.setCaption("Bijlagen (" + inboxItem.getDocumentsCount() + ")");

      OptieLayout optieLayout = new OptieLayout();
      optieLayout.getLeft().addComponent(new RequestInboxLayoutForm(inboxItem));

      if (inboxItem.getInboxItem() != null) {
        optieLayout.getLeft().addComponent(new Page2RequestInboxTable(inboxItem.getInboxItem()));
      }

      optieLayout.getRight().setCaption("Opties");
      optieLayout.getRight().setWidth("180px");
      optieLayout.getRight().addButton(buttonDocuments);
      optieLayout.getRight().addButton(buttonStatus);

      if (!inboxItem.getType().isUnknown()) {
        if (inboxItem.getType().isAutomaticProcessing()) {
          optieLayout.getRight().addButton(buttonProcess);
          buttonProcess.setEnabled(inboxItem.getStatus() == RECEIVED);
          optieLayout.getRight().addButton(buttonProcess, event -> {
            RequestInboxProcessors.get(inboxItem, getApplication().getServices()).process();
            new Message(getApplication().getParentWindow(), "Verzoek verwerkt", Message.TYPE_SUCCESS);
            setHandler();
          });
        } else {
          optieLayout.getRight().addButton(buttonHandle);
          optieLayout.getRight().addButton(buttonHandler);
          buttonHandle.setEnabled(inboxItem.getStatus() == RECEIVED);
          buttonHandler.setEnabled(inboxItem.getStatus() == RECEIVED);
        }
      }

      addComponent(optieLayout);

      // Process body
      RequestInboxBodyProcessor processor = RequestInboxProcessors.get(inboxItem, getApplication().getServices());
      Map<String, Object> data = processor.getBody().getData();
      VLayout layout = new VLayout();
      layout.add(new Fieldset(inboxItem.getDescription()));
      layout.addComponent(InboxOverzichtCaseLayout.builder()
          .numberOfColumns(2)
          .idListener(id -> getApplication().goToPl(getApplication().getParentWindow(),
              "#pl", STANDAARD, id))
          .data(data)
          .build()
          .getLayout());
      optieLayout.getLeft().addComponent(layout);

      optieLayout.getLeft().addComponent(new Fieldset("Gerelateerde verzoeken"));
      optieLayout.getLeft().addComponent(new RequestInboxLayoutTable(inboxItem) {

        @Override
        public void onDoubleClick(Record record) {
          NormalPageTemplate pageTemplate = VaadinUtils.getParent(Page2RequestInboxLayout.this,
              NormalPageTemplate.class);
          RequestInboxItem requestInboxItem = record.getObject(RequestInboxItem.class);
          pageTemplate.getNavigation().addPage(new Page2RequestInbox(pageTemplate.getTitle(), requestInboxItem));
        }
      });
    }
  }

  private DMSResult getDocuments() {
    return getApplication().getServices().getRequestInboxService().getDocuments(inboxItem);
  }
}
