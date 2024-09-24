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

package nl.procura.gba.web.services.zaken.algemeen.tasks;

import java.util.Arrays;

import nl.procura.gba.jpa.personen.db.TaskEntity;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;

public class Task extends TaskEntity {

  private Gebruiker user;
  private Zaak      zaak;

  public Task() {
  }

  public String getExecutionDescription() {
    String description = getExecutionType().getDescription();
    if (user != null && user.getCUsr() > 0) {
      description += " (" + user.getUsrfullname() + ")";
    }
    return description;
  }

  public TaskStatusType getStatusType() {
    return TaskStatusType.get(getStatus());
  }

  public TaskExecutionType getExecutionType() {
    return TaskExecutionType.get(getExecution());
  }

  public void setExecutionType(TaskExecutionType type) {
    setExecution(type.getCode());
  }

  public TaskType getTaskType() {
    return TaskType.get(getType());
  }

  public TaskEventType getEventType() {
    return TaskEventType.get(getEvent());
  }

  public void setUser(Gebruiker user) {
    setCUsr(user.getCUsr());
    this.user = user;
  }

  public Gebruiker getUser() {
    return user;
  }

  public void setZaak(Zaak zaak) {
    this.zaak = zaak;
  }

  public Zaak getZaak() {
    return zaak;
  }

  public boolean hasNoUser() {
    return getCUsr() == null || getCUsr() <= 0;
  }

  public boolean is(TaskType... type) {
    return Arrays.asList(type).contains(this);
  }
}
