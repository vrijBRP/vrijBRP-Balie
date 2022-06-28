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

package nl.procura.gbaws.web.rest.v2.tables;

import static nl.procura.gbaws.db.handlers.LandTabDao.*;
import static org.apache.commons.lang3.math.NumberUtils.toLong;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.servlet.RequestScoped;

import nl.procura.gbaws.db.handlers.LandTabDao;
import nl.procura.gbaws.web.rest.GbaWsRestDienstenbusResource;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;

@RequestScoped
@Path("v2/tables")
@AuthenticatieVereist
@Consumes({ MediaType.APPLICATION_JSON })
@Produces(MediaType.APPLICATION_JSON)
public class GbaWsRestTablesResources extends GbaWsRestDienstenbusResource {

  @POST
  @Path("/")
  public GbaWsRestTablesResponse getTables(GbaWsRestTablesRequest request) {
    List<LandTable> tables = LandTabDao.getTables(request.getCodes(), request.getDescription());
    GbaWsRestTablesResponse response = new GbaWsRestTablesResponse();
    response.getTables().addAll(tables.stream().map(table -> get(table,
        request.getRecordCode(),
        request.isShowHistory(),
        request.isShowRecords()))
        .collect(Collectors.toList()));
    return response;
  }

  private GbaWsRestTable get(LandTable table, String recordCode, boolean showHistory, boolean showRecords) {
    GbaWsRestTable restTable = new GbaWsRestTable();
    restTable.setCode(table.getCode());
    restTable.setDescription(table.getDescription());

    if (showRecords) {
      for (LandTableRecord landRecord : getAllRecords(table.getCode(), showHistory)) {
        if (StringUtils.isBlank(recordCode) || toLong(landRecord.getCode(), -1) == toLong(recordCode, -1)) {
          GbaWsRestTableRecord restRecord = new GbaWsRestTableRecord();
          restRecord.setCode(landRecord.getCode());
          restRecord.setDescription(landRecord.getDescription());
          restRecord.setStartDate(landRecord.getDateIn());
          restRecord.setEndDate(landRecord.getDateEnd());
          restRecord.setAttributes(landRecord.getAttr());
          restTable.getRecords().add(restRecord);
        }
      }
    }
    return restTable;
  }
}
