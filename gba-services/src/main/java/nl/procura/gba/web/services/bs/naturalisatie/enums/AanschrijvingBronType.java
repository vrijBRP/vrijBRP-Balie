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

package nl.procura.gba.web.services.bs.naturalisatie.enums;

import nl.procura.gba.common.EnumUtils;
import nl.procura.gba.common.EnumWithCode;

public enum AanschrijvingBronType implements EnumWithCode<Integer> {

  PERSOON(1, "Persoon"),
  BELANGHEBBENDE(2, "Belanghebbende"),
  OVERIGE(3, "Overige"),
  GERELATEERDE(4, "Gerelateerde van een betrokkene");

  private final int    code;
  private final String oms;

  AanschrijvingBronType(int code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static AanschrijvingBronType get(Number code) {
    return EnumUtils.get(values(), code, null);
  }

  @Override
  public Integer getCode() {
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
