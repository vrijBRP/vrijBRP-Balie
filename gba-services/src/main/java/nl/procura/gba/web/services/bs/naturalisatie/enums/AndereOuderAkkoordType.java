/*
 * Copyright 2022 - 2023 Procura B.V.
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

public enum AndereOuderAkkoordType implements EnumWithCode<Integer> {

  JA(1, "Ja"),
  NEE(2, "Nee"),
  OVERLEDEN(3, "Andere ouder overleden"),
  NIET_TRACEERBAAR(4, "Andere oude in buitenland niet traceerbaar"),
  ANDERE_OUDER_ONBEKEND(5, "Andere ouder onbekend"),
  OVERIGE(6, "Overig, zie toelichting");

  private final int    code;
  private final String oms;

  AndereOuderAkkoordType(int code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static AndereOuderAkkoordType get(Number code) {
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
    return oms;
  }
}
