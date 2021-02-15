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

package nl.procura.gba.web.modules.bs.common.pages.persooncontrole;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.bs.common.pages.persooncontrole.page1.BsPersoonControlePage1.SyncRecord;
import nl.procura.gba.web.modules.bs.common.pages.persooncontrole.page2.BsPersoonControlePage2;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class BsPersoonControleCompareWindow extends GbaModalWindow {

  private final SyncRecord syncRecord;

  public BsPersoonControleCompareWindow(SyncRecord syncRecord) {
    super("Personen van het dossier (Druk op escape om te sluiten)", "900px");
    setHeight("80%");
    this.syncRecord = syncRecord;
  }

  @Override
  public void attach() {
    super.attach();
    BsPersoonControlePage2 page = new BsPersoonControlePage2(syncRecord);
    MainModuleContainer moduleContainer = new MainModuleContainer(false, page);
    addComponent(moduleContainer);
  }
}
