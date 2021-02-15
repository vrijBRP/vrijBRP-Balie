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

package nl.procura.gba.web.rest.v2.model.zaken.geboorte;

import nl.procura.gba.web.rest.v2.model.base.GbaRestEnum;

public enum GbaRestBurgerlijkeStaatType implements GbaRestEnum<String> {

  ONBEKEND("ONB", "Onbekend"),
  ONGEHUWD("ONG", "Ongehuwd en nooit gehuwd geweest"),
  HUWELIJK("HUW", "Gehuwd"),
  GESCHEIDEN("SCH", "Gescheiden"),
  WEDUWE("WED", "Weduwe/Weduwnaar"),
  PARTNERSCHAP("PRT", "Partnerschap"),
  ONTBONDEN("BGP", "Partnerschap beëindigd"),
  ACHTERGEBLEVEN("AGP", "Achtergebleven partner");

  private final String code;
  private final String descr;

  GbaRestBurgerlijkeStaatType(String code, String descr) {
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
