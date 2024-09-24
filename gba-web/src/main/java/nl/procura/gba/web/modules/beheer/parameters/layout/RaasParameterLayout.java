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

package nl.procura.gba.web.modules.beheer.parameters.layout;

import com.vaadin.ui.Button;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.beheer.parameters.layout.raas.RaasTestDialog;

public class RaasParameterLayout extends DatabaseParameterLayout {

  private final Button buttonTest = new Button("Test RAAS-verbinding");

  public RaasParameterLayout(GbaApplication application, String naam, String category) {
    super(application, naam, category);
    addButton(buttonTest);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonTest) {
      getWindow().addWindow(new RaasTestDialog() {
      });
    }

    super.handleEvent(button, keyCode);
  }
}
