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

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum LvToestemmingType {

  MOEDER("Moeder uit wie het kind is geboren"),
  NVT("Niet van toepassing"),
  ANDERS("Anders, nl"),
  ONBEKEND("Onbekend");

  private final String oms;

  LvToestemmingType(String oms) {
    this.oms = oms;
  }

  public static LvToestemmingType get(String oms) {
    return Arrays.stream(values())
        .filter(locatie -> equalsIgnoreCase(locatie.getOms(), oms))
        .findFirst()
        .orElse(ONBEKEND);
  }

  @Override
  public String toString() {
    return oms;
  }
}
