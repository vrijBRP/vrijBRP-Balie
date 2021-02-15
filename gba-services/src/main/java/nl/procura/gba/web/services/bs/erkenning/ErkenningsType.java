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

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum ErkenningsType {

  GEEN_ERKENNING("G", "Geen erkenning"),
  ERKENNING_ONGEBOREN_VRUCHT("P", "Erkenning ongeboren vrucht"),
  ERKENNING_BESTAAND_KIND("B", "Erkenning bestaand kind"),
  ERKENNING_BIJ_AANGIFTE("N", "Erkenning bij aangifte"),
  ONBEKEND("", "Onbekend");

  private String code = "";
  private String oms  = "";

  ErkenningsType(String code, String oms) {
    setCode(code);
    setOms(oms);
  }

  public static ErkenningsType get(String code) {
    for (ErkenningsType a : values()) {
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

  public boolean is(ErkenningsType... types) {
    for (ErkenningsType type : types) {
      if (type == this) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
