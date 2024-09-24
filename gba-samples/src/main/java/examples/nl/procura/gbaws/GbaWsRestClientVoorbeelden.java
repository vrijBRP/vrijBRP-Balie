/*
 * Copyright 2021 - 2022 Procura B.V.
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

package examples.nl.procura.gbaws;

import static nl.procura.standard.Globalfunctions.pad_left;
import static nl.procura.standard.Globalfunctions.pad_right;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.ClientResponse.Status;

import nl.procura.gba.web.rest.client.GbaRestClientResponse;
import nl.procura.gbaws.web.rest.client.GbaWsRestClient;
import nl.procura.proweb.rest.utils.JsonUtils;
import nl.procura.commons.core.exceptions.ProException;

public class GbaWsRestClientVoorbeelden {

  private final static Logger     LOGGER = LoggerFactory.getLogger(GbaWsRestClientVoorbeelden.class.getName());
  protected final GbaWsRestClient client;

  {
    String url = "http://localhost:8088/personen-ws";
    String application = "Test";
    String username = "<username>";
    String password = "<password>";
    client = new GbaWsRestClient(url, application, username, password);
  }

  protected void info(int indent, String key, Object val) {
    LOGGER.info(pad_left("", " ", indent) + pad_right(key, " ", 25) + ": " + val);
  }

  protected void info(String val) {
    LOGGER.info(val);
  }

  protected <T> T getObject(GbaRestClientResponse<T> response) {
    return getResponse(response).getEntity();
  }

  protected <T> GbaRestClientResponse<T> getResponse(GbaRestClientResponse<T> response) {

    LOGGER.info("Request: " + response.getUrl());
    if (response.getRequest() != null) {
      try {

        String jsonRequest = JsonUtils.getPrettyJson(JsonUtils.toStream(response.getRequest()));
        LOGGER.info("-------");
        LOGGER.info(jsonRequest);
      } catch (Exception e) {
        LOGGER.info("Kan request niet lezen");
      }
    }

    if (response.getStatus() == Status.OK) {
      LOGGER.info("Response");
      try {

        String jsonResponse = JsonUtils.getPrettyJson(JsonUtils.toStream(response.getEntity()));
        LOGGER.info("-------");
        LOGGER.info(jsonResponse);
      } catch (Exception e) {
        LOGGER.info("Kan request niet lezen");
      }

      return response;
    }

    throw new ProException(ERROR, response.getStatus().getReasonPhrase());
  }
}
