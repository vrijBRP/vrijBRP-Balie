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

package nl.procura.gba.web.services.zaken.inhoudingen;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum InhoudingType {

  INHOUDING("I", "Inhouding"),
  VERMISSING("V", "Vermissing"),
  ONBEKEND(".", "Onbekend"),
  VAN_RECHTSWEGE_VERVALLEN("R", "Van rechtswege vervallen"),
  NIET_INGEHOUDEN("", "Niet ingehouden");

  private String code = "";
  private String oms  = "";

  InhoudingType(String code, String oms) {
    setCode(code);
    setOms(oms);
  }

  public boolean is(InhoudingType... types) {
    if (types != null) {
      for (InhoudingType type : types) {
        if (this == type) {
          return true;
        }
      }
    }
    return false;
  }

  public static InhoudingType get(String code) {
    for (InhoudingType var : values()) {
      if (equalsIgnoreCase(var.getCode(), code)) {
        return var;
      }
    }

    return NIET_INGEHOUDEN;
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

  public boolean isNogNietIngehouden() {
    return NIET_INGEHOUDEN == this;
  }

  public boolean isVanRechtswegeVervallen() {
    return VAN_RECHTSWEGE_VERVALLEN == this;
  }

  @Override
  public String toString() {
    return getOms();
  }

}
