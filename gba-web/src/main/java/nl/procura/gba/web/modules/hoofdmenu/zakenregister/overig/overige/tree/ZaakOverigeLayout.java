/*
 * Copyright 2024 - 2025 Procura B.V.
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

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabLayout;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class ZaakOverigeLayout extends GbaVerticalLayout implements ZaakTabLayout {

  private final MainModuleContainer mainModule = new MainModuleContainer();
  private Zaak                      zaak;

  public ZaakOverigeLayout(Zaak zaak) {
    this.zaak = zaak;
    updateComponents();
  }

  public String getHeader() {
    return "Overige";
  }

  @Override
  public void reloadLayout(GbaApplication application, Zaak zaak) {
    this.zaak = zaak;
    updateComponents();
  }

  private void updateComponents() {
    removeAllComponents();
    HorizontalLayout layout = new HorizontalLayout();
    layout.setSizeFull();

    Tree table = new Tree(zaak);
    layout.addComponent(table);
    layout.addComponent(mainModule);

    addComponent(layout);
    layout.setExpandRatio(mainModule, 1f);
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
