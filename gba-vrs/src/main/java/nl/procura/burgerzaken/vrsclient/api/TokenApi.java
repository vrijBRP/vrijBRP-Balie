/*
 * Copyright 2023 - 2024 Procura B.V.
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

/*
 * Signaleringcontroles API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package nl.procura.burgerzaken.vrsclient.api;

import nl.procura.burgerzaken.vrsclient.ApiClient;
import nl.procura.burgerzaken.vrsclient.api.model.IDPIssuerResponse;
import nl.procura.burgerzaken.vrsclient.api.model.TokenRequest;
import nl.procura.burgerzaken.vrsclient.api.model.TokenResponse;

public class TokenApi {

  private final ApiClient apiClient;

  public TokenApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public TokenResponse getTokenResponse(TokenRequest tokenRequest) {
    ApiClient.Request<String> request = new ApiClient.Request("");

    String formData = "grant_type=client_credentials"
        + "&client_id="
        + tokenRequest.clientId()
        + "&client_secret="
        + tokenRequest.clientSecret()
        + "&scope="
        + tokenRequest.scope()
        + "&resourceServer="
        + tokenRequest.resourceServer();
    request.body(formData);

    request.header("Content-Type", "*/*");
    request.header("Accept", "application/json");
    return apiClient.post(request, TokenResponse.class);
  }

  public IDPIssuerResponse getIDPIssuerResponse() {
    ApiClient.Request<Object> request = new ApiClient.Request<>("/idpconfiguratie/v1");
    request.header("Accept", "application/json");
    return apiClient.get(request, IDPIssuerResponse.class);
  }
}
