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

package nl.procura.gba.web.modules.account.wachtwoord.pages.changePasswordPage;

import static nl.procura.gba.web.modules.account.wachtwoord.pages.ChangePasswordBean.*;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

import nl.procura.gba.web.modules.account.wachtwoord.pages.ChangePasswordForm;
import nl.procura.gba.web.modules.account.wachtwoord.pages.ShowPasswordsForm;

public class ShowPwsListener implements ValueChangeListener {

  private final ChangePasswordForm form;
  private final ShowPasswordsForm  showPwForm;

  public ShowPwsListener(ChangePasswordForm form, ShowPasswordsForm showPwForm) {
    this.form = form;
    this.showPwForm = showPwForm;
  }

  @SuppressWarnings("deprecation")
  public void setPwVisibility() {

    boolean isShowPasswords = showPwForm.getBean().isShowPasswords();

    for (Field field : form.getFields(HUIDIG_WW, NIEUW_WW, HERHALING_NIEUW_WW)) {
      if (field != null) {
        TextField textField = (TextField) field;
        textField.setSecret(!isShowPasswords);
      }
    }
  }

  @Override
  public void valueChange(ValueChangeEvent event) {

    showPwForm.commit();
    setPwVisibility();
  }
}
