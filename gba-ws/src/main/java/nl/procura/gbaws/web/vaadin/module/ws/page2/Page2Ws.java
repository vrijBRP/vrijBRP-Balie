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

package nl.procura.gbaws.web.vaadin.module.ws.page2;

import nl.procura.gbaws.web.vaadin.layouts.DefaultPageLayout;
import nl.procura.gbaws.web.vaadin.module.ws.page1.Page1WsTable.Webservice;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

@SuppressWarnings("serial")
public class Page2Ws extends DefaultPageLayout {

  private final Webservice ws;
  private Page2WsForm      form = null;

  public Page2Ws(Webservice ws) {
    this.ws = ws;

    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);

      form = new Page2WsForm(ws);

      addComponent(form);

      addComponent(new Fieldset("Foutmeldingen"));
      addExpandComponent(new Page2WsTable(ws));
    }

    super.event(event);
  }
}
