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

import nl.procura.gba.web.common.misc.Landelijk;

import lombok.Getter;

@Getter
public enum LvToegepastRechtType {

  NL(Landelijk.LAND_NEDERLAND, "Nederland"),
  NVT(1L, "Niet van toepassing"),
  ANDERS(0L, "Anders, nl"),
  ONBEKEND(-1L, "onbekend");

  private final Long   code;
  private final String oms;

  LvToegepastRechtType(Long code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static LvToegepastRechtType get(Long code) {
    return Arrays.stream(values())
        .filter(type -> type.getCode().equals(code))
        .findFirst()
        .orElse(ONBEKEND);
  }

  @Override
  public String toString() {
    return oms;
  }
}
