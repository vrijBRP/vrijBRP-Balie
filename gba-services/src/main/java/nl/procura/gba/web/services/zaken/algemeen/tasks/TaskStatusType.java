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

package nl.procura.gba.web.services.zaken.algemeen.tasks;

import nl.procura.gba.common.EnumUtils;
import nl.procura.gba.common.EnumWithCode;

import lombok.Getter;

@Getter
public enum TaskStatusType implements EnumWithCode<Integer> {

  OPEN(1, "Open"),
  CLOSED(2, "Gesloten");

  TaskStatusType(int code, String description) {
    this.code = code;
    this.description = description;
  }

  private final Integer code;
  private final String  description;

  public static TaskStatusType get(Number code) {
    return EnumUtils.get(values(), code, null);
  }

  public boolean is(TaskStatusType type) {
    return this == type;
  }

  @Override
  public String toString() {
    return getDescription();
  }
}
