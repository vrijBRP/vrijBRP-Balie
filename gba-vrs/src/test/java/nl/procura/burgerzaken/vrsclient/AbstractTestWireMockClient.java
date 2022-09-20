package nl.procura.burgerzaken.vrsclient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.AANVRAAGNUMMER;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.IDP_CONFIG_ENDPOINT;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.TOKEN_ENDPOINT;

import java.util.function.Supplier;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;

import nl.procura.burgerzaken.vrsclient.api.SignaleringControllerApi;
import nl.procura.burgerzaken.vrsclient.api.SignaleringRequest;
import nl.procura.burgerzaken.vrsclient.api.TokenApi;
import nl.procura.burgerzaken.vrsclient.api.model.TokenRequest;
import nl.procura.burgerzaken.vrsclient.api.model.TokenResponse;
import nl.procura.burgerzaken.vrsclient.model.SignaleringRequestV1Bsn;

public abstract class AbstractTestWireMockClient {

  protected static WireMockServer wireMockServer;

  @BeforeAll
  static void setUp() {
    wireMockServer = new WireMockServer(wireMockConfig()
        //.notifier(new ConsoleNotifier(true))
        .dynamicPort()
        .usingFilesUnderClasspath("wiremock"));
    wireMockServer.start();
  }

  @AfterAll
  static void tearDown() {
    wireMockServer.stop();
    wireMockServer.shutdown();
  }

  @Test
  public void testMustReturnIdpURL() {
    wireMockServer.stubFor(get(urlEqualTo(IDP_CONFIG_ENDPOINT))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SC_OK)));

    final ExampleJerseyClient client = new ExampleJerseyClient(wireMockServer.baseUrl());
    final SignaleringControllerApi api = getApi(client);
    String issuerUri = api.getIDPIssuerResponse().getIssuerUri();
    Assertions.assertEquals("/token", issuerUri);
  }

  @Test
  public void testMustReturnToken() {
    wireMockServer.stubFor(get(urlEqualTo(TOKEN_ENDPOINT))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SC_OK)));

    TokenResponse response = getTokenSupplier().get();
    Assertions.assertEquals("MY_VERY_NICE_TOKEN", response.getAccess_token());
    Assertions.assertEquals("bearer", response.getToken_type());
    Assertions.assertEquals(3600, response.getExpires_in());
    Assertions.assertEquals("RDMREIS", response.getScope());
  }

  protected SignaleringRequest getSignaleringRequestV1(String bsn) {
    return new SignaleringRequest()
        .accessToken(getTokenSupplier().get().getAccess_token())
        .pseudoniem("user123")
        .instantieCode("inst456")
        .v1Bsn(new SignaleringRequestV1Bsn()
            .aanvraagnummer(AANVRAAGNUMMER)
            .bsn(bsn));
  }

  protected SignaleringControllerApi getApi(ExampleJerseyClient client) {
    return new SignaleringControllerApi(client);
  }

  protected Supplier<TokenResponse> getTokenSupplier() {
    return () -> new TokenApi(new ExampleJerseyClient(wireMockServer.baseUrl() + getIDPEndpoint()))
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
    final SignaleringControllerApi api = new SignaleringControllerApi(client);
    return api.getIDPIssuerResponse().getIssuerUri();
  }
}
