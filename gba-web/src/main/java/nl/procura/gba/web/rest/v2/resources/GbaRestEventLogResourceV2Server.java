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

package nl.procura.gba.web.rest.v2.resources;

import static java.util.stream.Collectors.toList;
import static nl.procura.gba.web.rest.v2.GbaRestEventLogResourceV2.BASE_EVENT_LOG_URI;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import nl.procura.burgerzaken.gba.StringUtils;
import nl.procura.gba.jpa.personen.dao.EventLogSearch;
import nl.procura.gba.jpa.personen.db.EventObjectType;
import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.rest.v2.GbaRestEventLogResourceV2;
import nl.procura.gba.web.rest.v2.converters.GbaRestEventLogConverter;
import nl.procura.gba.web.rest.v2.model.base.GbaRestAntwoord;
import nl.procura.gba.web.rest.v2.model.eventlog.GbaRestEventLog;
import nl.procura.gba.web.rest.v2.model.eventlog.GbaRestEventLogs;
import nl.procura.gba.web.services.applicatie.EventLogService;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;

@RequestScoped
@Path(BASE_EVENT_LOG_URI)
@AuthenticatieVereist
public class GbaRestEventLogResourceV2Server extends GbaRestServiceResource implements GbaRestEventLogResourceV2 {

  // injection in constructor doesn't work somehow
  @Inject
  private EventLogService service;

  /**
   * @param lastId last ID of event received, don't pass it the first time to receive events from the start
   * @param size maximum number of events returned
   * @return event list starting at last ID
   */
  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public GbaRestAntwoord<GbaRestEventLogs> getEvents(@QueryParam("lastId") Long lastId,
      @QueryParam("size") Integer size,
      @QueryParam("objectType") String objectType) {
    EventLogSearch.EventLogSearchBuilder search = EventLogSearch.builder()
        .lastId(lastId)
        .size(size);
    if (StringUtils.isNotBlank(objectType)) {
      search.objectType(EventObjectType.valueOf(objectType));
    }
    List<GbaRestEventLog> events = service.getEvents(search.build())
        .stream()
        .map(GbaRestEventLogConverter::toGbaRestEventLog)
        .collect(toList());
    return tryCall(() -> new GbaRestAntwoord<>(new GbaRestEventLogs(events)));
  }

}
