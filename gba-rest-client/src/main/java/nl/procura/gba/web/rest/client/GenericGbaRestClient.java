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

package nl.procura.gba.web.rest.client;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Map;
import java.util.Map.Entry;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

import nl.procura.proweb.rest.client.v1_0.ProRestClient;

public class GenericGbaRestClient extends ProRestClient {

  public GenericGbaRestClient(String url, String applicatie, String gebruikersnaam, String wachtwoord) {
    setUrl(url);
    setApplicatie(applicatie);
    setGebruikersnaam(gebruikersnaam);
    setWachtwoord(wachtwoord);
  }

  protected <T> GbaRestClientResponse<T> POST(Class o, String url, Object vraag) throws GbaRestClientException {
    return POST(o, url, vraag, APPLICATION_JSON, APPLICATION_JSON, null);
  }

  protected <T> GbaRestClientResponse<T> POST(Class o, String url, Object vraag, String type, String accepttype,
      Map<String, Object> headers) throws GbaRestClientException {

    try {

      ClientResponse cr = accept(getWebResource(url, headers), type, accepttype).post(ClientResponse.class,
          vraag);
      return new GbaRestClientResponse<T>(o, url, vraag, cr);
    } catch (RuntimeException e) {
      throw new GbaRestClientException(e);
    }
  }

  protected <T> GbaRestClientResponse<T> GET(Class o, String url) throws GbaRestClientException {
    return GET(o, url, null);
  }

  protected <T> GbaRestClientResponse<T> GET(Class o, String url, String accepttype) throws GbaRestClientException {
    try {

      ClientResponse cr = accept(getWebResource(url, null), APPLICATION_JSON, accepttype).get(
          ClientResponse.class);
      return new GbaRestClientResponse<T>(o, url, cr);
    } catch (RuntimeException e) {
      throw new GbaRestClientException(e);
    }
  }

  protected Builder accept(Builder builder, String type, String accepttype) {
    return builder.type(type).accept(accepttype);
  }

  protected Builder getWebResource(String resource, Map<String, Object> headers) {

    Builder builder = getClient().resource(getUrl() + resource).header("application", getApplicatie());

    if (headers != null) {
      for (Entry<String, Object> entry : headers.entrySet()) {
        builder = builder.header(entry.getKey(), entry.getValue());
      }
    }

    return builder;
  }
}
