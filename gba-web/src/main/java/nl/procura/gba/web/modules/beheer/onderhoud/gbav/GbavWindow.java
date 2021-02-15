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

package nl.procura.gba.web.modules.beheer.onderhoud.gbav;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.beheer.onderhoud.gbav.page1.Page1Gbav;
import nl.procura.gba.web.modules.beheer.onderhoud.gbav.page2.Page2Gbav;
import nl.procura.gba.web.modules.beheer.onderhoud.gbav.page3.Page3Gbav;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.GbaWsRestGbavAccount;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class GbavWindow extends GbaModalWindow {

  private final GbaWsRestGbavAccount account;
  private final GbavWindowListener   listener;

  public GbavWindow(GbaWsRestGbavAccount account, GbavWindowListener listener) {

    super("Instellingen landelijke database (Escape om te sluiten)", "650px");

    this.account = account;
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

        tabSheet.addTab(new Page1Gbav(account, listener), "Overzicht");
        tabSheet.addTab(new Page3Gbav(account, listener), "Nieuw wachtwoord");
        tabSheet.addTab(new Page2Gbav(account, listener), "Huidig wachtwoord");

        addComponent(tabSheet);
      }
    }
  }
}
