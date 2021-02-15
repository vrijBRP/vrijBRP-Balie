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

import static nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningIndicatie.VRIJE_AANTEKENING;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.servlet.RequestScoped;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.rest.v1_0.zaak.aantekening.GbaRestZaakAantekeningToevoegenVraag;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.*;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;

@RequestScoped
@Path("v1.0/zaak/aantekening")
@AuthenticatieVereist
public class GbaRestZaakAantekeningResources extends GbaRestServiceResource {

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/toevoegen")
  public GbaRestZaakAntwoord toevoegen(GbaRestZaakAantekeningToevoegenVraag vraag) {

    Zaak zaak = getMinimaleZaak(vraag.getZaakId());
    PlAantekening aantekening = new PlAantekening();
    aantekening.setZaakId(zaak.getZaakId());

    PlAantekeningHistorie historie = new PlAantekeningHistorie();
    historie.setTijdstip(new DateTime());
    historie.setGebruiker(new UsrFieldValue(getServices().getGebruiker()));
    historie.setIndicatie(new PlAantekeningIndicatie(VRIJE_AANTEKENING));
    historie.setOnderwerp(vraag.getOnderwerp());
    historie.setInhoud(vraag.getInhoud());

    if (vraag.getStatus() != null) {
      historie.setHistorieStatus(PlAantekeningStatus.get(vraag.getStatus().getCode()));
    }

    AantekeningService aantekeningen = getServices().getAantekeningService();
    aantekeningen.save(aantekening, historie);
    return new GbaRestZaakAntwoord();
  }
}
