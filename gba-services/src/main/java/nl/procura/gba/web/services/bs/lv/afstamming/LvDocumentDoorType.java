/*
 * Copyright 2023 - 2024 Procura B.V.
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
public enum LvDocumentDoorType implements EnumWithCode<Integer> {

  BURGEMEESTER(10, "burgemeester"),
  AMBTENAAR(20, "ambtenaar van de burgerlijke stand"),
  CONSULAIR(30, "consulair ambtenaar"),
  MOEDER(40, "moeder"),
  VADER(50, "vader"),
  NVT(60, "n.v.t."),
  ONBEKEND(-1, "Onbekend");

  private final Integer code;
  private final String  oms;

  LvDocumentDoorType(Integer code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static LvDocumentDoorType get(Integer code) {
    return get(code, ONBEKEND);
  }

  public static LvDocumentDoorType get(Integer code, LvDocumentDoorType defaultType) {
    return Arrays.stream(values())
        .filter(value -> value.getCode() > 0)
        .filter(value -> value.getCode().equals(code))
        .findFirst().orElse(defaultType);
  }

  @Override
  public Integer getCode() {
    return code;
  }

  @Override
  public String toString() {
    return oms;
  }
}
