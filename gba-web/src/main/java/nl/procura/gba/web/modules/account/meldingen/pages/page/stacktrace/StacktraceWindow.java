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

package nl.procura.gba.web.modules.account.meldingen.pages.page.stacktrace;

import static nl.procura.standard.Globalfunctions.fil;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.services.applicatie.meldingen.ServiceMelding;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.label.Ruler;

public class StacktraceWindow extends ModalWindow {

  private final CssLayout layout = new CssLayout() {

    @Override
    protected String getCss(com.vaadin.ui.Component c) {

      String css = "padding: 3px;";

      if (c instanceof Label) {

        String value = ((Label) c).getValue().toString();

        if (value.trim().startsWith("at")) {
          css += "padding-left: 30px;";
        } else {
          css += "font-weight: 700;";
        }
      }

      return css;
    }
  };

  public StacktraceWindow(ServiceMelding melding) {

    setCaption("Foutmelding (stacktrace)");

    VerticalLayout v = new VerticalLayout();
    v.setSpacing(true);
    v.setMargin(true);
    setContent(v);

    setModal(true);
    setWidth("800px");
    setHeight("500px");
    setResizable(false);
    setClosable(false);
    setDraggable(false);
    setCloseShortcut(KeyCode.ESCAPE, null);

    HorizontalLayout hLayout = new HorizontalLayout();
    hLayout.setWidth("100%");
    hLayout.setSpacing(true);
    hLayout.addComponent(BUTTON_CLOSE);

    layout.setWidth("100%");

    toString(melding.getExceptionString());

    addComponent(hLayout);
    addComponent(new StracktraceForm(melding));
    addComponent(new Ruler());
    addComponent(layout);
  }

  private void toString(String exception) {

    if (fil(exception)) {

      for (String line : exception.split("\n")) {

        layout.addComponent(new Label(line));
      }
    } else {
      layout.addComponent(new Label("Er is geen nadere informatie over deze melding."));
    }
  }
}
