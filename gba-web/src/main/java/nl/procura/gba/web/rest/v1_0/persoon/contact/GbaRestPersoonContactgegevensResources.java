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

package nl.procura.gba.web.rest.v1_0.persoon.contact;

import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.servlet.RequestScoped;

import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.standard.exceptions.ProException;

@RequestScoped
@Path("v1.0/persoon/contactgegevens")
public class GbaRestPersoonContactgegevensResources extends GbaRestServiceResource {

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/toevoegen")
  public GbaRestPersoonContactgegevenAntwoord toevoegen(GbaRestPersoonContactgegevenToevoegenVraag vraag) {

    GbaRestPersoonContactgegevenAntwoord antwoord = new GbaRestPersoonContactgegevenAntwoord();

    for (GbaRestPersoonContactgegeven c : vraag.getContactgegevens()) {

      String type;

      switch (c.getType()) {
        case EMAIL:
          type = ContactgegevensService.EMAIL;
          break;

        case TEL_MOBIEL:
          type = ContactgegevensService.TEL_MOBIEL;
          break;

        case TEL_THUIS:
          type = ContactgegevensService.TEL_THUIS;
          break;

        case TEL_WERK:
          type = ContactgegevensService.TEL_WERK;
          break;

        default:
          throw new ProException(ERROR, "Onbekend type: " + c.getType());
      }

      getServices().getContactgegevensService().setContactWaarde(-1, vraag.getBurgerServiceNummer(), type,
          c.getWaarde(), -1);
    }

    return antwoord;
  }
}
