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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page62;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterPage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page63.Page63Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.results.PresentatievraagResultsLayout;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagMatch;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;

/**
 * Tonen presentievraag
 */
public class Page62Zaken extends ZakenregisterPage<VerhuisAanvraag> {

  public Page62Zaken(final VerhuisAanvraag zaak, final Presentievraag presentievraag) {

    super(zaak, "Zakenregister - presentievraag");
    addButton(buttonPrev);

    PresentatievraagResultsLayout presentatievraagResultsLayout = new PresentatievraagResultsLayout(
        presentievraag) {

      @Override
      protected void navigateToResult(Presentievraag presentievraag, PresentievraagMatch match) {
        getNavigation().addPage(new Page63Zaken(zaak, presentievraag, match));
      }
    };

    addExpandComponent(presentatievraagResultsLayout);
  }
}
