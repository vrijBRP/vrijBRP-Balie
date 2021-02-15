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

package nl.procura.gba.web.modules.bs.common.pages.naamskeuzepage;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.bs.common.pages.naamskeuzepage.page1.BsNaamskeuzePage1;
import nl.procura.gba.web.modules.bs.common.pages.naamskeuzepage.page2.BsNaamskeuzePage2;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabsheet;

public class BsNaamskeuzeWindow extends GbaModalWindow {

  private final DossierGeboorte dossierGeboorte;

  public BsNaamskeuzeWindow(DossierGeboorte dossierGeboorte) {
    super("Zoek naamskeuzes (Druk op escape om te sluiten)", "70%");
    this.dossierGeboorte = dossierGeboorte;
  }

  @Override
  public void attach() {

    super.attach();

    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(false);
    setContent(layout);

    MainModuleContainer mainModule = new MainModuleContainer();
    addComponent(mainModule);
    mainModule.getNavigation().addPage(new Module());
  }

  public class Module extends ModuleTemplate {

    public Module() {
      setMargin(false);
    }

    @Override
    public void event(PageEvent event) {
      super.event(event);

      if (event.isEvent(InitPage.class)) {
        LazyTabsheet tabs = new LazyTabsheet();
        tabs.addStyleName(GbaWebTheme.TABSHEET_BORDERLESS);
        tabs.addStyleName(GbaWebTheme.TABSHEET_LIGHT);
        tabs.addStyleName(GbaWebTheme.TABSHEET_TOP);

        tabs.addTab(new BsNaamskeuzePage1(dossierGeboorte), "Naamskeuzes van de moeder", null);
        tabs.addTab(new BsNaamskeuzePage2(dossierGeboorte), "Zoeken op aktenummer", null);

        addComponent(tabs);
      }
    }
  }
}
