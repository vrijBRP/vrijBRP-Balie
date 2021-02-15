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

package nl.procura.gbaws.web.vaadin.layouts;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

import nl.procura.vaadin.component.layout.Fieldset;

public class OptieLayout extends GbaWsHorizontalLayout {

  private static final long serialVersionUID = -9087091999305984460L;
  private Left              left             = new Left();
  private Right             right            = new Right();
  private final Fieldset    rightFieldset    = new Fieldset(right);

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

  public void removeRightVisible(boolean isVisible) {
    if (!isVisible) {
      removeComponent(rightFieldset);
    }
  }

  public VerticalLayout getLeft() {
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

  public class Left extends VerticalLayout {

    private static final long serialVersionUID = 5827537145794483913L;

    public Left() {
      setSpacing(true);
    }
  }

  public class Right extends VerticalLayout {

    private static final long serialVersionUID = 6848684038802327083L;

    public Right() {
      setSpacing(true);
    }

    public Button addButton(Button button, ClickListener listener) {

      button.setWidth("100%");

      button.addListener(listener);

      addComponent(button);

      return button;
    }
  }
}
