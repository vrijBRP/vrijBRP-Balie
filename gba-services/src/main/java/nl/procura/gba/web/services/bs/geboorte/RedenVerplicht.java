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

public enum RedenVerplicht {

  BEGRAFENIS_ONDERNEMER("B", "Begrafenisondernemer"),
  VADER("V", "Vader"),
  DUO_MOEDER("D", "Duo-moeder"),
  AANWEZIG_BIJ_GEBOORTE("A", "Aanwezig bij geboorte"),
  GEBOORTE_IN_MIJN_WONING("W", "Geboorte in mijn woning"),
  GEBOORTE_IN_MIJN_INSTELLING("I", "Geboorte in mijn instelling"),
  MOEDER("M", "Moeder"),
  BURGEMEESTER("M", "Burgemeester"),
  KENNISDRAGER("K", "Kennisdrager"),
  ONBEKEND("", "Onbekend");

  private String code = "";
  private String oms  = "";

  RedenVerplicht(String code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static RedenVerplicht get(String code) {

    for (RedenVerplicht a : values()) {
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

  public boolean is(RedenVerplicht... redenen) {
    for (RedenVerplicht reden : redenen) {
      if (reden == this) {
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
