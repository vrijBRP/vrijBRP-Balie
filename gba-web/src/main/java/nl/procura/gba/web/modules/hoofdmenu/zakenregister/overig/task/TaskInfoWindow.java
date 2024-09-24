package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.task;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskType;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class TaskInfoWindow extends GbaModalWindow {

  public TaskInfoWindow(TaskType taskType, String remarks) {
    this("Informatie", taskType.getDescription(), remarks);
  }

  public TaskInfoWindow(String title, String description, String remarks) {
    super(title + " (Druk op escape om te sluiten)", "600px");
    GbaVerticalLayout v = new GbaVerticalLayout(true);
    v.addComponent(new Fieldset(description));
    v.addComponent(new InfoLayout("", remarks.replaceAll("\n", "<hr>")));
    addComponent(v);
  }

  @Override
  public void attach() {
    this.focus();
    super.attach();
  }
}
