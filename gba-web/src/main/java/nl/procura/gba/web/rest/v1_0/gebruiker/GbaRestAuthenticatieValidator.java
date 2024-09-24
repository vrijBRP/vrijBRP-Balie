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

package nl.procura.gba.web.rest.v1_0.gebruiker;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.Services.TYPE;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.proweb.rest.guice.misc.ProRestAuthenticatieValidator;
import nl.procura.proweb.rest.v1_0.Rol;
import nl.procura.proweb.rest.v1_0.gebruiker.ProRestGebruiker;
import nl.procura.proweb.rest.v1_0.gebruiker.ProRestGebruikerAntwoord;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.theme.Credentials;

public class GbaRestAuthenticatieValidator implements ProRestAuthenticatieValidator {

  @Override
  public ProRestGebruikerAntwoord getGebruiker(String application, String username, String password) {

    ProRestGebruikerAntwoord antwoord = new ProRestGebruikerAntwoord();

    Services services = new Services(TYPE.REST);
    GebruikerService gebruikers = services.getGebruikerService();
    ProRestGebruiker restGebruiker = new ProRestGebruiker();
    Credentials credentials = new Credentials(username, password);

    try {
      Gebruiker gebruiker = gebruikers.getGebruikerByCredentials(application, "", credentials, true);
      restGebruiker.setCode(astr(gebruiker.getCUsr()));
      restGebruiker.setGebruikersnaam(gebruiker.getGebruikersnaam());
      restGebruiker.setNaam(gebruiker.getNaam());
      restGebruiker.getRollen().add(Rol.GEBRUIKER);

      if (gebruiker.isAdministrator()) {
        restGebruiker.getRollen().add(Rol.BEHEERDER);
      }
    } catch (ProException e) {
      if (e.getSeverity() == ERROR) {
        e.printStackTrace();
      }
    }

    antwoord.setGebruiker(restGebruiker);

    return antwoord;
  }
}
