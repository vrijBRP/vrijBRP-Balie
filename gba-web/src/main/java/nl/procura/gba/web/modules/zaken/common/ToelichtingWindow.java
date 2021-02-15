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

package nl.procura.gba.web.modules.zaken.common;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class ToelichtingWindow extends GbaModalWindow {

  public ToelichtingWindow(String caption, String voorwaarde) {
    this("Voorwaarden", caption, voorwaarde);
  }

  public ToelichtingWindow(String title, String caption, String voorwaarde) {
    super(title + " (Druk op escape om te sluiten)", "600px");
    GbaVerticalLayout v = new GbaVerticalLayout(true);
    v.addComponent(new Fieldset(caption));
    v.addComponent(new InfoLayout("", voorwaarde));
    addComponent(v);
  }

  @Override
  public void attach() {
    this.focus();
    super.attach();
  }
}
