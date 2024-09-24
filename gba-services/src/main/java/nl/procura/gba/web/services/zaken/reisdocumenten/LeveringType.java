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

package nl.procura.gba.web.services.zaken.reisdocumenten;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LeveringType {

  LEVERING_NIET_BEKEND(0, "Levering niet bekend"),
  DOCUMENT_NIET_GELEVERD(1, "Document niet geleverd"),
  DOCUMENT_GOED(2, "Document goed"),
  DOCUMENT_NIET_GOED(3, "Document niet goed"),
  DOCUMENT_VERDWENEN(4, "Document verdwenen"),
  ONBEKEND(-1, "Onbekend");

  private final int    code;
  private final String oms;

  public static LeveringType get(int code) {
    return Arrays.stream(values())
        .filter(var -> var.getCode() == code)
        .findFirst()
        .orElse(ONBEKEND);

  }

  public boolean is(LeveringType... types) {
    return Arrays.stream(types)
        .anyMatch(type -> this == type);
  }

  @Override
  public String toString() {
    return getCode() >= 0 ? (getCode() + ": " + getOms()) : getOms();
  }

}
