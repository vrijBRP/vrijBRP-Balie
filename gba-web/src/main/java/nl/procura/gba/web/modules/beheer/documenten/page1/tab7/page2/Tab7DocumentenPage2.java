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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab7.page2;

import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab7.page1.Tab7DocumentenPage1;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerk;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerkType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Tab7DocumentenPage2 extends DocumentenTabPage {

  private DocumentKenmerk         documentKenmerk = null;
  private Tab7DocumentenPage2Form form            = null;

  public Tab7DocumentenPage2(DocumentKenmerk documentKenmerk) {

    super("Toevoegen / muteren documentkenmerk");

    setDocumentKenmerk(documentKenmerk);

    addButton(buttonPrev, buttonNew, buttonSave);

    setMargin(true);

  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form = new Tab7DocumentenPage2Form(documentKenmerk) {

        @Override
        protected void setOnChangeType(DocumentKenmerkType type) {
          documentKenmerk.setKenmerkType(type);
        }
      };

      addComponent(form);
    }

    super.event(event);
  }

  @Override
  public void onNew() {

    form.reset();

    setDocumentKenmerk(new DocumentKenmerk());
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goToPage(new Tab7DocumentenPage1());
    getNavigation().removeOtherPages();
  }

  @Override
  public void onSave() {

    form.commit();

    Tab7DocumentenPage2Bean b = form.getBean();

    documentKenmerk.setDocumentKenmerk(b.getKenmerk());
    documentKenmerk.setKenmerkType(b.getType());

    getServices().getDocumentService().save(documentKenmerk);

    successMessage("Documentkenmerk is opgeslagen.");
  }

  public void setDocumentKenmerk(DocumentKenmerk documentKenmerk) {
    this.documentKenmerk = documentKenmerk;
  }
}
