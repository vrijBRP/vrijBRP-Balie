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

package nl.procura.gba.web.modules.bs.registration.presenceq;

import java.util.function.Consumer;

import java.util.function.Supplier;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.search.PresentievraagZoekBean;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class PersonPresenceWindow extends GbaModalWindow {

  public PersonPresenceWindow(DossierRegistration dossier, DossierPersoon person,
      Consumer<DossierPersoon> nextListener) {
    this(dossier, person, null, nextListener);
  }

  public PersonPresenceWindow(DossierRegistration dossier, DossierPersoon person,
      Supplier<PresentievraagZoekBean> searchBean, Consumer<DossierPersoon> nextListener) {
    super("Presentievraag", "1000px");
    addComponent(
        new MainModuleContainer(false, new PersonPresenceSearchPage(dossier, person, searchBean, nextListener)));
  }
}
