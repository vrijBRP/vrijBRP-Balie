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

package nl.procura.gba.web.services.zaken.algemeen.aantekening;

public enum PlAantekeningStatus {

  OPEN(1, "Open"),
  AFGESLOTEN(0, "Afgesloten"),
  ONBEKEND(-1, "Onbekend");

  private int    code  = -1;
  private String descr = "";

  PlAantekeningStatus(int code, String descr) {
    setCode(code);
    setDescr(descr);
  }

  public static PlAantekeningStatus get(int code) {
    for (PlAantekeningStatus m : PlAantekeningStatus.values()) {
      if (m.getCode() == code) {
        return m;
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

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public boolean is(PlAantekeningStatus... statussen) {
    for (PlAantekeningStatus status : statussen) {
      if (getCode() == status.getCode()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return getDescr();
  }
}
