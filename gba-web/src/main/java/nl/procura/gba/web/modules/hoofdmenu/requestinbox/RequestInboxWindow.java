/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class RequestInboxWindow extends GbaModalWindow {

  private final ModuleRequestInboxParams params;

  public RequestInboxWindow(ModuleRequestInboxParams params) {
    super("Verzoeken (Escape om te sluiten)", "1200px");
    this.params = params;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new ModuleRequestInbox(params)));
  }

  @Override
  public void close() {
    params.getUpdateListener().run();
    super.close();
  }
}
