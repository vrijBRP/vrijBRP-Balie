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

package nl.procura.gba.web.windows.persoonslijst.navigatie;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.navigation.GbaAccordionTab;
import nl.procura.gba.web.windows.persoonslijst.layouts.PersoonslijstNavigation;
import nl.procura.vaadin.component.layout.accordion.AccordionLink;
import nl.procura.vaadin.functies.VaadinUtils;

public class PlAccordionTab extends GbaAccordionTab {

  private static final long serialVersionUID = -5503011867426677692L;

  public PlAccordionTab() {
  }

  public PlAccordionTab(String caption, GbaApplication application) {
    super(caption, application);
  }

  @Override
  public void onLinkSelect(AccordionLink link) {

    // Deactiveer alle links.

    PersoonslijstNavigation nav = VaadinUtils.getParent(this, PersoonslijstNavigation.class);

    for (PlAccordionTab tab : VaadinUtils.getChildrenBF(nav, AlgemeenAccordionTab.class)) {

      tab.setLinksInActive();
    }

    super.onLinkSelect(link);
  }
}
