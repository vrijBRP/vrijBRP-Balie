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

package nl.procura.gba.web.components.layouts;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.theme.ProcuraWindow;

public class GbaVerticalLayout extends VLayout {

  public GbaVerticalLayout() {
  }

  public GbaVerticalLayout(boolean margin) {
    super(margin);
  }

  @Override
  public GbaApplication getApplication() {
    return (GbaApplication) super.getApplication();
  }

  public ProcuraWindow getParentWindow() {
    return getApplication().getParentWindow((ProcuraWindow) getWindow());
  }
}
