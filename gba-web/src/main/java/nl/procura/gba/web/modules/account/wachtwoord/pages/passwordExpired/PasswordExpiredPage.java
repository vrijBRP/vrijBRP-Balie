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

package nl.procura.gba.web.modules.account.wachtwoord.pages.passwordExpired;

import static nl.procura.gba.web.modules.account.wachtwoord.pages.ShowPasswordsBean.SHOW_PWS;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Window;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.modules.account.wachtwoord.SaveNewPassword;
import nl.procura.gba.web.modules.account.wachtwoord.pages.ChangePasswordForm;
import nl.procura.gba.web.modules.account.wachtwoord.pages.ShowPasswordsForm;
import nl.procura.gba.web.modules.account.wachtwoord.pages.changePasswordPage.ShowPwsListener;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.dialog.OkDialog;
import nl.procura.vaadin.component.label.Break;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class PasswordExpiredPage extends ButtonPageTemplate {

  private ChangePasswordForm    form            = null;
  private ShowPasswordsForm     showPwForm      = null;
  private final PasswordExpired passwordExpired;
  private ShowPwsListener       showPwsListener = null;

  public PasswordExpiredPage(PasswordExpired passwordExpired) {
    this.passwordExpired = passwordExpired;

    addButton(buttonReset);
    addButton(buttonSave, 1f);
    addButton(buttonClose, Alignment.MIDDLE_RIGHT);

    setInfo(
        "Uw wachtwoord is verlopen. Stel hier een nieuw wachtwoord in. Het nieuwe wachtwoord moet minimaal <b>8 tekens</b> bevatten."
            + " Bovendien moet het wachtwoord tenminste 1 hoofdletter, 1 cijfer en 1 vreemd teken bevatten en verschillen van de vorige 6 "
            + "wachtwoorden");

    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form = new ChangePasswordForm(passwordExpired);
      showPwForm = new ShowPasswordsForm();
      showPwsListener = new ShowPwsListener(form, showPwForm);

      addComponents();
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onNew() {

    form.reset();
    showPwsListener.setPwVisibility();
  }

  @Override
  public void onSave() {

    SaveNewPassword.saveNewPw(form, passwordExpired.getGebruikers(), passwordExpired.getGebruiker());
    String message = SaveNewPassword.getMessage(passwordExpired.getGebruikers(), passwordExpired.getGebruiker());

    if (passwordExpired.getPersonenLink() != null) {
      passwordExpired.getLinks().delete(passwordExpired.getPersonenLink());
    }

    ModalWindow currentWindow = (ModalWindow) getWindow();
    Window parentWindow = currentWindow.getParent();

    currentWindow.closeWindow();

    parentWindow.addWindow(new OkDialog(message, 600) {

      @Override
      public void closeWindow() {
        getApplication().close();
      }
    });
  }

  private void addComponents() {

    addComponent(form);
    addComponent(new Break());
    addComponent(showPwForm);
    addlistenerToPwForm();
  }

  private void addlistenerToPwForm() {
    showPwForm.getField(SHOW_PWS).addListener(showPwsListener);
  }
}
