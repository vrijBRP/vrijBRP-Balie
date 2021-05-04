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

package nl.procura.gbaws.web.vaadin.layouts;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;

import nl.procura.gbaws.web.vaadin.application.GbaWsApplication;
import nl.procura.gbaws.web.vaadin.login.GbaWsCredentials;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.theme.LoginValidatable;
import nl.procura.vaadin.theme.twee.ProcuraTweeTheme;
import nl.procura.vaadin.theme.twee.layout.ToolBarLayout;

@SuppressWarnings("serial")
public class ToolBar extends ToolBarLayout {

  private final Label            gebruikerLabel = new Label();
  private final Button           logoutButton   = getLogoutButton();
  private final HorizontalLayout right          = new HorizontalLayout();
  private final HorizontalLayout left           = new HorizontalLayout();

  public ToolBar() {

    left.setSizeUndefined();
    left.addStyleName("left");
    addComponent(left);

    right.addStyleName("right");
    addComponent(right);

    left.addComponent(getLogo());
    right.addComponent(getGebruikerLabel());
    right.addComponent(logoutButton);
    //
    setExpandRatio(left, 1);
  }

  private Component getGebruikerLabel() {
    return gebruikerLabel;
  }

  protected Label getLogo() {

    Label logo_label = new Label("vrijBRP | Balie | webservice persoonsgegevens");
    logo_label.setStyleName("toolbar-logo");

    return logo_label;
  }

  private Button getLogoutButton() {

    Button b = new NativeButton("", (Button.ClickListener) event -> openLogoutWindow());

    b.setIcon(new ThemeResource(ProcuraTweeTheme.ICOON_18.UITLOGGEN));
    b.addStyleName("borderless");

    return b;
  }

  private void openLogoutWindow() {

    getWindow().addWindow(new ConfirmDialog("Weet u zeker dat u wilt uitloggen?") {

      @Override
      public void buttonYes() {

        ((LoginValidatable) ((GbaWsApplication) getApplication()).getLoginWindow()).getLoginValidator()
            .deleteCredentials();
        getApplication().close();
      }
    });
  }

  @Override
  public void attach() {

    super.attach();

    GbaWsCredentials creds = (GbaWsCredentials) getApplication().getUser();

    gebruikerLabel.setValue("Gebruiker: " + creds.getFullname());

    GbaWsApplication application = (GbaWsApplication) getApplication();

    if (application.getParameterMap().get("embedded") != null) {

      right.removeComponent(logoutButton);
    } else {

      gebruikerLabel.addStyleName("right-separator");
    }
  }
}
