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

package nl.procura.gba.web.services.bs.algemeen.enums;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum TypeBronDocument {

  AKTE_VAN_OVERLIJDEN("O", "akte van overlijden"),
  RECHTELIJKE_UITSPRAAK("R", "rechtelijke uitspraak overlijden"),
  AKTE_VAN_BEKENDHEID("B", "akte van bekendheid"),
  BEEDIGDEVERKLARING("V", "beëdigde verklaring ex art. 45 BW1"),
  ANDERS("A", "anders, nl."),
  ONBEKEND("", "onbekend");

  private String code = "";
  private String oms  = "";

  TypeBronDocument(String code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static TypeBronDocument get(String code) {
    for (TypeBronDocument a : values()) {
      if (equalsIgnoreCase(a.getCode(), code)) {
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
