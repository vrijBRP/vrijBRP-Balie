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

import static nl.procura.gba.web.rest.v2.GbaRestZaakDmsResourceV2.BASE_ZAAKSYSTEEM_URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.servlet.RequestScoped;

import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.rest.v2.GbaRestZaakDmsResourceV2;
import nl.procura.gba.web.rest.v2.model.base.GbaRestAntwoord;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakId;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;

@RequestScoped
@Path(BASE_ZAAKSYSTEEM_URI)
@AuthenticatieVereist
public class GbaRestZaakDmsResourceV2Server
    extends GbaRestServiceResource
    implements GbaRestZaakDmsResourceV2 {

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path(GENEREER_ZAAK_ID_URI)
  public GbaRestAntwoord<GbaRestZaakId> genereerZaakId() {
    return tryCall(
        () -> new GbaRestAntwoord<>(getGbaRestServices()
            .getZaakDmsService()
            .genereerZaakId()));
  }
}
