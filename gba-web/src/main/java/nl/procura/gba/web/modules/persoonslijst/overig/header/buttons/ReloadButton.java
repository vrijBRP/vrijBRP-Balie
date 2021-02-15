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

package nl.procura.gba.web.modules.persoonslijst.overig.header.buttons;

import com.vaadin.terminal.ThemeResource;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.theme.GbaWebTheme;

public class ReloadButton extends HeaderFormButton {

  public ReloadButton(Services services) {
    super(services);

    setStyleName(GbaWebTheme.BUTTON_LINK);
    addStyleName("pl-url-button");
    setIcon(new ThemeResource("../gba-web/buttons/img/refresh.png"));
    setDescription("Herlaad deze persoonslijst");

    addListener((ClickListener) event -> {
      GbaApplication app = (GbaApplication) getApplication();
      try {
        app.reloadCurrentPersonList();
      } catch (Exception e) {
        e.printStackTrace();
        app.handleException(getWindow(), e);
      }
    });
  }
}
