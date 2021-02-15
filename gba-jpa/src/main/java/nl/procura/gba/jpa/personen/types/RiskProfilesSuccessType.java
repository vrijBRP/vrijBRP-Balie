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

package nl.procura.gba.jpa.personen.types;

import nl.procura.gba.common.EnumUtils;
import nl.procura.gba.common.EnumWithId;

public enum RiskProfilesSuccessType implements EnumWithId<Integer> {

  DO_NOTHING(1, "Doe niets"),
  STATUS_ACCEPTED(2, "Zet de status van de gerelateerde zaak op opgenomen"),
  UNKNOWN(0, "Onbekend");

  private final int    id;
  private final String descr;

  RiskProfilesSuccessType(int id, String descr) {
    this.id = id;
    this.descr = descr;
  }

  public static RiskProfilesSuccessType get(Number id) {
    return EnumUtils.get(RiskProfilesSuccessType.values(), id, UNKNOWN);
  }

  public boolean is(RiskProfilesSuccessType... types) {
    return EnumUtils.is(types, id);
  }

  @Override
  public Integer getId() {
    return id;
  }

  public String getDescr() {
    return descr;
  }

  @Override
  public String toString() {
    return getDescr();
  }
}
