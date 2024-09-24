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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.page2;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.zaken.algemeen.tasks.Task;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Task extends NormalPageTemplate {

  private final Task     task;
  private Page2TaskForm  form;
  private final Runnable runnable;

  public Page2Task(Task task, Runnable runnable) {
    this.runnable = runnable;
    setMargin(false);
    this.task = task;
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      addButton(buttonSave, 1f);
      addButton(buttonClose);
      form = new Page2TaskForm(task);
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onSave() {
    form.commit();

    Page2TaskBean bean = form.getBean();
    task.setExecution(bean.getUitvoerderType().getCode());
    task.setType(bean.getTaskType().getCode());
    task.setDescr(bean.getTaskType().isChangeDescription()
        ? bean.getOms()
        : bean.getTaskType().getDescription());

    if (bean.getUitvoerder() != null) {
      task.setCUsr(bean.getUitvoerder().getLongValue());
    } else {
      task.setCUsr(0L);
    }

    task.setStatus(bean.getStatus().getCode());
    task.setRemarks(bean.getOpmerkingen());
    getServices().getTaskService().save(task);

    successMessage("De taak is opgeslagen");
    runnable.run();
    onClose();
    super.onSave();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }
}
