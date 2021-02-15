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

package nl.procura.gba.web.modules.beheer.gebruikers.page2;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.label.Ruler;

//public class WachtwoordWindow extends GbaModalWindow {

public class WachtwoordWindow extends ModalWindow implements Button.ClickListener {

  private final Button ok = new Button("Ok (Enter)", this);

  public WachtwoordWindow(Gebruiker gebruiker, String wachtwoord) {

    setCaption("Nieuw wachtwoord");

    setWidth("300px");

    setClosable(false);

    VerticalLayout v = new VerticalLayout();

    v.setSpacing(true);

    v.setMargin(true);

    Label label = new Label("Gebruiker: " + gebruiker.getNaam());

    v.addComponent(label);

    v.addComponent(new Ruler());

    H2 h2 = new H2(wachtwoord);

    v.addComponent(h2);

    HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);

    ok.setStyleName("primary");
    ok.focus();
    buttons.addComponent(ok);

    v.addComponent(buttons);

    v.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);

    setContent(v);
  }

  @Override
  public void buttonClick(ClickEvent event) {

    if (event.getButton() == ok) {
      closeWindow();
    }
  }
}
