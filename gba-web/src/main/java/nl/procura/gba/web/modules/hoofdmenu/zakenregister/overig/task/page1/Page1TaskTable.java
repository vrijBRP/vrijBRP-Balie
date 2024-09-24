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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.page1;

import static nl.procura.gba.web.services.zaken.algemeen.tasks.TaskStatusType.CLOSED;

import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.algemeen.tasks.Task;

public class Page1TaskTable extends GbaTable {

  private String zaakId;

  public Page1TaskTable() {
  }

  public Page1TaskTable(String zaakId) {
    this.zaakId = zaakId;
  }

  @Override
  public void setColumns() {
    setSelectable(true);
    setMultiSelect(true);

    addColumn("Nr", 30).setUseHTML(true);
    addColumn("Taak", 300).setUseHTML(true);
    addColumn("Aanleiding", 300).setUseHTML(true);
    addColumn("Uitvoerder", 200).setUseHTML(true);
    addColumn("Status", 100).setUseHTML(true);
    addColumn("Toelichting").setUseHTML(true);
  }

  @Override
  public void setRecords() {
    int nr = 1;
    clear();
    List<Task> tasks = getApplication().getServices().getTaskService().getByZaakId(zaakId);
    for (Task task : tasks) {
      Record r = addRecord(task);
      r.addValue(nr++);
      r.addValue(toHtmlOmschrijving(task, task.getDescr()));
      r.addValue(toHtmlOmschrijving(task, task.getEventType()));
      r.addValue(toHtmlOmschrijving(task, task.getExecutionDescription()));
      r.addValue(toHtmlOmschrijving(task, task.getStatusType()));
      r.addValue(toHtmlOmschrijving(task, task.getRemarks()));
    }

    super.setRecords();
  }

  private String toHtmlOmschrijving(Task task, Object value) {
    if (CLOSED.is(task.getStatusType())) {
      return "<strike>" + value + "</strike>";
    } else {
      return value.toString();
    }
  }
}
