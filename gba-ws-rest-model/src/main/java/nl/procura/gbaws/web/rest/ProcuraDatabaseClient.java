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

package nl.procura.gbaws.web.rest;

import nl.procura.gbaws.web.rest.v1_0.database.procura.query.GbaWsRestProcuraSelecterenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.database.procura.query.GbaWsRestProcuraSelecterenVraag;

public class ProcuraDatabaseClient extends AbstractClient {

  private final HttpClient client;

  public ProcuraDatabaseClient(HttpClient client, String baseUrl, String application, String user, String password) {
    super(baseUrl, application, user, password);
    this.client = client;
  }

  public GbaWsRestProcuraSelecterenAntwoord selecteren(GbaWsRestProcuraSelecterenVraag request) {
    JsonRequest<GbaWsRestProcuraSelecterenVraag> jsonRequest = newRequest("/rest/v1.0/procura/database/selecteren");
    jsonRequest.body(request);
    return client.post(jsonRequest, GbaWsRestProcuraSelecterenAntwoord.class);
  }
}
