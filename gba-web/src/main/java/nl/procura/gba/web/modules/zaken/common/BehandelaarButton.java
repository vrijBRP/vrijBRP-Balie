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

package nl.procura.gba.web.modules.zaken.common;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.ZakenregisterUtils.getZakenregisterAccordionTab;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.behandelaar.BehandelaarWindow;
import nl.procura.gba.web.modules.persoonslijst.overig.header.buttons.HeaderFormButton;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.gba.web.windows.home.navigatie.ZakenregisterAccordionTab;
import nl.procura.vaadin.theme.ProcuraWindow;

public class BehandelaarButton extends HeaderFormButton {

  public BehandelaarButton(Zaak zaak, Services services, Runnable update) {
    super(services);

    setCaption("Meer");
    setStyleName(GbaWebTheme.BUTTON_SMALL);
    addStyleName("zaak-behandelaar-button");
    setDescription("Selecteer de behandelaar");

    addListener((ClickListener) event -> {
      GbaApplication app = (GbaApplication) getApplication();
      try {
        ProcuraWindow parentWindow = app.getParentWindow();
        parentWindow.addWindow(new BehandelaarWindow(zaak, () -> {
          app.getServices().getZakenService().getService(zaak).setZaakHistory(zaak);
          getZakenregisterAccordionTab(parentWindow).ifPresent(ZakenregisterAccordionTab::recountTree);
          update.run();
        }));
      } catch (Exception e) {
        app.handleException(getWindow(), e);
      }
    });
  }
}
