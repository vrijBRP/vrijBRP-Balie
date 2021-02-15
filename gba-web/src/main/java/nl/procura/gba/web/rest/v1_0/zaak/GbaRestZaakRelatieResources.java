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

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.rest.v1_0.zaak.relatie.GbaRestZaakRelatieToevoegenVraag;
import nl.procura.gba.web.rest.v1_0.zaak.relatie.GbaRestZaakRelatieVerwijderenVraag;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatie;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatieService;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;

@RequestScoped
@Path("v1.0/zaak/relatie")
@AuthenticatieVereist
public class GbaRestZaakRelatieResources extends GbaRestServiceResource {

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/toevoegen")
  public GbaRestZaakAntwoord toevoegen(GbaRestZaakRelatieToevoegenVraag vraag) {

    ZaakRelatieService relaties = getServices().getZaakRelatieService();

    ZaakRelatie relatie = new ZaakRelatie();
    relatie.setZaakId(vraag.getZaakId());
    relatie.setZaakType(ZaakType.get(vraag.getZaakTypeCode()));
    relatie.setGerelateerdZaakId(vraag.getGerelateerdZaakId());
    relatie.setGerelateerdZaakType(ZaakType.get(vraag.getGerelateerdZaakTypeCode()));

    relaties.save(relatie);

    return new GbaRestZaakAntwoord();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/verwijderen")
  public GbaRestZaakAntwoord verwijderen(GbaRestZaakRelatieVerwijderenVraag vraag) {

    ZaakRelatieService relaties = getServices().getZaakRelatieService();
    ZaakRelatie relatie = new ZaakRelatie();
    relatie.setZaakId(vraag.getZaakId());
    relatie.setGerelateerdZaakId(vraag.getGerelateerdZaakId());
    relaties.delete(relatie);

    return new GbaRestZaakAntwoord();
  }
}
