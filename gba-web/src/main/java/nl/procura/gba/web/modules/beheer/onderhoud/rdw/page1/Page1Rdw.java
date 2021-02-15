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

package nl.procura.gba.web.modules.beheer.onderhoud.rdw.page1;

import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.beheer.onderhoud.rdw.RdwPage;
import nl.procura.gba.web.modules.beheer.onderhoud.rdw.RdwWindowListener;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAccount;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1Rdw extends RdwPage {

  private final Button buttonBlokkeren = new Button("Deblokkeren");
  private Page1RdwForm form            = null;

  public Page1Rdw(RdwWindowListener listener) {
    super(listener);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      buttonBlokkeren.setWidth("130px");

      addButton(buttonBlokkeren);
      addButton(buttonClose);

      setInfo("Overzicht", "");
      form = new Page1RdwForm(getAccount());
      addComponent(form);
      checkBlokkering();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonBlokkeren) {
      if (getAccount().isGeblokkeerd()) {
        getServices().getRijbewijsService().deblokkeer();
      } else {
        getServices().getRijbewijsService().blokkeer();
      }
      checkBlokkering();
    }

    super.handleEvent(button, keyCode);
  }

  private void checkBlokkering() {
    RijbewijsAccount account = getAccount();
    buttonBlokkeren.setCaption(account.isGeblokkeerd() ? "Deblokkeren" : "Blokkeren");
    form.update(account);
    getListener().update();
  }
}
