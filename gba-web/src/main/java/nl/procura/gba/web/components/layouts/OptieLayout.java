/*
 * Copyright 2024 - 2025 Procura B.V.
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

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import nl.procura.vaadin.component.layout.Fieldset;

public class OptieLayout extends GbaHorizontalLayout {

  private Left           left          = new Left();
  private Right          right         = new Right();
  private final Fieldset rightFieldset = new Fieldset(right);

  public OptieLayout() {
    setWidth("100%");
    addComponent(left);
    addComponent(rightFieldset);
    setExpandRatio(left, 1f);
  }

  public OptieLayout(Component component, String width) {
    this();
    getLeft().addComponent(component);
    getRight().setWidth(width);
  }

  public Left getLeft() {
    return left;
  }

  public void setLeft(Left left) {
    this.left = left;
  }

  public Right getRight() {
    return right;
  }

  public void setRight(Right right) {
    this.right = right;
  }

  public void removeRightVisible(boolean isRemove) {
    if (isRemove) {
      removeComponent(rightFieldset);
    }
  }

  public class Left extends VerticalLayout {

    public Left() {
      setSpacing(true);
    }

    public void addExpandComponent(Component component) {
      setSizeFull();
      component.setSizeFull();
      addComponent(component);
      setExpandRatio(component, 1f);
    }
  }

  public class Right extends VerticalLayout {

    public Right() {
      setSpacing(true);
    }

    public Button addButton(Button button) {
      addButton(button, event -> {});
      return button;
    }

    public Button addButton(Button button, ClickListener listener) {
      button.setWidth("100%");
      button.addListener(listener);
      addComponent(button);
      return button;
    }
  }
}
