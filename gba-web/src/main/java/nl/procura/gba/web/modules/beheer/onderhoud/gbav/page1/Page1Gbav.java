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

package nl.procura.gba.web.modules.beheer.onderhoud.gbav.page1;

import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.beheer.onderhoud.gbav.AbstractGbavPage;
import nl.procura.gba.web.modules.beheer.onderhoud.gbav.GbavWindowListener;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.GbaWsRestGbavAccount;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1Gbav extends AbstractGbavPage {

  private final Button               buttonDeblok = new Button("Deblokkeren");
  private final GbaWsRestGbavAccount account;
  private final GbavWindowListener   listener;

  private Page1GbavForm form = null;

  public Page1Gbav(GbaWsRestGbavAccount account, GbavWindowListener listener) {
    this.account = account;
    this.listener = listener;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonDeblok);
      addButton(buttonClose);

      setInfo("Overzicht", "");

      form = new Page1GbavForm(account);

      addComponent(form);

      checkBlokkering();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonDeblok) {
      personenWsService().gbavAccountUnblock(account);
      checkBlokkering();
      onClose();
    }

    super.handleEvent(button, keyCode);
  }

  private void checkBlokkering() {

    buttonDeblok.setEnabled(account.isGeblokkeerd());

    form.update(account);

    listener.update();
  }
}
