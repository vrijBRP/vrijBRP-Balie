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

import static java.util.Collections.unmodifiableMap;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.event.Action;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

import nl.procura.vaadin.component.listener.FunctionKeylistenerHandler;

public class ButtonKeyHandlerBuilder {

  private final Map<Integer, Button> keyMap = new HashMap<>();

  public void addKey(int keyCode, Button button) {
    keyMap.put(keyCode, button);
  }

  public Action.Handler build(Window window) {
    return new Handler(window, keyMap);
  }

  private static final class Handler extends FunctionKeylistenerHandler {

    private final Window               window;
    private final Map<Integer, Button> keyMap;

    private Handler(Window window, Map<Integer, Button> keyMap) {
      this.window = window;
      this.keyMap = unmodifiableMap(keyMap);
    }

    @Override
    public void handleKey(int keyCode) {
      if (window.getChildWindows().isEmpty() && keyMap.containsKey(keyCode)) {
        keyMap.get(keyCode).click();
      }
      super.handleKey(keyCode);
    }

  }
}
