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

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.page2.Page2TaskBean.EVENT;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.page2.Page2TaskBean.OMS;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.page2.Page2TaskBean.OPMERKINGEN;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.page2.Page2TaskBean.STATUS;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.page2.Page2TaskBean.TASK_TYPE;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.page2.Page2TaskBean.UITVOERDER;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.page2.Page2TaskBean.UITVOERDER_TYPE;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.page2.Page2TaskBean.ZAAK;
import static nl.procura.gba.web.services.zaken.algemeen.tasks.TaskExecutionType.MANUAL;

import java.util.List;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.containers.GebruikerContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.gba.web.services.zaken.algemeen.tasks.Task;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskExecutionType;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskType;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2TaskForm extends GbaForm<Page2TaskBean> {

  private Task task;

  public Page2TaskForm(Task task) {
    this.task = task;
    setOrder(EVENT, ZAAK, TASK_TYPE, OMS, UITVOERDER_TYPE, UITVOERDER, STATUS, OPMERKINGEN);
    setColumnWidths(WIDTH_130, "");
    setReadonlyAsText(false);

    Page2TaskBean bean = new Page2TaskBean();
    bean.setTaskType(task.getTaskType());
    bean.setOms(task.getDescr());
    bean.setUitvoerderType(task.getExecutionType());

    if (task.getZaakId() != null) {
      bean.setEvent(task.getEventType().getDescription());
      bean.setZaak(task.getZaakId());

    } else {
      bean.setEvent("N.v.t.");
      bean.setZaak("");
    }

    bean.setStatus(task.getStatusType());
    bean.setOpmerkingen(task.getRemarks());

    setBean(bean);

    getField(OMS).setReadOnly(!task.getTaskType().isChangeDescription());
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {
    if (property.is(UITVOERDER)) {
      column.setAppend(true);
    }
    super.setColumn(column, field, property);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();

    // Uitvoerder
    GbaNativeSelect uitvoerder = getField(UITVOERDER, GbaNativeSelect.class);
    uitvoerder.setVisible(getBean().getUitvoerderType() == MANUAL);
    uitvoerder.setContainerDataSource(getGebruikerContainer());

    if (task.getUser() != null) {
      uitvoerder.setValue(new UsrFieldValue(task.getCUsr(), task.getUser().getUsrfullname()));
    }

    // Uitvoerder type
    GbaNativeSelect uitvoerderType = getField(UITVOERDER_TYPE, GbaNativeSelect.class);
    uitvoerderType.addListener((ValueChangeListener) valueChangeEvent -> {
      TaskExecutionType type = (TaskExecutionType) valueChangeEvent.getProperty().getValue();
      if (type != null) {
        updateUitvoerder(uitvoerder, type);
        repaint();
      }
    });

    // Tasktype
    GbaNativeSelect taskType = getField(TASK_TYPE, GbaNativeSelect.class);
    taskType.setContainerDataSource(new TaskTypeContainer(task.getEventType()));
    taskType.addListener((ValueChangeListener) valueChangeEvent -> {
      TaskType type = (TaskType) valueChangeEvent.getProperty().getValue();
      if (type != null) {
        setTaskDescriptionField(type);
        updateUitvoerderType(uitvoerderType, uitvoerder, type, type.getExecutionTypes().get(0));
        repaint();
      }
    });

    setTaskDescriptionField(task.getTaskType());
    updateUitvoerderType(uitvoerderType, uitvoerder, task.getTaskType(), task.getExecutionType());
  }

  private void updateUitvoerderType(GbaNativeSelect uitvoerderType,
      GbaNativeSelect uitvoerder,
      TaskType type,
      TaskExecutionType executionType) {

    TaskUitvoerderContainer source = new TaskUitvoerderContainer(type);
    uitvoerderType.setContainerDataSource(source);
    uitvoerderType.setValue(executionType);
    updateUitvoerder(uitvoerder, executionType);
  }

  private void updateUitvoerder(GbaNativeSelect uitvoerder, TaskExecutionType type) {
    if (type == MANUAL) {
      uitvoerder.setVisible(true);
      if (getApplication() != null) {
        uitvoerder.setValue(new UsrFieldValue(getApplication().getServices().getGebruiker()));
      }
    } else {
      uitvoerder.setVisible(false);
      uitvoerder.setValue(null);
    }
  }

  private void setTaskDescriptionField(TaskType type) {
    if (type.isChangeDescription()) {
      getField(OMS).setVisible(true);
    } else {
      getField(OMS).setValue("");
      getField(OMS).setVisible(false);
    }
  }

  private static GebruikerContainer getGebruikerContainer() {
    Services services = Services.getInstance();
    GebruikerService gebruikerService = services.getGebruikerService();
    List<Gebruiker> gebruikers = gebruikerService.getGebruikers(false);
    return new GebruikerContainer(gebruikers, false);
  }
}
