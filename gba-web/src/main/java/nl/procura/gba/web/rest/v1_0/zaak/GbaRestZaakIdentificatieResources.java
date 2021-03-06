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
import nl.procura.gba.web.rest.v1_0.zaak.identificatie.GbaRestZaakIdentificatieToevoegenVraag;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatie;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatieService;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;

@RequestScoped
@Path("v1.0/zaak/identificatie")
@AuthenticatieVereist
public class GbaRestZaakIdentificatieResources extends GbaRestServiceResource {

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Path("/toevoegen")
  public GbaRestZaakAntwoord toevoegen(GbaRestZaakIdentificatieToevoegenVraag vraag) {

    Zaak zaak = getMinimaleZaak(vraag.getInternId());
    ZaakIdentificatieService service = getServices().getZaakIdentificatieService();
    ZaakIdentificatie identificatie = new ZaakIdentificatie();

    identificatie.setInternId(zaak.getZaakId());
    identificatie.setType(vraag.getType().getCode());
    identificatie.setExternId(vraag.getExternId());
    identificatie.setZaakType(zaak.getType());

    service.save(identificatie);

    return new GbaRestZaakAntwoord();
  }
}
