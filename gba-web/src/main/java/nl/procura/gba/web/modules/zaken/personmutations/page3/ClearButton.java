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

package nl.procura.gba.web.modules.zaken.personmutations.page3;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.NativeButton;

import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElem;
import nl.procura.gba.web.theme.GbaWebTheme;

public class ClearButton extends NativeButton {

  public ClearButton(PersonListMutElem mutElem) {
    setStyleName(GbaWebTheme.BUTTON_LINK);
    addStyleName("pl-url-button");
    setIcon(new ThemeResource("../gba-web/buttons/img/pl-delete.png"));
    setDescription("Maak veld leeg");

    addListener((ClickListener) event -> {
      mutElem.getField().setValue(null);
      mutElem.getField().focus();
    });
  }
}
