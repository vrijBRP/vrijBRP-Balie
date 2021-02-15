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

package nl.procura.gba.web.modules.beheer.onderhoud.rdw.page4;

import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.beheer.onderhoud.rdw.RdwPage;
import nl.procura.gba.web.modules.beheer.onderhoud.rdw.RdwWindowListener;
import nl.procura.rdw.functions.ProcesMelding;
import nl.procura.rdw.messages.P1303;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page4Rdw extends RdwPage {

  private final Button buttonTest = new Button("Test verbinding");
  private Page4RdwForm form;

  public Page4Rdw(RdwWindowListener listener) {
    super(listener);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonTest);
      addButton(buttonClose);

      setInfo("Test verbinding", "");
      form = new Page4RdwForm();
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonTest) {

      P1303 p1313 = new P1303();
      p1313.newF1();
      if (sendMessage(p1313)) {
        ProcesMelding melding = p1313.getResponse().getMelding();
        if (melding != null) {
          form.setMelding(melding.getMeldingKort());
        }
      }

      getListener().update();
    }

    super.handleEvent(button, keyCode);
  }
}
