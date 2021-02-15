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

package nl.procura.gba.web.rest.v2.converters;

import nl.procura.gba.jpa.personen.db.EventLog;
import nl.procura.gba.jpa.personen.db.EventObjectType;
import nl.procura.gba.jpa.personen.db.EventType;
import nl.procura.gba.web.rest.v2.model.eventlog.GbaRestEventLog;
import nl.procura.gba.web.rest.v2.model.eventlog.GbaRestEventObjectType;
import nl.procura.gba.web.rest.v2.model.eventlog.GbaRestEventType;

public final class GbaRestEventLogConverter {

  private GbaRestEventLogConverter() {
  }

  public static GbaRestEventLog toGbaRestEventLog(EventLog eventLog) {
    GbaRestEventLog restEventLog = new GbaRestEventLog();
    restEventLog.setEventId(eventLog.getCEventLog());
    restEventLog.setUserId(eventLog.getUserId());
    restEventLog.setEventType(toGbaRestEventType(eventLog.getType()));
    restEventLog.setObjectType(toGbaRestEventObjectType(eventLog.getObjectType()));
    restEventLog.setObjectId(eventLog.getObjectId());
    return restEventLog;
  }

  public static GbaRestEventType toGbaRestEventType(EventType type) {
    return GbaRestEventType.valueOf(type.name());
  }

  public static GbaRestEventObjectType toGbaRestEventObjectType(EventObjectType type) {
    return GbaRestEventObjectType.valueOf(type.name());
  }
}
