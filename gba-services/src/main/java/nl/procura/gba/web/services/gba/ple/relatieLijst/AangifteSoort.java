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

package nl.procura.gba.web.services.gba.ple.relatieLijst;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum AangifteSoort {

  AMBTSHALVE("A", "Ambtshalve"),
  MINISTERIEELBESLUIT("B", "Ministerieel besluit"),
  GEZAGHOUDER("G", "Gezaghouder"),
  HOOFDINSTELLING("H", "Hoofd instelling"),
  INGESCHREVENE("I", "Ingeschrevene"),
  MEERDERJARIG_INWONEND_KIND_VOOR_OUDER("K", "Meerderjarig inwonend kind voor ouder"),
  MEERDERJARIGE_GEMACHTIGDE("M", "Meerderjarige gemachtigde"),
  INWONENDE_OUDER_VOOR_MEERDERJARIG_KIND("O", "Inwonende ouder voor meerderjarig kind"),
  ECHTGENOOT_GEREGISTREERD_PARTNER("P", "Echtgenoot/geregistreerd partner"),
  TECHNISCHE_WIJZIGING_IVM_BAG("T", "Technische wijziging i.v.m. BAG"),
  INFRASTRUCTURELE_WIJZIGING("W", "Infrastructurele wijziging"),
  ONBEKEND("", "Onbekend");

  private String code = "";
  private String oms  = "";

  AangifteSoort(String code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static AangifteSoort get(String code) {
    for (AangifteSoort a : values()) {
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
    return getCode() + ": " + getOms();
  }
}
