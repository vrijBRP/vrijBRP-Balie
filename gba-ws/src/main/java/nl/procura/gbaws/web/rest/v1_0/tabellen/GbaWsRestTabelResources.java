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

package nl.procura.gbaws.web.rest.v1_0.tabellen;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.servlet.RequestScoped;

import nl.procura.gbaws.db.handlers.LandTabDao;
import nl.procura.gbaws.db.handlers.LandTabDao.LandTable;
import nl.procura.gbaws.db.handlers.LandTabDao.LandTableRecord;
import nl.procura.gbaws.web.rest.GbaWsRestDienstenbusResource;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;

@RequestScoped
@Path("v1.0/tabel")
@AuthenticatieVereist
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class GbaWsRestTabelResources extends GbaWsRestDienstenbusResource {

  @POST
  @Path("/tabellen")
  public GbaWsRestTabellenAntwoord getTabel(GbaWsRestTabellenVraag vraag) {
    GbaWsRestTabellenAntwoord antwoord = new GbaWsRestTabellenAntwoord();
    for (LandTable table : LandTabDao.getTables(vraag.getCodes())) {
      antwoord.getTabellen().add(get(table, vraag.isHistorie()));
    }

    return antwoord;
  }

  private GbaWsRestTabel get(LandTable table, boolean history) {

    GbaWsRestTabel restTabel = new GbaWsRestTabel();
    restTabel.setCode(table.getCode());
    restTabel.setOmschrijving(table.getDescription());

    for (LandTableRecord record : LandTabDao.getAllRecords(table.getCode(), history)) {
      GbaWsRestTabelRecord restRecord = new GbaWsRestTabelRecord();
      restRecord.setCode(record.getCode());
      restRecord.setOmschrijving(record.getDescription());
      restRecord.setDatumIngang(record.getDateIn());
      restRecord.setDatumEinde(record.getDateEnd());
      restRecord.setAttributen(record.getAttr());
      restTabel.getRecords().add(restRecord);
    }

    return restTabel;
  }
}
