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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task;

import static java.util.stream.Collectors.groupingBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.vaadin.data.Validator;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task.page2.TaskUitvoerderContainer;
import nl.procura.gba.web.services.zaken.algemeen.tasks.Task;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskEvent;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskEventType;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskExecutionType;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskType;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.theme.ProcuraWindow;
import nl.procura.vaadin.theme.twee.ProcuraTheme.ICOON_24;

public class TaskSelectieWindow extends GbaModalWindow {

  private final TaskEvent       taskEvent;
  private final Runnable        runnable;
  private final List<TaskField> taskFields = new ArrayList<>();
  private final Label           errorLabel = new Label();
  private boolean               saved;

  public static void init(ProcuraWindow window, TaskEvent taskEvent, Runnable runnable) {
    if (taskEvent.getTasks().isEmpty()) {
      runnable.run();
    } else {
      TaskSelectieWindow taskSelectieWindow = new TaskSelectieWindow(taskEvent, runnable);
      getGbaApplication(window).getParentWindow().addWindow(taskSelectieWindow);
    }
  }

  private TaskSelectieWindow(TaskEvent taskEvent, Runnable runnable) {
    this.taskEvent = taskEvent;
    this.runnable = runnable;
    setClosable(false);
    setCaption("Taken");
    setWidth("800px");
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(true, new Page()));
  }

  public class Page extends NormalPageTemplate {

    public Page() {
      setMargin(false);
    }

    @Override
    protected void initPage() {
      addButton(buttonSave);

      for (Task task : taskEvent.getTasks()) {
        GbaNativeSelect field = newField(task);
        field.setValue(TaskExecutionType.get(task.getExecution()));
        taskFields.add(new TaskField(task, field));
      }

      for (Entry<TaskEventType, List<TaskField>> entry : taskFields
          .stream()
          .collect(groupingBy(tf -> tf.getTask().getEventType()))
          .entrySet()) {

        TableLayout tasksLayout = new TableLayout();
        tasksLayout.addStyleName("v-form tableform v-form-error");
        tasksLayout.setColumnWidths("500px", "", "30px");

        for (TaskField taskField : entry.getValue()) {
          tasksLayout.addLabel(taskField.getTask().getTaskType().getDescription());
          tasksLayout.addData(taskField.getField());
          tasksLayout.addData(getInfoButton(taskField.getTask().getTaskType(), taskField.getTask().getRemarks()));
        }
        addComponent(new Fieldset(entry.getKey().getDescription()));
        addComponent(new InfoLayout(entry.getKey().getInfo()));
        addComponent(tasksLayout);
      }

      updateTasks();
      addComponent(errorLabel);
      super.initPage();
    }

    private Button getInfoButton(TaskType taskType, String remarks) {
      NativeButton button = new NativeButton("");
      button.setHeight("23px");
      button.setWidth("20px");
      button.setStyleName(GbaWebTheme.BUTTON_LINK);
      button.addStyleName("pl-url-button");
      button.setIcon(new ThemeResource(ICOON_24.INFORMATION));
      button.setDescription("Informatie over deze taak");
      button.addListener((ClickListener) event -> getApplication().getParentWindow()
          .addWindow(new TaskInfoWindow(taskType, remarks)));
      return button;
    }

    @Override
    public void onClose() {
      getWindow().closeWindow();
    }

    @Override
    public void onSave() {
      for (TaskField taskField : taskFields) {
        GbaNativeSelect field = taskField.getField();
        try {
          field.validate();
          taskField.getTask().setExecutionType((TaskExecutionType) field.getValue());
        } catch (Exception e) {
          field.setStyleName("v-textfield-error v-select-error v-filterselect-error");

          // Update errorLabel
          if (errorLabel == null) {
            errorLabel.setStyleName("v-form-errormessage");

            if (e instanceof Validator.EmptyValueException) {
              errorLabel.setValue("Veld \"" + field.getCaption() + "\" is verplicht");
              errorLabel.setVisible(true);
            } else {
              errorLabel.setValue(e.getMessage());
              errorLabel.setVisible(false);
            }
          }
        }
      }
      updateTasks();
      saved = true;
      close();
    }

    private void updateTasks() {
      runnable.run();
    }
  }

  @Override
  protected void close() {
    if (saved) {
      super.close();
    }
  }

  private static GbaApplication getGbaApplication(ProcuraWindow window) {
    return (GbaApplication) window.getApplication();
  }

  private GbaNativeSelect newField(Task task) {
    GbaNativeSelect select = new GbaNativeSelect();
    select.setContainerDataSource(new TaskUitvoerderContainer(task.getTaskType()));
    select.setValidationVisible(true);
    select.setNullSelectionAllowed(false);
    select.setWidth("130px");
    return select;
  }

  private static class TaskField {

    private final Task            task;
    private final GbaNativeSelect field;

    public TaskField(Task task, GbaNativeSelect field) {
      this.task = task;
      this.field = field;
    }

    public Task getTask() {
      return task;
    }

    public GbaNativeSelect getField() {
      return field;
    }
  }
}
