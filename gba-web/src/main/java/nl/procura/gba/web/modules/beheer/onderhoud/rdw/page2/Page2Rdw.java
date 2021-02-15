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

package nl.procura.gba.web.modules.beheer.onderhoud.rdw.page2;

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.modules.beheer.onderhoud.rdw.RdwPage;
import nl.procura.gba.web.modules.beheer.onderhoud.rdw.RdwWindowListener;
import nl.procura.gba.web.services.Services;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Rdw extends RdwPage {

  private Page2RdwForm form;

  public Page2Rdw(RdwWindowListener listener) {
    super(listener);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonSave);
      addButton(buttonClose);

      setInfo("Huidig wachtwoord",
          "Geef in het huidige wachtwoord en de datum waarop deze is gewijzigd.<br/>Dit laatste is om de verloopdatum te kunnen bepalen.");

      String gebruikersnaam = getAccount().getGebruikersnaam();
      String datum = getAccount().getDatumGewijzigd();

      form = new Page2RdwForm(gebruikersnaam, datum);
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onSave() {

    form.commit();

    String wachtwoord = form.getBean().getWachtwoord();
    String datum = astr(form.getBean().getDatum().getValue());

    Services.getInstance().getRijbewijsService().setAccount(wachtwoord, datum);
    successMessage("De gegevens zijn opgeslagen");
    getListener().update();

    super.onSave();
  }
}
