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

package nl.procura.gba.web.modules.hoofdmenu.aanmelding.page1;

import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.services.beheer.fileimport.FileImportService;
import nl.procura.gba.web.services.beheer.fileimport.FileImportService.Count;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1Aanmelding extends NormalPageTemplate {

  public Page1Aanmelding() {
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {

      setSizeFull();

      GbaTabsheet tabs = new GbaTabsheet();
      tabs.setSizeFull();
      tabs.setNoBorderTop();

      FileImportService service = getServices().getFileImportService();
      for (FileImport fileImport : service.getFileImports()) {
        if (!fileImport.isClosed()) {
          Count count = service.countRecords(fileImport);
          tabs.addTab(fileImport.getName() + count, () -> new Page1AanmeldingTab(fileImport));
        }
      }
      addComponent(tabs);
    }

    super.event(event);
  }
}
