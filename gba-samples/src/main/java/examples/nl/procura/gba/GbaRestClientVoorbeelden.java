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

package examples.nl.procura.gba;

import static nl.procura.standard.Globalfunctions.pad_left;
import static nl.procura.standard.Globalfunctions.pad_right;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

import nl.procura.gba.web.rest.client.GbaRestClient;
import nl.procura.gba.web.rest.client.GbaRestClientException;
import nl.procura.gba.web.rest.client.GbaRestClientResponse;
import nl.procura.proweb.rest.utils.JsonUtils;
import nl.procura.commons.core.exceptions.ProException;

public class GbaRestClientVoorbeelden {

  private final static Logger   LOGGER = LoggerFactory.getLogger(GbaRestClientVoorbeelden.class.getName());
  protected final GbaRestClient client;

  {
    String username = "<username>";
    String password = "<password>";
    client = new GbaRestClient("http://localhost:8081/personen", "BSM", username, password);
  }

  protected void writeStream(InputStream is, String extension) throws GbaRestClientException {
    try {
      File bestand = new File("target/test_" + new Date().getTime() + "." + extension);
      IOUtils.copy(is, new FileOutputStream(bestand));
      System.out.println("Weggeschreven naar: " + bestand.getAbsolutePath());
    } catch (Exception e) {
      throw new GbaRestClientException(e);
    }
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

  protected InputStream getStream(ClientResponse response) {
    if (response.getStatus() == Status.OK.getStatusCode()) {
      return response.getEntityInputStream();
    }

    throw new ProException(ERROR, response.getStatusInfo().getReasonPhrase());
  }
}
