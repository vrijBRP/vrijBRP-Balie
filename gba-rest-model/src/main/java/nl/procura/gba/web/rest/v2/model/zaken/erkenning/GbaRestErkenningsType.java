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

package nl.procura.gba.web.rest.v2.model.zaken.erkenning;

import nl.procura.gba.web.rest.v2.model.base.GbaRestEnum;

public enum GbaRestErkenningsType implements GbaRestEnum<String> {

  GEEN_ERKENNING("G", "Geen erkenning"),
  ERKENNING_ONGEBOREN_VRUCHT("P", "Erkenning ongeboren vrucht"),
  ERKENNING_BESTAAND_KIND("B", "Erkenning bestaand kind"),
  ERKENNING_BIJ_AANGIFTE("N", "Erkenning bij aangifte"),
  ONBEKEND("", "Onbekend");

  private String code;
  private String descr;

  GbaRestErkenningsType(String code, String descr) {
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
