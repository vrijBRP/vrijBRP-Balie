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
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.AANVRAAGNUMMER;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.NEEM_CONTACT_OP;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.V2_SIGNALERING_ENDPOINT;

import java.text.MessageFormat;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import nl.procura.burgerzaken.vrsclient.api.SignaleringApi;
import nl.procura.burgerzaken.vrsclient.api.VrsMetadata;
import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.burgerzaken.vrsclient.model.SignaleringResponse;
import nl.procura.validation.Bsn;

public class TestWireMockBSR101Client extends AbstractTestWireMockClient {

  @Test
  public void testMustRequestSignaleringNoHit() {
    wireMockServer.stubFor(get(urlEqualTo(V2_SIGNALERING_ENDPOINT))
        .withHeader(HttpHeaders.ACCEPT, equalTo(ContentType.APPLICATION_JSON.toString()))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())));

    VrsRequest request = getSignaleringRequestV2("999995959");
    final SignaleringApi api = getSignaleringApi();
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

    final SignaleringApi api = getSignaleringApi();
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

    final SignaleringApi api = getSignaleringApi();
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

    final SignaleringApi api = getSignaleringApi();
    final SignaleringResponse response = api.signaleringsRequest(getSignaleringRequestV2("100000231"));
    Assertions.assertEquals(SignaleringResponse.ResultaatCodeEnum.MULTIPLE_HITS, response.getResultaatCode(),
        MessageFormat.format("{0} verwacht", SignaleringResponse.ResultaatCodeEnum.MULTIPLE_HITS.getValue()));
  }

  private VrsRequest getSignaleringRequestV2(String bsn) {
    return new VrsRequest()
        .metadata(new VrsMetadata()
            .pseudoniem("user123")
            .instantieCode("inst456")
            .accessToken("MY_VERY_NICE_TOKEN"))
        .aanvraagnummer(AANVRAAGNUMMER)
        .bsn(new Bsn(bsn));
  }
}
