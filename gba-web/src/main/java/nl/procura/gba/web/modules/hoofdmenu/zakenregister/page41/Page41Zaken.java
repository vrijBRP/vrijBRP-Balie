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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page41;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterPrintPage;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;

/**
 * Afdrukken aanvraag
 */
public class Page41Zaken extends ZakenregisterPrintPage {

  public Page41Zaken(VogAanvraag aanvraag) {
    super("Zakenregister - verklaring omtrent gedrag - afdrukken", aanvraag, DocumentType.COVOG_AANVRAAG);
  }

  @Override
  public void setButtons() {

    addButton(buttonPrev);
    addButton(getPrintButtons());

    super.setButtons();
  }
}
