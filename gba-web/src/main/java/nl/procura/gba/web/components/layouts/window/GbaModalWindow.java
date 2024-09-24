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

package nl.procura.gba.web.components.layouts.window;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.theme.ProcuraWindow;

public class GbaModalWindow extends ModalWindow {

  public GbaModalWindow() {
    this(false);
  }

  public GbaModalWindow(boolean margin) {
    setContent(new GbaVerticalLayout(margin).sizeFull());
  }

  public GbaModalWindow(boolean margin, String caption, String width) {
    this(margin, caption, width, null);
  }

  public GbaModalWindow(boolean margin, String caption, String width, Runnable listener) {
    this(margin);
    setCaption(caption);
    setWidth(width);
    if (listener != null) {
      addListener((CloseListener) closeEvent -> listener.run());
    }
  }

  public GbaModalWindow(String caption, String width) {
    this(false, caption, width);
  }

  public GbaApplication getGbaApplication() {
    return (GbaApplication) super.getApplication();
  }

  public ProcuraWindow getProcuraWindow() {
    return (ProcuraWindow) super.getWindow();
  }
}
