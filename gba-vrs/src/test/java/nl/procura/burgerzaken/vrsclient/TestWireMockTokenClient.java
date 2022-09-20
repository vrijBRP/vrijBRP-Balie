package nl.procura.burgerzaken.vrsclient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.IDP_CONFIG_ENDPOINT;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.TOKEN_ENDPOINT;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import nl.procura.burgerzaken.vrsclient.api.SignaleringControllerApi;
import nl.procura.burgerzaken.vrsclient.api.model.TokenResponse;

public class TestWireMockTokenClient extends AbstractTestWireMockClient {

  @Override
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

  @Override
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
}
