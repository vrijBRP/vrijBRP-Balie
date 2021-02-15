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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.standard.security.Base64;

public class RequestHeaderHandler {

  private final static Logger LOGGER = LoggerFactory.getLogger(RequestHeaderHandler.class);

  private Credentials credentials = new Credentials();

  public RequestHeaderHandler(HttpServletRequest request) {

    try {

      final String authorization = request.getHeader("authorization");
      final String[] usernamepassword = new String(Base64.decode(authorization.split("Basic")[1].trim())).split(
          ":");

      credentials.setUsername(usernamepassword[0]);
      credentials.setPassword(usernamepassword[1]);
    } catch (final IOException e) {
      LOGGER.debug(e.toString());
    }
  }

  public Credentials getCredentials() {
    return credentials;
  }

  public void setCredentials(Credentials credentials) {
    this.credentials = credentials;
  }

  public final class Credentials {

    private String password = "";
    private String username = "";

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }
  }
}
