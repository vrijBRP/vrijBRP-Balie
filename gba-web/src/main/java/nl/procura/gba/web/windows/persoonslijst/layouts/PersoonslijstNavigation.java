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

package nl.procura.gba.web.windows.persoonslijst.layouts;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.navigation.GbaAccordionTab;
import nl.procura.gba.web.windows.home.layouts.NavigationFooterLayout;
import nl.procura.gba.web.windows.persoonslijst.navigatie.AlgemeenAccordionTab;
import nl.procura.gba.web.windows.persoonslijst.navigatie.PersoonslijstAccordionTab;
import nl.procura.gba.web.windows.persoonslijst.navigatie.ZakenAccordionTab;
import nl.procura.gba.web.windows.persoonslijst.navigatie.ZakenOptiesAccordionTab;

public class PersoonslijstNavigation extends VerticalLayout {

  private static final long serialVersionUID = -2252520020083598396L;

  public PersoonslijstNavigation() {

    PersoonslijstMenu persoonslijstMenu = new PersoonslijstMenu();
    PlAlgemeenTab1 plAlgemeenTab1 = new PlAlgemeenTab1();
    PlAlgemeenTab2 plAlgemeenTab2 = new PlAlgemeenTab2();

    setSizeFull();
    addComponent(persoonslijstMenu);
    addComponent(plAlgemeenTab1);
    addComponent(plAlgemeenTab2);
    addComponent(new NavigationFooterLayout());
    setExpandRatio(plAlgemeenTab2, 1f);
  }

  private class PlAlgemeenTab1 extends GbaAccordionTab {

    public PlAlgemeenTab1() {
    }

    @Override
    public void attach() {

      if (getComponentCount() == 0) {
        addTab(new AlgemeenAccordionTab(getApplication()));
      }

      super.attach();
    }
  }

  private class PlAlgemeenTab2 extends GbaAccordionTab {

    public PlAlgemeenTab2() {
      addStyleName("pl_algemeentab2");
      getAccordeon().setSizeFull();
      setSizeFull();
    }

    @Override
    public void attach() {

      if (getComponentCount() == 0) {
        addTab(new PersoonslijstAccordionTab(getApplication()));
        addTab(new ZakenAccordionTab(getApplication()));
        addTab(new ZakenOptiesAccordionTab(getApplication()));
      }

      super.attach();
    }
  }
}
