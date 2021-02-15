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

package nl.procura.gba.web.modules.hoofdmenu.inbox.page1;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.inbox.page2.Page2Inbox;
import nl.procura.gba.web.services.inbox.InboxRecord;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Inbox berichten
 */
public class Page1Inbox extends NormalPageTemplate {

  private final Button buttonRefresh = new Button("Ververs de pagina (F5)");
  private final Button buttonStatus  = new Button("Status wijzigen");
  private Table        table         = null;

  public Page1Inbox() {
    super("Berichten inbox");
    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      getMainbuttons().setWidth("100%");
      getMainbuttons().addComponent(buttonStatus);
      getMainbuttons().addComponent(buttonDel);
      getMainbuttons().addComponent(buttonRefresh);
      getMainbuttons().setExpandRatio(buttonStatus, 1f);
      getMainbuttons().setComponentAlignment(buttonStatus, Alignment.MIDDLE_RIGHT);
      getMainbuttons().setComponentAlignment(buttonDel, Alignment.MIDDLE_RIGHT);
      getMainbuttons().setComponentAlignment(buttonRefresh, Alignment.MIDDLE_RIGHT);

      buttonStatus.addListener(this);
      buttonDel.addListener(this);
      buttonRefresh.addListener(this);

      table = new Table();

      addExpandComponent(table);
    } else if (event.isEvent(AfterReturn.class)) {

      table.init();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (isKeyCode(button, keyCode, KeyCode.F5, buttonRefresh)) {
      table.init();
    }
  }

  public class Table extends Page1InboxTable {

    @Override
    public void onDoubleClick(Record record) {
      getNavigation().goToPage(new Page2Inbox(record.getObject(InboxRecord.class)));
      super.onDoubleClick(record);
    }
  }
}
