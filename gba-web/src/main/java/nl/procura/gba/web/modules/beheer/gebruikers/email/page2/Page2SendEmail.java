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

package nl.procura.gba.web.modules.beheer.gebruikers.email.page2;

import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.common.misc.email.Verzending;
import nl.procura.gba.web.common.misc.email.Verzending.VerzendingStatus;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;

public class Page2SendEmail extends NormalPageTemplate {

  private final List<Verzending> verzendingen;
  private Page2SendEmailTable    table = null;
  private Page2SendEmailForm     form  = null;

  public Page2SendEmail(List<Verzending> verzendingen) {

    super("E-mails versturen");

    this.verzendingen = verzendingen;

    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form = new Page2SendEmailForm(verzendingen.get(0).getEmail());
      table = new Page2SendEmailTable(verzendingen);

      buttonSave.setCaption("Versturen (F9)");

      addButton(buttonPrev);
      addButton(buttonSave, 1f);

      if (getWindow().getParent() != null) {
        addButton(buttonClose);
      }

      addComponent(form);

      addComponent(new Fieldset("Gebruikers", table));
    } else if (event.isEvent(AfterBackwardReturn.class)) {

      table.init();
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    int nr = 0;

    ArrayList<Verzending> verzendingen = table.getSelectedValues(Verzending.class);

    if (verzendingen.isEmpty()) {
      throw new ProException(WARNING, "Geen gebruikers geselecteerd");
    }

    for (Verzending verzending : verzendingen) {

      if (verzending.getStatus() == VerzendingStatus.GEEN) {

        try {

          String url = getApplication().getURL().toString();

          switch (verzending.getEmail().getType()) {

            case WACHTWOORD_VERGETEN:
              getServices().getEmailService().getWachtwoordVergetenEmail(url, verzending).send();
              break;

            case NIEUWE_GEBRUIKER:
              getServices().getEmailService().getNewGebruikerEmail(url, verzending).send();
              break;

            default:
              throw new ProException(ERROR,
                  "Onbekend e-mailtype : " + verzending.getEmail().getType().getOms());
          }

          verzending.setStatus(VerzendingStatus.VERSTUURD);

          nr++;
        } catch (Exception e) {

          e.printStackTrace();

          verzending.setStatus(VerzendingStatus.FOUT, e.getMessage());
        }
      }
    }

    switch (nr) {
      case 0:
        new Message(getWindow(), "Er zijn geen nieuwe e-mails verstuurd.", Message.TYPE_INFO);
        break;

      case 1:
        new Message(getWindow(), "Er is één e-mail verstuurd.", Message.TYPE_SUCCESS);
        break;

      default:
        new Message(getWindow(), "Er zijn " + nr + " e-mails verstuurd.", Message.TYPE_SUCCESS);
    }

    table.init();

    super.onSave();
  }
}
