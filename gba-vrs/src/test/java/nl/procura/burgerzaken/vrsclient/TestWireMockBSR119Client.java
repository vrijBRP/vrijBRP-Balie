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
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.BSN;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.DOCUMENTNUMMER;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.INSTANTIECODE;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.PSEUDONIEM;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.V2_DOCUMENTEN_ENDPOINT;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.V2_DOCUMENT_DETAILS_ENDPOINT;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import nl.procura.burgerzaken.vrsclient.api.DocumentenApi;
import nl.procura.burgerzaken.vrsclient.api.VrsAanleidingType;
import nl.procura.burgerzaken.vrsclient.api.VrsMetadata;
import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponse;
import nl.procura.validation.Bsn;

public class TestWireMockBSR119Client extends AbstractTestWireMockClient {

  @Test
  public void testMustReturnDocumenten() {
    wireMockServer.stubFor(get(urlEqualTo(V2_DOCUMENTEN_ENDPOINT))
        .withHeader(HttpHeaders.ACCEPT, equalTo(ContentType.APPLICATION_JSON.toString()))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())));

    final DocumentenApi api = getDocumentenApi();
    final ReisdocumentInformatiePersoonsGegevensInstantieResponse response = api.documenten(getDocumentenRequest());
    Assertions.assertEquals("Er zijn documenten gevonden.", response.getResultaatOmschrijving());
  }

  @Test
  public void testMustReturnDocument() {
    wireMockServer.stubFor(get(urlEqualTo(V2_DOCUMENT_DETAILS_ENDPOINT))
        .withHeader(HttpHeaders.ACCEPT, equalTo(ContentType.APPLICATION_JSON.toString()))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())));

    final DocumentenApi api = getDocumentenApi();
    final ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse response = api
        .document(getDocumentRequest());
    Assertions.assertEquals("Er is een document gevonden.", response.getResultaatOmschrijving());
  }

  private VrsRequest getDocumentenRequest() {
    return new VrsRequest()
        .metadata(new VrsMetadata()
            .accessToken(getToken().getAccess_token())
            .pseudoniem(PSEUDONIEM)
            .instantieCode(INSTANTIECODE))
        .aanleiding(VrsAanleidingType.IDENTITEITSONDERZOEK)
        .bsn(new Bsn(BSN));
  }

  private VrsRequest getDocumentRequest() {
    return new VrsRequest()
        .metadata(new VrsMetadata()
            .accessToken(getToken().getAccess_token())
            .pseudoniem(PSEUDONIEM)
            .instantieCode(INSTANTIECODE))
        .aanleiding(VrsAanleidingType.REISDOCUMENTAANVRAAG)
        .documentnummer(DOCUMENTNUMMER);
  }
}
