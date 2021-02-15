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

public enum DeclarantType {

  PERSON("P", "Inschrijver"),
  INSTITUTION_HEAD("H", "Hoofd instelling"),
  BY_CIVIL_SERVANT("A", "Ambtshalve"),
  UNKNOWN("", "Onbekend");

  private final String code;
  private final String description;

  DeclarantType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public static DeclarantType valueOfCode(String code) {
    for (DeclarantType value : values()) {
      if (value.code.equals(code)) {
        return value;
      }
    }
    return UNKNOWN;
  }

  public String getCode() {
    return code;
  }

  @Override
  public String toString() {
    return description;
  }
}
