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

package nl.procura.gbaws.web.vaadin.module.sources;

import nl.procura.gbaws.web.vaadin.layouts.GbaWsTabSheet;
import nl.procura.gbaws.web.vaadin.module.ModuleTemplate;
import nl.procura.gbaws.web.vaadin.module.sources.gbav.ModuleDbGbav;
import nl.procura.gbaws.web.vaadin.module.sources.procura.ModuleDbProcura;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class ModuleDb extends ModuleTemplate {

  public ModuleDb() {
  }

  @Override
  public void event(PageEvent event) {

    super.event(event);

    if (event.isEvent(InitPage.class)) {

      setMargin(true);
      setSpacing(true);

      addComponent(new H2("Bronnen"));

      GbaWsTabSheet tabs = new GbaWsTabSheet();

      tabs.addTab(ModuleDbProcura.class, "Procura database", null);
      tabs.addTab(ModuleDbGbav.class, "GBA-V", null);

      addExpandComponent(tabs);
    }
  }
}
