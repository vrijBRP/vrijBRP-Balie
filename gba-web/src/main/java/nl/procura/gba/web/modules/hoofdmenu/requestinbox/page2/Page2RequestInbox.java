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

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.ModuleRequestInboxParams;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.page1.Page1RequestInbox;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2RequestInbox extends NormalPageTemplate {

  private final RequestInboxItem inboxItem;

  public Page2RequestInbox(String title, RequestInboxItem inboxItem) {
    super(title);
    this.inboxItem = inboxItem;
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      addButton(buttonPrev);
      if (isFirstPage()) {
        buttonPrev.setCaption("Toon alle verzoeken (F1)");
      }
      addComponent(new Page2RequestInboxLayout(inboxItem));
    }

    super.event(event);
  }

  private boolean isFirstPage() {
    return getNavigation().getPage(Page1RequestInbox.class) == null;
  }

  @Override
  public void onPreviousPage() {
    if (isFirstPage()) {
      getNavigation().goToPage(new Page1RequestInbox(ModuleRequestInboxParams.builder()
          .bsn(inboxItem.getBsn())
          .build()));
    } else {
      getNavigation().goBackToPreviousPage();
    }
  }
}
