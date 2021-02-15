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

package nl.procura.gba.web.modules.zaken.sms.page2;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.hoofdmenu.sms.page2.Page2SmsForm;
import nl.procura.gba.web.modules.hoofdmenu.sms.page2.Page2SmsTable;
import nl.procura.gba.web.modules.persoonslijst.overig.PlPage;
import nl.procura.gba.web.modules.zaken.reisdocument.page2.Page2Reisdocument;
import nl.procura.gba.web.modules.zaken.rijbewijs.page2.Page2Rijbewijs;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.sms.rest.domain.Message;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Sms extends PlPage {

  protected final Button buttonZaak = new Button("Toon zaak");

  private final Message message;
  private Page2SmsForm  form;
  private Page2SmsTable table;

  public Page2Sms(Message message) {
    super("SMS bericht");
    this.message = message;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addButton(buttonPrev);

      form = new Page2SmsForm(message);
      table = new Page2SmsTable(message);

      OptieLayout ol = new OptieLayout();
      ol.getLeft().addComponent(form);
      ol.getRight().setCaption("Opties");
      ol.getRight().setWidth("160px");
      ol.getRight().addButton(buttonZaak, this);
      buttonZaak.setEnabled(fil(message.getZaakId()));

      addComponent(ol);
      addComponent(new Fieldset("Statussen (van nieuw naar oud)"));
      addExpandComponent(table);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (buttonZaak.equals(button)) {
      goToZaak();
    }

    super.handleEvent(button, keyCode);
  }

  private void goToZaak() {
    if (fil(message.getZaakId())) {
      for (Zaak zaak : getServices().getZakenService().getMinimaleZaken(
          new ZaakArgumenten(message.getZaakId()))) {
        if (zaak instanceof ReisdocumentAanvraag) {
          getNavigation().goToPage(new Page2Reisdocument((ReisdocumentAanvraag) zaak));
        } else if (zaak instanceof RijbewijsAanvraag) {
          getNavigation().goToPage(new Page2Rijbewijs((RijbewijsAanvraag) zaak));
        } else {
          String type = zaak.getType().getOms().toLowerCase();
          throw new ProException(WARNING, "Er kan niet worden doorverwezen naar de zaak van het type: {0}",
              type);
        }
        break;
      }
    }
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }
}
