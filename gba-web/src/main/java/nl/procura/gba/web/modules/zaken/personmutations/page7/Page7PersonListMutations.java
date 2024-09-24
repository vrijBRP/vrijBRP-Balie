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

package nl.procura.gba.web.modules.zaken.personmutations.page7;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.form.document.PrintMultiLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListPrintData;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page7PersonListMutations extends NormalPageTemplate {

  private final PersonListPrintData printData;
  private PrintMultiLayout          printLayout;

  public Page7PersonListMutations(PersonListPrintData printData) {
    super("Categorie persoonlijst - afdrukken");
    this.printData = printData;
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      PersonListCategoryTemplateData data = new PersonListCategoryTemplateData(printData);
      printLayout = new PrintMultiLayout(data, null, null, DocumentType.PL_CATEGORIE);
      printLayout.setInfo("");
      addButton(buttonPrev);
      addButton(printLayout.getButtons());
      addComponent(printLayout);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    printLayout.handleActions(button, keyCode);
    super.handleEvent(button, keyCode);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }
}
