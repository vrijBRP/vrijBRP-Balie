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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab5.page3;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.documenten.KoppelDocumentPage;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;

public class Tab5DocumentenPage3 extends NormalPageTemplate {

  private final PrintOptie printOptie;

  public Tab5DocumentenPage3(PrintOptie printOptie) {

    super("Overzicht van gekoppelde documenten van printoptie " + printOptie.getOms());
    this.printOptie = printOptie;
    setMargin(true);

    addCoupleDocumentPage();
  }

  private void addCoupleDocumentPage() {

    KoppelDocumentPage<PrintOptie> couple = new KoppelDocumentPage<PrintOptie>(printOptie, "printoptie") {

      @Override
      public void onPreviousPage() {
        Tab5DocumentenPage3.this.getNavigation().goBackToPreviousPage();
      }
    };

    addExpandComponent(couple);
  }
}
