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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.behandelaar;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.behandelaar.page1.Page1ZaakBehandelaar;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class BehandelaarWindow extends GbaModalWindow {

  private final Zaak     zaak;
  private final Runnable update;

  public BehandelaarWindow(Zaak zaak, Runnable update) {
    super("Behandelaar van de zaak", "650px");
    this.zaak = zaak;
    this.update = update;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(true, new Page1ZaakBehandelaar(zaak) {

      @Override
      public void onReload() {
        update.run();
      }
    }));
  }

  @Override
  public void closeWindow() {
    update.run();
    super.closeWindow();
  }
}
