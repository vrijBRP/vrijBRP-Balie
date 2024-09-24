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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.page3;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.algemeen.tasks.Task;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskService;
import nl.procura.gba.web.theme.GbaWebTheme;

public class Page3TaakTable extends GbaTable {

  public Page3TaakTable() {
  }

  @Override
  public void setColumns() {
    setSelectable(true);
    setMultiSelect(true);

    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);
    addStyleName(GbaWebTheme.TABLE.ALIGN_TOP);

    addColumn("Nr", 30);
    addColumn("Gebeurtenis", 300);
    addColumn("Taak");
  }

  @Override
  public void setRecords() {
    clear();
    int nr = 1;
    TaskService taskService = getApplication().getServices().getTaskService();
    for (Task task : taskService.getOpenUserTasks()) {
      Record record = addRecord(task);
      record.addValue(nr++);
      record.addValue(task.getEventType().getDescription());
      record.addValue(task.getDescr());
    }

    super.setRecords();
  }
}
