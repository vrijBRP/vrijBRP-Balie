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

public class ExceptionUtils {

  /**
   * Search for cause that maches the cl parameter
   */
  public static Throwable getCause(Throwable t, Class cl) {

    if (t == null) {
      return null;
    }

    if (cl.isAssignableFrom(t.getClass())) {
      return t;
    }

    if ((t.getCause() != null) && cl.isAssignableFrom(t.getCause().getClass())) {
      return t;
    }

    return getCause(t.getCause(), cl);
  }

  /**
   * Search for the cause that contains the message
   */
  public static Throwable getCause(Throwable t, String msg) {

    if (t == null) {
      return null;
    }

    if ((t.getMessage() != null) && t.getMessage().contains(msg)) {
      return t;
    }

    return getCause(t.getCause(), msg);
  }
}
