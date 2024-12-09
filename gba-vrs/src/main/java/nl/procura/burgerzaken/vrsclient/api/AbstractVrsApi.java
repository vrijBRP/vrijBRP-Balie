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

package nl.procura.burgerzaken.vrsclient.api;

import static java.util.Objects.requireNonNull;

import nl.procura.burgerzaken.vrsclient.ApiClient.Request;

public class AbstractVrsApi {

  protected static void setHeaders(VrsMetadata metadata, Request<Object> apiRequest) {
    requireNonNull(metadata, "metadata is required");
    requireNonNull(metadata.pseudoniem(), "pseudoniem is required");
    requireNonNull(metadata.instantieCode(), "instantieCode is required");
    requireNonNull(metadata.accessToken(), "accessToken is required");
    requireNonNull(metadata, "metadata is required");
    apiRequest.header("pseudoniem", metadata.pseudoniem());
    apiRequest.header("instantie_code", metadata.instantieCode());
    apiRequest.header("Authorization", "Bearer " + metadata.accessToken());
    apiRequest.header("Content-Type", "application/json");
    apiRequest.header("Accept", "application/json");
  }
}
