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

public enum LeveringType {

  LEVERING_NIET_BEKEND(0, "Levering niet bekend"),
  DOCUMENT_NIET_GELEVERD(1, "Document niet geleverd"),
  DOCUMENT_GOED(2, "Document goed"),
  DOCUMENT_NIET_GOED(3, "Document niet goed"),
  DOCUMENT_VERDWENEN(4, "Document verdwenen"),
  ONBEKEND(-1, "Onbekend");

  private int    code = 0;
  private String oms  = "";

  LeveringType(int code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static LeveringType get(int code) {

    for (LeveringType var : values()) {
      if (var.getCode() == code) {
        return var;
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

  @Override
  public String toString() {
    return getCode() >= 0 ? (getCode() + ": " + getOms()) : getOms();
  }

}
