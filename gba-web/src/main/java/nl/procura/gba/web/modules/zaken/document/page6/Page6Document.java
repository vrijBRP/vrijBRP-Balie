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

package nl.procura.gba.web.modules.zaken.document.page6;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.zaken.common.ZakenOverzichtPage;
import nl.procura.gba.web.modules.zaken.document.overzicht.DocumentOverzichtBuilder;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;

/**
 * Tonen uittrekselaanvraag
 */

public class Page6Document extends ZakenOverzichtPage<DocumentZaak> {

  public Page6Document(DocumentZaak aanvraag) {

    super(aanvraag, "Documenten - ingediende aanvraag");

    addButton(buttonPrev);
  }

  @Override
  protected void addTabs(ZaakTabsheet<DocumentZaak> tabsheet) {
    getTabsheet().setBorderTop();
    DocumentOverzichtBuilder.addTab(tabsheet, getZaak());
  }
}
