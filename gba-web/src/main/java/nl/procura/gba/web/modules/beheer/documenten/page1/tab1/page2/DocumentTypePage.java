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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2;

import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;

public class DocumentTypePage extends NormalPageTemplate {

  private final List<DocumentRecord> docList;
  private final DocumentTypeForm     form;

  public DocumentTypePage(List<DocumentRecord> docList) {

    super("Documenten: indelen naar type");

    setMargin(true);

    addButton(buttonPrev, buttonSave);

    this.docList = docList;

    form = new DocumentTypeForm(docList);

    addComponent(form);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form.commit();
    for (DocumentRecord doc : docList) {
      doc.setType(form.getBean().getDocType().getType());
    }

    getServices().getDocumentService().save(docList);

    onPreviousPage();
  }
}
