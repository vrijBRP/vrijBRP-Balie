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

package nl.procura.burgerzaken.vrsclient;

import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.AANVRAAGNUMMER;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.BSN;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.DOCUMENTNUMMER;
import static nl.procura.burgerzaken.vrsclient.api.VrsAanleidingType.IDENTITEITSONDERZOEK;
import static nl.procura.burgerzaken.vrsclient.api.VrsAanleidingType.REISDOCUMENTAANVRAAG;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.common.Json;
import java.io.IOException;
import java.util.Properties;
import nl.procura.burgerzaken.vrsclient.api.AanvragenApi;
import nl.procura.burgerzaken.vrsclient.api.DocumentenApi;
import nl.procura.burgerzaken.vrsclient.api.RegistratieMeldingApi;
import nl.procura.burgerzaken.vrsclient.api.SignaleringApi;
import nl.procura.burgerzaken.vrsclient.api.TokenApi;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingRedenType;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingRequest;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingType;
import nl.procura.burgerzaken.vrsclient.api.VrsMetadata;
import nl.procura.burgerzaken.vrsclient.api.VrsRequest;
import nl.procura.burgerzaken.vrsclient.api.VrsResponse;
import nl.procura.burgerzaken.vrsclient.api.model.IDPIssuerResponse;
import nl.procura.burgerzaken.vrsclient.api.model.TokenRequest;
import nl.procura.standard.ProcuraDate;
import nl.procura.validation.Bsn;

public class VrsExample {

  /*
  Create vrs.properties with following content:
  
  pseudoniem=Demodam
  instantieCode=<1234>
  clientId=
  clientSecret=
  scope=RDMREIS
  resourceServer=REISLAP
  tlsProxy=http://srv-411t:85/cert-manager/proxy?url=
  baseUri=https://lap.api.reis.idm.diginetwerk.net
  */

  private final static Properties  properties = new Properties();
  private final        VrsMetadata metadata;
  private final        String      token;

  String pseudoniem;
  String instantieCode;
  String clientId;
  String clientSecret;
  String scope;
  String resourceServer;
  String tlsProxy;
  String baseUri;


  public static void main(String[] args) throws IOException {
    new VrsExample();
  }

  public VrsExample() throws IOException {
    properties.load(VrsExample.class.getResourceAsStream("/vrs.properties"));

    pseudoniem = properties.getProperty("pseudoniem");
    instantieCode = properties.getProperty("instantieCode");
    clientId = properties.getProperty("clientId");
    clientSecret = properties.getProperty("clientSecret");
    scope = properties.getProperty("scope");
    resourceServer = properties.getProperty("resourceServer");
    tlsProxy = properties.getProperty("tlsProxy");
    baseUri = tlsProxy + properties.getProperty("baseUri");

    IDPIssuerResponse tokenResponse = getTokenApi(baseUri).getIDPIssuerResponse();
    String issuerUri = tokenResponse.getIssuerUri();
    token = getAccessToken(defaultIfBlank(tlsProxy, baseUri) + issuerUri);
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
    registrerenMeldingReisdocument();
  }

  private void registrerenMeldingReisdocument() throws JsonProcessingException {
    RegistratieMeldingApi api = new RegistratieMeldingApi(new ExampleJerseyClient(baseUri));
    System.out.println(toJson(api.meldingRequest(new VrsMeldingRequest()
        .metadata(metadata)
        .documentnummer("IMF0LHCP6")
        .melding(VrsMeldingType.RRV)
        .reden(VrsMeldingRedenType.REVS)
        .datumReden(new ProcuraDate())
        .datumTijd(new ProcuraDate().addDays(-1)))));
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
    Object response = object;
    if (object instanceof VrsResponse) {
      response = ((VrsResponse<?, ?>) object).response();
    }
    return Json.getObjectMapper()
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(response);
  }

  private String getAccessToken(String endpoint) {
    return getTokenApi(endpoint)
        .getTokenResponse(new TokenRequest()
            .clientId(clientId)
            .clientSecret(clientSecret)
            .scope(scope)
            .resourceServer(resourceServer))
        .getAccess_token();
  }

  private TokenApi getTokenApi(String endpoint) {
    return new TokenApi(new ExampleJerseyClient(endpoint));
  }
}
