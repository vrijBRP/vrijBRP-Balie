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

package nl.procura.gba.web.modules.bs.registration;

import java.util.Optional;

import com.vaadin.ui.Button;
import com.vaadin.ui.Layout;

import nl.procura.gba.common.ZaakFragment;
import nl.procura.gba.web.common.annotations.ModuleAnnotation;
import nl.procura.gba.web.modules.beheer.fileimport.FileImportType;
import nl.procura.gba.web.modules.bs.common.modules.BsModule;
import nl.procura.gba.web.modules.bs.registration.fileimport.FileImportRegistrant;
import nl.procura.gba.web.modules.bs.registration.page10.Page10Registration;
import nl.procura.gba.web.modules.bs.registration.page20.Page20Registration;
import nl.procura.gba.web.modules.bs.registration.printing.PrintingWindow;
import nl.procura.gba.web.modules.bs.registration.processes.RegistrationProcesses;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

@ModuleAnnotation(url = "#" + ZaakFragment.FR_REGISTRATION,
    caption = "Eerste inschrijving",
    profielActie = ProfielActie.SELECT_ZAAK_REGISTRATIE)
public class ModuleRegistration extends BsModule {

  private FileImportRegistrant importRegistrant;
  private Dossier              dossier;

  public ModuleRegistration() {
  }

  public ModuleRegistration(Dossier dossier) {
    this.dossier = dossier;
  }

  @Override
  protected void setExtraButtons(Layout layout) {
    // Aanschrijving
    Button buttonPrint = new Button("Afdrukken");
    buttonPrint.setWidth("100%");
    buttonPrint.addListener((Button.ClickListener) event -> {

      final Page10Registration declaration = VaadinUtils.getChild(this, Page10Registration.class);
      final Page20Registration person = VaadinUtils.getChild(this, Page20Registration.class);

      if (declaration != null) {
        declaration.checkPage();
      }

      if (person != null) {
        person.checkPage();
      }
      getApplication().getParentWindow()
          .addWindow(new PrintingWindow((DossierRegistration) dossier.getZaakDossier()));
    });

    layout.addComponent(buttonPrint);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      final Dossier selectedDossier = getApplication().getServices()
          .getMemoryService()
          .getAndRemoveByClass(Dossier.class);

      if (selectedDossier != null) {
        dossier = selectedDossier;
      }

      if (dossier == null) {
        dossier = (Dossier) getApplication().getServices().getRegistrationService().getNewZaak();
      }

      setProcessen(new RegistrationProcesses(dossier, getApplication()));
    }

    super.event(event);
  }

  public FileImportRegistrant setImportRegistrant(FileImportRegistrant importRegistrant) {
    this.importRegistrant = importRegistrant;
    return importRegistrant;
  }

  public Optional<FileImportRegistrant> getImportRegistrant() {
    Optional<FileImportRegistrant> registrant = Optional.ofNullable(importRegistrant);
    if (registrant.isPresent()) {
      return registrant;
    } else {
      DossierRegistration zaakDossier = (DossierRegistration) dossier.getZaakDossier();
      return Optional.ofNullable(zaakDossier.getCFileRecord())
          .map(id -> getApplication().getServices().getFileImportService().getFileImportRecord(id))
          .flatMap(rec -> FileImportType.getById(rec.getTemplate())
              .map(type -> setImportRegistrant(FileImportRegistrant.of(type, rec))));
    }
  }
}
