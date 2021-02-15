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
import java.util.Arrays;

public enum RelationMatchType {

  NOT_SET(-1, ""),
  INAPPLICABLE(1, "Wijzigen bestaande gegevens niet van toepassing"), // Changing existing information inapplicable 
  YES(2, "Gegevens op gerelateerde PL wordt bijgewerkt"), // Information on related PL will be updated
  NO(3, "Gegevens op gerelateerde PL komt niet overeen"), // Information on related PL does not match
  UNKNOWN(0, "Onbekend");

  private final BigDecimal code;
  private final String     description;

  RelationMatchType(int code, String description) {
    this.code = BigDecimal.valueOf(code);
    this.description = description;
  }

  public static RelationMatchType valueOfCode(BigDecimal code) {
    for (final RelationMatchType value : RelationMatchType.values()) {
      if (value.code.equals(code)) {
        return value;
      }
    }
    return NOT_SET;
  }

  public boolean is(RelationMatchType... types) {
    return Arrays.stream(types).anyMatch(type -> type == this);
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
