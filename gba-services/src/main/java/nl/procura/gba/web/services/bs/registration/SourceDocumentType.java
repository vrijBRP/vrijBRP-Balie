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

import static nl.procura.gba.jpa.personen.db.DossSourceDoc.SOURCE_TYPE_NOT_SET;

import java.util.Arrays;

public enum SourceDocumentType {

  NOT_SET(SOURCE_TYPE_NOT_SET, ""),
  NONE("N", "Geen"),
  CUSTOM("C", "Zelf invullen"),
  DUTCH("D", "Nederlandse akte");

  private final String code;
  private final String description;

  SourceDocumentType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public static SourceDocumentType valueOfCode(String code) {
    for (SourceDocumentType value : SourceDocumentType.values()) {
      if (value.code.equals(code)) {
        return value;
      }
    }
    return NOT_SET;
  }

  public boolean is(SourceDocumentType... types) {
    return Arrays.stream(types).anyMatch(type -> type == this);
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
