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

package nl.procura.gba.web.common.window;

import static nl.procura.standard.Globalfunctions.isTru;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.common.misc.GbaApplicationCookie;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.gba.web.windows.home.HomeWindow;
import nl.procura.gba.web.windows.locatie.LocatieWindow;

public class WindowHandler {

  private GbaApplication application = null;

  public WindowHandler(GbaApplication application) {
    setApplication(application);
  }

  public GbaApplication getApplication() {
    return application;
  }

  public void setApplication(GbaApplication application) {
    this.application = application;
  }

  public GbaWindow getNextWindow() {

    Gebruiker gebruiker = getApplication().getServices().getGebruiker();

    if (!gebruiker.heeftLocatie()) {

      // Zoek locatie keuze in cookie
      GbaApplicationCookie cookie = new GbaApplicationCookie(getApplication());
      Locatie locatie = getApplication().getServices().getLocatieService().getGebruikerLocatie(
          cookie.getLocation());
      boolean locatieLaden = isTru(getApplication().getParmValue(ParameterConstant.LOCATIE_OPSLAG));

      if (locatieLaden && locatie != null && locatie.getCLocation() > 0) {
        getApplication().getServices().getLocatieService().setLocatie(locatie);
        new GbaApplicationCookie(getApplication()).setDefaultLocation(locatie);
        return new HomeWindow();
      }

      return new LocatieWindow();
    }

    return new HomeWindow();
  }
}
