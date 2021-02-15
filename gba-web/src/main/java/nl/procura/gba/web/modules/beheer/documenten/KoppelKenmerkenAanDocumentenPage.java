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
import nl.procura.gba.web.modules.beheer.documenten.kenmerk.KoppelKenmerkPage;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;

public class KoppelKenmerkenAanDocumentenPage extends NormalPageTemplate {

  private KoppelKenmerkPage<DocumentRecord> kenmerkenPage = null;

  public KoppelKenmerkenAanDocumentenPage(DocumentRecord document) {
    super("Overzicht van gekoppelde kenmerken van document " + document.getDocument());
    disablePreviousButton();
    addKoppelKenmerkenPage(Arrays.asList(document), false);
  }

  public KoppelKenmerkenAanDocumentenPage(List<DocumentRecord> documents) {
    super("");
    disablePreviousButton(); // nodig om de F1 sneltoets voor deze pagina uit te schakelen!
    addKoppelKenmerkenPage(documents, true);
    setMargin(false);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  private void addKoppelKenmerkenPage(List<DocumentRecord> docList, boolean koppelMeerdereDocumenten) {

    if (koppelMeerdereDocumenten) {

      kenmerkenPage = new KoppelKenmerkPage<>(docList, "documenten");
      kenmerkenPage.disablePreviousButton();
    } else {

      kenmerkenPage = new KoppelKenmerkPage<DocumentRecord>(docList, "documenten") {

        @Override
        public void onPreviousPage() {
          KoppelKenmerkenAanDocumentenPage.this.onPreviousPage();
        }
      };
    }

    addExpandComponent(kenmerkenPage);
  }

  private void disablePreviousButton() {
    buttonPrev.setVisible(false);
  }
}
