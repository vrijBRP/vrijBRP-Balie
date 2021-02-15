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

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static org.hamcrest.Matchers.equalTo;

import java.io.*;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.inject.servlet.RequestScoped;

import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.rest.v1_0.zaak.bestand.GbaRestZaakBestand;
import nl.procura.gba.web.rest.v1_0.zaak.bestand.GbaRestZaakBestandAntwoord;
import nl.procura.gba.web.rest.v1_0.zaak.bestand.GbaRestZaakBestandToevoegenVraag2;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.dms.DmsDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DmsService;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid;
import nl.procura.gba.web.services.zaken.documenten.printen.DocumentenPrintenService;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;
import nl.procura.standard.exceptions.ProException;

import ch.lambdaj.Lambda;
import ch.lambdaj.function.matcher.LambdaJMatcher;

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

    DmsService dms = getServices().getDmsService();
    DocumentService documentService = getServices().getDocumentService();
    DocumentVertrouwelijkheid vertr = documentService.getStandaardVertrouwelijkheid(null, null);

    try {
      File bestand = DocumentenPrintenService.newTijdelijkBestand(bestandsnaam);
      FileUtils.copyInputStreamToFile(is, bestand);
      Zaak zaak = getMinimaleZaak(zaakId);
      dms.save(bestand, bestandsnaam, bestandsnaam, getServices().getGebruiker().getNaam(),
          zaak, vertr.getNaam());
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

    DmsService dms = getServices().getDmsService();
    DocumentService documentService = getServices().getDocumentService();
    DocumentVertrouwelijkheid vertr = documentService.getStandaardVertrouwelijkheid(null, null);

    try {
      File bestand = DocumentenPrintenService.newTijdelijkBestand(vraag.getBestandsNaam());
      IOUtils.write(vraag.getInhoud(), new FileOutputStream(bestand));
      Zaak zaak = getMinimaleZaak(vraag.getZaakId());
      dms.save(bestand, vraag.getTitel(), vraag.getBestandsNaam(), getServices().getGebruiker().getNaam(),
          zaak, vertr.getNaam());
    } catch (IOException exception) {
      throw new ProException(WARNING, "Fout bij wegschrijven bestand", exception);
    }

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

    Zaak zaak = getStandaardZaak(
        getMinimaleZaak(zaakId)); // Standaardzaak nodig voor opvragen bsn voor burgerlijke stand
    List<DmsDocument> dmsDocumenten = getDmsDocumenten(zaak, bestandsnaam);

    if (dmsDocumenten.isEmpty()) {
      throw new ProException(WARNING, "Bestand met naam " + bestandsnaam + " niet gevonden");
    }

    for (DmsDocument bestand : dmsDocumenten) {
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

    Zaak zaak = getStandaardZaak(
        getMinimaleZaak(zaakId)); // Standaardzaak nodig voor opvragen bsn voor burgerlijke stand
    for (DmsDocument dmsDocument : getDmsDocumenten(zaak, bestandsnaam)) {
      try {
        return getResponse(new FileInputStream(dmsDocument.getPad()), dmsDocument.getExtensie());
      } catch (FileNotFoundException exception) {
        throw new ProException(WARNING, "Fout bij laden bestand", exception);
      }
    }

    throw new ProException(WARNING, "Bestand met naam " + bestandsnaam + " niet gevonden");
  }

  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/zoeken/{zaakid}")
  public GbaRestZaakBestandAntwoord getBestanden(
      @PathParam("zaakid") String zaakId) {

    GbaRestZaakBestandAntwoord antwoord = new GbaRestZaakBestandAntwoord();

    for (DmsDocument dmsDocument : getDmsDocumenten(getStandaardZaak(getMinimaleZaak(zaakId)),
        "")) { // Standaardzaak nodig voor opvragen bsn voor burgerlijke stand

      GbaRestZaakBestand bestand = new GbaRestZaakBestand();
      bestand.setZaakId(dmsDocument.getZaakId());
      bestand.setTitel(dmsDocument.getTitel());
      bestand.setBestandsnaam(dmsDocument.getBestandsnaam());
      bestand.setGebruiker(dmsDocument.getAangemaaktDoor());
      bestand.setDatum(dmsDocument.getDatum());
      bestand.setTijd(dmsDocument.getTijd());
      bestand.setDmsNaam(dmsDocument.getDmsNaam());
      bestand.setVertrouwelijkheid(dmsDocument.getVertrouwelijkheid());

      antwoord.getBestanden().add(bestand);
    }

    return antwoord;
  }

  private List<DmsDocument> getDmsDocumenten(Zaak zaak, String bestandsnaam) {

    LambdaJMatcher matcher = having(on(DmsDocument.class).getZaakId(), equalTo(zaak.getZaakId()));

    // Ook zoeken op bestandsnaam
    if (fil(bestandsnaam)) {
      matcher = matcher.and(having(on(DmsDocument.class).getBestandsnaam(), equalTo(bestandsnaam)));
    }

    return Lambda.filter(matcher, getServices().getDmsService().getDocumenten(zaak).getDocumenten());
  }
}
