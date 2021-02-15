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
import static nl.procura.standard.Globalfunctions.astr;

import java.util.List;

import com.vaadin.ui.Alignment;

import nl.procura.gba.web.common.misc.email.Verzending;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;

public class PasswordLostPage extends ButtonPageTemplate {

  private PasswordLostForm form = null;

  public PasswordLostPage() {

    buttonSave.setCaption("Versturen (F9)");

    addButton(buttonSave, 1f);
    addButton(buttonClose, Alignment.MIDDLE_RIGHT);
    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form = new PasswordLostForm() {

        @Override
        protected List<Gebruiker> getGebruikers() {
          return getApplication().getServices().getGebruikerService().getGebruikersByEmail(
              astr(getField(EMAIL).getValue()));
        }
      };

      addComponent(form);
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
  }

  @Override
  public void onSave() {

    form.commit();

    sendEmail(form.getGebruiker());

    onClose();
  }

  private void sendEmail(Gebruiker gebruiker) {

    String url = getApplication().getURL().toString();

    getServices().getEmailService().getWachtwoordVergetenEmail(url, new Verzending(gebruiker)).send();

    String message = "Een e-mail is verstuurd naar <b>" + gebruiker.getEmail() + "</b>";

    new Message(getApplication().getLoginWindow(), message, Message.TYPE_SUCCESS);
  }
}
