/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.inhouding.page2;

import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.inhouding.overzicht.DocumentInhoudingOverzichtBuilder;
import nl.procura.gba.web.modules.zaken.inhouding.overzicht.DocumentInhoudingOverzichtLayout;
import nl.procura.gba.web.modules.zaken.inhouding.page3.Page3Inhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.gba.web.services.zaken.reisdocumenten.Reisdocument;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentVermissing;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Inhouding extends ZakenPage {

  private final DocumentInhouding                inhouding;
  private final Reisdocument                     reisdocument;
  private       DocumentInhoudingOverzichtLayout reisdocumentInhoudingLayout = null;

  public Page2Inhouding(DocumentInhouding inhouding, Reisdocument reisdocument) {
    super("Inhouding / vermissing");

    this.inhouding = inhouding;
    this.reisdocument = reisdocument;

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      reisdocumentInhoudingLayout = DocumentInhoudingOverzichtBuilder.create(inhouding, reisdocument,
          false,
          false);
      addComponent(reisdocumentInhoudingLayout);
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {
    reisdocumentInhoudingLayout.commit();
    DocumentInhoudingenService inhoudingen = getServices().getDocumentInhoudingenService();
    ReisdocumentVermissing vermissing = inhoudingen.getVermissing(inhouding);
    getNavigation().goToPage(new Page3Inhouding(vermissing, inhouding));
  }

  @Override
  public void onPreviousPage() {
    getWindow().addWindow(new ConfirmDialog("De vermissing is nog niet opgeslagen.<br/>Wilt u de pagina verlaten?") {

      @Override
      public void buttonYes() {
        super.buttonYes();
        Page2Inhouding.super.onPreviousPage();
      }
    });
  }
}
