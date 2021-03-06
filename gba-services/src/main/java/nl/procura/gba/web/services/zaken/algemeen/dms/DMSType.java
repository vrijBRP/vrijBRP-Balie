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

package nl.procura.gba.web.services.zaken.algemeen.dms;

import java.util.Arrays;

public enum DMSType {

  PERSONEN("personen", "Opslaan in Procura Burgerzaken (oude manier)"),
  STORAGE("storage", "Opslaan in Procura document service (nieuwe manier)");

  private final String code;
  private final String description;

  DMSType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public static DMSType valueOfCode(String code) {
    return Arrays.stream(DMSType.values())
        .filter(value -> value.code.equals(code))
        .findFirst().orElse(PERSONEN);
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
