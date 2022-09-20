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

package nl.procura.gba.web.services.gba.sql;

import static nl.procura.gba.web.services.gba.templates.ZoekProfielType.PROFIEL_STANDAARD;

import java.util.ArrayList;
import java.util.List;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.gba.web.services.gba.ple.PersonenWsClient;
import nl.procura.gba.web.services.gba.templates.GbaTemplateService;
import nl.procura.gbaws.web.rest.v1_0.database.procura.query.GbaWsRestProcuraSelecterenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.database.procura.query.GbaWsRestProcuraSelecterenVraag;
import nl.procura.gbaws.web.rest.v1_0.database.procura.query.GbaWsRestQueryRecord;
import nl.procura.gbaws.web.rest.v1_0.database.procura.query.GbaWsRestQueryVeld;

public class ProbevSqlService extends GbaTemplateService {

  public ProbevSqlService() {
    super("PROBEV SQL");
  }

  public List<String[]> find(String sql) {
    List<String[]> recordList = new ArrayList<>();
    try {
      PersonenWsClient client = getServices().getPersonenWsService().getPersonenWsClient(PROFIEL_STANDAARD);
      GbaWsRestProcuraSelecterenVraag vraag = new GbaWsRestProcuraSelecterenVraag(sql);
      GbaWsRestProcuraSelecterenAntwoord antwoord = client.getProcuraDatabase().selecteren(vraag);

      for (GbaWsRestQueryRecord record : antwoord.getRecords()) {
        List<String> fieldList = new ArrayList<>();

        for (GbaWsRestQueryVeld veld : record.getVelden()) {
          fieldList.add(veld.getWaarde());
        }

        recordList.add(fieldList.toArray(new String[0]));
      }

      return recordList;
    } catch (RuntimeException e) {
      throw new ProException("Fout bij aanroepen ophalen tabel", e);
    }
  }
}
