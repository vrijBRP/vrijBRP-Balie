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

import static java.util.Collections.unmodifiableMap;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JsonRequest<T> {

  private static final String APPLICATION_JSON = "application/json";

  private final URI                 uri;
  private final Map<String, String> headers = new HashMap<>();
  private T                         body;

  public JsonRequest(URI uri) {
    this.uri = uri;
    headers.put("Accept", APPLICATION_JSON);
  }

  public Map<String, String> headers() {
    return unmodifiableMap(headers);
  }

  public JsonRequest<T> setHeader(String key, String value) {
    headers.put(key, value);
    return this;
  }

  public URI uri() {
    return uri;
  }

  public Optional<T> body() {
    return Optional.ofNullable(body);
  }

  public JsonRequest<T> body(T body) {
    this.body = body;
    headers.put("Content-Type", APPLICATION_JSON);
    return this;
  }
}
