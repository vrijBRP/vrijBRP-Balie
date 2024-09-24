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

package nl.procura.burgerzaken.vrsclient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.IDP_CONFIG_ENDPOINT;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.TOKEN_ENDPOINT;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import nl.procura.burgerzaken.vrsclient.api.TokenApi;
import nl.procura.burgerzaken.vrsclient.api.model.TokenResponse;

public class TestWireMockTokenClient extends AbstractTestWireMockClient {

  @Test
  public void testMustReturnIdpURL() {
    wireMockServer.stubFor(get(urlEqualTo(IDP_CONFIG_ENDPOINT))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SC_OK)));

    final ExampleJerseyClient client = new ExampleJerseyClient(wireMockServer.baseUrl());
    final TokenApi api = new TokenApi(client);
    String issuerUri = api.getIDPIssuerResponse().getIssuerUri();
    Assertions.assertEquals("/token", issuerUri);
  }

  @Test
  public void testMustReturnToken() {
    wireMockServer.stubFor(get(urlEqualTo(TOKEN_ENDPOINT))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SC_OK)));

    TokenResponse response = getToken();
    Assertions.assertEquals("MY_VERY_NICE_TOKEN", response.getAccess_token());
    Assertions.assertEquals("bearer", response.getToken_type());
    Assertions.assertEquals(3600, response.getExpires_in());
    Assertions.assertEquals("RDMREIS", response.getScope());
  }
}
