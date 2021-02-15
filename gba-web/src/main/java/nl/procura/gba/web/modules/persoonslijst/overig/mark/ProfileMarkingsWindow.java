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

import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.components.listeners.ChangeListener;
import nl.procura.gba.web.modules.persoonslijst.overig.mark.page2.Page2Marking1;
import nl.procura.gba.web.modules.persoonslijst.overig.mark.page2.Page2Marking2;

public class ProfileMarkingsWindow extends GbaModalWindow {

  private ChangeListener changeListener;

  public ProfileMarkingsWindow(ChangeListener changeListener) {
    super("Markeren voor risicoanalyses (Escape om te sluiten)", "750px");
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

    GbaTabsheet tabsheet = new GbaTabsheet();
    tabsheet.addStyleName("zoektab");
    tabsheet.setNoBorderTop();
    tabsheet.setSizeFull();

    tabsheet.addTab(new Page2Marking1(), "Personen");
    tabsheet.addTab(new Page2Marking2(), "Adressen");

    addComponent(tabsheet);
  }
}
