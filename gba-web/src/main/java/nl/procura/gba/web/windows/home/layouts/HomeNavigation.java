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

package nl.procura.gba.web.windows.home.layouts;

import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.navigation.GbaAccordionTab;
import nl.procura.gba.web.windows.home.navigatie.*;

public class HomeNavigation extends VerticalLayout {

  private static final long serialVersionUID = 466549840186933327L;

  public HomeNavigation() {

    setSizeFull();
    HomeMenu homeMenu = new HomeMenu();
    HomeAlgemeenTab homeAlgemeenTab = new HomeAlgemeenTab();

    addComponent(homeMenu);
    addComponent(homeAlgemeenTab);
    addComponent(new NavigationFooterLayout());
    setExpandRatio(homeAlgemeenTab, 1f);
  }

  private class HomeAlgemeenTab extends GbaAccordionTab {

    public HomeAlgemeenTab() {
      getAccordeon().setSizeFull();
      setSizeFull();
    }

    @Override
    public void attach() {

      if (getComponentCount() == 0) {

        final ZakenregisterAccordionTab zakenTab = new ZakenregisterAccordionTab(getApplication());

        addTab(new HoofdAccordionTab(getApplication()));
        addTab(zakenTab);
        addTab(new AfstammingAccordionTab(getApplication()));
        addTab(new OverlijdenAccordionTab(getApplication()));
        addTab(new HuwelijkAccordionTab(getApplication()));
        addTab(new OverigeAccordionTab(getApplication()));

        getAccordeon().addListener((SelectedTabChangeListener) event -> {
          if (zakenTab == event.getTabSheet().getSelectedTab()) {
            zakenTab.resetTable();
          }
        });
      }

      super.attach();
    }
  }
}
