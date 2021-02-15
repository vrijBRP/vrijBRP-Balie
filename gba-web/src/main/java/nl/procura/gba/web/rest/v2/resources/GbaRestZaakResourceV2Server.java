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

import static nl.procura.gba.web.rest.v2.GbaRestZaakResourceV2.BASE_ZAKEN_URI;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.servlet.RequestScoped;

import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.rest.v2.GbaRestDocumentV2;
import nl.procura.gba.web.rest.v2.GbaRestZaakResourceV2;
import nl.procura.gba.web.rest.v2.model.base.GbaRestAntwoord;
import nl.procura.gba.web.rest.v2.model.zaken.*;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaak;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakDocument;
import nl.procura.gba.web.rest.v2.services.GbaRestServices;
import nl.procura.gba.web.services.zaken.algemeen.dms.DmsStream;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;
import nl.procura.standard.exceptions.ProException;

@RequestScoped
@Path(BASE_ZAKEN_URI)
@AuthenticatieVereist
public class GbaRestZaakResourceV2Server extends GbaRestServiceResource
    implements GbaRestZaakResourceV2, GbaRestDocumentV2 {

  private static final Logger LOGGER = LoggerFactory.getLogger(GbaRestZaakResourceV2Server.class);

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path(GET_ZAAK_BY_ZAAK_ID_URI)
  public GbaRestAntwoord<GbaRestZaak> getZaakByZaakId(@PathParam("zaakId") String zaakId) {
    return tryCall(
        () -> new GbaRestAntwoord<>(new GbaRestServices(getServices())
            .getZaakService()
            .getByZaakId(zaakId)));
  }

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path(GET_DOCUMENTS_BY_ZAAK_ID_URI)
  public GbaRestAntwoord<GbaRestZaakDocumentenZoekenAntwoord> getDocumentsByZaakId(@PathParam("zaakId") String zaakId) {
    return tryCall(
        () -> new GbaRestAntwoord<>(new GbaRestServices(getServices())
            .getDmsService()
            .getDocumentsByZaakId(zaakId)));
  }

  @Override
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path(FIND_ZAAK_URI)
  public GbaRestAntwoord<GbaRestZaakZoekenAntwoord> findZaken(GbaRestZaakZoekenVraag request) {
    return tryCall(
        () -> new GbaRestAntwoord<>(new GbaRestServices(getServices())
            .getZaakService()
            .search(request)));
  }

  @Override
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path(ADD_ZAAK_URI)
  public GbaRestAntwoord<GbaRestZaak> addZaak(GbaRestZaakToevoegenVraag request) {
    return tryCall(
        () -> new GbaRestAntwoord<>(new GbaRestServices(getServices())
            .getZaakService()
            .addZaak(request)));
  }

  @Override
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path(UPDATE_ZAAK_URI)
  public GbaRestAntwoord<GbaRestZaak> updateZaak(GbaRestZaakUpdateVraag request) {
    return tryCall(
        () -> new GbaRestAntwoord<>(new GbaRestServices(getServices())
            .getZaakService()
            .updateZaak(request)));
  }

  @Override
  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @Path(DELETE_ZAAK_BY_ZAAK_ID_URI)
  public GbaRestAntwoord<?> deleteZaakByZaakId(@PathParam("zaakId") String zaakId) {
    return tryCall(() -> {
      new GbaRestServices(getServices())
          .getZaakService()
          .deleteByZaakId(zaakId);
      return new GbaRestAntwoord<>();
    });
  }

  @Override
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path(UPDATE_ZAAK_STATUS_URI)
  public GbaRestAntwoord<?> updateZaakStatus(GbaRestZaakStatusUpdateVraag request) {
    return tryCall(() -> {
      new GbaRestServices(getServices())
          .getZaakService()
          .updateZaakStatus(request);
      return new GbaRestAntwoord<>();
    });
  }

  @Override
  @GET
  @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM })
  @Path(GET_DOCUMENT_BY_ID_URI)
  public Response getDocumentById(
      @PathParam("zaakId") String zaakId,
      @PathParam("id") String documentId) {
    Response.ResponseBuilder builder;
    try {
      DmsStream dmsStream = new GbaRestServices(getServices())
          .getDmsService()
          .getDocumentByZaakId(zaakId, documentId);
      if (dmsStream != null) {
        builder = Response.ok(dmsStream.getInputStream(), MediaType.APPLICATION_OCTET_STREAM);
        builder = builder.header("Content-Disposition", "attachment; filename=\"" + dmsStream.getUitvoernaam() + "\"");
        return builder.build();
      } else {
        throw new ProException("Geen document met id: " + documentId);
      }
    } catch (RuntimeException ex) {
      LOGGER.error(ex.getMessage(), ex);
      return Response
          .serverError()
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
          .entity(new GbaRestAntwoord<>(ex.getMessage()))
          .build();
    }
  }

  @Override
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path(BASE_DOCUMENTS_URI)
  public GbaRestAntwoord<GbaRestZaakDocument> addDocument(@PathParam("zaakId") String zaakId,
      GbaRestZaakDocumentToevoegenVraag request) {
    return tryCall(() -> new GbaRestAntwoord<>(new GbaRestServices(getServices())
        .getDmsService()
        .addDocument(zaakId, request)));
  }
}
