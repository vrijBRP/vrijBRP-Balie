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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.address;

import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.address.page1.Page1QuickSearch;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class QuickSearchAddressWindow extends GbaModalWindow {

  private final SelectListener selectListener;
  private CheckListener        checkListener;
  private MainModuleContainer  mainModule = null;

  public QuickSearchAddressWindow(SelectListener selectListener, CheckListener checkListener) {
    super("Zoek adressen (Druk op escape om te sluiten)", "800px");
    this.selectListener = selectListener;
    this.checkListener = checkListener;
  }

  @Override
  public void attach() {
    super.attach();

    if (mainModule == null) {
      mainModule = new MainModuleContainer();
      addComponent(mainModule);
      mainModule.getNavigation().addPage(new Module());
    }
  }

  public class Module extends ModuleTemplate {

    public Module() {
      setMargin(false);
    }

    @Override
    public void event(PageEvent event) {
      super.event(event);
      if (event.isEvent(InitPage.class)) {
        getPages().getNavigation().goToPage(new Page1QuickSearch(selectListener, checkListener));
      }
    }
  }
}
