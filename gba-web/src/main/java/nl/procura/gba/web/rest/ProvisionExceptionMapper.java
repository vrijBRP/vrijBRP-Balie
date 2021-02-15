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

package nl.procura.gba.web.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.inject.ProvisionException;
import com.google.inject.Singleton;

import nl.procura.proweb.rest.guice.misc.ProRestAuthenticatieException;
import nl.procura.proweb.rest.guice.providers.ProRestAuthenticationExceptionMapper;

@Singleton
@Provider
public class ProvisionExceptionMapper implements ExceptionMapper<ProvisionException> {

  public ProvisionExceptionMapper() {
  }

  @Override
  public Response toResponse(ProvisionException e) {
    if (e.getCause() != null && e.getCause() instanceof ProRestAuthenticatieException) {
      ProRestAuthenticatieException authException = (ProRestAuthenticatieException) e.getCause();
      return new ProRestAuthenticationExceptionMapper().toResponse(authException);
    }
    ResponseBuilder response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
    response = response.entity(e.getMessage());
    return response.build();
  }
}
