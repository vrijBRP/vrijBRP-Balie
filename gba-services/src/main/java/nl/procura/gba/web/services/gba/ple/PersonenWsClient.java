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

package nl.procura.gba.web.services.gba.ple;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import nl.procura.gbaws.web.rest.*;
import nl.procura.proweb.rest.client.v1_0.ProRestClient;
import nl.procura.proweb.rest.v1_0.ProRestAntwoordImpl;
import nl.procura.proweb.rest.v1_0.meldingen.ProRestMeldingType;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.theme.Credentials;

public class PersonenWsClient implements HttpClient {

  private final ProRestClient client;
  private final String        baseUrl;
  private final String        application;
  private final Credentials   credentials;

  public PersonenWsClient(String baseUrl, String application, Credentials credentials) {
    this.baseUrl = baseUrl;
    this.application = application;
    this.credentials = credentials;
    client = new ProRestClient();
  }

  @Override
  public <T, R> R get(JsonRequest<T> request, Class<R> type) {
    ClientResponse clientResponse = requestBuilder(request).get(ClientResponse.class);
    return returnOrThrowException(type, clientResponse);
  }

  @Override
  public <T, R> R post(JsonRequest<T> request, Class<R> type) {
    ClientResponse clientResponse = requestBuilder(request).post(ClientResponse.class);
    return returnOrThrowException(type, clientResponse);
  }

  public GbavClient getGbav() {
    return new GbavClient(this, baseUrl, application, credentials.getUsername(), credentials.getPassword());
  }

  public ProcuraDatabaseClient getProcuraDatabase() {
    return new ProcuraDatabaseClient(this, baseUrl, application, credentials.getUsername(), credentials.getPassword());
  }

  public TabellenClient getTabel() {
    return new TabellenClient(this, baseUrl, application, credentials.getUsername(), credentials.getPassword());
  }

  private <T> WebResource.Builder requestBuilder(JsonRequest<T> request) {
    WebResource.Builder builder = client.getClient().resource(request.uri()).getRequestBuilder();
    request.headers().forEach(builder::header);
    request.body().ifPresent(builder::entity);
    return builder;
  }

  private static <R> R returnOrThrowException(Class<R> type, ClientResponse clientResponse) {
    Response.StatusType statusInfo = clientResponse.getStatusInfo();
    if (statusInfo.getFamily() != Response.Status.Family.SUCCESSFUL) {
      throw new ProException(ERROR, statusInfo.getReasonPhrase());
    }
    R entity = clientResponse.getEntity(type);
    throwErrorWhenErrorMessageExists(entity);
    return entity;
  }

  private static void throwErrorWhenErrorMessageExists(Object entity) {
    if (entity instanceof ProRestAntwoordImpl) {
      ((ProRestAntwoordImpl) entity).getMeldingen().stream()
          .filter(m -> m.getType() == ProRestMeldingType.FOUT)
          .findFirst()
          .ifPresent(m -> {
            throw new ProException(ERROR, m.getOmschrijving());
          });
    }
  }
}
