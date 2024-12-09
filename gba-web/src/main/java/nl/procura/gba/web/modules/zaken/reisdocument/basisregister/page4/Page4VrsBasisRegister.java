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

package nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page4;

import static nl.procura.gba.web.services.zaken.documenten.DocumentType.DOCUMENT_VERMISSING;

import com.vaadin.ui.Button;
import nl.procura.gba.web.components.layouts.form.document.PrintMultiLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentVermissing;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page4VrsBasisRegister extends NormalPageTemplate {

  private final DocumentInhouding zaak;
  private       PrintMultiLayout  printLayout;

  public Page4VrsBasisRegister(DocumentInhouding zaak) {
    this.zaak = zaak;
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      DocumentInhoudingenService inhoudingen = getServices().getDocumentInhoudingenService();
      ReisdocumentVermissing vermissing = inhoudingen.getVermissing(zaak);
      printLayout = new PrintMultiLayout(vermissing, zaak, null, DOCUMENT_VERMISSING);
      addButton(printLayout.getButtons());
      addButton(buttonNext);
      addExpandComponent(printLayout);
      buttonNext.setCaption("Voltooien (F2)");
      super.event(event);
    }
    getWindow().setWidth("90%");
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    printLayout.handleActions(button, keyCode);
    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {
    getWindow().closeWindow();
    super.onNextPage();
  }
}
