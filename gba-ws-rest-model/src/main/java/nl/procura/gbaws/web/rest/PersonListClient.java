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

import nl.procura.gbaws.web.rest.v2.personlists.GbaWsPersonListRequest;
import nl.procura.gbaws.web.rest.v2.personlists.GbaWsPersonListResponse;

public class PersonListClient extends AbstractClient {

  private final HttpClient client;

  public PersonListClient(HttpClient client, String baseUrl, String application, String user, String password) {
    super(baseUrl, application, user, password);
    this.client = client;
  }

  public PersonListClient(HttpClient client, String baseUrl, String application, String authorization) {
    super(baseUrl, application, authorization);
    this.client = client;
  }

  public GbaWsPersonListResponse getPersonLists(GbaWsPersonListRequest request) {
    JsonRequest<GbaWsPersonListRequest> jsonRequest = newRequest("/v2/personlists");
    jsonRequest.body(request);
    return client.post(jsonRequest, GbaWsPersonListResponse.class);
  }
}
