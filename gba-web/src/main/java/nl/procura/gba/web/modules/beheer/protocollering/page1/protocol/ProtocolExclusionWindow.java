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

package nl.procura.gba.web.modules.beheer.protocollering.page1.protocol;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class ProtocolExclusionWindow extends GbaModalWindow {

  public ProtocolExclusionWindow() {
    super("Gebruikers waarvan gegevens niet worden geprotocolleerd", "400px");
    Page1ProtIgnoreTable ignoreTable = new Page1ProtIgnoreTable();
    InfoLayout ignoreInfo = new InfoLayout("",
        "De volgende gebruikers zijn uitgesloten van protocollering." +
            "<br/>Dit kunnen de inlogpogingen en/of zoekopdrachten zijn.");
    addComponent(new VLayout(ignoreInfo, ignoreTable).margin(true));
  }
}
