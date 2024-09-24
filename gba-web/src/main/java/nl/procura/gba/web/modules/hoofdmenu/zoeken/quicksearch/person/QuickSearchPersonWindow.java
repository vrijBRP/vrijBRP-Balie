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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page1.Page1QuickSearch;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.page3.Page3QuickSearch;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.validation.Anummer;

public class QuickSearchPersonWindow extends GbaModalWindow {

  private final QuickSearchPersonConfig config;

  public QuickSearchPersonWindow(SelectListener selectListener) {
    this(QuickSearchPersonConfig.builder().selectListener(selectListener).build());
  }

  public QuickSearchPersonWindow(Anummer anummer, SelectListener selectListener) {
    this(QuickSearchPersonConfig.builder()
        .selectListener(selectListener)
        .anummer(anummer)
        .build());
  }

  public QuickSearchPersonWindow(QuickSearchPersonConfig config) {
    super("Zoek personen (Druk op escape om te sluiten)", "700px");
    this.config = config;
  }

  @Override
  public void attach() {
    super.attach();

    if (config.hasBsnOrAnr()) {
      GbaTabsheet tabSheet = new GbaTabsheet();
      tabSheet.setSizeFull();
      tabSheet.setNoBorderTop();
      tabSheet.addTab(new Module(), "Zoeken");
      tabSheet.addTab(new Page3QuickSearch(config), "Gerelateerden");
      config.getPages().forEach((key, value) -> tabSheet.addTab(value, key));
      setContent(tabSheet);

    } else {
      MainModuleContainer mainModule = new MainModuleContainer();
      VerticalLayout v = new VerticalLayout();
      v.setMargin(false);
      v.addComponent(mainModule);
      setContent(v);
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
        getPages().getNavigation().goToPage(new Page1QuickSearch(config.getSelectListener()));
      }
    }
  }
}
