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
import nl.procura.burgerzaken.vrsclient.ApiException;
import nl.procura.burgerzaken.vrsclient.api.model.IDPIssuerResponse;
import nl.procura.burgerzaken.vrsclient.model.SignaleringResponse;

public class SignaleringControllerApi {

  private final ApiClient apiClient;

  public SignaleringControllerApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Vind een persoon met signalering(en) op BSN nummer
   * Het resultaat van deze aanroep is 1 persoon met signalering(en) gevonden (HIT) of geen persoon met signalering(en) gevonden (NO_HIT)
   */
  public SignaleringResponse signaleringsRequest(SignaleringRequest request) throws ApiException {
    if (request == null) {
      throw new ApiException(400, "Missing the required parameter 'request' when calling signaleringsRequest");
    }

    if (request.v2Bsn() != null && request.v2PersoonsGegevens() != null) {
      throw new ApiException(400, "Cannot have both request.bsn and request.persoonsgegevens");
    }

    ApiClient.Request<Object> apiRequest;
    if (request.v1Bsn() != null) {
      String localVarPath = "/signaleringcontroles/v1/bsn";
      apiRequest = new ApiClient.Request<>(localVarPath);
      apiRequest.body(request.v1Bsn());

    } else if (request.v1PersoonsGegevens() != null) {
      String localVarPath = "/signaleringcontroles/v1/persoonsgegevens";
      apiRequest = new ApiClient.Request<>(localVarPath);
      apiRequest.body(request.v1PersoonsGegevens());

    } else if (request.v2Bsn() != null) {
      String localVarPath = "/signaleringcontroles/v2/bsn";
      apiRequest = new ApiClient.Request<>(localVarPath);
      apiRequest.body(request.v2Bsn());

    } else if (request.v2PersoonsGegevens() != null) {
      String localVarPath = "/signaleringcontroles/v2/persoonsgegevens";
      apiRequest = new ApiClient.Request<>(localVarPath);
      apiRequest.body(request.v2PersoonsGegevens());

    } else {
      throw new ApiException(400, "Both request.bsn and request.persoonsgegevens are missing");
    }

    apiRequest.header("pseudoniem", request.pseudoniem());
    apiRequest.header("instantie_code", request.instantieCode());
    apiRequest.header("Content-Type", "application/json");
    apiRequest.header("Accept", "application/json");
    apiRequest.header("Authorization", "Bearer " + request.getAccessToken());

    return apiClient.post(apiRequest, SignaleringResponse.class);
  }

  public IDPIssuerResponse getIDPIssuerResponse() {
    ApiClient.Request<Object> request = new ApiClient.Request<>("/idpconfiguratie/v1");
    request.header("Accept", "application/json");
    return apiClient.get(request, IDPIssuerResponse.class);
  }
}
