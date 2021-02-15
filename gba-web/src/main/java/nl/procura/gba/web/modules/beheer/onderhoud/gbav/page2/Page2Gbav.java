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

package nl.procura.gba.web.modules.beheer.onderhoud.gbav.page2;

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.modules.beheer.onderhoud.gbav.AbstractGbavPage;
import nl.procura.gba.web.modules.beheer.onderhoud.gbav.GbavWindowListener;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.GbaWsRestGbavAccount;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Gbav extends AbstractGbavPage {

  private final GbaWsRestGbavAccount account;
  private final GbavWindowListener   listener;

  private Page2GbavForm form;

  public Page2Gbav(GbaWsRestGbavAccount account, GbavWindowListener listener) {
    this.account = account;
    this.listener = listener;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonSave);
      addButton(buttonClose);

      setInfo("Huidig wachtwoord",
          "Geef in het huidige wachtwoord en de datum waarop deze is gewijzigd.<br/>Dit laatste is om de verloopdatum te kunnen bepalen.");

      String datum = account.getDatumGewijzigd();

      form = new Page2GbavForm(datum);

      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onSave() {

    form.commit();

    String datum = astr(form.getBean().getDatum().getValue());
    String wachtwoord = form.getBean().getWachtwoord();

    if (personenWsService().gbavAccountUpdatePassword(account, datum, wachtwoord)) {

      successMessage("De gegevens zijn opgeslagen");

      listener.update();
    } else {
      errorMessage("De gegevens zijn niet opgeslagen");
    }

    super.onSave();
  }
}
