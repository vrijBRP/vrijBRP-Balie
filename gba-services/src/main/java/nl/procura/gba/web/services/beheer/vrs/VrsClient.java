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

package nl.procura.gba.web.services.beheer.vrs;

import static com.sun.jersey.api.json.JSONConfiguration.FEATURE_POJO_MAPPING;
import static java.lang.String.format;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.CONFIG;

import java.net.URI;
import java.time.Duration;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;

import nl.procura.burgerzaken.vrsclient.ApiClient;
import nl.procura.gba.web.common.jackson.ObjectMapperContextResolver;
import nl.procura.standard.exceptions.ProException;

public class VrsClient extends ApiClient {

  private final Client client;

  public VrsClient(String baseUri, Duration timeout) {
    super(baseUri);
    DefaultClientConfig config = new DefaultClientConfig();
    config.getFeatures().put(FEATURE_POJO_MAPPING, true);
    config.getSingletons().add(new ObjectMapperContextResolver());
    client = new Client(new URLConnectionClientHandler(), config);
    client.setConnectTimeout((int) timeout.toMillis());
    client.setReadTimeout((int) timeout.toMillis());
  }

  @Override
  public <T, R> R get(Request<T> request, Class<R> type) {
    String uri = getBaseUri() + request.path;
    try {
      WebResource.Builder builder = client.resource(URI.create(uri)).getRequestBuilder();
      request.headers.forEach(builder::header);
      return builder.get(type);
    } catch (RuntimeException e) {
      throw new ProException(CONFIG, ERROR, format("Fout bij het uitvoeren van GET %s", uri), e);
    }
  }

  @Override
  public <T, R> R post(Request<T> request, Class<R> type) {
    String uri = getBaseUri() + request.path;
    try {
      WebResource.Builder builder = client.resource(URI.create(uri)).getRequestBuilder();
      request.headers.forEach(builder::header);
      return builder.post(type, request.body);
    } catch (RuntimeException e) {
      throw new ProException(CONFIG, ERROR, format("Fout bij het uitvoeren van POST %s", uri), e);
    }
  }
}
