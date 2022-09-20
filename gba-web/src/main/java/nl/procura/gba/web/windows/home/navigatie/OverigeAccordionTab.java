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
import nl.procura.gba.web.modules.bs.onderzoek.ModuleOnderzoek;
import nl.procura.gba.web.modules.bs.registration.ModuleRegistration;
import nl.procura.gba.web.modules.bs.riskanalysis.ModuleRiskAnalysis;
import nl.procura.gba.web.modules.hoofdmenu.gv.ModuleGv;
import nl.procura.gba.web.modules.hoofdmenu.klapper.ModuleKlapper;
import nl.procura.gba.web.modules.hoofdmenu.pv.ModulePresentievraag;
import nl.procura.gba.web.modules.hoofdmenu.raas.ModuleRaas;
import nl.procura.gba.web.modules.hoofdmenu.sms.ModuleSms;

public class OverigeAccordionTab extends GbaAccordionTab {

  public OverigeAccordionTab(GbaApplication application) {
    super("Overige", application);

    addLink(ModuleGv.class);
    addLink(ModuleKlapper.class);
    addLink(ModuleOnderzoek.class);
    addLink(ModuleRiskAnalysis.class);
    addLink(ModuleRegistration.class);
    addLink(ModulePresentievraag.class);
    addLink(ModuleSms.class);
    addLink(ModuleRaas.class);
  }
}
