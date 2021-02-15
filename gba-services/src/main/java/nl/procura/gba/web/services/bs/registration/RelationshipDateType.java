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

import java.math.BigDecimal;

public enum RelationshipDateType {

  NOT_SET(-1, ""),
  UNKNOWN(1, "Onbekend"),
  CUSTOM(2, "Zelf invullen"),
  DATE_OF_BIRTH(3, "Geboortedatum kind");

  private final BigDecimal code;
  private final String     description;

  RelationshipDateType(int code, String description) {
    this.code = BigDecimal.valueOf(code);
    this.description = description;
  }

  public static RelationshipDateType valueOfCode(BigDecimal code) {
    for (final RelationshipDateType value : RelationshipDateType.values()) {
      if (value.code.equals(code)) {
        return value;
      }
    }
    return NOT_SET;
  }

  public BigDecimal getCode() {
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
