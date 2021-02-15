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

package nl.procura.gba.web.rest.v2.model.base;

public interface GbaRestEnum<T> {

  T getCode();

  String getDescr();

  static <X extends Number, T extends GbaRestEnum<X>> T toEnum(T[] values, Number sourceValue) {
    return toEnum(values, String.valueOf(sourceValue));
  }

  static <T extends GbaRestEnum<?>> T toEnum(T[] values, String sourceValue) {
    if (sourceValue != null) {
      for (T targetValue : values) {
        if (targetValue.getCode().toString().equals(sourceValue)) {
          return targetValue;
        }
      }
      throw new IllegalArgumentException("Unknown value: " + sourceValue);
    }
    return null;
  }
}
