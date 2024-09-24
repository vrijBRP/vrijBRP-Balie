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

package nl.procura.gba.web.services.beheer.zynyo;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZYNYO_API_ENDPOINT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZYNYO_API_KEY;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.ZYNYO_ENABLED;
import static nl.procura.standard.Globalfunctions.pos;

import java.time.Duration;

import nl.procura.burgerzaken.zynyo.api.ApiClient;
import nl.procura.burgerzaken.zynyo.api.ApiClientConfig;
import nl.procura.burgerzaken.zynyo.api.SigningApi;
import nl.procura.burgerzaken.zynyo.client.OkHttpZynyoClient;
import nl.procura.gba.web.services.AbstractService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZynyoService extends AbstractService {

  public ZynyoService() {
    super("Zynyo");
  }

  public boolean isZynyoEnabled() {
    return pos(getServices().getParameterService().getParm(ZYNYO_ENABLED, false));
  }

  public SigningApi getSigningApi() {
    return new SigningApi(getZynyoApiClient());
  }

  private ApiClient getZynyoApiClient() {
    String baseUrl = getSysteemParm(ZYNYO_API_ENDPOINT, true);
    String apiKey = getSysteemParm(ZYNYO_API_KEY, true);

    ApiClientConfig config = new ApiClientConfig()
        .baseUrl(baseUrl)
        .apiKey(apiKey);
    return new OkHttpZynyoClient(config, Duration.ofSeconds(15));
  }
}
