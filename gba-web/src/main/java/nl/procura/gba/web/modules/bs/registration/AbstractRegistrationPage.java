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

package nl.procura.gba.web.modules.bs.registration;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Optional;

import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.registration.fileimport.FileImportRegistrant;
import nl.procura.gba.web.modules.bs.registration.fileimport.FileImportRegistrantLayout;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

public abstract class AbstractRegistrationPage extends BsPage<DossierRegistration> {

  protected final FileImportRegistrantLayout registrantLayout = new FileImportRegistrantLayout();

  public AbstractRegistrationPage(String title) {
    super(title);
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  public Optional<FileImportRegistrant> getImportRegistrant() {
    return ofNullable(getApplication().getMainWindow())
        .flatMap(window -> VaadinUtils.getChild(window, ModuleRegistration.class)
            .getImportRegistrant());
  }

  public void setImportRegistrant(FileImportRegistrant registrant) {
    ofNullable(getApplication().getMainWindow())
        .map(window -> VaadinUtils.getChild(window, ModuleRegistration.class))
        .ifPresent(module -> module.setImportRegistrant(registrant));
    registrantLayout.setRegistrant(registrant);
  }

  protected void loadImportRegistrant(FileImportRegistrant registrant) {
  }

  @Override
  public void event(PageEvent event) {
    super.event(event);
    if (event.isEvent(InitPage.class)) {
      getImportRegistrant().ifPresent(importRegistrant -> {
        if (isBlank(getZaakDossier().getDuration())) {
          loadImportRegistrant(importRegistrant);
        }
        registrantLayout.setRegistrant(importRegistrant);
      });
    }
  }
}
