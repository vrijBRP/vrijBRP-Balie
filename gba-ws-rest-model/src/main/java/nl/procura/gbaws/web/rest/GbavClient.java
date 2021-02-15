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

import nl.procura.gbaws.web.rest.v1_0.gbav.account.GbaWsRestGbavAccountsAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.deblokkeren.GbaWsRestGbavDeblokkerenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.deblokkeren.GbaWsRestGbavDeblokkerenVraag;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.update.GbaWsRestGbavAccountUpdatenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.update.GbaWsRestGbavAccountUpdatenVraag;
import nl.procura.gbaws.web.rest.v1_0.gbav.wachtwoord.genereren.GbaWsRestGbavWachtwoordGenererenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.wachtwoord.versturen.GbaWsRestGbavWachtwoordVersturenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.gbav.wachtwoord.versturen.GbaWsRestGbavWachtwoordVersturenVraag;

public class GbavClient extends AbstractClient {

  private final HttpClient client;

  public GbavClient(HttpClient client, String baseUrl, String application, String user, String password) {
    super(baseUrl, application, user, password);
    this.client = client;
  }

  public GbaWsRestGbavAccountsAntwoord getAccounts() {
    JsonRequest<Void> request = newRequest("/rest/v1.0/gbav/accounts");
    return client.get(request, GbaWsRestGbavAccountsAntwoord.class);
  }

  public GbaWsRestGbavDeblokkerenAntwoord accountDeblokkeren(GbaWsRestGbavDeblokkerenVraag request) {
    JsonRequest<GbaWsRestGbavDeblokkerenVraag> jsonRequest = newRequest("/rest/v1.0/gbav/account/deblokkeren");
    jsonRequest.body(request);
    return client.post(jsonRequest, GbaWsRestGbavDeblokkerenAntwoord.class);
  }

  public GbaWsRestGbavAccountUpdatenAntwoord accountUpdaten(GbaWsRestGbavAccountUpdatenVraag request) {
    JsonRequest<GbaWsRestGbavAccountUpdatenVraag> jsonRequest = newRequest("/rest/v1.0/gbav/account/updaten");
    jsonRequest.body(request);
    return client.post(jsonRequest, GbaWsRestGbavAccountUpdatenAntwoord.class);
  }

  public GbaWsRestGbavWachtwoordGenererenAntwoord wachtwoordGenereren() {
    JsonRequest<Void> jsonRequest = newRequest("/rest/v1.0/gbav/wachtwoord/genereren");
    return client.get(jsonRequest, GbaWsRestGbavWachtwoordGenererenAntwoord.class);
  }

  public GbaWsRestGbavWachtwoordVersturenAntwoord wachtwoordVersturen(GbaWsRestGbavWachtwoordVersturenVraag request) {
    JsonRequest<GbaWsRestGbavWachtwoordVersturenVraag> jsonRequest = newRequest("/rest/v1.0/gbav/wachtwoord/versturen");
    jsonRequest.body(request);
    return client.post(jsonRequest, GbaWsRestGbavWachtwoordVersturenAntwoord.class);
  }
}
