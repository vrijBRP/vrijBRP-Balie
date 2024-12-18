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

package nl.procura.gbaws.web.vaadin.module.ws.page1;

import nl.procura.gbaws.web.vaadin.layouts.DefaultPageLayout;
import nl.procura.gbaws.web.vaadin.module.ws.page2.Page2Ws;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

@SuppressWarnings("serial")
public class Page1Ws extends DefaultPageLayout {

  private Page1WsTable table;

  public Page1Ws() {
    super("Webservices");
    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table = new Page1WsTable() {

        @Override
        public void onClick(Record record) {
          getNavigation().goToPage(new Page2Ws(record.getObject(Webservice.class)));
        }
      };

      addExpandComponent(table);
    }

    super.event(event);
  }
}
