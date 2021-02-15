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

package nl.procura.gba.web.services.zaken.rijbewijs;

public enum RijbewijsUitgever {

  GEMEENTE("G", "Gemeente"),
  PROVINCIE("P", "Provincie"),
  LAND("L", "Land"),
  PARKET("K", "Parket"),
  REGIOKERNKORPS("R", "Regiokernkorps"),
  DEVISIEVORDERING("V", "Divisie vordering"),
  DIENST_WEGVERKEER("W", "Dienst wegverkeer"),
  OVERIG("O", "Overig");

  private String code = "";
  private String oms  = "";

  RijbewijsUitgever(String code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static RijbewijsUitgever get(String code) {

    for (RijbewijsUitgever e : values()) {
      if (e.getCode().equalsIgnoreCase(code)) {
        return e;
      }
    }

    return OVERIG;
  }

  public static String getCodeOms(String code) {
    return code.toUpperCase() + " (" + get(code).getOms() + ")";
  }

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
}
