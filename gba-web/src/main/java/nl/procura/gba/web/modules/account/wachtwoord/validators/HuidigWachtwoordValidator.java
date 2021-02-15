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

package nl.procura.gba.web.modules.account.wachtwoord.validators;

import com.vaadin.data.validator.AbstractStringValidator;

import nl.procura.gba.web.modules.account.wachtwoord.pages.passwordExpired.PasswordExpired;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.standard.exceptions.ProExceptionType;
import nl.procura.vaadin.theme.Credentials;

public class HuidigWachtwoordValidator extends AbstractStringValidator {

  private final PasswordExpired passwordExpired;

  public HuidigWachtwoordValidator(PasswordExpired passwordExpired) {

    super("Dit is niet uw huidige wachtwoord.");
    this.passwordExpired = passwordExpired;
  }

  @Override
  protected boolean isValidString(String value) {

    Gebruiker gebruiker = passwordExpired.getGebruiker();

    if (gebruiker == null) {
      throw new ProException(ProExceptionType.PROGRAMMING, ProExceptionSeverity.ERROR,
          "Gebruiker niet gezet: kan geen wachtwoord opvragen.");
    }

    GebruikerService gebruikers = passwordExpired.getGebruikers();
    return gebruikers.isCorrectWachtwoord(gebruiker, new Credentials(gebruiker.getGebruikersnaam(),
        value)); // gebruiker.isCorrectPassword (value);
  }
}
