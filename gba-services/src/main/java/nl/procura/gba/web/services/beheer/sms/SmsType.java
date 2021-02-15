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

package nl.procura.gba.web.services.beheer.sms;

import static nl.procura.standard.Globalfunctions.eq;

public enum SmsType {

  REISDOCUMENT_ONTVANGEN("reisdocument_ontvangen", "Reisdocument ontvangen"),
  RIJBEWIJS_ONTVANGEN("rijbewijs_ontvangen", "Rijbewijs ontvangen"),
  ONBEKEND("", "Onbekend");

  private String code;
  private String oms;

  SmsType(String code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static SmsType get(String code) {
    for (SmsType a : values()) {
      if (eq(a.getCode(), code)) {
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

  @Override
  public String toString() {
    return getOms();
  }
}
