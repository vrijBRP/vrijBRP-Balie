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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.overige.tree;

import com.vaadin.ui.HorizontalLayout;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class ZaakOverigeLayout extends GbaVerticalLayout {

  private final MainModuleContainer mainModule = new MainModuleContainer();

  public ZaakOverigeLayout(Zaak zaak) {

    HorizontalLayout layout = new HorizontalLayout();
    layout.setSizeFull();

    Tree table = new Tree(zaak);
    layout.addComponent(table);
    layout.addComponent(mainModule);

    addComponent(layout);
    layout.setExpandRatio(mainModule, 1f);
  }

  public String getHeader() {
    return "Overige";
  }

  public class Tree extends ZaakOverigeTree {

    public Tree(Zaak zaak) {
      super(zaak);
    }

    @Override
    public void selecteerModule(ZakenModuleTemplate module) {
      mainModule.getNavigation().addPage(module);
    }
  }
}
