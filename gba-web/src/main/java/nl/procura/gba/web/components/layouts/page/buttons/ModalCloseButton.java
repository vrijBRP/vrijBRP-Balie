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

package nl.procura.gba.web.components.layouts.page.buttons;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;

public class ModalCloseButton extends Button implements ClickListener {

  public ModalCloseButton() {
    super("Sluiten (Esc)");
    setStyleName("primary");
    focus();
    setWidth("130px");
    addListener((ClickListener) this);
  }

  @Override
  public void buttonClick(ClickEvent event) {
    closeWindow();
  }

  public void closeWindow() {
    ((GbaModalWindow) getWindow()).closeWindow();
  }
}
