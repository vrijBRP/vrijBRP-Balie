package nl.procura.gba.web.services.zaken.algemeen.tasks;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TaskEvent {

  private String              zaakId;
  private List<TaskEventType> eventTypes;
  private List<Task>          tasks = new ArrayList<>();

  public TaskEvent(String zaakId, List<TaskEventType> eventTypes) {
    this.zaakId = zaakId;
    this.eventTypes = eventTypes;
  }
}
