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

import static com.vaadin.event.ShortcutAction.KeyCode.*;

import java.util.Set;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.common.exceptions.ExceptionHandler;
import nl.procura.gba.web.components.layouts.page.buttons.ButtonDelete;
import nl.procura.gba.web.components.layouts.page.buttons.ButtonNew;
import nl.procura.gba.web.components.layouts.page.buttons.ButtonSave;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.listener.FunctionKeylistenerHandler;
import nl.procura.vaadin.functies.ComponentFocusHandler;
import nl.procura.vaadin.functies.VaadinUtils;

public class ButtonPageTemplate extends GbaPageTemplate implements ClickListener {

  protected final Button       buttonPrev   = new Button("Vorige (F1)");
  protected final Button       buttonNext   = new Button("Volgende (F2)");
  protected final Button       buttonPrint  = new Button("Afdrukken (F3)");
  protected final Button       buttonSearch = new Button("Zoeken (Enter)");
  protected final Button       buttonReset  = new Button("Reset (F7)");
  protected final Button       buttonClose  = new Button("Sluiten (Esc)");
  protected final ButtonNew    buttonNew    = new ButtonNew();
  protected final ButtonSave   buttonSave   = new ButtonSave();
  protected final ButtonDelete buttonDel    = new ButtonDelete();

  private HLayout                    buttonLayout       = new HLayout();
  private FunctionKeylistenerHandler keylistenerHandler = null;

  public ButtonPageTemplate() {
    buttonLayout.setSpacing(true);
    buttonLayout.setHeight("30px");
    buttonLayout.align(Alignment.MIDDLE_LEFT);
  }

  public void addButton(Component... cs) {
    for (Component c : cs) {
      if (c != null) {
        addButton(c, Alignment.MIDDLE_LEFT);
      }
    }
  }

  public void addButton(Component c, Alignment a) {

    if (c instanceof Button) {
      ((Button) c).addListener(this);
    }

    getButtonLayout().addComponent(c);
    getButtonLayout().setComponentAlignment(c, a);

    if (getButtonLayout().getComponentCount() == 1) {
      addComponent(getButtonLayout());
    }
  }

  public void addButton(Component c, float expandRatio) {

    addButton(c);
    getButtonLayout().setWidth("100%");
    getButtonLayout().setExpandRatio(c, expandRatio);
  }

  @Override
  public void attach() {

    keylistenerHandler = new FunctionKeylistenerHandler() {

      @Override
      public void handleKey(int keyCode) {

        Window window = getWindow();

        try {
          if (window.getChildWindows().isEmpty()) {
            handleEvent(null, keyCode);
          }
        } catch (Exception e) {
          ExceptionHandler.handle(window, e);
        } finally {
          VaadinUtils.resetHeight(getWindow());
        }
      }
    };

    getWindow().addActionHandler(keylistenerHandler);

    super.attach();
  }

  @Override
  public void buttonClick(ClickEvent event) {

    try {
      handleEvent(event.getButton(), 0);
    } finally {
      VaadinUtils.resetHeight(getWindow());
    }
  }

  @Override
  public void detach() {
    getWindow().removeActionHandler(keylistenerHandler);
    super.detach();
  }

  @Override
  public void event(PageEvent event) {

    super.event(event);

    // Geen focus als er sprake is van subwindows
    if (getParentWindow() != null) {
      Set<Window> childWindows = getParentWindow().getChildWindows();
      Window lastChild = childWindows.stream().reduce((first, second) -> second).orElse(null);
      if (childWindows.isEmpty() || (lastChild != null && lastChild == getWindow())) {
        ComponentFocusHandler.focus(this);
      }
    }
  }

  public HLayout getButtonLayout() {
    return buttonLayout;
  }

  public void setButtonLayout(HLayout buttonLayout) {
    this.buttonLayout = buttonLayout;
  }

  public void handleEvent(Button button, int keyCode) {

    if (isKeyCode(button, keyCode, F1, buttonPrev)) {
      onPreviousPage();
    } else if (isKeyCode(button, keyCode, F2, buttonNext)) {
      onNextPage();
    } else if (isKeyCode(button, keyCode, F7, buttonNew, buttonReset)) {
      onNew();
    } else if (isKeyCode(button, keyCode, F3, buttonSearch)) {
      onSearch();
    } else if (isKeyCode(button, keyCode, F3, buttonPrint)) {
      onPrint();
    } else if (isKeyCode(button, keyCode, F9, buttonSave)) {
      onSave();
    } else if (isKeyCode(button, keyCode, F8, buttonDel)) {
      onDelete();
    } else if (isKeyCode(button, keyCode, 0, buttonClose)) {
      onClose();
    } else if (isKeyCode(null, keyCode, ENTER)) {
      onEnter();
    } else if (isKeyCode(null, keyCode, F12)) {
      onHome();
    }
  }

  public void onClose() {
  }

  public void onDelete() {
  }

  public void onEnter() {
  }

  public void onHome() {
  }

  public void onNew() {
  }

  public void onNextPage() {
  }

  public void onPreviousPage() {
  }

  public void onSave() {
  }

  public void onSearch() {
  }

  public void onPrint() {
  }

  public void removeButton(Component... cs) {

    for (Component c : cs) {
      if (c.getParent() != null) {
        ((Layout) c.getParent()).removeComponent(c);
      }
    }
  }

  protected boolean isKeyCode(Button button, int keyCode, int functionKey, Button... pressedButtons) {

    // Als er geen button is
    if (keyCode > 0 && keyCode == functionKey && (pressedButtons == null || pressedButtons.length == 0)) {
      return true;
    }

    // Controle of corresponderende button wel 'aanstaat'.
    for (Button pressedButton : pressedButtons) {

      boolean buttonIsPressed = button == pressedButton;
      boolean keyCodeUsed = keyCode > 0 && keyCode == functionKey;
      boolean buttonIsAvailable = pressedButton.getParent() != null && pressedButton.isEnabled()
          && pressedButton.isVisible();

      if (buttonIsPressed || (keyCodeUsed && buttonIsAvailable)) {
        return true;
      }
    }

    return false;
  }
}
