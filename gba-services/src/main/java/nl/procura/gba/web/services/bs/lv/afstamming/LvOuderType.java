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

package nl.procura.gba.web.services.bs.lv.afstamming;

import java.util.Arrays;

import nl.procura.gba.common.EnumWithCode;

import lombok.Getter;

@Getter
public enum LvOuderType implements EnumWithCode<String> {

  MOEDER("M", "Moeder"),
  VADER("V", "Vader"),
  NVT("N", "Niet van toepassing"),
  ONBEKEND("", "Niet van toepassing");

  private final String code;
  private final String oms;

  LvOuderType(String code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static LvOuderType get(String code) {
    return Arrays.stream(values())
        .filter(value -> value.getCode().equalsIgnoreCase(code))
        .findFirst()
        .orElse(ONBEKEND);
  }

  public boolean is(LvOuderType... types) {
    return Arrays.stream(types).anyMatch(type -> type == this);
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String toString() {
    return oms;
  }
}
