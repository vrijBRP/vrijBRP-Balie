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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4;

import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4c.Page4cRijbewijsTable1;
import nl.procura.rdw.messages.P0252;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;

class Page4RijbewijsSubpage2 extends VerticalLayout {

  Page4RijbewijsSubpage2(Page4Rijbewijs page4Rijbewijs, P0252 p0252) {

    setSpacing(true);

    InfoLayout infoLayout = new InfoLayout("Ter informatie", "Klik op de regel voor meer informatie.");
    addComponent(new Fieldset("Uitgegeven rijbewijzen", infoLayout));
    addComponent(new Page4cRijbewijsTable1(page4Rijbewijs, p0252));
  }
}
