/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.verkiezing.page4;

import java.util.List;

import nl.procura.gba.jpa.personen.db.KiesrWijz;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class Page4VerkiezingWindow extends GbaModalWindow {

  private final List<KiesrWijz> wijzigingen;

  public Page4VerkiezingWindow(List<KiesrWijz> wijzigingen) {
    super("Uitgevoerde handelingen (Escape om te sluiten)", "900px");
    this.wijzigingen = wijzigingen;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page4Verkiezing(wijzigingen)));
  }
}
