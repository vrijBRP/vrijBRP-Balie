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

package nl.procura.gba.web.services.zaken.verhuizing;

public enum VerhuisType {

  BINNENGEMEENTELIJK(1, "Binnengemeentelijk"),
  INTERGEMEENTELIJK(2, "Intergemeentelijk"),
  EMIGRATIE(3, "Emigratie"),
  HERVESTIGING(4, "Hervestiging"),
  ONBEKEND(99, "Onbekend");

  private long   code = 0;
  private String oms  = "";

  VerhuisType(long code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static VerhuisType get(long code) {
    for (VerhuisType e : values()) {
      if (e.getCode() == code) {
        return e;
      }
    }

    return ONBEKEND;
  }

  public long getCode() {
    return code;
  }

  public void setCode(long code) {
    this.code = code;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public boolean is(VerhuisType... types) {
    for (VerhuisType type : types) {
      if (type.getCode() == code) {
        return true;
      }
    }
    return false;
  }

  public boolean isHervestiging() {
    return getCode() == HERVESTIGING.getCode();
  }

  @Override
  public String toString() {
    return getOms();
  }
}
