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

package nl.procura.gba.common;

import java.util.Arrays;

public final class EnumUtils {

  private EnumUtils() {
  }

  /**
   * Returns the enum where the id matches the id of the enum   *
   */
  public static <T extends EnumWithId<?>> T get(T[] values, Number id, T defaultValue) {
    if (id != null) {
      return get(values, id.toString(), defaultValue);
    }
    return defaultValue;
  }

  /**
   * Returns the enum where the id matches the id of the enum
   */
  public static <T extends EnumWithId<?>> T get(T[] values, String id, T defaultValue) {
    if (id != null) {
      return Arrays.stream(values)
          .filter(et -> et.getId() != null)
          .filter(et -> et.getId().toString().equals(id))
          .findFirst()
          .orElse(defaultValue);
    }
    return defaultValue;
  }

  /**
   * Returns true if id matches the id of one of the enums
   */
  public static boolean is(EnumWithId[] types, long id) {
    return get(types, id, null) != null;
  }
}
