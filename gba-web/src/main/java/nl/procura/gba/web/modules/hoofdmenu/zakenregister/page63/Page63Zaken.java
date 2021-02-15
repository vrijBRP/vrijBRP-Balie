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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page63;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterPage;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.result.PresentatievraagResultLayout;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagMatch;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;

/**
 * Tonen presentievraag
 */
public class Page63Zaken extends ZakenregisterPage<VerhuisAanvraag> {

  public Page63Zaken(VerhuisAanvraag aanvraag, Presentievraag presentievraag, PresentievraagMatch match) {

    super(aanvraag, "Presentievraag - gevonden persoon");

    addButton(buttonPrev);

    addExpandComponent(new PresentatievraagResultLayout(presentievraag, match));
  }
}
