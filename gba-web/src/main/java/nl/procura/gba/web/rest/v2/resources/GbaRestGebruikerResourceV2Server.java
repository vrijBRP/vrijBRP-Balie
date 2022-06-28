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

import static nl.procura.gba.web.rest.v2.GbaRestGebruikerResourceV2.BASE_GEBRUIKER_URI;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.google.inject.servlet.RequestScoped;

import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.rest.v2.GbaRestGebruikerResourceV2;
import nl.procura.gba.web.rest.v2.model.base.GbaRestAntwoord;
import nl.procura.gba.web.rest.v2.model.gebruikers.GbaRestGebruikerVraag;
import nl.procura.gba.web.rest.v2.model.gebruikers.GbaRestGebruikerZoekenAntwoord;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;

@RequestScoped
@Path(BASE_GEBRUIKER_URI)
@AuthenticatieVereist
public class GbaRestGebruikerResourceV2Server extends GbaRestServiceResource implements GbaRestGebruikerResourceV2 {

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public GbaRestAntwoord<GbaRestGebruikerZoekenAntwoord> getGebruiker() {
    return tryCall(() -> {
      GbaRestGebruikerVraag request = new GbaRestGebruikerVraag();
      request.setGebruikersnaam(getServices().getGebruiker().getGebruikersnaam());
      request.setEmail(getServices().getGebruiker().getEmail());
      return new GbaRestAntwoord<>(getGbaRestServices()
          .getGebruikerService()
          .getGebruiker(request));
    });

  }

  @Override
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public GbaRestAntwoord<GbaRestGebruikerZoekenAntwoord> getGebruiker(GbaRestGebruikerVraag request) {
    return tryCall(() -> new GbaRestAntwoord<>(getGbaRestServices()
        .getGebruikerService()
        .getGebruiker(request)));

  }
}
