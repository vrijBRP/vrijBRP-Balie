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

public enum EersteKindType {

  NVT(2, "N.v.t.", "Niet van toepassing"),
  JA(1, "Ja"),
  NEE(0, "Nee"),
  ONBEKEND(-1, "Onbekend");

  private int    code;
  private String type;
  private String oms;

  EersteKindType(int code, String type) {
    this(code, type, type);
  }

  EersteKindType(int code, String type, String oms) {
    this.code = code;
    this.type = type;
    this.oms = oms;
  }

  public static EersteKindType get(BigDecimal code) {

    for (EersteKindType type : values()) {
      if (type.getCode() == aval(code)) {
        return type;
      }
    }

    return ONBEKEND;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
