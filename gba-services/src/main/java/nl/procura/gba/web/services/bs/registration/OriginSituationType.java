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

import java.util.Arrays;

public enum OriginSituationType {

  NOT_BORN_IN_NL("", "Persoon niet geboren in NL"),
  CHILD_BORN_IN_NL("B", "Kind geboren in NL, moeder niet in BRP");

  private final String code;
  private final String description;

  OriginSituationType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public static OriginSituationType valueOfCode(String code) {
    return Arrays.stream(values())
        .filter(var -> var.getCode().equals(code))
        .findFirst().orElse(NOT_BORN_IN_NL);

  }

  public String getDescription() {
    return description;
  }

  public String getCode() {
    return code;
  }

  @Override
  public String toString() {
    return description;
  }
}
