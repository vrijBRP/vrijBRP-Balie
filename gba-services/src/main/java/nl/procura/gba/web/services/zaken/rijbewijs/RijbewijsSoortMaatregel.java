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

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum RijbewijsSoortMaatregel {

  STRAFMAATREGEL("S", "Strafmaatregel"),
  INNEMING_RIJBEWIJS("I", "Inneming rijbewijs"),
  VORDERINGSPROCEDURE("V", "Vorderingsprocedure"),
  ONGELDIGVERKLARING("O", "Ongeldigverklaring"),
  OVERIG("", "Overig");

  private String code = "";
  private String oms  = "";

  RijbewijsSoortMaatregel(String code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static RijbewijsSoortMaatregel get(String code) {
    for (RijbewijsSoortMaatregel var : values()) {
      if (equalsIgnoreCase(var.getCode(), code)) {
        return var;
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
