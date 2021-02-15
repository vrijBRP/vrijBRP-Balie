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

package nl.procura.gba.web.windows.home.navigatie;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.navigation.GbaAccordionTab;
import nl.procura.gba.web.modules.bs.erkenning.ModuleErkenning;
import nl.procura.gba.web.modules.bs.geboorte.ModuleGeboorte;
import nl.procura.gba.web.modules.bs.naamskeuze.ModuleNaamskeuze;

public class AfstammingAccordionTab extends GbaAccordionTab {

  private static final long serialVersionUID = -4996705844277080025L;

  public AfstammingAccordionTab(GbaApplication application) {

    super("Naam & Afstamming", application);

    addLink(ModuleGeboorte.class);
    addLink(ModuleErkenning.class);
    addLink(ModuleNaamskeuze.class);
  }
}
