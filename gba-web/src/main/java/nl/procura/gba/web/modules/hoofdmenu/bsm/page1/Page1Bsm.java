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

package nl.procura.gba.web.modules.hoofdmenu.bsm.page1;

import java.text.MessageFormat;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Taakplanner (BSM)
 */
public class Page1Bsm extends NormalPageTemplate {

  private final Button  buttonRefresh = new Button("Ververs de pagina (F5)");
  private final Button  buttonBsm     = new Button("Naar de Taakplanner (F2)");
  private Page1BsmTable table         = null;

  public Page1Bsm() {
    super("Taakplanner / BSM");
    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      if (!getServices().getBsmService().isBsmEnabled()) {
        setInfo("<hr/>De taakplanner is niet ingeschakeld.");
      }

      getMainbuttons().setWidth("100%");
      getMainbuttons().addComponent(buttonRefresh);
      getMainbuttons().setExpandRatio(buttonRefresh, 1f);
      getMainbuttons().setComponentAlignment(buttonRefresh, Alignment.MIDDLE_RIGHT);
      buttonRefresh.addListener(this);

      if (getServices().getGebruiker().isAdministrator()) {
        getMainbuttons().addComponent(buttonBsm);
        getMainbuttons().setComponentAlignment(buttonBsm, Alignment.MIDDLE_RIGHT);
        buttonBsm.addListener(this);
      }

      table = new Page1BsmTable();
      addExpandComponent(table);

    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (isKeyCode(button, keyCode, KeyCode.F2, buttonBsm)) {
      String url = getServices().getBsmService().getBsmExternalUrl();
      String ticket = getServices().getBsmService().getBsmTicket();
      String vUrl = MessageFormat.format("{0}?ticket={1}", url, ticket);
      getApplication().getParentWindow().open(new ExternalResource(vUrl), "BSM", false);
    }

    if (isKeyCode(button, keyCode, KeyCode.F5, buttonRefresh)) {
      table.init();
    }
  }
}
