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

package nl.procura.gba.web.windows.persoonslijst;

import com.vaadin.terminal.ExternalResource;

import nl.procura.gba.web.components.layouts.ToolBar;
import nl.procura.gba.web.components.layouts.footer.GbaWebNormalFooterLayout;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.gba.web.windows.persoonslijst.layouts.PersoonslijstContent;
import nl.procura.gba.web.windows.persoonslijst.layouts.PersoonslijstNavigation;
import nl.procura.vaadin.component.window.windowEvents.WindowEvent;
import nl.procura.vaadin.component.window.windowEvents.WindowInit;

public class PersoonslijstWindow extends GbaWindow {

  public static final String NAME             = "pl";
  private static final long  serialVersionUID = 8361742333562056337L;

  public PersoonslijstWindow() {
    super("vrijBRP | Balie", NAME);
  }

  @Override
  public void event(WindowEvent event) {

    if (event.isEvent(WindowInit.class)) {

      getHeaderLayout().setToolBarLayout(new ToolBar());
      getMainLayout().setNavigationLayout(new PersoonslijstNavigation());
      getMainLayout().setContentLayout(new PersoonslijstContent());
      getMainLayout().setFooterLayout(new GbaWebNormalFooterLayout());
    }

    super.event(event);
  }

  /**
   * Switched de gebruiker weer naar het bovenste scherm
   */
  public void resetNavigatie(String fragment) {
    getWindow().open(new ExternalResource(getGbaApplication().getExternalURL("pl#" + fragment)));
    getMainLayout().setNavigationLayout(new PersoonslijstNavigation());
    gotoFragment(fragment);
  }
}
