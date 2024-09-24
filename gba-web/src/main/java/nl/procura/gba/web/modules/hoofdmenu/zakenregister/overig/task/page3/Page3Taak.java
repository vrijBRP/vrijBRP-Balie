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

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.TaskWindow;
import nl.procura.gba.web.services.zaken.algemeen.tasks.Task;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page3Taak extends NormalPageTemplate {

  private Page3TaakTable table;

  public Page3Taak() {
    setSpacing(true);
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      addButton(buttonClose);
      setInfo("Dubbelklik op een regel voor meer informatie.");

      table = new Page3TaakTable() {

        @Override
        public void onDoubleClick(Record record) {
          getApplication().getParentWindow()
              .addWindow(new TaskWindow(record.getObject(Task.class), () -> table.init()));
        }
      };
      addExpandComponent(table);
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
    };
  }
}
