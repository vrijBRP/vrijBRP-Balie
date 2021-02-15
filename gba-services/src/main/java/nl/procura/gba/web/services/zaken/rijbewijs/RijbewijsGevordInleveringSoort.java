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

public enum RijbewijsGevordInleveringSoort {

  VOLLEDIG_ONGELDIG_RIJBEWIJS("10", "Volledig ongeldig rijbewijs"),
  VOLLEDIG_ONTZEGGING("20", "Volledig ontzegging"),
  VOLLEDIG_SCHORSING("30", "Volledig schorsing"),
  INHOUDING_RIJBEWIJS("40", "Inhouding rijbewijs"),
  ART_164("50", "Vordering tot overgifte ex art. 164"),
  ART_130("60", "Vordering tot overgifte ex art. 130"),
  INNEMING_WET_MULDER("70", "Inneming wet Mulder"),
  PARTIELE_ONTZEGGING("80", "Aantekening partiele ontzegging"),
  PARTIELE_SCHORSING("90", "Aantekening partiele schorsing"),
  PARTIELE_ONGELDIGVERKLARING("95", "Aantekening partiele ongeldigverklaring"),
  OVERIG("O", "Onbekend");

  private String code = "";
  private String oms  = "";

  RijbewijsGevordInleveringSoort(String code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static RijbewijsGevordInleveringSoort get(String code) {
    for (RijbewijsGevordInleveringSoort var : values()) {
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
