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

package nl.procura.gba.web.modules.zaken.inhouding.page3;

import nl.procura.gba.web.modules.zaken.common.ZakenPrintPage;
import nl.procura.gba.web.modules.zaken.reisdocument.page14.Page14Reisdocument;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentVermissing;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Afdrukken vermissing van een document
 */

public class Page3Inhouding extends ZakenPrintPage {

  public Page3Inhouding(DocumentInhouding aanvraag) {
    super("Document vermissing: nieuw", aanvraag, aanvraag, (DocumentType) null);
  }

  public Page3Inhouding(ReisdocumentVermissing aanvraag, Zaak zaak) {
    super("Document vermissing: nieuw", aanvraag, zaak, DocumentType.DOCUMENT_VERMISSING);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      if (getNavigation().getPage(Page14Reisdocument.class) == null) {
        removeButton(buttonNext);
      }
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {
    getApplication().getServices().getDocumentInhoudingenService().save((DocumentInhouding) getZaak());
    getNavigation().goBackToPage(Page14Reisdocument.class);
    super.onNextPage();
  }

  @Override
  public void setButtons() {
    addButton(buttonPrev);
    addButton(buttonNext);
    addButton(getPrintButtons());
    super.setButtons();
  }
}
