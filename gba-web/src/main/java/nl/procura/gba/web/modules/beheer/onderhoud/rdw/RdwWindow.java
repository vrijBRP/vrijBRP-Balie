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

package nl.procura.gba.web.modules.beheer.onderhoud.rdw;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.beheer.onderhoud.rdw.page1.Page1Rdw;
import nl.procura.gba.web.modules.beheer.onderhoud.rdw.page2.Page2Rdw;
import nl.procura.gba.web.modules.beheer.onderhoud.rdw.page3.Page3Rdw;
import nl.procura.gba.web.modules.beheer.onderhoud.rdw.page4.Page4Rdw;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class RdwWindow extends GbaModalWindow {

  private final RdwWindowListener listener;

  public RdwWindow(RdwWindowListener listener) {
    super("Instellingen RDW account (Escape om te sluiten)", "650px");
    this.listener = listener;
  }

  @Override
  public void attach() {

    super.attach();

    MainModuleContainer mainModule = new MainModuleContainer();
    VerticalLayout v = new VerticalLayout();
    v.setMargin(false);
    v.addComponent(mainModule);
    setContent(v);
    mainModule.getNavigation().addPage(new Module());
  }

  public class Module extends ZakenModuleTemplate {

    public Module() {
      setMargin(false);
    }

    @Override
    public void event(PageEvent event) {

      super.event(event);

      if (event.isEvent(InitPage.class)) {

        GbaTabsheet tabSheet = new GbaTabsheet();
        tabSheet.setSizeFull();
        tabSheet.setNoBorderTop();

        tabSheet.addTab(new Page1Rdw(listener), "Overzicht");
        tabSheet.addTab(new Page3Rdw(listener), "Nieuw wachtwoord");
        tabSheet.addTab(new Page2Rdw(listener), "Huidige accountgegevens");
        tabSheet.addTab(new Page4Rdw(listener), "Test verbinding");

        addComponent(tabSheet);
      }
    }
  }
}
