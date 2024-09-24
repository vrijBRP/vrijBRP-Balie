/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.burgerzaken.keesy.api;

import nl.procura.burgerzaken.keesy.api.model.SignalenRequest;
import nl.procura.burgerzaken.keesy.api.model.SignalenResponse;

public class SignalenApi {

  private final ApiClient apiClient;

  public SignalenApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiResponse<SignalenResponse> geefAantalSignalen(SignalenRequest request) throws ApiException {
    ApiClient.Request<Object> apiRequest;
    String localVarPath = "/geefAantalSignalen";
    apiRequest = new ApiClient.Request<>(localVarPath);
    apiRequest.body(request);
    defaultHeaders(apiClient.config(), apiRequest);
    return apiClient.post(apiRequest, SignalenResponse.class);
  }

  protected static void defaultHeaders(ApiClientConfig config, ApiClient.Request<Object> apiRequest) {
    apiRequest.header("Authorization", "Bearer " + config.apiKey());
    apiRequest.header("Accept", "application/json");
    apiRequest.header("Content-type", "application/json");
  }
}
