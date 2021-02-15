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

package nl.procura.gba.web.modules.bs.common.pages.zoekpage;

import java.util.List;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class BsZoekWindow extends GbaModalWindow {

  private final List<BsZoekTab> extraTabs;
  private final DossierPersoon  dossierPersoon;

  public BsZoekWindow(DossierPersoon dossierPersoon, List<BsZoekTab> extraTabs) {

    super("Zoek persoon (Druk op escape om te sluiten)", "80%");

    this.extraTabs = extraTabs;
    this.dossierPersoon = dossierPersoon;
  }

  @Override
  public void attach() {

    super.attach();

    MainModuleContainer mainModule = new MainModuleContainer();

    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(false);
    layout.addComponent(mainModule);
    setContent(layout);

    mainModule.getNavigation().addPage(new Module());
  }

  public class Module extends ModuleTemplate {

    public Module() {
    }

    @Override
    public void event(PageEvent event) {

      super.event(event);

      if (event.isEvent(InitPage.class)) {

        getPages().getNavigation().goToPage(new BsZoekTabPage(dossierPersoon, extraTabs));
      }
    }
  }
}
