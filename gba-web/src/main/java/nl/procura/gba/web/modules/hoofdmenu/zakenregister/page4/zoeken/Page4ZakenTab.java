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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken;

import com.vaadin.ui.TabSheet;

import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.page.GbaPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.dashboard.page1.Page1Dashboard;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.selecties.Page4SelectiesZaken1;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabsheet;

public class Page4ZakenTab extends GbaPageTemplate {

  public Page4ZakenTab() {
    addStyleName("zakenregister-zoektab");
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      LazyTabsheet tabs = new LazyTabsheet();
      tabs.addStyleName(GbaWebTheme.TABSHEET_BORDERLESS);
      tabs.addStyleName(GbaWebTheme.TABSHEET_LIGHT);
      tabs.addStyleName(GbaWebTheme.TABSHEET_TOP);

      TabSheet.Tab tab1 = tabs.addTab(ModuleZaken.class, "Zoeken", null);

      TabSheet.Tab tab2 = null;
      if (getApplication().isProfielActie(ProfielActie.SELECT_SELECTIES)) {
        tab2 = tabs.addTab(ModuleSelectiesZaken.class, "Selecties", null);
      }

      TabSheet.Tab tab3 = tabs.addTab(ModuleDashboard.class, "Dashboard", null);

      String tab = String.valueOf(getApplication().getParameterMap().get("tab"));
      if ("zoeken".equalsIgnoreCase(tab)) {
        tabs.setSelectedTab(tab1);
      }
      if ("selecties".equalsIgnoreCase(tab) && tab2 != null) {
        tabs.setSelectedTab(tab2);
      }
      if ("dashboard".equalsIgnoreCase(tab)) {
        tabs.setSelectedTab(tab3);
      }

      addExpandComponent(tabs);
    }

    super.event(event);
  }

  public static class ModuleDashboard extends ModuleTemplate {

    @Override
    public void event(PageEvent event) {
      super.event(event);

      if (event.isEvent(InitPage.class)) {
        getPages().getNavigation().goToPage(Page1Dashboard.class);
      }
    }
  }

  public static class ModuleSelectiesZaken extends ModuleTemplate {

    @Override
    public void event(PageEvent event) {
      super.event(event);

      if (event.isEvent(InitPage.class)) {
        getPages().getNavigation().goToPage(Page4SelectiesZaken1.class);
      }
    }
  }

  public static class ModuleZaken extends ModuleTemplate {

    @Override
    public void event(PageEvent event) {
      super.event(event);

      if (event.isEvent(InitPage.class)) {
        getPages().getNavigation().goToPage(Page4Zaken.class);
      }
    }
  }
}
