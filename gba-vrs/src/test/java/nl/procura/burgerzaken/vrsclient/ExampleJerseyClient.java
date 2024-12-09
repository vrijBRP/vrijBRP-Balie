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

import static com.sun.jersey.api.json.JSONConfiguration.FEATURE_POJO_MAPPING;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;
import java.net.URI;
import nl.procura.burgerzaken.vrsclient.api.jackson.ObjectMapperContextResolver;

/**
 * Example client implemented with Jersey 1.x and Jackson 1.x
 */
public class ExampleJerseyClient extends ApiClient {

  private final Client client;

  public ExampleJerseyClient(final String basePath) {
    super(basePath);
    final DefaultClientConfig config = new DefaultClientConfig();
    config.getFeatures().put(FEATURE_POJO_MAPPING, true);
    config.getSingletons().add(new ObjectMapperContextResolver());
    client = new Client(new URLConnectionClientHandler(), config);
    client.setConnectTimeout(1_000);
    client.setReadTimeout(1_000);
  }

  @Override
  public <T, R> R get(final Request<T> request, final Class<R> type) {
    final WebResource.Builder builder = client.resource(URI.create(getBaseUri() + request.path)).getRequestBuilder();
    request.headers.forEach(builder::header);
    return builder.get(type);
  }

  @Override
  public <T, R> R post(final Request<T> request, final Class<R> type) {
    final WebResource.Builder builder = client.resource(URI.create(getBaseUri() + request.path)).getRequestBuilder();
    request.headers.forEach(builder::header);
    return builder.post(type, request.body);
  }
}
