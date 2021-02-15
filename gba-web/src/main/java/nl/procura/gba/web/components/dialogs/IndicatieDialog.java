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

package nl.procura.gba.web.components.dialogs;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class IndicatieDialog extends ModalWindow implements Button.ClickListener {

  private final Button ok = new Button("Ok (Enter)", this);

  public IndicatieDialog(String title, String caption, String msg, String width) {
    this(title, caption, msg, width, ProcuraTheme.ICOON_24.WARNING);
  }

  public IndicatieDialog(String title, String caption, String msg, String width, String icon) {

    setCaption(title);
    setWidth(width);
    setClosable(false);
    setStyleName("indicatie-dialog");

    VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setMargin(true);
    verticalLayout.setSpacing(true);

    InfoLayout infoLayout = new InfoLayout(caption, icon, msg);
    HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);

    ok.setStyleName("primary");
    ok.focus();
    buttons.addComponent(ok);

    verticalLayout.addComponent(infoLayout);
    verticalLayout.addComponent(buttons);
    verticalLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);

    setContent(verticalLayout);
  }

  @Override
  public void buttonClick(ClickEvent event) {

    if (event.getButton() == ok) {
      closeWindow();
    }
  }
}
