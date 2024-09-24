/*
 * Copyright 2023 - 2024 Procura B.V.
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

import static nl.procura.gba.web.services.zaken.algemeen.tasks.TaskEventType.EVENT_ADD_HUW;
import static nl.procura.gba.web.services.zaken.algemeen.tasks.TaskEventType.EVENT_ADD_NAT;
import static nl.procura.gba.web.services.zaken.algemeen.tasks.TaskEventType.EVENT_ADD_OVERL;
import static nl.procura.gba.web.services.zaken.algemeen.tasks.TaskEventType.EVENT_MISC;
import static nl.procura.gba.web.services.zaken.algemeen.tasks.TaskEventType.EVENT_ZAAK;
import static nl.procura.gba.web.services.zaken.algemeen.tasks.TaskExecutionType.MANUAL;
import static nl.procura.gba.web.services.zaken.algemeen.tasks.TaskExecutionType.SKIP;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nl.procura.gba.common.EnumUtils;
import nl.procura.gba.common.EnumWithCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskType implements EnumWithCode<Integer> {

  // Algemeen
  UNKNOWN(-1, "Onbekend",
      false, EVENT_MISC, MANUAL),

  TASK_MISC(1, "Overige",
      true, EVENT_MISC, MANUAL),

  TASK_ZAAK(2, "Overige",
      true, EVENT_ZAAK, MANUAL),

  TASK_BERICHT_OVERL_PARTNER(3, "Zorgdragen voor verwerken van overlijden op PL partner",
      false, EVENT_ADD_OVERL, MANUAL, SKIP),

  TASK_BERICHT_HUW_PARTNER(4, "Zorgdragen voor verwerken verbintenis op PL partner",
      false, EVENT_ADD_HUW, MANUAL, SKIP),

  TASK_EU_KIESR(5, "Verwijder cat. 13 (kiesrecht) vanwege aanduiding europees kiesrecht",
      false, EVENT_ADD_NAT, MANUAL, SKIP);

  private final Integer                 code;
  private final String                  description;
  private final List<TaskExecutionType> executionTypes;
  private final boolean                 changeDescription;
  private final TaskEventType           eventType;

  TaskType(Integer code, String description, boolean changeDescription,
      TaskEventType eventType, TaskExecutionType... executionTypes) {
    this.code = code;
    this.description = description;
    this.changeDescription = changeDescription;
    this.eventType = eventType;
    this.executionTypes = Arrays.asList(executionTypes);
  }

  public static TaskType get(Number code) {
    return EnumUtils.get(values(), code, UNKNOWN);
  }

  public static List<TaskType> getByEventTypes(List<TaskEventType> eventTypes) {
    return Arrays.stream(values())
        .filter(t -> eventTypes.contains(t.getEventType()))
        .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return description;
  }
}
