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

import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.io.InputStream;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

import nl.procura.proweb.rest.v1_0.ProRestAntwoordImpl;
import nl.procura.proweb.rest.v1_0.meldingen.ProRestMelding;
import nl.procura.proweb.rest.v1_0.meldingen.ProRestMeldingType;
import nl.procura.standard.exceptions.ProException;

public class GbaRestClientResponse<T> {

  private final Class<T>       type;
  private final ClientResponse response;
  private Object               request = null;
  private String               url;
  private T                    entity  = null;

  public GbaRestClientResponse(Class<T> type, String url, Object request, ClientResponse response) {
    this(type, url, response);
    this.request = request;
  }

  public GbaRestClientResponse(Class<T> type, String url, ClientResponse response) {
    this.url = url;
    this.type = type;
    this.response = response;
  }

  public GbaRestClientResponse<T> check() {

    if (getStatus() != Status.OK) {
      throw new ProException(ERROR, getStatus().getReasonPhrase());
    }

    if (getEntity() instanceof ProRestAntwoordImpl) {
      for (ProRestMelding melding : ((ProRestAntwoordImpl) getEntity()).getMeldingen()) {
        if (ProRestMeldingType.FOUT.equals(melding.getType())) {
          throw new ProException(ERROR, melding.getOmschrijving());
        }
      }
    }

    return this;
  }

  public Status getStatus() {
    return Status.fromStatusCode(response.getStatus());
  }

  public ClientResponse getClientResponse() {
    return response;
  }

  public T getEntity() {
    if (entity == null) {
      entity = response.getEntity(type);
    }
    return entity;
  }

  public InputStream getInputStream() {
    return response.getEntityInputStream();
  }

  public Object getRequest() {
    return request;
  }

  public String getUrl() {
    return url;
  }
}
