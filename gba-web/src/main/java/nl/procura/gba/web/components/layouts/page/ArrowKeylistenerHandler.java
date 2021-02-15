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

package nl.procura.gba.web.components.layouts.page;

import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutAction.KeyCode;

public class ArrowKeylistenerHandler implements Handler {

  @Override
  public Action[] getActions(Object target, Object sender) {

    int[] nrs = new int[]{ KeyCode.ARROW_LEFT, KeyCode.ARROW_UP, KeyCode.ARROW_RIGHT, KeyCode.ARROW_DOWN };

    Action[] list = new Action[nrs.length];
    for (int i = 0; i < nrs.length; i++) {
      list[i] = new ShortcutAction("", nrs[i], null);
    }

    return list;
  }

  @Override
  public void handleAction(Action action, Object sender, Object target) {

    if (action instanceof ShortcutAction) {
      int keyCode = ((ShortcutAction) action).getKeyCode();
      handleKey(keyCode);
    }
  }

  @SuppressWarnings("unused")
  protected void handleKey(int keyCode) {
  } // Override
}
