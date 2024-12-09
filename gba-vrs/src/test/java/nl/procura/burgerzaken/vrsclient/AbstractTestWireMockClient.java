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

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import nl.procura.burgerzaken.vrsclient.api.AanvragenApi;
import nl.procura.burgerzaken.vrsclient.api.DocumentenApi;
import nl.procura.burgerzaken.vrsclient.api.RegistratieMeldingApi;
import nl.procura.burgerzaken.vrsclient.api.SignaleringApi;
import nl.procura.burgerzaken.vrsclient.api.TokenApi;
import nl.procura.burgerzaken.vrsclient.api.model.TokenRequest;
import nl.procura.burgerzaken.vrsclient.api.model.TokenResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractTestWireMockClient {

  protected static WireMockServer wireMockServer;

  @BeforeAll
  static void setUp() {
    wireMockServer = new WireMockServer(wireMockConfig()
        .notifier(new ConsoleNotifier(true))
        .dynamicPort()
        .usingFilesUnderClasspath("wiremock"));
    wireMockServer.start();
  }

  @AfterAll
  static void tearDown() {
    wireMockServer.stop();
    wireMockServer.shutdown();
  }

  protected SignaleringApi getSignaleringApi() {
    return new SignaleringApi(new ExampleJerseyClient(wireMockServer.baseUrl()));
  }

  protected AanvragenApi getAanvragenApi() {
    return new AanvragenApi(new ExampleJerseyClient(wireMockServer.baseUrl()));
  }

  protected DocumentenApi getDocumentenApi() {
    return new DocumentenApi(new ExampleJerseyClient(wireMockServer.baseUrl()));
  }

  protected RegistratieMeldingApi getRegistratieMeldingApi() {
    return new RegistratieMeldingApi(new ExampleJerseyClient(wireMockServer.baseUrl()));
  }

  protected TokenResponse getToken() {
    return new TokenApi(new ExampleJerseyClient(wireMockServer.baseUrl() + getIDPEndpoint()))
        .getTokenResponse(getTokenRequest());
  }

  private TokenRequest getTokenRequest() {
    TokenRequest tokenRequest = new TokenRequest();
    tokenRequest.clientId("abc");
    tokenRequest.clientSecret("456");
    tokenRequest.scope("testScope");
    tokenRequest.resourceServer("testServer");
    return tokenRequest;
  }

  private String getIDPEndpoint() {
    final ExampleJerseyClient client = new ExampleJerseyClient(wireMockServer.baseUrl());
    final TokenApi tokenApi = new TokenApi(client);
    return tokenApi.getIDPIssuerResponse().getIssuerUri();
  }
}
