package nl.procura.burgerzaken.vrsclient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.AANVRAAGNUMMER;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.IDP_CONFIG_ENDPOINT;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.NEEM_CONTACT_OP;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.TOKEN_ENDPOINT;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.V2_SIGNALERING_ENDPOINT;

import java.text.MessageFormat;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import nl.procura.burgerzaken.vrsclient.api.SignaleringControllerApi;
import nl.procura.burgerzaken.vrsclient.api.SignaleringRequest;
import nl.procura.burgerzaken.vrsclient.api.model.TokenResponse;
import nl.procura.burgerzaken.vrsclient.model.SignaleringRequestV2Bsn;
import nl.procura.burgerzaken.vrsclient.model.SignaleringResponse;

public class TestWireMockV2Client extends AbstractTestWireMockClient {

  @Test
  public void testMustRequestSignaleringNoHit() {
    wireMockServer.stubFor(get(urlEqualTo(V2_SIGNALERING_ENDPOINT))
        .withHeader(HttpHeaders.ACCEPT, equalTo(ContentType.APPLICATION_JSON.toString()))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())));

    SignaleringRequest request = getSignaleringRequestV2("999995959");
    final ExampleJerseyClient client = new ExampleJerseyClient(wireMockServer.baseUrl());
    final SignaleringControllerApi api = getApi(client);
    final SignaleringResponse response = api.signaleringsRequest(request);
    Assertions.assertEquals(SignaleringResponse.ResultaatCodeEnum.NO_HIT, response.getResultaatCode(),
        MessageFormat.format("{0} verwacht", SignaleringResponse.ResultaatCodeEnum.NO_HIT.getValue()));
  }

  @Test
  public void testMustRequestSignaleringHit() {
    wireMockServer.stubFor(get(urlEqualTo(V2_SIGNALERING_ENDPOINT))
        .withHeader(HttpHeaders.ACCEPT, equalTo(ContentType.APPLICATION_JSON.toString()))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())));

    final ExampleJerseyClient client = new ExampleJerseyClient(wireMockServer.baseUrl());
    final SignaleringControllerApi api = getApi(client);
    final SignaleringResponse response = api.signaleringsRequest(getSignaleringRequestV2("000001351"));
    Assertions.assertEquals(SignaleringResponse.ResultaatCodeEnum.HIT, response.getResultaatCode(),
        MessageFormat.format("{0} verwacht", SignaleringResponse.ResultaatCodeEnum.HIT.getValue()));
  }

  @Test
  public void testMustRequestSignaleringHitMededelingRvIG() {
    wireMockServer.stubFor(get(urlEqualTo(V2_SIGNALERING_ENDPOINT))
        .withHeader(HttpHeaders.ACCEPT, equalTo(ContentType.APPLICATION_JSON.toString()))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())));

    final ExampleJerseyClient client = new ExampleJerseyClient(wireMockServer.baseUrl());
    final SignaleringControllerApi api = getApi(client);
    final SignaleringResponse response = api.signaleringsRequest(getSignaleringRequestV2("999995315"));
    Assertions.assertEquals(SignaleringResponse.ResultaatCodeEnum.HIT, response.getResultaatCode(),
        MessageFormat.format("{0} verwacht", SignaleringResponse.ResultaatCodeEnum.HIT.getValue()));
    Assertions.assertEquals(NEEM_CONTACT_OP, response.getMededelingRvIG(),
        MessageFormat.format("Volgende mededeling RvIG verwacht: {0}", NEEM_CONTACT_OP));
  }

  @Test
  public void testMustRequestSignaleringMultipleHit() {
    wireMockServer.stubFor(get(urlEqualTo(V2_SIGNALERING_ENDPOINT))
        .withHeader(HttpHeaders.ACCEPT, equalTo(ContentType.APPLICATION_JSON.toString()))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())));

    final ExampleJerseyClient client = new ExampleJerseyClient(wireMockServer.baseUrl());
    final SignaleringControllerApi api = getApi(client);
    final SignaleringResponse response = api.signaleringsRequest(getSignaleringRequestV2("100000231"));
    Assertions.assertEquals(SignaleringResponse.ResultaatCodeEnum.MULTIPLE_HITS, response.getResultaatCode(),
        MessageFormat.format("{0} verwacht", SignaleringResponse.ResultaatCodeEnum.MULTIPLE_HITS.getValue()));
  }

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

  private SignaleringRequest getSignaleringRequestV2(String bsn) {
    return new SignaleringRequest()
        .accessToken(getTokenSupplier().get().getAccess_token())
        .pseudoniem("user123")
        .instantieCode("inst456")
        .v2Bsn(new SignaleringRequestV2Bsn()
            .aanvraagnummer(AANVRAAGNUMMER)
            .bsn(bsn));
  }
}
