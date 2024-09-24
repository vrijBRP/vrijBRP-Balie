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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bijlage;

import com.vaadin.ui.TabSheet.Tab;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bijlage.page1.Page1ZaakBijlage;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class ZaakBijlageLayout extends GbaVerticalLayout implements ZaakTabLayout {

  private Zaak zaak;
  private Tab  tab;

  public ZaakBijlageLayout(Zaak zaak) {

    this.zaak = zaak;

    MainModuleContainer mainModule = new MainModuleContainer();
    addComponent(mainModule);
    mainModule.getNavigation().addPage(new ModuleZaakBijlage());
  }

  public String getHeader(GbaApplication application) {
    int aantal = application.getServices().getDmsService().countDocumentsByZaak(zaak);
    return "Bijlagen (" + aantal + ")";
  }

  @Override
  public void reloadLayout(GbaApplication application, Zaak zaak) {
    this.zaak = zaak;
    tab.setCaption(getHeader(application));
  }

  public void setTab(Tab tab) {
    this.tab = tab;
  }

  public class ModuleZaakBijlage extends ZakenModuleTemplate {

    public ModuleZaakBijlage() {
      setMargin(false);
    }

    @Override
    public void event(PageEvent event) {
      super.event(event);

      if (event.isEvent(InitPage.class)) {
        getPages().getNavigation().goToPage(new Page1ZaakBijlage(zaak) {

          @Override
          public void onReload() {
            reloadLayout(ZaakBijlageLayout.this.getApplication(), zaak);
          }
        });
      }
    }
  }
}
