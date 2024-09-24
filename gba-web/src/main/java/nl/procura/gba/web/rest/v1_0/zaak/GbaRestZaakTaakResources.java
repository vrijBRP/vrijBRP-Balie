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

package nl.procura.gba.web.rest.v1_0.zaak;

import static nl.procura.gba.web.services.zaken.algemeen.tasks.TaskExecutionType.AUTO;
import static nl.procura.gba.web.services.zaken.algemeen.tasks.TaskStatusType.CLOSED;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.servlet.RequestScoped;

import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.rest.v1_0.zaak.taak.GbaRestTaak;
import nl.procura.gba.web.rest.v1_0.zaak.taak.GbaRestTaakAntwoord;
import nl.procura.gba.web.rest.v1_0.zaak.taak.GbaRestZaakTaakAfsluitenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.taak.GbaRestZaakTaakZoekenVraag;
import nl.procura.gba.web.services.zaken.algemeen.tasks.Task;
import nl.procura.gba.web.services.zaken.algemeen.tasks.TaskService;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;
import nl.procura.standard.Globalfunctions;

@RequestScoped
@Path("v1.0/zaak/taak")
@AuthenticatieVereist
public class GbaRestZaakTaakResources extends GbaRestServiceResource {

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/zoeken")
  public GbaRestTaakAntwoord zoeken(GbaRestZaakTaakZoekenVraag vraag) {

    TaskService taskService = getServices().getTaskService();
    List<Task> tasks = taskService.getByZaakId(vraag.getZaakId());

    GbaRestTaakAntwoord antwoord = new GbaRestTaakAntwoord();
    antwoord.setTaken(tasks.stream()
        .map(GbaRestZaakTaakResources::toTask)
        .collect(Collectors.toList()));
    return antwoord;
  }

  private static GbaRestTaak toTask(Task task) {
    GbaRestTaak taak = new GbaRestTaak();
    taak.setId(task.getTaskType().getCode().toString());
    taak.setDescription(task.getDescr());
    taak.setClosed(task.getStatusType().is(CLOSED));
    taak.setAuto(task.getExecutionType().is(AUTO));
    return taak;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/afsluiten")
  public GbaRestTaakAntwoord afsluiten(GbaRestZaakTaakAfsluitenVraag vraag) {

    TaskService taskService = getServices().getTaskService();
    taskService.closeTask(vraag.getZaakId(), Globalfunctions.aval(vraag.getTaak()), vraag.getOpmerking());

    return new GbaRestTaakAntwoord();
  }
}
