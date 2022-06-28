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
import nl.procura.gba.web.modules.zaken.protocol.ModuleProtocol;
import nl.procura.gba.web.modules.zaken.sms.ModuleSms;
import nl.procura.gba.web.modules.zaken.verkiezing.ModuleVerkiezing;
import nl.procura.gba.web.modules.zaken.woningkaart.ModuleWoningkaart;

public class ZakenOptiesAccordionTab extends PlAccordionTab {

  private static final long serialVersionUID = -6379541530039690072L;

  public ZakenOptiesAccordionTab(GbaApplication application) {
    super("Opties", application);

    addLink(ModuleProtocol.class);
    addLink(ModuleWoningkaart.class);
    addLink(ModuleSms.class);
    addLink(ModuleVerkiezing.class);
  }
}
