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

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import nl.procura.gba.common.EnumUtils;
import nl.procura.gba.common.EnumWithCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskEventType implements EnumWithCode<Integer> {

  EVENT_UNKNOWN(-1, "Onbekend", ""),
  EVENT_MISC(1, "Overige", ""),
  EVENT_ZAAK(2, "Zaakgerelateerd", ""),
  EVENT_MUT_OVERL(100, "Registratie overlijden in categorie 06",
      "De mutatie betreft de registratie van een overlijden in categorie 06");

  private final Integer code;
  private final String  description;
  private final String  info;

  public static TaskEventType get(Number code) {
    return EnumUtils.get(values(), code, null);
  }

  public static List<TaskEventType> getEventTypes(Predicate<TaskEventType> predicate) {
    return Arrays.stream(values()).filter(predicate).collect(Collectors.toList());
  }

  public boolean is(TaskEventType... type) {
    return Arrays.asList(type).contains(this);
  }

  @Override
  public String toString() {
    return description;
  }
}
