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

package nl.procura.gbaws.web.rest.v1_0.gbav.account;

public enum GbavType {

  BEPERKT(0, "Beperkte GBA-V"),
  VOLLEDIG(1, "Volledige GBA-V");

  private int    code;
  private String oms;

  GbavType(int code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static GbavType get(int code) {
    for (GbavType type : values()) {
      if (type.getCode() == code) {
        return type;
      }
    }

    return BEPERKT;
  }

  @Override
  public String toString() {
    return getOms();
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
}
