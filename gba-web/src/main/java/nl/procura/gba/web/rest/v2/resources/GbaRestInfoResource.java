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

import static nl.procura.gba.web.rest.v2.GbaRestInfoResourceV2.BASE_INFO_URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.servlet.RequestScoped;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.rest.v1_0.GbaRestServiceResource;
import nl.procura.gba.web.rest.v2.GbaRestInfoResourceV2;
import nl.procura.gba.web.rest.v2.model.info.GbaRestInfo;

@RequestScoped
@Path(BASE_INFO_URI)
public class GbaRestInfoResource extends GbaRestServiceResource implements GbaRestInfoResourceV2 {

  @Override
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public GbaRestInfo getInfo() {
    GbaRestInfo info = new GbaRestInfo();
    info.setVersion(MiscUtils.getVersion());
    info.setBuildDate(MiscUtils.getBuilddate());
    return info;
  }
}
