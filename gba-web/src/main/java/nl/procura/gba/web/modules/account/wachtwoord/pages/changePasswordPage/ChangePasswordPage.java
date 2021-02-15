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

import static nl.procura.gba.web.modules.account.wachtwoord.pages.ShowPasswordsBean.SHOW_PWS;

import com.vaadin.ui.Button.ClickEvent;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.account.wachtwoord.SaveNewPassword;
import nl.procura.gba.web.modules.account.wachtwoord.pages.ChangePasswordForm;
import nl.procura.gba.web.modules.account.wachtwoord.pages.ShowPasswordsForm;
import nl.procura.gba.web.modules.account.wachtwoord.pages.passwordExpired.PasswordExpired;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.gba.web.windows.home.HomeWindow;
import nl.procura.vaadin.component.dialog.OkDialog;
import nl.procura.vaadin.component.label.Break;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class ChangePasswordPage extends NormalPageTemplate {

  private ChangePasswordForm form            = null;
  private ShowPasswordsForm  showPwForm      = null;
  private Gebruiker          gebruiker       = null;
  private GebruikerService   gebruikers      = null;
  private ShowPwsListener    showPwsListener = null;

  public ChangePasswordPage() {

    super("Wachtwoord wijzigen");
    addButton(buttonReset, buttonSave);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      gebruikers = getApplication().getServices().getGebruikerService();
      gebruiker = getGebruikerFromDatabase(
          getApplication().getServices().getGebruiker()); // haal gebruiker uit database: ww kan gereset zijn!

      form = new ChangePasswordForm(new PasswordExpired(gebruiker, null, gebruikers, null));
      showPwForm = new ShowPasswordsForm();
      showPwsListener = new ShowPwsListener(form, showPwForm);

      addComponents();
    }

    super.event(event);
  }

  @Override
  public void onNew() {

    form.reset();
    showPwsListener.setPwVisibility();
  }

  @Override
  public void onSave() {

    SaveNewPassword.saveNewPw(form, gebruikers, gebruiker);

    String message = SaveNewPassword.getMessage(gebruikers, gebruiker);

    getWindow().addWindow(new OkDialog(message, 600) {

      @Override
      public void buttonClick(ClickEvent event) {

        ChangePasswordPage currentInstance = ChangePasswordPage.this;

        super.buttonClick(event);
        currentInstance.onNew();
        currentInstance.getApplication().openWindow(currentInstance.getWindow(), new HomeWindow());
      }
    });
  }

  private void addComponents() {

    addComponent(form);
    addComponent(new Break());
    addComponent(showPwForm);
    addListenerToPwForm();

  }

  private void addListenerToPwForm() {
    showPwForm.getField(SHOW_PWS).addListener(showPwsListener);

  }

  private Gebruiker getGebruikerFromDatabase(Gebruiker gebruiker) {
    return gebruikers.getGebruikerByGebruiker(gebruiker);
  }
}
