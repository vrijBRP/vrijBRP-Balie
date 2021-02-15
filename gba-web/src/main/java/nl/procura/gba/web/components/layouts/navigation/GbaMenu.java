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

import nl.procura.gba.web.application.WindowChangeListener;
import nl.procura.gba.web.windows.account.AccountWindow;
import nl.procura.gba.web.windows.beheer.BeheerWindow;

public class GbaMenu extends MenuLayout {

  public GbaMenu(boolean useBackButton) {
    super(useBackButton);
  }

  @Override
  public void onAdminButton() {
    getApplication().openWindow(getWindow(), new BeheerWindow(), "");
  }

  @Override
  public void onHomeButton() {
    getApplication().onHome();
  }

  @Override
  public void onMyAccountButton() {
    getApplication().openWindow(getWindow(), new AccountWindow(), "");
  }

  @Override
  public void onPreviousButton() {

    getApplication().goBackToWindow(new WindowChangeListener(getWindow(), getApplication().getPreviousWindow()) {

      @Override
      public void windowChanged() {
        getApplication().removePreviousWindow();
      }
    });
  }
}
