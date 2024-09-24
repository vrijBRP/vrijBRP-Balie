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

package nl.procura.gba.web.modules.persoonslijst.overig.mark;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.components.listeners.ChangeListener;
import nl.procura.gba.web.modules.persoonslijst.overig.mark.page1.Page1Marking;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class ProfileMarkWindow extends GbaModalWindow {

  private final BasePLExt      pl;
  private final ChangeListener changeListener;

  public ProfileMarkWindow(BasePLExt pl, ChangeListener changeListener) {
    super("Markeren voor risicoanalyses (Escape om te sluiten)", "850px");
    this.pl = pl;
    this.changeListener = changeListener;
  }

  @Override
  protected void close() {
    changeListener.onChange();
    super.close();
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page1Marking(pl)));
  }
}
