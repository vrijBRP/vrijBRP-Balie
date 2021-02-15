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

import java.util.List;

import com.vaadin.data.validator.AbstractStringValidator;

import nl.procura.gba.web.modules.account.wachtwoord.pages.passwordExpired.PasswordCheck;
import nl.procura.gba.web.modules.account.wachtwoord.pages.passwordExpired.PasswordExpired;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.standard.exceptions.ProExceptionType;

public class WachtwoordHistorieValidator extends AbstractStringValidator {

  private final PasswordExpired passwordExpired;
  private final int             aantalOudeWw;

  public WachtwoordHistorieValidator(String errorMessage, PasswordExpired passwordExpired, int aantalOudeWw) {

    super(errorMessage);
    this.passwordExpired = passwordExpired;
    this.aantalOudeWw = aantalOudeWw;
  }

  @Override
  protected boolean isValidString(String value) {

    Gebruiker gebruiker = passwordExpired.getGebruiker();
    GebruikerService gebruikerService = passwordExpired.getGebruikers();

    if (gebruikerService == null) {
      throw new ProException(ProExceptionType.PROGRAMMING, ProExceptionSeverity.ERROR,
          "Kan de historie van de wachtwoorden niet opvragen.");
    }

    List<String> oldPws = gebruikerService.getGebruikerWachtwoorden(gebruiker, aantalOudeWw);
    return PasswordCheck.isDiff(oldPws, value);
  }
}
