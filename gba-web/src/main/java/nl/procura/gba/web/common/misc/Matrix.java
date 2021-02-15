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

package nl.procura.gba.web.common.misc;

import java.util.ArrayList;
import java.util.List;

public class Matrix<T> {

  private final List<Element<T>> elements       = new ArrayList<>();
  private final int              columns;
  private Element<T>             currentElement = null;

  public Matrix(int columns) {
    this.columns = columns;
  }

  public void add(T object) {

    Element last = getLast();

    int x = 0;
    int y = 0;

    if (last != null) {

      x = last.getX() + 1;

      y = last.getY();

      if (x == columns) {

        y++;

        x = 0;
      }
    }

    getElements().add(new Element<>(x, y, object));
  }

  public Element<T> getCurrentElement() {
    return (currentElement == null ? elements.get(0) : currentElement);
  }

  public List<Element<T>> getElements() {
    return elements;
  }

  public void move(MOVES move) {

    Element ce = getCurrentElement();

    switch (move) {
      case UP:

        setCurrentElement(ce.getX(), ce.getY() - 1);
        break;

      case RIGHT:

        setCurrentElement(ce.getX() + 1, ce.getY());
        break;

      case DOWN:

        setCurrentElement(ce.getX(), ce.getY() + 1);
        break;

      case LEFT:

        setCurrentElement(ce.getX() - 1, ce.getY());
        break;

      default:
        break;
    }

  }

  private Element<T> getElement(int x, int y) {
    for (Element<T> e : getElements()) {
      if (e.getX() == x && e.getY() == y) {
        return e;
      }
    }

    return null;
  }

  private Element<T> getLast() {
    return elements.size() > 0 ? elements.get(elements.size() - 1) : null;
  }

  private void setCurrentElement(int x, int y) {

    Element<T> newElement = getElement(x, y);

    if (newElement != null) {
      this.currentElement = newElement;
    }
  }

  public enum MOVES {
    UP,
    RIGHT,
    DOWN,
    LEFT
  }

  public static class Element<T> {

    private int y;
    private int x;
    private T   object;

    private Element(int x, int y, T object) {

      this.y = y;
      this.x = x;
      this.object = object;
    }

    public T getObject() {
      return object;
    }

    public void setObject(T object) {
      this.object = object;
    }

    public int getX() {
      return x;
    }

    public void setX(int x) {
      this.x = x;
    }

    public int getY() {
      return y;
    }

    public void setY(int y) {
      this.y = y;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Element [y=");
      builder.append(y);
      builder.append(", x=");
      builder.append(x);
      builder.append(", object=");
      builder.append(object);
      builder.append("]");
      return builder.toString();
    }
  }
}
