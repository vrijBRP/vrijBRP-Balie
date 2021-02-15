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

package nl.procura.gba.web.components.layouts.navigation;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.GbaHorizontalLayout;

public class MenuLayout extends GbaHorizontalLayout {

  private final Button homeButton  = new Button();
  private final Button backButton  = new Button();
  private final Button adminButton = new Button();
  private final Button userButton  = new Button();

  public MenuLayout(boolean useBackButton) {

    setStyleName("menulayout");

    homeButton.setWidth("40px");
    homeButton.setDescription("Terug naar het zoekargumentenscherm (F12)");
    homeButton.setIcon(new ThemeResource("../gba-web/buttons/img/home.png"));
    homeButton.addListener((ClickListener) event -> onHomeButton());

    backButton.setWidth("40px");
    backButton.setDescription("Terug naar het vorige onderdeel");
    backButton.setIcon(new ThemeResource("../gba-web/buttons/img/back.png"));
    backButton.addListener((ClickListener) event -> onPreviousButton());

    adminButton.setWidth("40px");
    adminButton.setDescription("Beheer");
    adminButton.setIcon(new ThemeResource("../gba-web/buttons/img/admin.png"));
    adminButton.addListener((ClickListener) event -> onAdminButton());

    userButton.setWidth("40px");
    userButton.setDescription("Mijn instellingen");
    userButton.setIcon(new ThemeResource("../gba-web/buttons/img/user.png"));
    userButton.addListener((ClickListener) event -> onMyAccountButton());

    if (useBackButton) {

      addComponent(homeButton);
      addComponent(backButton);
    } else {

      addComponent(homeButton);
    }

    if (GbaApplication.getInstance().getServices().getGebruiker().isAdministrator()) {
      addComponent(adminButton);
    }

    addComponent(userButton);
  }

  public void onAdminButton() {
  }

  public void onHomeButton() {
  }

  public void onMyAccountButton() {
  }

  public void onPreviousButton() {
  }
}
