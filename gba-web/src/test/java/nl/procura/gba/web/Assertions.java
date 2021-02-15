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

package nl.procura.gba.web;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;

public final class Assertions {

  private Assertions() {
  }

  public static <T extends Exception> T assertException(Class<T> expected, Executable executable) {
    try {
      executable.execute();
      throw new AssertionError(format("Expected: %s Actual: no exception", expected.getName()));
    } catch (Exception e) {
      assertEquals(expected, e.getClass());
      return (T) e;
    }
  }

  /**
   * Like Runnable but can throw checked and unchecked exceptions. Useful for lambda's.
   */
  @FunctionalInterface
  public interface Executable {

    void execute() throws Exception;

  }

}
