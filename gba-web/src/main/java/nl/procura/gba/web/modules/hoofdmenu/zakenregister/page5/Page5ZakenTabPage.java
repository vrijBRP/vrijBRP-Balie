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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page5;

import nl.procura.gba.web.components.layouts.page.GbaPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page5.tab1.Page5ZakenTab1;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page5.tab2.Page5ZakenTab2;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabsheet;

public class Page5ZakenTabPage extends GbaPageTemplate {

  Page5ZakenTabPage() {
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      LazyTabsheet tabs = new LazyTabsheet();
      tabs.addStyleName(GbaWebTheme.TABSHEET_BORDERLESS);
      tabs.addStyleName(GbaWebTheme.TABSHEET_LIGHT);
      tabs.addStyleName(GbaWebTheme.TABSHEET_TOP);

      tabs.addTab(new Page5ZakenTab2(), "Zoek op actuele status", null);
      tabs.addTab(new Page5ZakenTab1(), "Zoek op periode / status", null);
      addExpandComponent(tabs);
    }

    super.event(event);
  }
}
