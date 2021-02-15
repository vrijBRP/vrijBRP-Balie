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

package nl.procura.gba.web.rest.v1_0.persoon;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.google.inject.servlet.RequestScoped;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.base.BasePL;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.contact.PlContactgegeven;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;
import nl.procura.standard.exceptions.ProException;
import nl.procura.validation.Anummer;
import nl.procura.validation.Bsn;
import nl.procura.validation.IdNummer;

@RequestScoped
@Path("v1.0/persoon")
@AuthenticatieVereist
public class GbaRestPersoonResources extends GbaRestServiceResource {

  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/zoeken/nummer/{nummer}")
  public GbaRestPersoonNummerAntwoord zoekenPersoonNummer(@PathParam("nummer") String nummer) {

    GbaRestPersoonNummerAntwoord antwoord = new GbaRestPersoonNummerAntwoord();

    for (BasePLValue nr : getServices().getPersonenWsService().getNummers(nummer)) {

      if (new Bsn(nr.getVal()).isCorrect()) {
        antwoord.getAntwoordElement().add(BSN).set(nr.getVal(), nr.getDescr());
      } else if (new Anummer(nr.getVal()).isCorrect()) {
        antwoord.getAntwoordElement().add(ANR).set(nr.getVal(), nr.getDescr());
      }
    }

    return antwoord;
  }

  @POST
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/zoeken")
  public GbaRestPersoonAntwoord zoekPersonen(GbaRestPersoonVraag vraag) {

    GbaRestPersoonAntwoord antwoord = new GbaRestPersoonAntwoord();
    GbaRestElement personen = antwoord.getAntwoordElement().add(PERSONEN);
    PLEArgs args = vraagToArgumenten(vraag);

    for (BasePLExt persoonslijst : getServices().getPersonenWsService().getPersoonslijsten(args,
        false).getBasisPLWrappers()) {

      GbaRestElement persoon = personen.add(PERSOON);
      addPersoonsGegevens(persoonslijst, persoon);
      addContactGegevens(persoonslijst, persoon);
    }

    return antwoord;
  }

  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/zoeken/{nummer}")
  public GbaRestPersoonAntwoord zoekPersoon(@PathParam("nummer") String nummer) {

    GbaRestPersoonVraag vraag = new GbaRestPersoonVraag();
    IdNummer id = new IdNummer(nummer);

    if (id.isCorrect()) {
      vraag.getNummers().add(id.getLong());
    } else {
      throw new ProException(ERROR, "Nummer is geen BSN en geen a-nummer: " + nummer);
    }

    return zoekPersonen(vraag);
  }

  @POST
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/zoeken/persoonslijsten")
  public GbaRestPersoonPersoonslijstAntwoord zoekPersoonslijsten(GbaRestPersoonVraag vraag) {

    GbaRestPersoonPersoonslijstAntwoord antwoord = new GbaRestPersoonPersoonslijstAntwoord();
    GbaRestElement personen = antwoord.getAntwoordElement().add(PERSONEN);
    PLEArgs args = vraagToArgumenten(vraag);

    for (BasePL persoonslijst : getServices().getPersonenWsService().getPersoonslijsten(args,
        false).getResult().getBasePLs()) {

      GbaRestElement persoon = personen.add(PERSOON);
      new GbaRestPersoonslijstHandler2(getServices()).getPersoon(persoonslijst, persoon);
    }

    return antwoord;
  }

  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/zoeken/persoonslijst/{nummer}")
  public GbaRestPersoonPersoonslijstAntwoord zoekPersoonslijsten(@PathParam("nummer") String nummer) {

    GbaRestPersoonVraag vraag = new GbaRestPersoonVraag();
    IdNummer id = new IdNummer(nummer);

    if (id.isCorrect()) {
      vraag.getNummers().add(id.getLong());
    } else {
      throw new ProException(ERROR, "Nummer is geen BSN en geen a-nummer: " + nummer);
    }

    return zoekPersoonslijsten(vraag);
  }

  private void addContactGegevens(BasePLExt persoonslijst, GbaRestElement persoon) {

    GbaRestElement contact = persoon.add(CONTACT);
    GbaRestElement telefoon = contact.add(GbaRestElementType.TELEFOON);

    for (PlContactgegeven aant : getServices().getContactgegevensService().getContactgegevens(persoonslijst)) {

      String gegeven = aant.getContactgegeven().getGegeven();
      String waarde = aant.getAant();

      if (ContactgegevensService.TEL_MOBIEL.equalsIgnoreCase(gegeven)) {
        telefoon.add(MOBIEL).set(waarde.replaceAll("\\D+", ""));
      } else if (ContactgegevensService.TEL_THUIS.equalsIgnoreCase(gegeven)) {
        telefoon.add(THUIS).set(waarde.replaceAll("\\D+", ""));
      } else if (ContactgegevensService.TEL_WERK.equalsIgnoreCase(gegeven)) {
        telefoon.add(WERK).set(waarde.replaceAll("\\D+", ""));
      } else if (ContactgegevensService.EMAIL.equalsIgnoreCase(gegeven)) {
        contact.add(EMAIL).set(waarde);
      }
    }
  }

  private void addPersoonsGegevens(BasePLExt persoonslijst, GbaRestElement persoon) {
    new GbaRestPersoonslijstHandler1(getServices()).getPersoon(persoonslijst, persoon);
  }

  private PLEArgs vraagToArgumenten(GbaRestPersoonVraag vraag) {

    PLEArgs args = new PLEArgs();

    for (long nummer : vraag.getNummers()) {
      args.addNummer(astr(nummer));
    }

    for (int cat : vraag.getCategorieen()) {
      args.getCategories().add(GBACat.getByCode(cat));
    }

    args.setShowHistory(vraag.isToonHistorie());
    args.setShowArchives(vraag.isToonArchief());
    args.setShowSuspended(vraag.isToonOpgeschort());

    return args;
  }
}
