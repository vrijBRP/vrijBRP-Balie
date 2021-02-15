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

package nl.procura.gba.web.modules.beheer.onderhoud.rdw.page3;

import static nl.procura.gba.web.modules.beheer.onderhoud.rdw.page3.Page3RdwBean.WACHTWOORD;

import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.beheer.onderhoud.rdw.RdwPage;
import nl.procura.gba.web.modules.beheer.onderhoud.rdw.RdwWindowListener;
import nl.procura.gba.web.services.Services;
import nl.procura.rdw.functions.RdwPasswordGenerator;
import nl.procura.rdw.messages.P0013;
import nl.procura.standard.ProcuraDate;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.window.Message;

public class Page3Rdw extends RdwPage {

  private final Button buttonGenerate = new Button("Genereer wachtwoord");
  private Page3RdwForm form;

  public Page3Rdw(RdwWindowListener listener) {
    super(listener);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonGenerate);
      addButton(buttonSave);
      addButton(buttonClose);

      setInfo("Nieuw wachtwoord", "Genereer of vul een zelfbedacht wachtwoord in.");
      form = new Page3RdwForm();
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonGenerate) {
      try {
        form.setWachtwoord(RdwPasswordGenerator.newPassword());
      } catch (Exception e) {
        throw new ProException("Fout bij genereren wachtwoord", e);
      }
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onSave() {

    form.commit();

    String wachtwoord = form.getBean().getWachtwoord();

    P0013 p0013 = new P0013();
    p0013.newF1(wachtwoord);
    p0013.setIgnoreBlock(true);

    if (sendMessage(p0013)) {
      Services.getInstance().getRijbewijsService().setAccount(wachtwoord, new ProcuraDate().getSystemDate());
      getListener().update();
      new Message(getParentWindow(), "Het wachtwoord is bijgewerkt", Message.TYPE_SUCCESS);
      form.getField(WACHTWOORD).setReadOnly(true);
      form.repaint();
    }

    super.onSave();
  }
}
