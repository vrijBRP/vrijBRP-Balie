/*
 * Copyright 2021 - 2022 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.inbox.page2;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.inbox.InboxRecord;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Inbox berichten
 */
public class Page2Inbox extends NormalPageTemplate {

  private final InboxRecord inboxRecord;
  private Page2InboxForm1   form1  = null;
  private Table             table1 = null;

  public Page2Inbox(InboxRecord inboxRecord) {

    super("Berichten inbox");
    this.inboxRecord = inboxRecord;
    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addButton(buttonPrev);
      form1 = new Page2InboxForm1(inboxRecord);
      table1 = new Table();
      addComponent(form1);
      addComponent(new Fieldset("Gerelateerde berichten", table1));
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  public class Table extends Page2InboxTable {

    @Override
    public void onDoubleClick(Record record) {
      getNavigation().goToPage(new Page2Inbox(record.getObject(InboxRecord.class)));
      super.onDoubleClick(record);
    }
  }
}
