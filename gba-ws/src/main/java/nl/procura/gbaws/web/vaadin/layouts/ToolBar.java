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
import nl.procura.vaadin.theme.LoginValidatable;
import nl.procura.vaadin.theme.twee.ProcuraTweeTheme;
import nl.procura.vaadin.theme.twee.layout.ToolBarLayout;
import nl.vrijbrp.hub.client.HubContext;

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
    gebruikerLabel.addStyleName("right-separator");
    return gebruikerLabel;
  }

  protected Label getLogo() {
    Label logo = new Label("vrijBRP | Balie | webservice persoonsgegevens");
    logo.setStyleName("toolbar-logo");
    return logo;
  }

  private Button getLogoutButton() {
    Button button = new NativeButton("", (Button.ClickListener) event -> openLogoutWindow());
    button.setIcon(new ThemeResource(ProcuraTweeTheme.ICOON_18.UITLOGGEN));
    button.addStyleName("borderless");
    button.setWidth("100px");
    return button;
  }

  private void openLogoutWindow() {
    HubContext.instance().logout().requestReturnToHub();
    GbaWsApplication application = (GbaWsApplication) getApplication();
    LoginValidatable loginWindow = (LoginValidatable) application.getLoginWindow();
    loginWindow.getLoginValidator().deleteCredentials();
    getApplication().close();
  }

  @Override
  public void attach() {
    super.attach();
    GbaWsCredentials creds = (GbaWsCredentials) getApplication().getUser();
    gebruikerLabel.setValue(creds.getFullname());
  }
}
