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

package nl.procura.gba.web.windows.locatie.layouts;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.windows.locatie.navigation.LocatieAccordionTab;

public class LocatieNavigation extends VerticalLayout {

  private static final long   serialVersionUID = 2996726355281867456L;
  private LocatieAccordionTab mainAccordeon    = new LocatieAccordionTab();

  public LocatieNavigation() {

    addComponent(getMainAccordeon());
  }

  public LocatieAccordionTab getMainAccordeon() {
    return mainAccordeon;
  }

  public void setMainAccordeon(LocatieAccordionTab mainAccordeon) {
    this.mainAccordeon = mainAccordeon;
  }
}
