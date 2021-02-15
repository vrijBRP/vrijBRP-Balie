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

package nl.procura.gbaws.web.rest;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

public abstract class AbstractClient {

  private final String baseUrl;
  private final String application;
  private final String authorization;

  AbstractClient(String baseUrl, String application, String user, String password) {
    this(baseUrl, application, basicAuthentication(user, password));
  }

  AbstractClient(String baseUrl, String application, String authorization) {
    this.baseUrl = baseUrl;
    this.application = application;
    this.authorization = authorization;
  }

  protected <T> JsonRequest<T> newRequest(String path) {
    return new JsonRequest<T>(uri(path))
        .setHeader("Authorization", authorization)
        .setHeader("application", application);
  }

  private URI uri(String path) {
    try {
      return new URI(baseUrl + path);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private static String basicAuthentication(String user, String password) {
    return "Basic " + Base64.getEncoder().encodeToString((user + ":" + password).getBytes(UTF_8));
  }

}
