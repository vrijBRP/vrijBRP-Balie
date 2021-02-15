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

package nl.procura.gba.web.services.zaken.reisdocumenten;

public enum SpoedType {

  NEE(0, "Nee"),
  JA_OP_VERZOEK_AANVRAGER(1, "Ja, op verzoek aanvrager"),
  JA_OP_VERZOEK_GEMEENTE(2, "Ja, op verzoek gemeente");

  private int    code = 0;
  private String oms  = "";

  SpoedType(int code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static SpoedType get(int code) {

    for (SpoedType var : values()) {
      if (var.getCode() == code) {
        return var;
      }
    }

    return NEE;
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

  public boolean is(SpoedType... types) {
    for (SpoedType type : types) {
      if (type.getCode() == getCode()) {
        return true;
      }
    }

    return false;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
