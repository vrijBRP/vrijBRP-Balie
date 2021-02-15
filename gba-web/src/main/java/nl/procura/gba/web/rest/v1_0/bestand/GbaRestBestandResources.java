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

package nl.procura.gba.web.rest.v1_0.bestand;

import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.inject.servlet.RequestScoped;

import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.standard.exceptions.ProException;

/**

 * <p>
 * Wordt o.a. gebruikt door Haarlem. Niet wijzigen s.v.p.
 */
@RequestScoped
@Path("v1.0/bestand")
public class GbaRestBestandResources extends GbaRestServiceResource {

  @GET
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("/zoeken/{bestandid}")
  public Response get(@PathParam("bestandid") String bestandsNaam) {

    ResponseBuilder builder;

    try {

      InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(bestandsNaam);

      if (inStream == null) {
        throw new ProException(WARNING, "Geen bestand gevonden: " + bestandsNaam);
      }

      long size = inStream.available();
      builder = Response.ok(new BufferedInputStream(inStream), MediaType.APPLICATION_OCTET_STREAM);
      builder = builder.header("content-Disposition", "attachment; filename = " + bestandsNaam);
      builder = builder.header("Content-Length", size);
    } catch (IOException e) {
      throw new ProException(WARNING, "Onbekende fout: " + e.getMessage());
    }

    return builder.build();
  }
}
