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

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.google.inject.servlet.RequestScoped;

import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.rest.v1_0.zaak.controles.GbaRestZaakControlesHandler;
import nl.procura.gba.web.rest.v1_0.zaak.dashboard.GbaRestDashboardAntwoord;
import nl.procura.gba.web.rest.v1_0.zaak.dashboard.GbaRestDashboardHandler;
import nl.procura.gba.web.rest.v1_0.zaak.dashboard.GbaRestDashboardVraag;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.GbaRestZaakToevoegenHandler;
import nl.procura.gba.web.rest.v1_0.zaak.toevoegen.GbaRestZaakToevoegenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.GbaRestZaakVerwerkenHandler;
import nl.procura.gba.web.rest.v1_0.zaak.verwerken.GbaRestZaakVerwerkenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.GbaRestZaakZoekenHandler;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;

@RequestScoped
@Path("v1.0/zaak")
@AuthenticatieVereist
public class GbaRestZaakResources extends GbaRestServiceResource {

  @POST
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/toevoegen")
  public GbaRestZaakAntwoord add(GbaRestZaakToevoegenVraag vraag) {
    return new GbaRestZaakAntwoord(new GbaRestZaakToevoegenHandler(getServices()).toevoegen(vraag));
  }

  @POST
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/verwerken")
  public GbaRestZaakAntwoord add(GbaRestZaakVerwerkenVraag vraag) {
    return new GbaRestZaakVerwerkenHandler(getServices()).verwerken(vraag);
  }

  @GET
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/controles")
  public GbaRestZaakAntwoord controles() {
    return new GbaRestZaakAntwoord(new GbaRestZaakControlesHandler(getServices()).getControles());
  }

  @POST
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/dashboard")
  public GbaRestDashboardAntwoord dashboard(GbaRestDashboardVraag vraag) {
    return new GbaRestDashboardAntwoord(new GbaRestDashboardHandler(getServices()).getDashboard(vraag));
  }

  @POST
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/zoeken")
  public GbaRestZaakAntwoord get(GbaRestZaakVraag vraag) {
    return new GbaRestZaakAntwoord(new GbaRestZaakZoekenHandler(getServices()).getZaken(vraag));
  }

  @GET
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/zoeken/zaakid/{zaakid}")
  public GbaRestZaakAntwoord get(@PathParam("zaakid") String zaakId) {
    GbaRestZaakVraag vraag = new GbaRestZaakVraag().setZaakId(zaakId);
    return new GbaRestZaakAntwoord(new GbaRestZaakZoekenHandler(getServices()).getZaken(vraag));
  }

  @POST
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/zoeken/sleutels")
  public GbaRestZaakSleutelAntwoord getSleutels(GbaRestZaakVraag vraag) {
    return new GbaRestZaakSleutelAntwoord(new GbaRestZaakZoekenHandler(getServices()).getZaakSleutels(vraag));
  }

  @POST
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/status/toevoegen")
  public GbaRestZaakAntwoord setStatussen(GbaRestZaakStatusUpdateVraag vraag) {
    GbaRestZaakZoekenHandler zaakhandler = new GbaRestZaakZoekenHandler(getServices());
    zaakhandler.updateStatus(vraag);
    return new GbaRestZaakAntwoord(zaakhandler.getZaken(new GbaRestZaakVraag().setZaakId(vraag.getZaakId())));
  }
}
