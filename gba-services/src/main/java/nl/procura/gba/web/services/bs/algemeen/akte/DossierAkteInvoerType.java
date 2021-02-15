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

package nl.procura.gba.web.services.bs.algemeen.akte;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum DossierAkteInvoerType {

  HANDMATIG("H", "Handmatig"),
  PROWEB_PERSONEN("P", "Proweb Personen"),
  BLANCO("B", "Blanco"),
  ONBEKEND("", "Onbekend");

  private String code;
  private String oms;

  DossierAkteInvoerType(String code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static DossierAkteInvoerType get(String code) {
    for (DossierAkteInvoerType a : values()) {
      if (equalsIgnoreCase(a.getCode(), code)) {
        return a;
      }
    }
    return ONBEKEND;
  }

  public String getCode() {
    return code;
  }

  public String getOms() {
    return oms;
  }

  public boolean is(DossierAkteInvoerType... types) {
    for (DossierAkteInvoerType type : types) {
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
