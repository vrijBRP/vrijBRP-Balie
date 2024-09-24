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

import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.AANVRAAGNUMMER;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.BSN;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.DOCUMENTNUMMER;
import static nl.procura.burgerzaken.vrsclient.api.VrsAanleidingType.IDENTITEITSONDERZOEK;
import static nl.procura.burgerzaken.vrsclient.api.VrsAanleidingType.REISDOCUMENTAANVRAAG;

import java.io.IOException;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.common.Json;

import nl.procura.burgerzaken.vrsclient.api.AanvragenApi;
import nl.procura.burgerzaken.vrsclient.api.DocumentenApi;
import nl.procura.burgerzaken.vrsclient.api.SignaleringApi;
import nl.procura.burgerzaken.vrsclient.api.TokenApi;
import nl.procura.burgerzaken.vrsclient.api.VrsMetadata;
import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.burgerzaken.vrsclient.api.model.IDPIssuerResponse;
import nl.procura.burgerzaken.vrsclient.api.model.TokenRequest;
import nl.procura.validation.Bsn;

public class VrsExample {

  /*
  Create vrs.properties with following content:
  
  pseudoniem=Demodam
  instantieCode=<1234>
  clientId=
  clientSecret=
   */
  private final static Properties  properties = new Properties();
  private final        VrsMetadata metadata;
  private final        String      token;
  private final        String      baseUri;

  String pseudoniem;
  String instantieCode;
  String clientId;
  String clientSecret;

  public static void main(String[] args) throws IOException {
    new VrsExample();
  }

  public VrsExample() throws IOException {
    properties.load(VrsExample.class.getResourceAsStream("/vrs.properties"));
    String tlsProxy = "http://srv-411t:85/cert-manager/proxy?url=";
    baseUri = tlsProxy + "https://lap.api.reis.idm.diginetwerk.net";
    pseudoniem = properties.getProperty("pseudoniem");
    instantieCode = properties.getProperty("instantieCode");
    clientId = properties.getProperty("clientId");
    clientSecret = properties.getProperty("clientSecret");

    IDPIssuerResponse tokenResponse = getTokenApi(baseUri).getIDPIssuerResponse();
    String issuerUri = tokenResponse.getIssuerUri();
    token = getAccessToken(tlsProxy + issuerUri);
    metadata = new VrsMetadata()
        .accessToken(token)
        .pseudoniem(pseudoniem)
        .instantieCode(instantieCode);

    signalering();
    aanvragen();
    aanvraag();
    aanvraagDetails();
    documenten();
    document();
  }

  private void signalering() throws JsonProcessingException {
    SignaleringApi api = new SignaleringApi(new ExampleJerseyClient(baseUri));
    System.out.println(toJson(api.signaleringsRequest(new VrsRequest()
        .metadata(metadata)
        .bsn(new Bsn(BSN))
        .aanvraagnummer(AANVRAAGNUMMER))));
  }

  private void aanvragen() throws JsonProcessingException {
    AanvragenApi api = new AanvragenApi(new ExampleJerseyClient(baseUri));
    System.out.println(toJson(api.aanvragen(new VrsRequest()
        .metadata(metadata)
        .aanleiding(REISDOCUMENTAANVRAAG)
        .bsn(new Bsn(BSN))
        .aanvraagnummer(AANVRAAGNUMMER))));
  }

  private void aanvraag() throws JsonProcessingException {
    AanvragenApi api = new AanvragenApi(new ExampleJerseyClient(baseUri));
    System.out.println(toJson(api.aanvraag(new VrsRequest()
        .metadata(metadata)
        .aanleiding(IDENTITEITSONDERZOEK)
        .aanvraagnummer("468480588"))));
  }

  private void aanvraagDetails() throws JsonProcessingException {
    AanvragenApi api = new AanvragenApi(new ExampleJerseyClient(baseUri));
    System.out.println(toJson(api.aanvraagDetails(new VrsRequest()
        .metadata(metadata)
        .aanleiding(IDENTITEITSONDERZOEK)
        .aanvraagnummer("468480588"))));
  }

  private void documenten() throws JsonProcessingException {
    DocumentenApi api = new DocumentenApi(new ExampleJerseyClient(baseUri));
    System.out.println(toJson(api.documenten(new VrsRequest()
        .metadata(metadata)
        .aanleiding(IDENTITEITSONDERZOEK)
        .bsn(new Bsn(BSN))
        .aanvraagnummer("468480588"))));
  }

  private void document() throws JsonProcessingException {
    DocumentenApi api = new DocumentenApi(new ExampleJerseyClient(baseUri));
    System.out.println(toJson(api.document(new VrsRequest()
        .metadata(metadata)
        .aanleiding(REISDOCUMENTAANVRAAG)
        .documentnummer(DOCUMENTNUMMER)
        .aanvraagnummer("468480588"))));
  }

  private String toJson(Object object) throws JsonProcessingException {
    return Json.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
  }

  private String getAccessToken(String endpoint) {
    return getTokenApi(endpoint)
        .getTokenResponse(new TokenRequest()
            .clientId(clientId)
            .clientSecret(clientSecret)
            .scope("RDMREIS")
            .resourceServer("REISLAP"))
        .getAccess_token();
  }

  private TokenApi getTokenApi(String endpoint) {
    return new TokenApi(new ExampleJerseyClient(endpoint));
  }
}