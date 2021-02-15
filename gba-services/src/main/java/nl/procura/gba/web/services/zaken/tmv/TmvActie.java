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

package nl.procura.gba.web.services.zaken.tmv;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum TmvActie {

  REGISTRATIE("ONBV", "Registratie"),
  INZAG("OZDO", "Inzage"),
  INTREKKING("INTRK", "Intrekking"),
  ONBEKEND("", "Onbekend");

  private String berichtcode = "";
  private String oms         = "";

  TmvActie(String berichtcode, String oms) {

    setBerichtcode(berichtcode);
    setOms(oms);
  }

  public static TmvActie get(String code) {
    for (TmvActie var : values()) {
      if (equalsIgnoreCase(var.getBerichtcode(), code)) {
        return var;
      }
    }

    return ONBEKEND;
  }

  public String getBerichtcode() {
    return berichtcode;
  }

  public void setBerichtcode(String berichtcode) {
    this.berichtcode = berichtcode;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  @Override
  public String toString() {
    return getBerichtcode();
  }
}
