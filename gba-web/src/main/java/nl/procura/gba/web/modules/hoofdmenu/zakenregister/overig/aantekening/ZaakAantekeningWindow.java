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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.aantekening;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.components.listeners.ChangeListener;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.aantekening.page1.Page1ZaakAantekening;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class ZaakAantekeningWindow extends GbaModalWindow {

  public ZaakAantekeningWindow(Zaak zaak, ChangeListener listener) {
    super(false, "Aantekeningen bij de zaak (Escape om te sluiten)", "950px");
    addComponent(new MainModuleContainer(true, new Page1ZaakAantekening(zaak, listener)));
  }
}
