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

package nl.procura.gbaws.web.rest.v1_0.gebruiker;

import static java.util.Optional.ofNullable;
import static nl.procura.gbaws.web.vaadin.login.GbaWsAuthenticationHandler.getUserByCredentials;
import static nl.procura.gbaws.web.vaadin.login.GbaWsAuthenticationHandler.getUserByUsernameAndEmail;

import java.util.Optional;

import nl.procura.gbaws.web.vaadin.login.GbaWsCredentials;
import nl.procura.proweb.rest.guice.misc.ProRestAuthenticatieValidator;
import nl.procura.proweb.rest.v1_0.Rol;
import nl.procura.proweb.rest.v1_0.gebruiker.ProRestGebruiker;
import nl.procura.proweb.rest.v1_0.gebruiker.ProRestGebruikerAntwoord;
import nl.vrijbrp.hub.client.HubAuth;
import nl.vrijbrp.hub.client.HubContext;

public class GbaWsRestAuthenticatieValidator implements ProRestAuthenticatieValidator {

  @Override
  public ProRestGebruikerAntwoord getGebruiker(String application, String username, String password) {
    ProRestGebruikerAntwoord antwoord = new ProRestGebruikerAntwoord();
    Optional<HubAuth> authentication = HubContext.instance().authentication();
    GbaWsCredentials user = ofNullable(authentication
        .map(auth -> getUserByUsernameAndEmail(auth.username(), auth.email()))
        .orElseGet(() -> getUserByCredentials(username, password, false)))
            .orElseGet(() -> {
              HubContext.instance().logout();
              return null;
            });

    ProRestGebruiker restGebruiker = new ProRestGebruiker();
    if (user != null) {
      restGebruiker.setGebruikersnaam(user.getUsername());
      restGebruiker.setNaam(user.getFullname());
      restGebruiker.getRollen().add(Rol.GEBRUIKER);
      if (user.isAdmin()) {
        restGebruiker.getRollen().add(Rol.BEHEERDER);
      }
    }

    antwoord.setGebruiker(restGebruiker);
    return antwoord;
  }
}
