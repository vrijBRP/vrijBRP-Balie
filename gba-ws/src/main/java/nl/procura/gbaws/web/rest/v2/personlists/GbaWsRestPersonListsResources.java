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

package nl.procura.gbaws.web.rest.v2.personlists;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.base.BasePLBuilder;
import nl.procura.diensten.gba.ple.base.converters.ws.BasePLToGbaWsConverter;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gbaws.db.handlers.UsrDao;
import nl.procura.gbaws.db.wrappers.UsrWrapper;
import nl.procura.gbaws.requests.gba.GbaRequestHandlerWS;
import nl.procura.gbaws.web.rest.GbaWsRestDienstenbusResource;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;
import nl.procura.proweb.rest.v1_0.gebruiker.ProRestGebruiker;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
@Path("v2/personlists")
@AuthenticatieVereist
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class GbaWsRestPersonListsResources extends GbaWsRestDienstenbusResource {

  @Inject
  ProRestGebruiker loggedInUser;

  @POST
  @Path("/")
  public GbaWsPersonListResponse getPersonlists(GbaWsPersonListRequest request) {

    GbaWsPersonListResponse response;
    PLEArgs args = new PLEArgs();
    try {
      request.getIds().forEach(id -> args.addNummer(String.valueOf(id)));
      request.getCategories().forEach(c -> args.addCat(GBACat.getByCode(c)));
      args.setDatasource(PLEDatasource.get(request.getDatasource()));
      args.setMaxFindCount(request.getMaxFindCount());

      args.setShowArchives(request.getShowArchives());
      args.setShowRemoved(request.getShowRemoved());
      args.setShowHistory(request.getShowHistory());
      args.setShowSuspended(request.getShowSuspended());
      args.setShowMutations(request.getShowMutations());
      args.setSearchOnAddress(request.getSearchOnAddress());
      args.setSearchRelations(request.getSearchRelations());
      args.setSearchIndications(request.getSearchIndications());
      args.setReasonForIndications("");

      args.setGeslachtsnaam(request.getLastName());
      args.setVoornaam(request.getFirstName());
      args.setVoorvoegsel(request.getPrefix());
      args.setGeslacht(request.getGender());
      args.setGeboortedatum(request.getDateOfBirth());
      args.setTitel(request.getTitle());
      args.setStraat(request.getStreet());
      args.setHuisnummer(request.getHnr());
      args.setHuisletter(request.getHnrL());
      args.setHuisnummertoevoeging(request.getHnrT());
      args.setAanduiding(request.getHnrA());
      args.setPostcode(request.getPostalCode());
      args.setGemeentedeel(request.getResidence());
      args.setGemeente(request.getMunicipality());

      UsrWrapper usr = UsrDao.getGebruiker(loggedInUser.getGebruikersnaam());
      BasePLBuilder builder = new GbaRequestHandlerWS(usr, args).getBuilder();
      response = BasePLToGbaWsConverter.toGbaWsPersonListResponse(builder.getResult());

    } catch (RuntimeException e) {
      log.error("Error", e);
      response = new GbaWsPersonListResponse();
      response.getErrors().add(e.getMessage());
    }

    return response;
  }
}
