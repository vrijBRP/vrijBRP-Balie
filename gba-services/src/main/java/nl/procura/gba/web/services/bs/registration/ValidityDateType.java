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

package nl.procura.gba.web.services.bs.registration;

import static nl.procura.gba.jpa.personen.db.DossSourceDoc.DATE_TYPE_NOT_SET;

public enum ValidityDateType {

  NOT_SET(DATE_TYPE_NOT_SET, ""),
  DATE_OF_BIRTH("G", "Geboortedatum"),
  COMMITMENT_START_DATE("S", "Datum sluiting"),
  COMMITMENT_END_DATE("E", "Datum ontbinding"),
  UNKNOWN("O", "Onbekend"),
  CUSTOM("C", "Zelf invullen");

  private final String code;
  private final String description;

  ValidityDateType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public static ValidityDateType valueOfCode(String code) {
    for (ValidityDateType value : ValidityDateType.values()) {
      if (value.code.equals(code)) {
        return value;
      }
    }
    return NOT_SET;
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return description;
  }
}
