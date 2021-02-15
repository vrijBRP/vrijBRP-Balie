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

import com.vaadin.terminal.ErrorMessage;
import com.vaadin.terminal.UserError;

import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.theme.Credentials;
import nl.procura.vaadin.theme.twee.login.CookieLoginValidator;

public class GbaWsLoginValidator extends CookieLoginValidator {

  public GbaWsLoginValidator() {
    super("qoemxbceuogjnbwbhfueggph0837", "304ncnybf8r3u24bf9fiefn23e23bge");
  }

  @Override
  public ErrorMessage validate(Credentials credentials) {

    try {
      validated(GbaWsAuthenticationHandler.getUser(credentials.getUsername(), credentials.getPassword(), true));
      return null;
    } catch (ProException e) {
      return new UserError(e.getMessage());
    }
  }
}
