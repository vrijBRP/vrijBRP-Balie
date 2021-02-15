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

package nl.procura.gba.web.modules.beheer.documenten;

import java.util.Arrays;
import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.documenten.stempel.KoppelStempelPage;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;

public class KoppelStempelsAanDocumentenPage extends NormalPageTemplate {

  private KoppelStempelPage<DocumentRecord> stempelsPage = null;

  public KoppelStempelsAanDocumentenPage(DocumentRecord document) {
    super("Overzicht van gekoppelde stempels van document " + document.getDocument());
    disablePreviousButton();
    addKoppelStempelsPage(Arrays.asList(document), false);
  }

  public KoppelStempelsAanDocumentenPage(List<DocumentRecord> documents) {
    super("");
    disablePreviousButton(); // nodig om de F1 sneltoets voor deze pagina uit te schakelen!
    addKoppelStempelsPage(documents, true);
    setMargin(false);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  private void addKoppelStempelsPage(List<DocumentRecord> docList, boolean koppelMeerdereDocumenten) {

    if (koppelMeerdereDocumenten) {

      stempelsPage = new KoppelStempelPage<>(docList, "documenten");
      stempelsPage.disablePreviousButton();
    } else {

      stempelsPage = new KoppelStempelPage<DocumentRecord>(docList, "documenten") {

        @Override
        public void onPreviousPage() {
          KoppelStempelsAanDocumentenPage.this.onPreviousPage();
        }
      };
    }

    addExpandComponent(stempelsPage);
  }

  private void disablePreviousButton() {
    buttonPrev.setVisible(false);
  }
}
