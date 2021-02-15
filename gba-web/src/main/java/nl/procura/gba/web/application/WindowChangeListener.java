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

package nl.procura.gba.web.application;

import com.vaadin.ui.Window;

public class WindowChangeListener {

  private Window  currentWindow;
  private Window  newWindow;
  private String  fragment;
  private boolean removeFirst;

  public WindowChangeListener(Window currentWindow, Window newWindow) {
    this(currentWindow, newWindow, "", false);
  }

  public WindowChangeListener(Window currentWindow, Window newWindow, String fragment, boolean removeFirst) {
    this.currentWindow = currentWindow;
    this.newWindow = newWindow;
    this.fragment = fragment;
    this.removeFirst = removeFirst;
  }

  public Window getCurrentWindow() {
    return currentWindow;
  }

  public void setCurrentWindow(Window currentWindow) {
    this.currentWindow = currentWindow;
  }

  public String getFragment() {
    return fragment;
  }

  public void setFragment(String fragment) {
    this.fragment = fragment;
  }

  public Window getNewWindow() {
    return newWindow;
  }

  public void setNewWindow(Window newWindow) {
    this.newWindow = newWindow;
  }

  public boolean isRemoveFirst() {
    return removeFirst;
  }

  public void setRemoveFirst(boolean removeFirst) {
    this.removeFirst = removeFirst;
  }

  public void windowChanged() {
  }
}
