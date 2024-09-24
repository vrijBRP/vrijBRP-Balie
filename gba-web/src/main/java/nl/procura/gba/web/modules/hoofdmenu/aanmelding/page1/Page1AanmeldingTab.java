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

import static nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTableListener.newRegistration;

import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.fileimport.FileImportType;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTable;
import nl.procura.gba.web.services.beheer.fileimport.FileImportRecord;
import nl.procura.gba.web.services.beheer.fileimport.FileImportService;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.windows.home.HomeWindow;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1AanmeldingTab extends NormalPageTemplate {

  private final FileImport fileImport;

  public Page1AanmeldingTab(FileImport fileImport) {
    this.fileImport = fileImport;
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      FileImportService service = getApplication().getServices().getFileImportService();
      FileImportType.getById(fileImport.getTemplate()).ifPresent(type -> {
        FileImportTable table = type.getImporter().createTable(newRegistration(this::createNewRegistration));
        addComponent(type.getImporter().createFilter(fileImport, table));
        addExpandComponent(table);
        table.update(service.getFileImportRecords(fileImport));
      });
    }

    super.event(event);
  }

  private void createNewRegistration(FileImportRecord importRecord) {
    Dossier dossier = getApplication().getServices().getRegistrationService().newDossier();
    DossierRegistration zaakDossier = (DossierRegistration) dossier.getZaakDossier();
    zaakDossier.setCFileRecord(importRecord.getId());
    getApplication().getServices().getMemoryService().setObject(Dossier.class, dossier);
    getApplication().openWindow(getParentWindow(), new HomeWindow(), "bs.registratie");
    getApplication().closeAllModalWindows(getApplication().getWindows());
  }
}
