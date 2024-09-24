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

package nl.procura.gba.web.windows.verwijzing;

import nl.procura.gba.web.components.layouts.toolbar.ToolBar;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.gba.web.windows.verwijzing.layouts.VerwijzingContent;
import nl.procura.gba.web.windows.verwijzing.layouts.VerwijzingNavigation;
import nl.procura.vaadin.component.window.windowEvents.WindowEvent;
import nl.procura.vaadin.component.window.windowEvents.WindowInit;

public class VerwijzingWindow extends GbaWindow {

  public static final String NAME = "verwijzing";

  public VerwijzingWindow() {
    super("Proweb Personen: Verwijzing", "verwijzing");
  }

  @Override
  public void event(WindowEvent event) {

    if (event.isEvent(WindowInit.class)) {

      getHeaderLayout().setToolBarLayout(new ToolBar());
      getMainLayout().setNavigationLayout(new VerwijzingNavigation());
      getMainLayout().setContentLayout(new VerwijzingContent());
    }

    super.event(event);
  }
}
