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

package nl.procura.gba.web.windows.home.modules;

import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.vaadin.component.layout.page.PageContainer;
import nl.procura.vaadin.component.layout.page.PageLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class MainModuleContainer extends PageContainer {

  private static final long serialVersionUID = 2739864808372441679L;

  public MainModuleContainer() {
  }

  public MainModuleContainer(final boolean margin, final PageLayout page) {

    ModuleTemplate moduleTemplate = new ModuleTemplate() {

      @Override
      public void event(PageEvent event) {

        setMargin(margin);

        super.event(event);

        if (event.isEvent(InitPage.class)) {

          getPages().getNavigation().goToPage(page);
        }
      }
    };

    getNavigation().addPage(moduleTemplate);
  }
}
