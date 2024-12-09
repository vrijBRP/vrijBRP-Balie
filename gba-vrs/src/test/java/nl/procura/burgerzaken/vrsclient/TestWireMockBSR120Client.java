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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.INSTANTIECODE;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.PSEUDONIEM;
import static nl.procura.burgerzaken.vrsclient.TestConstantsInterface.V1_REGISTRATIE_MELDING;

import nl.procura.burgerzaken.vrsclient.api.RegistratieMeldingApi;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingRedenType;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingRequest;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingType;
import nl.procura.burgerzaken.vrsclient.api.VrsMetadata;
import nl.procura.burgerzaken.vrsclient.model.RegistratieMeldingReisdocumentResponse;
import nl.procura.standard.ProcuraDate;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestWireMockBSR120Client extends AbstractTestWireMockClient {

  @Test
  public void testMustRegisterMelding() {
    wireMockServer.stubFor(get(urlEqualTo(V1_REGISTRATIE_MELDING))
        .withHeader(HttpHeaders.ACCEPT, equalTo(ContentType.APPLICATION_JSON.toString()))
        .willReturn(aResponse()
            .withStatus(HttpStatus.SC_OK)
            .withHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())));

    final RegistratieMeldingApi api = getRegistratieMeldingApi();
    final RegistratieMeldingReisdocumentResponse response = api.meldingRequest(getMeldingRequest()).response();
    Assertions.assertEquals("RDX9218084", response.getInstantieCode());
  }

  private VrsMeldingRequest getMeldingRequest() {
    return new VrsMeldingRequest()
        .metadata(new VrsMetadata()
            .accessToken(getToken().getAccess_token())
            .pseudoniem(PSEUDONIEM)
            .instantieCode(INSTANTIECODE))
        .documentnummer("123")
        .melding(VrsMeldingType.RDO)
        .reden(VrsMeldingRedenType.REBO)
        .datumTijd(new ProcuraDate())
        .datumReden(new ProcuraDate().addDays(1));
  }
}
