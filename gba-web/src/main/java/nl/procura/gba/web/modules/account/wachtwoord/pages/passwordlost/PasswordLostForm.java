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

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;

import nl.procura.gba.web.components.layouts.form.GbaForm;

public class PasswordLostForm extends GbaForm<PasswordLostBean> {

    public PasswordLostForm() {

        setOrder(EMAIL);
        setColumnWidths("80px", "");
        setBean(new PasswordLostBean());
    }

    public String getEmail() {
        return getValue(EMAIL).toString();
    }

    @Override
    public void setBean(Object bean) {

        super.setBean(bean);

        getField(EMAIL).addValidator(new Validator());
    }

    class Validator extends AbstractValidator {
        public Validator() {
            super("Fout");
        }

        @Override
        public boolean isValid(Object value) {
            return new EmailValidator("").isValid(value);
        }
    }
}
