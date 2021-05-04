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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab8.page2;

import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab8.page1.Tab8DocumentenPage1;
import nl.procura.gba.web.services.zaken.documenten.dmstypes.DmsDocumentType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Tab8DocumentenPage2 extends DocumentenTabPage {

  private DmsDocumentType         documentType = null;
  private Tab8DocumentenPage2Form form         = null;

  public Tab8DocumentenPage2(DmsDocumentType documentType) {
    super("Toevoegen / muteren DMS documenttype");
    setDocumentType(documentType);
    addButton(buttonPrev, buttonNew, buttonSave);
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      form = new Tab8DocumentenPage2Form(documentType);
      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onNew() {
    form.reset();
    setDocumentType(new DmsDocumentType());
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goToPage(new Tab8DocumentenPage1());
    getNavigation().removeOtherPages();
  }

  @Override
  public void onSave() {
    form.commit();
    Tab8DocumentenPage2Bean bean = form.getBean();
    documentType.setDocumentDmsType(bean.getOmschrijving());
    documentType.setZaakTypes(bean.getZaakTypesAsList());

    getServices().getDocumentService().save(documentType);

    successMessage("Gegevens zijn opgeslagen.");
  }

  public void setDocumentType(DmsDocumentType documentType) {
    this.documentType = documentType;
  }
}
