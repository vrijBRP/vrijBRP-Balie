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

package nl.procura.gba.web.services.bs.erkenning;

import static nl.procura.standard.Globalfunctions.aval;

import java.math.BigDecimal;
import java.util.Arrays;

public enum NaamsPersoonType {

  MOEDER(1, "De moeder"),
  ERKENNER(2, "De erkenner"),
  VADER_DUO_MOEDER(3, "De vader / duo-moeder"),
  GEEN_VAN_BEIDE(4, "Geen van beide"),
  PARTNER(5, "De partner"),
  ONBEKEND(-1, "Onbekend");

  private int    code;
  private String type;

  NaamsPersoonType(int code, String type) {
    this.code = code;
    this.type = type;
  }

  public static NaamsPersoonType get(BigDecimal code) {
    return Arrays.stream(values())
        .filter(type -> type.getCode() == aval(code))
        .findFirst().orElse(ONBEKEND);

  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean is(NaamsPersoonType... naamsPersoonType) {
    return Arrays.stream(naamsPersoonType).anyMatch(type -> getCode() == type.getCode());
  }

  @Override
  public String toString() {
    return getType();
  }
}
