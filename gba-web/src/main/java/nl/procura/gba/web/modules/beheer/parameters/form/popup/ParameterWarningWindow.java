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

package nl.procura.gba.web.modules.beheer.parameters.form.popup;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class ParameterWarningWindow extends GbaModalWindow {

  private Button                     ok = null;
  private final ParameterWarningForm parameterWarningForm;

  public ParameterWarningWindow(String parameter, String warning, String cause) {

    super("Waarschuwing bij invoer parameter", "700px");

    parameterWarningForm = new ParameterWarningForm(parameter, warning, cause);

    addComponent(new Page());
  }

  class Page extends ButtonPageTemplate {

    @Override
    public void event(PageEvent event) {

      if (event.isEvent(InitPage.class)) {

        setSpacing(true);

        addComponent(parameterWarningForm);
        addComponent(new Label("<i>De parameter is wel opgeslagen.</i>", Label.CONTENT_XHTML));

        ok = new Button("Ok (Enter)", this);
        ok.setStyleName("primary");
        ok.focus();
        addComponent(ok);
        setComponentAlignment(ok, Alignment.BOTTOM_CENTER);
      }

      super.event(event);
    }

    @Override
    public void handleEvent(Button button, int keyCode) {

      if (button == ok || keyCode == KeyCode.ENTER) {
        closeWindow();
      }

      super.handleEvent(button, keyCode);
    }
  }
}
