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

package nl.procura.gbaws.requests;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import nl.procura.gbaws.db.handlers.UsrDao;
import nl.procura.gbaws.db.wrappers.UsrWrapper;
import nl.procura.standard.security.Base64;
import nl.vrijbrp.hub.client.HubContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestHeaderHandler {

  private RequestCredentials credentials = new RequestCredentials();

  public RequestHeaderHandler(HttpServletRequest request) {

    try {
      UsrWrapper userWrapper = HubContext.instance().authentication()
          .map(auth -> UsrDao.getUserByUsernames(auth.username(),
              auth.email()))
          .orElse(null);

      if (userWrapper != null) {
        credentials.setUser(userWrapper);

      } else {
        final String authorization = request.getHeader("authorization");
        byte[] usernamePassword = Base64.decode(authorization.split("Basic")[1].trim());
        final String[] usernamepassword = new String(usernamePassword).split(":");
        credentials.setUsername(usernamepassword[0]);
        credentials.setPassword(usernamepassword[1]);
      }
    } catch (final IOException e) {
      log.debug(e.toString());
    }
  }

  public RequestCredentials getCredentials() {
    return credentials;
  }

  public void setCredentials(RequestCredentials credentials) {
    this.credentials = credentials;
  }
}
