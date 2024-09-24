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

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.inject.servlet.RequestScoped;

import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.rest.v1_0.zaak.bestand.GbaRestZaakBestand;
import nl.procura.gba.web.rest.v1_0.zaak.bestand.GbaRestZaakBestandAntwoord;
import nl.procura.gba.web.rest.v1_0.zaak.bestand.GbaRestZaakBestandToevoegenVraag2;
import nl.procura.gba.web.rest.v1_0.zaak.bestand.GbaRestZaakBestandUpdatenVraag;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSFileContent;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSService;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid;
import nl.procura.gba.web.services.zaken.documenten.printen.DocumentenPrintenService;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;
import nl.procura.commons.core.exceptions.ProException;

@RequestScoped
@Path("v1.0/zaak/bestand")
@AuthenticatieVereist
public class GbaRestZaakBestandResources extends GbaRestServiceResource {

  @POST
  @Consumes(MediaType.APPLICATION_OCTET_STREAM)
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/toevoegen/{zaakid}/{bestandsnaam}")
  public GbaRestZaakAntwoord documentToevoegen(
      @PathParam("zaakid") String zaakId,
      @PathParam("bestandsnaam") String bestandsnaam, InputStream is) {

    DMSService dms = getServices().getDmsService();
    DocumentService documentService = getServices().getDocumentService();
    DocumentVertrouwelijkheid vertr = documentService.getStandaardVertrouwelijkheid(null, null);

    try {

      File bestand = DocumentenPrintenService.newTijdelijkBestand(bestandsnaam);
      FileUtils.copyInputStreamToFile(is, bestand);
      Zaak zaak = getMinimaleZaak(zaakId);
      String naam = getServices().getGebruiker().getNaam();

      DMSDocument dmsDocument = DMSDocument
          .builder()
          .content(DMSFileContent.from(bestand))
          .user(naam)
          .zaakId(zaak.getZaakId())
          .confidentiality(vertr.getNaam())
          .build();

      dms.save(zaak, dmsDocument);

    } catch (IOException exception) {
      throw new ProException(WARNING, "Fout bij wegschrijven bestand", exception);
    }

    return new GbaRestZaakAntwoord();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/toevoegen2")
  public GbaRestZaakAntwoord documentToevoegen2(GbaRestZaakBestandToevoegenVraag2 vraag) {

    DMSService dms = getServices().getDmsService();
    DocumentService documentService = getServices().getDocumentService();
    DocumentVertrouwelijkheid vertr = documentService.getStandaardVertrouwelijkheid(null, null);

    try {
      File bestand = DocumentenPrintenService.newTijdelijkBestand(vraag.getBestandsNaam());
      IOUtils.write(vraag.getInhoud(), Files.newOutputStream(bestand.toPath()));
      Zaak zaak = getMinimaleZaak(vraag.getZaakId());
      String naam = getServices().getGebruiker().getNaam();

      DMSDocument dmsDocument = DMSDocument.builder()
          .content(DMSFileContent.from(bestand))
          .title(vraag.getTitel())
          .user(naam)
          .confidentiality(vertr.getNaam())
          .build();

      dms.save(zaak, dmsDocument);

    } catch (IOException exception) {
      throw new ProException(WARNING, "Fout bij wegschrijven bestand", exception);
    }

    return new GbaRestZaakAntwoord();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/updaten")
  public GbaRestZaakAntwoord documentUpdaten(GbaRestZaakBestandUpdatenVraag vraag) {
    if (emp(vraag.getCollectie())) {
      throw new IllegalArgumentException("Geen collectie");
    }

    if (emp(vraag.getBestandId())) {
      throw new IllegalArgumentException("Geen id");
    }

    DMSService dms = getServices().getDmsService();
    dms.updateMetadata(vraag.getCollectie(), vraag.getBestandId(), vraag.getMetadata());
    return new GbaRestZaakAntwoord();
  }

  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/verwijderen/{zaakid}/{bestandsnaam}")
  public GbaRestZaakAntwoord documentVerwijderen(
      @PathParam("zaakid") String zaakId,
      @PathParam("bestandsnaam") String bestandsnaam) {

    if (emp(zaakId)) {
      throw new IllegalArgumentException("Geen zaak-id");
    }

    if (emp(bestandsnaam)) {
      throw new IllegalArgumentException("Geen bestandsnaam");
    }

    Zaak zaak = getStandaardZaak(getMinimaleZaak(zaakId)); // Standaardzaak nodig voor opvragen bsn voor burgerlijke stand
    List<DMSDocument> dmsDocumenten = getDmsDocumenten(zaak, bestandsnaam);

    if (dmsDocumenten.isEmpty()) {
      throw new ProException(WARNING, "Bestand met naam " + bestandsnaam + " niet gevonden");
    }

    for (DMSDocument bestand : dmsDocumenten) {
      getServices().getDmsService().delete(bestand);
    }

    return new GbaRestZaakAntwoord();
  }

  @GET
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Path("/zoeken/{zaakid}/{bestandsnaam}")
  public Response getBestand(
      @PathParam("zaakid") String zaakId,
      @PathParam("bestandsnaam") String bestandsnaam) {

    if (emp(zaakId)) {
      throw new IllegalArgumentException("Geen zaak-id");
    }

    if (emp(bestandsnaam)) {
      throw new IllegalArgumentException("Geen bestandsnaam");
    }

    Zaak zaak = getStandaardZaak(getMinimaleZaak(zaakId)); // Standaardzaak nodig voor opvragen bsn voor burgerlijke stand
    for (DMSDocument dmsDocument : getDmsDocumenten(zaak, bestandsnaam)) {
      return getResponse(dmsDocument.getContent().getInputStream(), dmsDocument.getContent().getExtension());
    }

    throw new ProException(WARNING, "Bestand met naam " + bestandsnaam + " niet gevonden");
  }

  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/zoeken/{zaakid}")
  public GbaRestZaakBestandAntwoord getBestanden(@PathParam("zaakid") String zaakId) {
    GbaRestZaakBestandAntwoord antwoord = new GbaRestZaakBestandAntwoord();
    antwoord.setBestanden(getDmsDocumenten(getStandaardZaak(getMinimaleZaak(zaakId)), "")
        .stream()
        .map(GbaRestZaakBestandResources::toDoc)
        .collect(Collectors.toList()));
    return antwoord;
  }

  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/zoeken")
  public GbaRestZaakBestandAntwoord getBestandenByQuery(@QueryParam("query") String query) {
    GbaRestZaakBestandAntwoord antwoord = new GbaRestZaakBestandAntwoord();
    antwoord.setBestanden(getServices().getDmsService().getDocumentsByQuery(query)
        .getDocuments().stream()
        .map(GbaRestZaakBestandResources::toDoc)
        .collect(Collectors.toList()));
    return antwoord;
  }

  private static GbaRestZaakBestand toDoc(DMSDocument dmsDocument) {
    GbaRestZaakBestand bestand = new GbaRestZaakBestand();
    bestand.setCollectie(dmsDocument.getCollection());
    bestand.setId(dmsDocument.getUuid());
    bestand.setDatum(dmsDocument.getDate());
    bestand.setTijd(dmsDocument.getTime());
    bestand.setBestandsnaam(dmsDocument.getContent().getFilename());
    bestand.setTitel(dmsDocument.getTitle());
    bestand.setZaakId(dmsDocument.getZaakId());
    bestand.setGebruiker(dmsDocument.getUser());
    bestand.setDocumentTypeOmschrijving(dmsDocument.getDocumentTypeDescription());
    bestand.setVertrouwelijkheid(dmsDocument.getConfidentiality());
    bestand.setAlias(dmsDocument.getAlias());
    bestand.setOverige(dmsDocument.getOtherProperties());
    return bestand;

  }

  private List<DMSDocument> getDmsDocumenten(Zaak zaak, String bestandsnaam) {
    return getServices().getDmsService().getDocumentsByZaak(zaak).getDocuments().stream()
        .filter(d -> StringUtils.equals(d.getZaakId(), zaak.getZaakId()))
        .filter(d -> isBlank(bestandsnaam)
            || StringUtils.equals(d.getContent().getFilename(), bestandsnaam))
        .collect(Collectors.toList());
  }
}
