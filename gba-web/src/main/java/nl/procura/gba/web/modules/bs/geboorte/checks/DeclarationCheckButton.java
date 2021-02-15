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

package nl.procura.gba.web.modules.bs.geboorte.checks;

import java.util.Set;
import java.util.function.Supplier;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.vaadin.component.label.Ruler;
import nl.procura.vaadin.theme.twee.Icons;

public class DeclarationCheckButton extends GbaVerticalLayout {

  protected final Button          button      = new Button("Controle");
  private DeclarationCheckWindow  popupWindow = null;
  private boolean                 openWindow;
  private Supplier<Set<Class<?>>> popupState;

  public DeclarationCheckButton() {
    setSpacing(true);
    button.setWidth("100%");
    button.addStyleName("online-declaration");
    button.addListener((Button.ClickListener) event -> openWindow());
    addComponent(new Ruler());
    addComponent(button);
  }

  private void openWindow() {
    getApplication().getParentWindow().addWindow(popupWindow);
  }

  @Override
  public void attach() {
    if (openWindow
        && popupWindow != null
        && popupWindow.getParent() == null
        && popupState != null
        && !popupState.get().contains(popupWindow.getClass())) {
      getApplication().getParentWindow().addWindow(popupWindow);
    }
    openWindow = false;
    super.attach();

  }

  public void check(DeclarationCheckWindow window, boolean showPopup) {
    this.popupWindow = window;
    popupWindow.addListener((Window.CloseListener) closeEvent -> popupState.get().add(window.getClass()));

    if (window.isShowIcon()) {
      button.setIcon(new ThemeResource(Icons.getIcon(window.isMatchValues() ? Icons.ICON_OK : Icons.ICON_WARN)));
    }
    if (showPopup && !popupWindow.isMatchValues()) {
      this.openWindow = true;
    }
  }

  public void setPopupState(Supplier<Set<Class<?>>> popupStateSupplier) {
    this.popupState = popupStateSupplier;
  }
}
