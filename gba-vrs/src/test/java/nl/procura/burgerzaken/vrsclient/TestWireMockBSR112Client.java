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
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.INSTANTIECODE;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.PSEUDONIEM;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.V2_AANVRAAG_DETAILS_ENDPOINT;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.V2_AANVRAAG_ENDPOINT;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.V2_AANVRAGEN_ENDPOINT;

import java.text.MessageFormat;
import nl.procura.burgerzaken.vrsclient.api.AanvragenApi;
import nl.procura.burgerzaken.vrsclient.api.VrsAanleidingType;
import nl.procura.burgerzaken.vrsclient.api.VrsMetadata;
import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagDetailResponse;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvraagVolledigResponse;
import nl.procura.burgerzaken.vrsclient.model.ControleAanvragenResponse;
import nl.procura.burgerzaken.vrsclient.model.SignaleringResponse;
import nl.procura.validation.Bsn;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestWireMockBSR112Client extends AbstractTestWireMockClient {

  @Test
  public void testMustReturnAanvragen() {
    wireMockServer.stubFor(get(urlEqualTo(V2_AANVRAGEN_ENDPOINT))
        .withHeader(HttpHeaders.ACCEPT, equalTo(ContentType.APPLICATION_JSON.toString()))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())));

    final AanvragenApi api = getAanvragenApi();
    final ControleAanvragenResponse response = api.aanvragen(getAanvraagReisdocumentenRequest()).response();
    Assertions.assertEquals("Er zijn aanvragen gevonden.", response.getResultaatomschrijving(),
        MessageFormat.format("{0} verwacht", SignaleringResponse.ResultaatCodeEnum.HIT.getValue()));
  }

  @Test
  public void testMustReturnAanvraag() {
    wireMockServer.stubFor(get(urlEqualTo(V2_AANVRAAG_ENDPOINT))
        .withHeader(HttpHeaders.ACCEPT, equalTo(ContentType.APPLICATION_JSON.toString()))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())));

    final AanvragenApi api = getAanvragenApi();
    final ControleAanvraagDetailResponse response = api.aanvraag(getAanvraagReisdocumentRequest()).response();
    Assertions.assertEquals("IP8799D18", response.getDocumentNummer());
  }

  @Test
  public void testMustReturnAanvraagDetails() {
    wireMockServer.stubFor(get(urlEqualTo(V2_AANVRAAG_DETAILS_ENDPOINT))
        .withHeader(HttpHeaders.ACCEPT, equalTo(ContentType.APPLICATION_JSON.toString()))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())));

    final AanvragenApi api = getAanvragenApi();
    final ControleAanvraagVolledigResponse response = api.aanvraagDetails(getAanvraagReisdocumentRequest()).response();
    Assertions.assertEquals("IP8799D18", response.getReisdocument().getDocumentNummer());
    Assertions.assertEquals("Aanvraag is afgerond",
        response.getAanvraagstatussen().get(0).getAanvraagmeldingen().get(0).getOmschrijving());
  }

  private VrsRequest getAanvraagReisdocumentenRequest() {
    return new VrsRequest()
        .metadata(new VrsMetadata()
            .accessToken(getToken().getAccess_token())
            .pseudoniem(PSEUDONIEM)
            .instantieCode(INSTANTIECODE))
        .aanleiding(VrsAanleidingType.REISDOCUMENTAANVRAAG)
        .aanvraagnummer(AANVRAAGNUMMER)
        .bsn(new Bsn(999995947L));
  }

  private VrsRequest getAanvraagReisdocumentRequest() {
    return new VrsRequest()
        .metadata(new VrsMetadata()
            .accessToken(getToken().getAccess_token())
            .pseudoniem(PSEUDONIEM)
            .instantieCode(INSTANTIECODE))
        .aanleiding(VrsAanleidingType.REISDOCUMENTAANVRAAG)
        .aanvraagnummer(AANVRAAGNUMMER);
  }
}
