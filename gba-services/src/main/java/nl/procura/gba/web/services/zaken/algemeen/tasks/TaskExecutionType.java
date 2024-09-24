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

import java.util.Arrays;

import nl.procura.gba.common.EnumUtils;
import nl.procura.gba.common.EnumWithCode;

import lombok.Getter;

@Getter
public enum TaskExecutionType implements EnumWithCode<Integer> {

  AUTO(1, "Autom. bij verwerking"),
  MANUAL(2, "Handmatig"),
  SKIP(3, "Overslaan");

  private final Integer code;
  private final String  description;

  TaskExecutionType(int code, String description) {
    this.code = code;
    this.description = description;
  }

  public static TaskExecutionType get(Number code) {
    return EnumUtils.get(values(), code, null);
  }

  public boolean is(TaskExecutionType... type) {
    return Arrays.asList(type).contains(this);
  }

  @Override
  public String toString() {
    return description;
  }
}
