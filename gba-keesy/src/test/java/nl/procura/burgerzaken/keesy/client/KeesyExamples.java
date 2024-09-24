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

package nl.procura.burgerzaken.keesy.client;

import java.time.Duration;

import org.junit.Assert;

import nl.procura.burgerzaken.keesy.api.ApiClient;
import nl.procura.burgerzaken.keesy.api.ApiClientConfig;
import nl.procura.burgerzaken.keesy.api.ApiResponse;
import nl.procura.burgerzaken.keesy.api.AutorisatieApi;
import nl.procura.burgerzaken.keesy.api.BerichtenApi;
import nl.procura.burgerzaken.keesy.api.DocumentenApi;
import nl.procura.burgerzaken.keesy.api.SignalenApi;
import nl.procura.burgerzaken.keesy.api.ZakenApi;
import nl.procura.burgerzaken.keesy.api.model.DownloadBerichtenoverzichtRequest;
import nl.procura.burgerzaken.keesy.api.model.DownloadVerzamelDocumentPDFRequest;
import nl.procura.burgerzaken.keesy.api.model.GeefStartUrlRequest;
import nl.procura.burgerzaken.keesy.api.model.GeefZaakRequest;
import nl.procura.burgerzaken.keesy.api.model.GeefZaakResponse;
import nl.procura.burgerzaken.keesy.api.model.MaakZaakRequest;
import nl.procura.burgerzaken.keesy.api.model.MaakZaakRequestPersoon;
import nl.procura.burgerzaken.keesy.api.model.MaakZaakResponse;
import nl.procura.burgerzaken.keesy.api.model.Signaal;
import nl.procura.burgerzaken.keesy.api.model.SignalenRequest;
import nl.procura.burgerzaken.keesy.api.model.SignalenResponse;

import lombok.SneakyThrows;

public class KeesyExamples {

  public static void main(String[] args) {
    new KeesyExamples();
  }

  @SneakyThrows
  public KeesyExamples() {
    // SET ENV VARS: USER, GROUP, API_KEY, URL
    String url = System.getenv("URL");
    String user = System.getenv("USER");
    String group = System.getenv("GROUP");
    String apiKey = System.getenv("API_KEY");

    Assert.assertNotNull("URL is not set", url);
    Assert.assertNotNull("USER is not set", user);
    Assert.assertNotNull("GROUP is not set", group);
    Assert.assertNotNull("API_KEY is not set", apiKey);

    ApiClientConfig config = new ApiClientConfig()
        .debug(true)
        .baseUrl(url)
        .apiKey(apiKey);

    ApiClient apiClient = new OkHttpKeesyClient(config, Duration.ofSeconds(10));
    geefAllSignalen(apiClient, user, group);
    geefAllSignalenPerZaak(apiClient, user, group);
    geefStartUrl(apiClient, user, group);
    geefZaak(apiClient);
    geefWrongZaak(apiClient);
    downloadBerichtenoverzicht(apiClient);
    downloadVerzamelPDF(apiClient);
    // maakZaak(apiClient, user, group);
  }

  private static void geefAllSignalen(ApiClient apiClient, String user, String group) {
    SignalenApi signalenApi = new SignalenApi(apiClient);

    SignalenRequest request = new SignalenRequest()
        .externUserId(user)
        .groupId(group);

    SignalenResponse response = signalenApi.geefAantalSignalen(request).getEntity();
    System.out.println("Aantal: " + response.getAantal());
    System.out.println("Signalen: " + response.getSignalen().size());

    for (Signaal signaal : response.getSignalen()) {
      System.out.println("signaal.getAantal() = " + signaal.getAantal());
      System.out.println("signaal.getExternZaakId() = " + signaal.getExternZaakId());
    }
  }

  private static void geefAllSignalenPerZaak(ApiClient apiClient, String user, String group) {
    SignalenApi signalenApi = new SignalenApi(apiClient);

    SignalenRequest request = new SignalenRequest()
        .externUserId(user)
        .groupId(group)
        .externZaakId("PR123");

    SignalenResponse response = signalenApi.geefAantalSignalen(request).getEntity();
    System.out.println("Aantal: " + response.getAantal());
    Assert.assertNull(response.getSignalen());
  }

  private void geefStartUrl(ApiClient apiClient, String user, String group) {
    AutorisatieApi autorisatieApi = new AutorisatieApi(apiClient);
    GeefStartUrlRequest request = new GeefStartUrlRequest()
        .externUserId(user)
        .groupId(group);
    String startUrl = autorisatieApi.geefStartUrl(request).getEntity().getUrl();
    System.out.println("Start URL: " + startUrl);
  }

  private void geefZaak(ApiClient apiClient) {
    ZakenApi zakenApi = new ZakenApi(apiClient);
    GeefZaakRequest request = new GeefZaakRequest()
        .externZaakId("PR123");
    GeefZaakResponse response = zakenApi.geefZaak(request).getEntity();
    System.out.println("Zaak-id:" + response.getZaakId());
  }

  private void geefWrongZaak(ApiClient apiClient) {
    ZakenApi zakenApi = new ZakenApi(apiClient);
    GeefZaakRequest request = new GeefZaakRequest()
        .externZaakId("PR123XX");
    ApiResponse<GeefZaakResponse> response = zakenApi.geefZaak(request);
    System.out.println("Zaak-id:" + response.getHttpCode());
    System.out.println("Error message: " + response.getError().getException().message());
    System.out.println("Error severity: " + response.getError().getException().severity());
  }

  private void maakZaak(ApiClient apiClient, String user, String group) {
    ZakenApi zakenApi = new ZakenApi(apiClient);
    MaakZaakRequest request = new MaakZaakRequest()
        .groupId(group)
        .zaakSoort("Huwelijk")
        .behandelaarId(user)
        .onderwerp("Huwelijk sluiten")
        .externZaakId("K128")
        .persoon(new MaakZaakRequestPersoon()
            .naam("Frits Janssen")
            .email("burgerzaken@procura.nl")
            .telnr("1234567890"));
    ApiResponse<MaakZaakResponse> response = zakenApi.maakZaak(request);
    System.out.println("Zaak-id:" + response.getEntity().getZaakId());
  }

  private void downloadBerichtenoverzicht(ApiClient apiClient) {
    BerichtenApi berichtenApi = new BerichtenApi(apiClient);
    ApiResponse<byte[]> response = berichtenApi.downloadBerichtenoverzicht(
        new DownloadBerichtenoverzichtRequest().zaakId("236006"));
    System.out.println("Response code: " + response.getHttpCode());
    System.out.println("Response: " + response.getEntity().length);
  }

  private void downloadVerzamelPDF(ApiClient apiClient) {
    DocumentenApi documentenApi = new DocumentenApi(apiClient);
    ApiResponse<byte[]> response = documentenApi.downloadVerzamelPDF(
        new DownloadVerzamelDocumentPDFRequest()
            .zaakId("236006")
            .inclusiefInhoudsopgave(true)
            .inclusiefBerichtenoverzicht(true));
    System.out.println("Response code: " + response.getHttpCode());
    System.out.println("Response: " + response.getEntity().length);
  }
}
