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

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.AUTHENTICATION;

import java.net.ConnectException;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.config.GbaConfigProperty;
import nl.procura.gbaws.db.handlers.UsrDao;
import nl.procura.gbaws.db.wrappers.UsrWrapper;
import nl.procura.proweb.rest.client.v1_0.ProRestClient;
import nl.procura.proweb.rest.guice.misc.ProRestAuthenticatieException;
import nl.procura.proweb.rest.v1_0.Rol;
import nl.procura.proweb.rest.v1_0.gebruiker.ProRestGebruiker;
import nl.procura.proweb.rest.v1_0.gebruiker.ProRestGebruikerAntwoord;
import nl.procura.standard.exceptions.ProException;
import nl.vrijbrp.hub.client.HubContext;

public class GbaWsAuthenticationHandler {

  public static GbaWsCredentials getUserByUsernameAndEmail(String username, String email) {
    GbaWsCredentials credentials = new GbaWsCredentials();
    UsrWrapper gebruiker = UsrDao.getUserByUsernames(username, email);
    if (gebruiker != null) {
      credentials.setUsername(gebruiker.getGebruikersNaam());
      credentials.setFullname(gebruiker.getVolledigeNaam());
      credentials.setAdmin(gebruiker.isAdmin());
      return credentials;
    }
    return null;
  }

  public static GbaWsCredentials getUserByCredentials(String username, String password, boolean useProwebAuthentication)
      throws ProRestAuthenticatieException {

    final UsrWrapper gebruiker = UsrDao.getUserByCredentials(username, password);
    GbaWsCredentials credentials = new GbaWsCredentials();

    if (gebruiker != null) {
      credentials.setUsername(gebruiker.getGebruikersNaam());
      credentials.setFullname(gebruiker.getVolledigeNaam());
      credentials.setAdmin(gebruiker.isAdmin());
      return credentials;
    }

    return getProwebCredentials(username, password, useProwebAuthentication, credentials);
  }

  private static GbaWsCredentials getProwebCredentials(String username, String password,
      boolean useProwebAuthentication, GbaWsCredentials credentials) {
    String prowebAuthentication = GbaConfig.get(GbaConfigProperty.PROWEB_AUTHENTICATION);
    if (useProwebAuthentication && fil(prowebAuthentication)) {
      ProRestClient client = new ProRestClient();
      client.setUrl(prowebAuthentication);
      client.setApplicatie("Personen-ws");
      client.setGebruikersnaam(username);
      client.setWachtwoord(password);

      ClientResponse response = null;

      try {
        response = client.getGebruiker(username);
      } catch (ClientHandlerException e) {
        if (e.getCause() instanceof ConnectException) {
          throw new ProException(ERROR,
              "Probleem met inloggen. Er kan geen verbinding worden gemaakt met de authenticatie server");
        }
      }

      if (response != null) {
        if (response.getStatus() == Status.OK.getStatusCode()) {
          ProRestGebruiker restUser = response.getEntity(ProRestGebruikerAntwoord.class).getGebruiker();
          if (fil(restUser.getGebruikersnaam())) {
            if (!restUser.getRollen().contains(Rol.BEHEERDER)) {
              throw new ProException(ERROR, "Alleen toegankelijk voor beheerders");
            }
            credentials.setUsername(restUser.getGebruikersnaam());
            credentials.setFullname(restUser.getNaam());
            credentials.setAdmin(true);
            return credentials;
          } else if (response.getStatus() != Status.UNAUTHORIZED.getStatusCode()) {
            throw new ProException(ERROR, response.getStatusInfo().getReasonPhrase());
          }
        }
      }
    }
    return null;
  }

  public static ProException getLogoutException() {
    HubContext.instance().logout();
    return new ProException(AUTHENTICATION, ERROR, "Foutieve inloggegevens");
  }
}
