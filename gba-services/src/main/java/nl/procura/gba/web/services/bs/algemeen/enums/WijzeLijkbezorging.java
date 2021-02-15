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

package nl.procura.gba.web.services.bs.algemeen.enums;

import java.util.Arrays;

import nl.procura.gba.common.EnumWithCode;

public enum WijzeLijkbezorging implements EnumWithCode<String> {

  BEGRAVING_CREMATIE("BC", "Begraving / Crematie"),
  ONTLEDING("O", "Ontleding"),
  ONBEKEND("", "Onbekend");

  private String code;
  private String oms;

  WijzeLijkbezorging(String code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static WijzeLijkbezorging get(String code) {
    return Arrays.stream(values())
        .filter(a -> a.getCode().toLowerCase()
            .contains(code.toLowerCase()))
        .findFirst().orElse(ONBEKEND);

  }

  @Override
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
