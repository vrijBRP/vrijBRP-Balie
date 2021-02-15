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

package nl.procura.gba.web.modules.beheer.onderhoud.gbav.page3;

import static nl.procura.gba.web.modules.beheer.onderhoud.gbav.page3.Page3GbavBean.WACHTWOORD;

import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.beheer.onderhoud.gbav.AbstractGbavPage;
import nl.procura.gba.web.modules.beheer.onderhoud.gbav.GbavWindowListener;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.GbaWsRestGbavAccount;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;

public class Page3Gbav extends AbstractGbavPage {

  private final Button               buttonGenerate = new Button("Genereer wachtwoord");
  private final GbaWsRestGbavAccount account;
  private final GbavWindowListener   listener;

  private Page3GbavForm form;

  public Page3Gbav(GbaWsRestGbavAccount account, GbavWindowListener listener) {
    this.account = account;
    this.listener = listener;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonGenerate);
      addButton(buttonSave);
      addButton(buttonClose);

      setInfo("Nieuw wachtwoord", "Genereer of vul een zelfbedacht wachtwoord in.");

      form = new Page3GbavForm();

      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonGenerate) {
      form.setWachtwoord(personenWsService().gbavGeneratePassword());
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onSave() {

    form.commit();

    String wachtwoord = form.getBean().getWachtwoord();

    if (personenWsService().gbavAccountSetPassword(account, wachtwoord)) {
      new Message(getParentWindow(), "Het GBA-V wachtwoord is bijgewerkt", Message.TYPE_SUCCESS);
      listener.update();
      form.getField(WACHTWOORD).setReadOnly(true);
      form.repaint();
    } else {
      errorMessage("De gegevens zijn niet opgeslagen");
    }

    super.onSave();
  }
}
