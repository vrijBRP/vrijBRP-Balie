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

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.servlet.RequestScoped;

import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.rest.v1_0.zaak.attribuut.GbaRestZaakAttribuutToevoegenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.attribuut.GbaRestZaakAttribuutVerwijderenVraag;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.AttribuutHistorie;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuut;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuutService;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;

@RequestScoped
@Path("v1.0/zaak/attribuut")
@AuthenticatieVereist
public class GbaRestZaakAttribuutResources extends GbaRestServiceResource {

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/toevoegen")
  public GbaRestZaakAntwoord toevoegen(GbaRestZaakAttribuutToevoegenVraag vraag) {

    ZaakAttribuut attribuut = new ZaakAttribuut();
    Zaak zaak = getMinimaleZaak(vraag.getZaakId());
    attribuut.setZaakId(zaak.getZaakId());
    attribuut.setAttribuut(vraag.getAttribuut());

    ZaakAttribuutService attributen = getServices().getZaakAttribuutService();
    attributen.save(attribuut);

    return new GbaRestZaakAntwoord();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/verwijderen")
  public GbaRestZaakAntwoord verwijderen(GbaRestZaakAttribuutVerwijderenVraag vraag) {

    ZaakAttribuutService attributen = getServices().getZaakAttribuutService();
    AttribuutHistorie historie = attributen.getZaakAttributen(vraag.getZaakId(), vraag.getAttribuut());

    for (ZaakAttribuut attribuut : historie.getAttributen()) {
      attributen.delete(attribuut);
    }

    return new GbaRestZaakAntwoord();
  }
}
