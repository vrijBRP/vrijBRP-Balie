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

package nl.procura.gba.web.windows.home;

import com.vaadin.ui.UriFragmentUtility.FragmentChangedEvent;

import nl.procura.gba.web.components.layouts.toolbar.ToolBar;
import nl.procura.gba.web.components.layouts.footer.GbaWebNormalFooterLayout;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.gba.web.windows.home.layouts.HomeContent;
import nl.procura.gba.web.windows.home.layouts.HomeNavigation;
import nl.procura.gba.web.windows.home.navigatie.HoofdAccordionTab;
import nl.procura.gba.web.windows.home.navigatie.ZakenregisterAccordionTab;
import nl.procura.vaadin.component.window.windowEvents.WindowEvent;
import nl.procura.vaadin.component.window.windowEvents.WindowInit;
import nl.procura.vaadin.functies.VaadinUtils;

public class HomeWindow extends GbaWindow {

  public static final String  NAME   = "home";
  private static final String ZAKEN  = "zaken";
  private static final String RESET  = "reset";
  private static final String ZOEKEN = "zoeken";

  public HomeWindow() {
    super("vrijBRP | Balie", NAME);
  }

  @Override
  public void event(WindowEvent event) {

    if (event.isEvent(WindowInit.class)) {

      getHeaderLayout().setToolBarLayout(new ToolBar());
      getMainLayout().setNavigationLayout(new HomeNavigation());
      getMainLayout().setContentLayout(new HomeContent());
      getMainLayout().setFooterLayout(new GbaWebNormalFooterLayout());
    }

    super.event(event);
  }

  @Override
  public void fragmentChanged(FragmentChangedEvent source) {

    String fragment = source.getUriFragmentUtility().getFragment();

    if (RESET.equals(fragment)) {
      gotoFragment(ZOEKEN);
      VaadinUtils.getChild(getMainLayout().getNavigationLayout(), HoofdAccordionTab.class).setActive(true);
    }

    if (ZAKEN.equals(fragment)) {
      VaadinUtils.getChild(this, ZakenregisterAccordionTab.class).setActive(true);
      VaadinUtils.getChild(this, ZakenregisterAccordionTab.class).reloadTree();
    }

    super.fragmentChanged(source);
  }
}
