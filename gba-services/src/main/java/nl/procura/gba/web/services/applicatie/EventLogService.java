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

package nl.procura.gba.web.services.applicatie;

import java.util.List;

import javax.inject.Inject;

import nl.procura.gba.jpa.personen.dao.EventLogDao;
import nl.procura.gba.jpa.personen.dao.EventLogSearch;
import nl.procura.gba.jpa.personen.db.EventLog;
import nl.procura.gba.jpa.personen.db.EventLogEntity;
import nl.procura.gba.jpa.personen.db.EventType;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;

public class EventLogService {

  private final EventLogDao eventLogDao;

  @Inject
  public EventLogService(EventLogDao eventLogDao) {
    this.eventLogDao = eventLogDao;
  }

  public void addEvent(EventLogEntity entity, EventType eventType, Gebruiker gebruiker) {
    Long userId = gebruiker == null ? -1L : gebruiker.getCUsr();
    eventLogDao.add(entity, eventType, userId);
  }

  public List<EventLog> getEvents(EventLogSearch search) {
    return eventLogDao.find(search);
  }
}
