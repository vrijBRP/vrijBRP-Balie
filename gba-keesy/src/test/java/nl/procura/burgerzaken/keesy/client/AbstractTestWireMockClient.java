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

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;

import nl.procura.burgerzaken.keesy.api.ApiClientConfig;
import nl.procura.burgerzaken.keesy.api.AutorisatieApi;
import nl.procura.burgerzaken.keesy.api.SignalenApi;
import nl.procura.burgerzaken.keesy.api.ZakenApi;

public abstract class AbstractTestWireMockClient {

  protected static WireMockServer wireMockServer;
  private static ApiClientConfig  config;

  @BeforeAll
  static void setUp() {
    wireMockServer = new WireMockServer(wireMockConfig()
        .notifier(new ConsoleNotifier(true))
        .dynamicPort()
        .usingFilesUnderClasspath("wiremock"));
    wireMockServer.start();
    config = new ApiClientConfig()
        .baseUrl(wireMockServer.baseUrl())
        .apiKey("myToken");
  }

  @AfterAll
  static void tearDown() {
    wireMockServer.stop();
    wireMockServer.shutdown();
  }

  protected SignalenApi getSignalenApi() {
    return new SignalenApi(new OkHttpKeesyClient(config, Duration.ofSeconds(10)));
  }

  protected ZakenApi getZakenApi() {
    return new ZakenApi(new OkHttpKeesyClient(config, Duration.ofSeconds(10)));
  }

  protected AutorisatieApi getAutorisatieApi() {
    return new AutorisatieApi(new OkHttpKeesyClient(config, Duration.ofSeconds(10)));
  }
}
