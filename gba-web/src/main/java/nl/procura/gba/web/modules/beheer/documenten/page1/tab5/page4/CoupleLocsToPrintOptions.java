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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab5.page4;

import java.util.Arrays;
import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.locaties.KoppelLocatiesPage;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;

public class CoupleLocsToPrintOptions extends NormalPageTemplate {

  private KoppelLocatiesPage<PrintOptie> coupleLocationsPage = null;

  public CoupleLocsToPrintOptions(List<PrintOptie> printOptionList) {

    super("");
    disablePreviousButton(); // nodig om de F1 sneltoets voor deze pagina uit te schakelen!
    addCoupleLocationsPage(printOptionList, true);
  }

  public CoupleLocsToPrintOptions(PrintOptie printOptie) {

    super("Overzicht van gekoppelde locaties van printoptie " + printOptie.getOms());
    setMargin(true);
    List<PrintOptie> printOptionsList = Arrays.asList(printOptie);
    disablePreviousButton(); // nodig om de F1 sneltoets voor deze pagina uit te schakelen, zie ButtonPageTemplate! We
    // we hebben alleen de sneltoets van CoupleLocationsPage nodig
    addCoupleLocationsPage(printOptionsList, false);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  private void addCoupleLocationsPage(List<PrintOptie> printOptionList, boolean coupleMultiplePrintOptions) {

    if (coupleMultiplePrintOptions) {

      coupleLocationsPage = new KoppelLocatiesPage<>(printOptionList, "printopties");
      coupleLocationsPage.disablePreviousButton();
    } else {
      coupleLocationsPage = new KoppelLocatiesPage<PrintOptie>(printOptionList, "printopties") {

        @Override
        public void onPreviousPage() {
          CoupleLocsToPrintOptions.this.onPreviousPage();
        }
      };
    }

    addExpandComponent(coupleLocationsPage);
  }

  private void disablePreviousButton() {
    buttonPrev.setVisible(false);
  }
}
