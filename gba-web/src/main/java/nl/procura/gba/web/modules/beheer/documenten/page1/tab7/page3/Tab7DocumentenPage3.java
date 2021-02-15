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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab7.page3;

import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.tabsheet.BeheerTabsheet;
import nl.procura.gba.web.modules.beheer.KoppelbaarAanDocumentType;
import nl.procura.gba.web.modules.beheer.documenten.KoppelKenmerkenAanDocumentenPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page4.SelectedDocsPage;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.layout.tabsheet.LazyTabChangeListener;

public class Tab7DocumentenPage3 extends NormalPageTemplate {

  private final BeheerTabsheet tabs = new BeheerTabsheet();

  private final List<DocumentRecord> docList;

  public Tab7DocumentenPage3(List<DocumentRecord> docList) {

    super("Kenmerken koppelen aan documenten");

    this.docList = docList;

    addButton(buttonPrev);
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      final SelectedDocsPage docTab = new SelectedDocsPage(docList, KoppelbaarAanDocumentType.KENMERK);
      final KoppelKenmerkenAanDocumentenPage printoptieTab = new KoppelKenmerkenAanDocumentenPage(docList);

      tabs.addTab("Geselecteerde documenten", (LazyTabChangeListener) () -> docTab);

      tabs.addTab("Kenmerken", (LazyTabChangeListener) () -> printoptieTab);

      addComponent(tabs);
    }
    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }
}
