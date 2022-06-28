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

package nl.procura.gba.web.modules.account.wachtwoord.pages.passwordlost;

import static nl.procura.gba.web.modules.account.wachtwoord.pages.passwordlost.PasswordLostBean.EMAIL;

import java.util.Optional;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;

public class PasswordLostForm extends GbaForm<PasswordLostBean> {

  private Gebruiker gebruiker = null;

  public PasswordLostForm() {

    setOrder(EMAIL);
    setColumnWidths("80px", "");
    setBean(new PasswordLostBean());
  }

  public Gebruiker getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(Gebruiker gebruiker) {
    this.gebruiker = gebruiker;
  }

  @Override
  public void setBean(Object bean) {

    super.setBean(bean);

    getField(EMAIL).addValidator(new Validator());
  }

  protected Optional<Gebruiker> findGebruiker() {
    return Optional.empty();
  }

  class Validator extends AbstractValidator {
    public Validator() {
      super("Fout");
    }

    @Override
    public boolean isValid(Object value) {
      if (new EmailValidator("").isValid(value)) {
        Optional<Gebruiker> gebruiker = findGebruiker();
        if (gebruiker.isPresent()) {
          setGebruiker(gebruiker.get());
          return true;
        } else {
          setErrorMessage("Dit e-mail adres is niet gekopppeld aan een account");
          return false;
        }
      }

      return true;
    }
  }
}
