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
public enum KeuzeVaststellingGeslachtsnaam implements EnumWithCode<String> {

  GEWIJZIGD("G", "Geslachtsnaam kind gewijzigd in"),
  GEKOZEN("K", "Gekozen voor geslachtsnaam"),
  IS("V", "Geslachtsnaam kind is"),
  ONBEKEND("", "Niet van toepassing");

  private final String code;
  private final String oms;

  KeuzeVaststellingGeslachtsnaam(String code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static KeuzeVaststellingGeslachtsnaam get(String code) {
    return get(code, ONBEKEND);
  }

  public static KeuzeVaststellingGeslachtsnaam get(String code, KeuzeVaststellingGeslachtsnaam defaultType) {
    return Arrays.stream(values())
        .filter(value -> isNotBlank(value.getCode()))
        .filter(value -> value.getCode().equals(code))
        .findFirst().orElse(defaultType);
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
