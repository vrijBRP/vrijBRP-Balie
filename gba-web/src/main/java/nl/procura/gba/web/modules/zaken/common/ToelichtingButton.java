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

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.theme.GbaWebTheme;

@SuppressWarnings("unchecked")
public class ToelichtingButton extends Button implements ClickListener {

  public ToelichtingButton() {
    this("Voorwaarden");
  }

  public ToelichtingButton(String caption) {
    addStyleName(GbaWebTheme.BUTTON_LINK);
    addStyleName("buttonlink");
    addListener((ClickListener) this);
    setCaption("(" + caption + ")");
  }

  @Override
  public void buttonClick(ClickEvent event) {
  }
}
