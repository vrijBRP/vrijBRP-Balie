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

package nl.procura.gba.web.rest.v1_0.document;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import java.io.ByteArrayInputStream;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.servlet.RequestScoped;

import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.rest.v1_0.document.contactgegevens.GbaRestDocument;
import nl.procura.gba.web.rest.v1_0.document.contactgegevens.GbaRestDocumentAntwoord;
import nl.procura.gba.web.rest.v1_0.document.genereren.GbaRestDocumentGenererenVraag;
import nl.procura.gba.web.rest.v1_0.document.genereren.GbaRestDocumentPersoon;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentZaakArgumenten;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsZaakArgumenten;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;
import nl.procura.proweb.rest.v1_0.meldingen.ProRestMeldingen;

@RequestScoped
@Path("v1.0/document")
@AuthenticatieVereist
public class GbaRestDocumentResources extends GbaRestServiceResource {

  @POST
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/genereren")
  public Response genereren(GbaRestDocumentGenererenVraag vraag) {

    GbaRestDocumentGenererenHandler handler = new GbaRestDocumentGenererenHandler(getServices());

    DocumentRecord document;

    if (pos(vraag.getDocumentCode())) {
      document = handler.getDocumentByAlias(vraag.getDocumentCode());
    } else if (fil(vraag.getDocumentDmsNaam())) {
      document = handler.getDocumentByAlias(vraag.getDocumentDmsNaam());
    } else {
      throw new IllegalArgumentException("Geen documentcode of dms-naam opgegeven");
    }

    byte[] bytes;

    if (vraag.getPersoon() != null) {
      bytes = handler.getDocumentByAlias(document, vraag.getDocumentType(), vraag.getPersoon());
    } else if (vraag.getZaak() != null) {
      bytes = handler.getDocumentByAlias(document, vraag.getDocumentType(), vraag.getZaak());
    } else {
      throw new IllegalArgumentException("Zowel persoon als zaak zijn niet gevuld");
    }

    return getResponse(new ByteArrayInputStream(bytes), vraag.getDocumentType());
  }

  @GET
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("/genereren/{code}/{type}/{bsn}")
  public Response genereren(@PathParam("code") long code, @PathParam("type") String type,
      @PathParam("bsn") String bsn) {

    GbaRestDocumentGenererenHandler handler = new GbaRestDocumentGenererenHandler(getServices());

    DocumentRecord document = handler.getDocumentByAlias(code);
    byte[] bytes = handler.getDocumentByAlias(document, type, new GbaRestDocumentPersoon(bsn));

    return getResponse(new ByteArrayInputStream(bytes), type);
  }

  /**
   * Wordt o.a. gebruikt door Swisslog. Niet wijzigen s.v.p.
   */
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/documentnummer/{documentnummer}")
  public GbaRestDocumentAntwoord get(@PathParam("documentnummer") String documentnummer) {

    GbaRestDocumentContactgevensHandler handler = new GbaRestDocumentContactgevensHandler(getServices());

    GbaRestDocumentAntwoord antwoord = new GbaRestDocumentAntwoord();

    GbaRestDocument document = handler.getRijbewijsProweb(documentnummer, "");

    if (document == null) {

      document = handler.getReisdocumentProweb(documentnummer, "");

      if (document == null) {

        document = handler.getReisdocumentBackoffice(new ReisdocumentZaakArgumenten(documentnummer));

        if (document == null) {

          document = handler.getRijbewijsBackoffice(new RijbewijsZaakArgumenten(documentnummer));

          if (document == null) {

            antwoord.getMeldingen().add(ProRestMeldingen.GEEN_GEGEVENS);
          }
        }
      }
    }

    if (document != null) {
      antwoord.setDocument(document);
    }

    return antwoord;
  }
}
