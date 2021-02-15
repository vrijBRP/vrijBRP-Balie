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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab4;

import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.gba.web.services.zaken.documenten.doelen.DocumentDoel;

public class Tab4DocumentenPage2 extends DocumentenTabPage {

  private DocumentDoel                  documentDoel = null;
  private final Tab4DocumentenPage2Form form;

  public Tab4DocumentenPage2(DocumentDoel documentDoel) {

    super("Toevoegen / muteren documentdoel");
    setDocumentDoel(documentDoel);

    addButton(buttonPrev, buttonNew, buttonSave);

    setMargin(true);

    form = new Tab4DocumentenPage2Form(documentDoel);
    addComponent(form);
  }

  public DocumentDoel getDocumentDoel() {
    return documentDoel;
  }

  public void setDocumentDoel(DocumentDoel documentDoel) {
    this.documentDoel = documentDoel;
  }

  @Override
  public void onNew() {
    form.reset();
    setDocumentDoel(new DocumentDoel());
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goToPage(new Tab4DocumentenPage1());
    getNavigation().removeOtherPages();
  }

  @Override
  public void onSave() {
    form.commit();

    Tab4DocumentenPage2Bean b = form.getBean();

    getDocumentDoel().setDocumentDoel(b.getDoel());
    getServices().getDocumentService().save(getDocumentDoel());
    successMessage("Documentdoel is opgeslagen.");
  }
}
