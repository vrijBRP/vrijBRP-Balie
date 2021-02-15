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

package nl.procura.gba.web.modules.zaken.verhuizing.page26;

import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.result.PresentatievraagResultLayout;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagMatch;

public class Page26Verhuizing extends ZakenPage {

  public Page26Verhuizing(Presentievraag presentievraag, PresentievraagMatch match) {
    super("Presentievraag - gevonden persoon");
    addButton(buttonPrev);
    addExpandComponent(new PresentatievraagResultLayout(presentievraag, match));
  }
}
