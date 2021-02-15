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

package nl.procura.gba.web.rest.v2.model.zaken.base.namenrecht;

import nl.procura.gba.web.rest.v2.model.base.GbaRestEnum;

public enum GbaRestNaamskeuzeType implements GbaRestEnum<Integer> {

  NVT(2, "N.v.t."),
  JA(1, "Ja"),
  NEE(0, "Nee"),
  ONBEKEND(-1, "Onbekend");

  private final int    code;
  private final String descr;

  GbaRestNaamskeuzeType(int code, String descr) {
    this.code = code;
    this.descr = descr;
  }

  @Override
  public Integer getCode() {
    return code;
  }

  @Override
  public String getDescr() {
    return descr;
  }
}
