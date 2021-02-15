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

public enum KindLeeftijdsType {

  JONGER_DAN_7("7", "Jonger dan 7 jaar (is ivm wel / niet verkrijgen NL nationaliteit)", 0, 6),
  VAN_7_TM_11("11", "7 t/m 11 jaar", 7, 11),
  VAN_12_TM_15("15", "12 t/m 15 jaar", 12, 15),
  OUDER_DAN_16("16", "Ouder dan 16 jaar", 16, 200),
  NVT("nvt", "Niet van toepassing", -1, -1),
  ONBEKEND("", "Onbekend", -1, -1);

  private String code = "";
  private String oms  = "";
  private int    min  = 0;
  private int    max  = 0;

  KindLeeftijdsType(String code, String oms, int min, int max) {

    setCode(code);
    setOms(oms);
    setMin(min);
    setMax(max);
  }

  public static KindLeeftijdsType get(int leeftijd) {

    for (KindLeeftijdsType a : values()) {
      if (a.getMin() <= leeftijd && a.getMax() >= leeftijd) {
        return a;
      }
    }

    return ONBEKEND;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public int getMax() {
    return max;
  }

  public void setMax(int max) {
    this.max = max;
  }

  public int getMin() {
    return min;
  }

  public void setMin(int min) {
    this.min = min;
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
