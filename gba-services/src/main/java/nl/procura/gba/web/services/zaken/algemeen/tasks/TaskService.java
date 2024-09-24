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

package nl.procura.gba.web.services.zaken.algemeen.tasks;

import static nl.procura.gba.web.services.zaken.algemeen.tasks.TaskStatusType.OPEN;

import java.util.List;
import java.util.stream.Collectors;

import nl.procura.gba.jpa.personen.dao.TaskDao;
import nl.procura.gba.jpa.personen.db.TaskEntity;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.java.reflection.ReflectionUtil;

public class TaskService extends AbstractService {

  public TaskService() {
    super("Taken");
  }

  @ThrowException("Fout bij ophalen taken")
  public List<Task> getByZaakId(String zaakId) {
    return TaskDao.getByZaakId(zaakId)
        .stream().map(this::toTask)
        .collect(Collectors.toList());
  }

  @ThrowException("Fout bij ophalen taken")
  public List<Task> getOpenUserTasks() {
    long cUsr = getServices().getGebruiker().getCUsr();
    return TaskDao.getByUsrAndStatus(cUsr, OPEN.getCode())
        .stream().map(this::toTask)
        .collect(Collectors.toList());
  }

  @Transactional
  @ThrowException("Fout bij opslaan taak")
  public void save(Task task) {
    if (task.getExecutionType() == TaskExecutionType.MANUAL) {
      if (task.hasNoUser()) {
        task.setUser(getServices().getGebruiker());
      }
    }
    if (task.getExecutionType() == TaskExecutionType.SKIP) {
      task.setStatus(TaskStatusType.CLOSED.getCode());
    }
    saveEntity(task);
    callListeners(ServiceEvent.CHANGE);
  }

  @Transactional
  @ThrowException("Fout bij opslaan taken")
  public void save(TaskEvent taskEvent) {
    taskEvent.getTasks().forEach(this::save);
    callListeners(ServiceEvent.CHANGE);
  }

  @Transactional
  @ThrowException("Fout bij verwijderen")
  public void delete(Task task) {
    removeEntity(task);
    callListeners(ServiceEvent.CHANGE);
  }

  @Transactional
  @ThrowException("Fout bij verwijderen")
  public void delete(Zaak zaak) {
    getByZaakId(zaak.getZaakId()).forEach(this::removeEntity);
    callListeners(ServiceEvent.CHANGE);
  }

  public Task newTask(String zaakId, TaskType taskType) {
    Task task = new Task();
    task.setDescr(taskType.isChangeDescription() ? null : taskType.getDescription());
    task.setRemarks("");
    task.setStatus(TaskStatusType.OPEN.getCode());
    task.setCUsr(0L);
    task.setType(taskType.getCode());
    task.setEvent(taskType.getEventType().getCode());
    task.setZaakId(zaakId);
    return task;
  }

  private Task toTask(TaskEntity taskEntity) {
    Task task = ReflectionUtil.deepCopyBean(Task.class, taskEntity);
    task.setUser(getServices().getGebruikerService().getGebruikerByCode(taskEntity.getCUsr(), false));
    return task;
  }
}
