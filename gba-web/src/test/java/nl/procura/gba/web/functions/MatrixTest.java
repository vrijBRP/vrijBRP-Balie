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

package nl.procura.gba.web.functions;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import nl.procura.gba.web.common.misc.Matrix;
import nl.procura.gba.web.common.misc.Matrix.Element;

public class MatrixTest {

  @Test
  public void nieuweMatrix() {

    Matrix<String> matrix = new Matrix<String>(3);

    for (int i = 0; i < 10; i++) {
      matrix.add("nr." + i);
    }

    for (Element e : matrix.getElements()) {
      System.out.println(e);
    }

    msg("currentElement", matrix.getCurrentElement());
    assertTrue(matrix.getCurrentElement().getY() == 0);
    assertTrue(matrix.getCurrentElement().getX() == 0);

    matrix.move(Matrix.MOVES.DOWN);
    msg("currentElement, down", matrix.getCurrentElement());
    assertTrue(matrix.getCurrentElement().getY() == 1);
    assertTrue(matrix.getCurrentElement().getX() == 0);

    matrix.move(Matrix.MOVES.RIGHT);
    assertTrue(matrix.getCurrentElement().getY() == 1);
    assertTrue(matrix.getCurrentElement().getX() == 1);
    msg("currentElement, right", matrix.getCurrentElement());

    matrix.move(Matrix.MOVES.RIGHT);
    assertTrue(matrix.getCurrentElement().getY() == 1);
    assertTrue(matrix.getCurrentElement().getX() == 2);
    msg("currentElement, right", matrix.getCurrentElement());

    matrix.move(Matrix.MOVES.RIGHT);
    assertTrue(matrix.getCurrentElement().getY() == 1);
    assertTrue(matrix.getCurrentElement().getX() == 2);
    msg("currentElement, right", matrix.getCurrentElement());

    matrix.move(Matrix.MOVES.LEFT);
    assertTrue(matrix.getCurrentElement().getY() == 1);
    assertTrue(matrix.getCurrentElement().getX() == 1);
    msg("currentElement, left", matrix.getCurrentElement());

    matrix.move(Matrix.MOVES.UP);
    assertTrue(matrix.getCurrentElement().getY() == 0);
    assertTrue(matrix.getCurrentElement().getX() == 1);
    msg("currentElement, up", matrix.getCurrentElement());

  }

  private void msg(String text, Object o) {
    System.out.println(String.format("%-25s: %-20s", text, o));
  }
}
