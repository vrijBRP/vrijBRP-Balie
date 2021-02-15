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

package nl.procura.gbaws.web.vaadin.layouts.pages;

import static com.vaadin.event.ShortcutAction.KeyCode.*;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.vaadin.component.application.exception.ExceptionHandler;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.listener.FunctionKeylistenerHandler;
import nl.procura.vaadin.functies.ComponentFocusHandler;

public class ButtonPageTemplate extends BsmPageTemplate implements ClickListener {

  protected Button buttonPrev   = new Button("Vorige (F1)");
  protected Button buttonNext   = new Button("Volgende (F2)");
  protected Button buttonSearch = new Button("Zoeken (Enter)");
  protected Button buttonReset  = new Button("Reset (F7)");
  protected Button buttonClose  = new Button("Sluiten (Esc)");
  protected Button buttonNew    = new Button("Nieuw (F7)");
  protected Button buttonSave   = new Button("Opslaan (F9)");
  protected Button buttonDel    = new Button("Verwijderen (F8)");

  private HorizontalLayout           buttonLayout       = new HorizontalLayout();
  private FunctionKeylistenerHandler keylistenerHandler = null;

  public ButtonPageTemplate() {

    getButtonLayout().setSpacing(true);
    getButtonLayout().setHeight("30px");
  }

  public void onPreviousPage() {
  }

  public void onNextPage() {
  }

  public void onSave() {
  }

  public void onDelete() {
  }

  public void onNew() {
  }

  public void onEnter() {
  }

  public void onSearch() {
  }

  public void onHome() {
  }

  public void onClose() {
  }

  public void addButton(Component... cs) {
    for (Component c : cs) {
      if (c != null) {
        addButton(c, Alignment.MIDDLE_LEFT);
      }
    }
  }

  public void addButton(Component c, float expandRatio) {

    addButton(c);
    getButtonLayout().setWidth("100%");
    getButtonLayout().setExpandRatio(c, expandRatio);
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

  public void removeButton(Component... cs) {

    for (Component c : cs) {
      ((Layout) c.getParent()).removeComponent(c);
    }
  }

  @Override
  public void buttonClick(ClickEvent event) {

    handleEvent(event.getButton(), 0);
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
      boolean buttonIsAvailable = pressedButton.isEnabled() && pressedButton.isVisible();

      if (buttonIsPressed || (keyCodeUsed && buttonIsAvailable)) {
        return true;
      }
    }

    return false;
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

  @Override
  public void event(PageEvent event) {

    super.event(event);

    // Geen focus als er sprake is van subwindows
    if (getWindow() != null && getWindow().getChildWindows() != null) {
      if (getWindow().getChildWindows().isEmpty()) {
        ComponentFocusHandler.focus(this);
      }
    }
  }

  @Override
  public void attach() {

    keylistenerHandler = new FunctionKeylistenerHandler() {

      @Override
      public void handleKey(int keyCode) {

        Window window = getWindow();

        try {
          handleEvent(null, keyCode);
        } catch (Exception e) {
          ExceptionHandler.handle(window, e);
        }
      }
    };

    getWindow().addActionHandler(keylistenerHandler);

    super.attach();
  }

  @Override
  public void detach() {

    getWindow().removeActionHandler(keylistenerHandler);

    super.detach();
  }

  public HorizontalLayout getButtonLayout() {
    return buttonLayout;
  }

  public void setButtonLayout(HorizontalLayout buttonLayout) {
    this.buttonLayout = buttonLayout;
  }
}
