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

package nl.procura.gba.web.modules.beheer.gebruikers.email.page1;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.common.misc.email.Verzending;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.gebruikers.email.page2.Page2SendEmail;
import nl.procura.gba.web.modules.beheer.gebruikers.page1.Page1Gebruikers;
import nl.procura.gba.web.services.beheer.email.EmailTemplate;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1SendEmail extends NormalPageTemplate {

  private Page1SendEmailTable1   table = null;
  private final List<Verzending> verzendingen;

  public Page1SendEmail(List<Verzending> verzendingen) {

    super("E-mails versturen");

    this.verzendingen = verzendingen;

    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      if (getWindow().getParent() == null) {

        addButton(buttonPrev);
      } else {
        addButton(buttonClose);
      }

      table = new Page1SendEmailTable1() {

        @Override
        public void onDoubleClick(Record record) {

          List<Verzending> nieuweVerzendingen = new ArrayList<>();

          for (Verzending verzending : verzendingen) {

            nieuweVerzendingen.add(
                new Verzending(getSelectedRecord().getObject(EmailTemplate.class), verzending));
          }

          getNavigation().goToPage(new Page2SendEmail(nieuweVerzendingen));
        }
      };

      setInfo("", "Selecteer een e-mail.");

      addExpandComponent(table);
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
    getNavigation().goBackToPage(Page1Gebruikers.class);
  }
}
