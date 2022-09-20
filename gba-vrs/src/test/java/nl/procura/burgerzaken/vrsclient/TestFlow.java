package nl.procura.burgerzaken.vrsclient;

import nl.procura.burgerzaken.vrsclient.api.SignaleringControllerApi;
import nl.procura.burgerzaken.vrsclient.api.SignaleringRequest;
import nl.procura.burgerzaken.vrsclient.api.TokenApi;
import nl.procura.burgerzaken.vrsclient.api.model.TokenRequest;
import nl.procura.burgerzaken.vrsclient.model.SignaleringRequestV1Bsn;

public class TestFlow {

  public static void main(String[] args) {
    String proxy = "http://localhost:8090/ssl-web/proxy?url=";
    SignaleringControllerApi api = getApi(proxy);
    String issuerUri = api.getIDPIssuerResponse().getIssuerUri();

    System.out.println(api.signaleringsRequest(new SignaleringRequest()
        .accessToken(getAccessToken(proxy, issuerUri))
        .pseudoniem("user123")
        .instantieCode("<instantiecode>")
        .v1Bsn(new SignaleringRequestV1Bsn()
            .bsn("999995947")
            .aanvraagnummer("1234567890")))
        .getResultaatCode());
  }

  private static SignaleringControllerApi getApi(String proxy) {
    return new SignaleringControllerApi(new ExampleJerseyClient(proxy + "https://lap.api.reis.idm.diginetwerk.net"));
  }

  private static String getAccessToken(String proxy, String endpoint) {
    return new TokenApi(new ExampleJerseyClient(proxy + endpoint))
        .getTokenResponse(new TokenRequest()
            .clientId("<clientid>")
            .clientSecret("<secret>")
            .scope("RDMREIS")
            .resourceServer("REISLAP"))
        .getAccess_token();
  }
}
