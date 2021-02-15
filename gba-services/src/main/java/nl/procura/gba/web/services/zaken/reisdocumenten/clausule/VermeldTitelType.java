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

package nl.procura.gba.web.services.zaken.reisdocumenten.clausule;

import java.util.Arrays;

public enum VermeldTitelType {

  JA(1, "Ja"),
  NEE(0, "Nee"),
  NVT(-2, "Niet van toepassing"),
  ONBEKEND(-1, "Onbekend");

  private int    code;
  private String oms;

  VermeldTitelType(int code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static VermeldTitelType get(int code) {
    return Arrays.stream(values())
        .filter(var -> var.getCode() == code)
        .findFirst()
        .orElse(ONBEKEND);
  }

  public int getCode() {
    return code;
  }

  public String getOms() {
    return oms;
  }

  @Override
  public String toString() {
    return getOms();
  }

}
