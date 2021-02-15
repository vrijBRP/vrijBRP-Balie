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

package nl.procura.gba.web.modules.zaken.rijbewijs.errorpage;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import nl.procura.rdw.functions.ProcesMelding;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class RijbewijsErrorWindow extends ModalWindow implements Button.ClickListener {

  public RijbewijsErrorWindow(ProcesMelding procesMelding) {

    setCaption("Foutmelding van het RDW");
    setWidth("600px");

    VerticalLayout v = new VerticalLayout();
    v.setMargin(true);
    v.setSpacing(true);
    setCloseShortcut(KeyCode.ENTER, null);

    v.addComponent(
        new InfoLayout("Er is een foutmelding ontvangen van het RDW", ProcuraTheme.ICOON_24.WARNING, ""));
    v.addComponent(new RijbewijsErrorForm1(procesMelding));

    Button okButton = new Button("Ok (enter)", (ClickListener) event -> closeWindow());

    okButton.setWidth("100px");

    HorizontalLayout h = new HorizontalLayout();
    h.setSizeFull();
    h.addComponent(okButton);
    h.setComponentAlignment(okButton, Alignment.MIDDLE_CENTER);
    v.addComponent(h);

    setContent(v);

    okButton.focus();
  }

  @Override
  public void buttonClick(ClickEvent event) {
  }
}
