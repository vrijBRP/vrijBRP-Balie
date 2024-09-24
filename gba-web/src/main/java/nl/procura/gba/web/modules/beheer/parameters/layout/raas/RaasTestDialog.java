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

package nl.procura.gba.web.modules.beheer.parameters.layout.raas;

import com.vaadin.ui.TextArea;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.raas.rest.domain.ResponseMessages;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;

public abstract class RaasTestDialog extends GbaModalWindow {

  public RaasTestDialog() {
    super("RAAS testen (Escape om te sluiten)", "600px");
  }

  @Override
  public void attach() {
    StringBuilder text = new StringBuilder();
    try {
      ResponseMessages messages = getGbaApplication().getServices().getRaasService().test();
      messages.forEach(msg -> text.append(msg.getMessage()).append("\n"));
    } catch (RuntimeException e) {
      text.append(e.getMessage());
    }

    TextArea textArea = new TextArea();
    textArea.setStyleName("raas-textarea");
    textArea.setValue(text.toString());
    textArea.setReadOnly(true);
    textArea.setRows(12);
    textArea.setSizeFull();
    textArea.setHeight("300px");
    setContent(new VLayout()
        .margin(true)
        .add(new Fieldset("Meldingen", textArea)));

    super.attach();
  }
}
