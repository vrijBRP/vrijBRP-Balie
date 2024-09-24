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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Arrays;

import nl.procura.gba.common.EnumWithCode;

import lombok.Getter;

@Getter
public enum LvGezagType implements EnumWithCode<String> {

  MOEDER("M", "De moeder uit wie het kind is geboren oefent alleen het gezag uit"),
  MOEDER_ERKENNER("ME",
      "De moeder uit wie het kind is geboren en de erkenner hebben verklaard dat het gezag over het kind alleen door de moeder uit wie het kind is geboren wordt uitgeoefend"),
  NVT("-", "Niet van toepassing"),
  ONBEKEND("", "Onbekend");

  private final String code;
  private final String oms;

  LvGezagType(String code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static LvGezagType get(String code) {
    return get(code, ONBEKEND);
  }

  public static LvGezagType get(String code, LvGezagType defaultType) {
    return Arrays.stream(values())
        .filter(value -> isNotBlank(value.getCode()))
        .filter(value -> value.getCode().equalsIgnoreCase(code))
        .findFirst()
        .orElse(defaultType);
  }

  public boolean is(LvGezagType... types) {
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
