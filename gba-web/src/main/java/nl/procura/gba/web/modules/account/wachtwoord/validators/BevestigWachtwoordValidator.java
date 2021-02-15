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

import static nl.procura.gba.web.modules.account.wachtwoord.pages.ChangePasswordBean.NIEUW_WW;

import com.vaadin.data.validator.AbstractStringValidator;

import nl.procura.gba.web.modules.account.wachtwoord.pages.ChangePasswordForm;

public class BevestigWachtwoordValidator extends AbstractStringValidator {

  private final ChangePasswordForm form;

  public BevestigWachtwoordValidator(ChangePasswordForm form) {
    super("Bevestiging niet gelijk aan het nieuwe wachtwoord.");
    this.form = form;
  }

  // Wordt aangeroepen bij form.commit(). Op dit moment staat nieuwe ww in veld NIEUW WW.

  @Override
  protected boolean isValidString(String value) {

    String nieuwWW = (String) form.getField(NIEUW_WW).getValue();
    return nieuwWW.equals(value);
  }
}
