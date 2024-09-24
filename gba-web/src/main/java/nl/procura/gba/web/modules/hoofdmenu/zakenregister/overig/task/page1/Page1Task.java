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

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.TaskWindow;
import nl.procura.gba.web.services.zaken.algemeen.tasks.Task;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskService;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page1Task extends NormalPageTemplate {

  private Page1TaskTable table;
  private final Runnable listener;
  private final String   zaakId;

  public Page1Task(String zaakId, Runnable listener) {
    this.zaakId = zaakId;
    setMargin(false);
    this.listener = listener;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addButton(buttonNew);
      addButton(buttonDel, 1f);

      if (getWindow() instanceof TaskWindow) {
        addButton(buttonClose);
        addComponent(new Page1TaskForm(zaakId));
      }

      table = new Page1TaskTable(zaakId) {

        @Override
        public void onDoubleClick(Record record) {
          getApplication().getParentWindow()
              .addWindow(new TaskWindow(record.getObject(Task.class), () -> {
                table.init();
                listener.run();
                VaadinUtils.resetHeight(getWindow());
              }));
        }
      };
      addComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onDelete() {
    new DeleteProcedure<Task>(table) {

      @Override
      public void deleteValue(Task task) {
        getServices().getTaskService().delete(task);
      }

      @Override
      protected void afterDelete() {
        listener.run();
      }
    };
  }

  @Override
  public void onNew() {
    TaskService taskService = getServices().getTaskService();
    Task task = taskService.newTask(zaakId, TaskType.TASK_ZAAK);
    getApplication().getParentWindow().addWindow(new TaskWindow(task, () -> {
      table.init();
      listener.run();
      VaadinUtils.resetHeight(getWindow());
    }));
    super.onNew();
  }
}
