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

package nl.procura.gbaws.web.vaadin.login;

import static java.util.Optional.ofNullable;
import static nl.procura.gbaws.web.vaadin.login.GbaWsAuthenticationHandler.getUserByCredentials;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.vrijbrp.hub.client.HubAuthConstants.ROLE_VRIJBRP_PERSOONSLIJSTEN_WS;

import java.util.Optional;
import java.util.function.Supplier;

import com.vaadin.terminal.ErrorMessage;
import com.vaadin.terminal.UserError;

import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.theme.Credentials;
import nl.procura.vaadin.theme.twee.login.CookieLoginValidator;
import nl.vrijbrp.hub.client.HubAuth;
import nl.vrijbrp.hub.client.HubContext;
import nl.vrijbrp.hub.client.auth.BasicAuth;
import nl.vrijbrp.hub.client.auth.HeaderAuth;

public class GbaWsLoginValidator extends CookieLoginValidator {

  public GbaWsLoginValidator() {
    super("qoemxbceuogjnbwbhfueggph0837", "304ncnybf8r3u24bf9fiefn23e23bge");
  }

  @Override
  public Credentials loadCredentials() {
    HubContext.instance().returnToHubIfRequested().loginOnHubIfDefault();
    Credentials credentials = super.loadCredentials();
    return getCredentialsFromHub(credentials)
        .map(HubCredentials::getCredentials)
        .orElse(credentials);
  }

  @Override
  public ErrorMessage validate(final Credentials credentials) {
    try {
      validated(ofNullable(getCredentialsFromHub(credentials)
          .map(creds -> (GbaWsCredentials) creds)
          .orElseGet(() -> getUserByCredentials(credentials.getUsername(), credentials.getPassword(), true)))
              .orElseThrow(GbaWsAuthenticationHandler::getLogoutException));
      return null;
    } catch (ProException e) {
      if (e.getSeverity() != INFO) {
        e.printStackTrace();
      }
      return new UserError(e.getMessage());

    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return new UserError(e.getMessage());

    } catch (Exception e) {
      e.printStackTrace();
      return new UserError("Er is een onbekende fout opgetreden.");
    }
  }

  private Optional<HubCredentials> getCredentialsFromHub(Credentials credentials) {
    return HubContext.instance()
        .authenticate(getBasicAuth(credentials))
        .authentication()
        .filter(auth -> auth.hasRole(ROLE_VRIJBRP_PERSOONSLIJSTEN_WS))
        .map(HubCredentials::new);
  }

  private Supplier<HeaderAuth> getBasicAuth(Credentials credentials) {
    return () -> {
      if (credentials != null) {
        return BasicAuth.of(credentials.getUsername(), credentials.getPassword());
      }
      return null;
    };
  }

  public static class HubCredentials extends GbaWsCredentials {

    public HubCredentials(HubAuth auth) {
      setUsername(auth.username());
      setFullname(auth.name());
      setAdmin(true);
    }

    public Credentials getCredentials() {
      return new Credentials(getUsername(), "");
    }
  }
}
