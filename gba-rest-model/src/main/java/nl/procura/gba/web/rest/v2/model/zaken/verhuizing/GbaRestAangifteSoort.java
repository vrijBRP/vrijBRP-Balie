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

package nl.procura.gba.web.rest.v2.model.zaken.verhuizing;

import nl.procura.gba.web.rest.v2.model.base.GbaRestEnum;

public enum GbaRestAangifteSoort implements GbaRestEnum<String> {

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

  private String code  = "";
  private String descr = "";

  GbaRestAangifteSoort(String code, String descr) {
    this.code = code;
    this.descr = descr;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getDescr() {
    return descr;
  }
}
