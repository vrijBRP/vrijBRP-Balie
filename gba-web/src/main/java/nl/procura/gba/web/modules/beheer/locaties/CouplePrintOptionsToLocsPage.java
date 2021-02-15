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

package nl.procura.gba.web.modules.beheer.locaties;

import java.util.Arrays;
import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.documenten.printoptie.KoppelPrintoptiePage;
import nl.procura.gba.web.services.beheer.locatie.Locatie;

public class CouplePrintOptionsToLocsPage extends NormalPageTemplate {

  private KoppelPrintoptiePage<Locatie> printoptionsPage = null;

  public CouplePrintOptionsToLocsPage(List<Locatie> locations) {

    super("");

    disablePreviousButton(); // nodig om de F1 sneltoets voor deze pagina uit te schakelen!
    addCouplePrintOptionsPage(locations, true);
  }

  public CouplePrintOptionsToLocsPage(Locatie locatie) {

    super("Overzicht van gekoppelde printers van locatie " + locatie.getLocatie());

    List<Locatie> locList = Arrays.asList(locatie);

    disablePreviousButton(); // nodig om de F1 sneltoets voor deze pagina uit te schakelen, zie ButtonPageTemplate! We

    // we hebben alleen de sneltoets van CoupleLocationsPage nodig
    addCouplePrintOptionsPage(locList, false);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  private void addCouplePrintOptionsPage(List<Locatie> locList, boolean coupleMultipleLocations) {

    if (coupleMultipleLocations) {

      printoptionsPage = new KoppelPrintoptiePage<>(locList, "gebruikers");
      printoptionsPage.disablePreviousButton();
    } else {

      printoptionsPage = new KoppelPrintoptiePage<Locatie>(locList, "gebruikers") {

        @Override
        public void onPreviousPage() {

          CouplePrintOptionsToLocsPage.this.onPreviousPage();
        }
      };
    }

    addExpandComponent(printoptionsPage);
  }

  private void disablePreviousButton() {
    buttonPrev.setVisible(false);
  }
}
