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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab3;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class Tab3DocumentWindow extends GbaModalWindow {

  public Tab3DocumentWindow() {
    super("Afnemers", "80%");
  }

  @Override
  public void attach() {

    super.attach();

    VerticalLayout layout = new VerticalLayout();

    MainModuleContainer mainModule = new MainModuleContainer();
    layout.addComponent(mainModule);
    layout.setMargin(false);
    mainModule.getNavigation().addPage(new Tab3DocumentenPage1());
    setContent(layout);
  }
}
