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

package nl.procura.gba.web.services.bs.geboorte;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum GezinssituatieType {

  BINNEN_HETERO_HUWELIJK("B", "Kind geboren binnen huwelijk/GPS of < 306 dagen na ontbinding door overlijden"),
  BINNEN_HOMO_HUWELIJK("H", "Kind geboren binnen huwelijk/GPS, partner van rechtswege geen ouder"),
  BUITEN_HUWELIJK("U", "Kind geboren buiten huwelijk/GPS"),
  ONBEKEND("", "Onbekend");

  private String code = "";
  private String oms  = "";

  GezinssituatieType(String code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static GezinssituatieType get(String code) {
    for (GezinssituatieType a : values()) {
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

  public boolean is(GezinssituatieType... types) {
    for (GezinssituatieType type : types) {
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
