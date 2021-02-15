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

package nl.procura.gba.web.modules.zaken.verhuizing.page18;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.emp;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.window.Message;

public abstract class IncompleetDialog extends GbaModalWindow implements Button.ClickListener {

  private final TextArea textArea        = new TextArea();
  private final Button   buttonSave      = new Button("Opslaan", this);
  private final Button   buttonAnnuleren = new Button("Annuleren (Esc)", this);

  public IncompleetDialog(String melding) {

    setStyleName("small-margin-dialog");

    VerticalLayout v = (VerticalLayout) getContent();
    v.setMargin(true);
    v.setSpacing(true);

    setWidth("400px");
    setCaption("Geef aanvullende informatie");
    v.addComponent(new InfoLayout("", "Geef aan waarom deze zaak initieël op incompleet moet komen te staan."));

    HorizontalLayout h = new HorizontalLayout();
    h.setSpacing(true);
    h.addComponent(buttonSave);
    h.addComponent(buttonAnnuleren);

    textArea.setSizeFull();
    textArea.setRows(3);
    textArea.setValue(melding);

    v.addComponent(textArea);
    v.addComponent(h);
    v.setComponentAlignment(h, Alignment.MIDDLE_CENTER);

    setContent(v);
  }

  @Override
  public void buttonClick(ClickEvent event) {

    if (event.getButton() == buttonSave) {

      String melding = astr(textArea.getValue());

      if (emp(melding)) {
        new Message(getParent(), "Geef aan waarom deze zaak initieël op incompleet moet komen te staan",
            Message.TYPE_INFO);
        return;
      }

      onSave(melding);
    } else if (event.getButton() == buttonAnnuleren) {
      onCancel();
    }

    closeWindow();
  }

  public abstract void onCancel();

  public abstract void onSave(String melding);
}
