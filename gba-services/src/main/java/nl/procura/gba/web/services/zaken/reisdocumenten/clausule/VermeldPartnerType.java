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

package nl.procura.gba.web.services.zaken.reisdocumenten.clausule;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum VermeldPartnerType {

  PARTNER_NIET_VERMELDEN("0", "Partner niet vermelden"),
  ECHTGENOOT_VAN("f", "Echtgeno(o)t(e) van"),
  WEDUWNAAR_VAN("g", "Weduw(e)(naar) van"),
  GESCHEIDEN_VAN("h", "Gescheiden van"),
  GEREGISTREERD_PARTNER_VAN("i", "Geregistreerd partner van"),
  GEREGISTREERD_PARTNER_GEWEEST_VAN("j", "Geregistreerd partner geweest van"),
  ACHTERGEBLEVEN_GEREGISTREERD_PARTNER_VAN("k", "Achtergebleven geregistreerd partner van"),
  NIET_VAN_TOEPASSING("0", "Niet van toepassing"),
  ONBEKEND("", "Onbekend");

  private String code = "0";
  private String oms  = "";

  VermeldPartnerType(String code, String oms) {
    setCode(code);
    setOms(oms);
  }

  public static VermeldPartnerType get(String code) {
    for (VermeldPartnerType var : values()) {
      if (equalsIgnoreCase(var.getCode(), code)) {
        return var;
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
