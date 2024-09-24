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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.page2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nl.procura.gba.web.modules.bs.naturalisatie.aanschrijving.EnumContainer;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskExecutionType;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskType;

public class TaskUitvoerderContainer extends EnumContainer {

  public TaskUitvoerderContainer() {
    super(TaskExecutionType.values());
  }

  public TaskUitvoerderContainer(TaskType type) {
    super(getValues(type));
  }

  private static List<TaskExecutionType> getValues(TaskType type) {
    return Arrays.stream(TaskExecutionType.values())
        .filter(et -> type.getExecutionTypes().contains(et))
        .collect(Collectors.toList());
  }
}
