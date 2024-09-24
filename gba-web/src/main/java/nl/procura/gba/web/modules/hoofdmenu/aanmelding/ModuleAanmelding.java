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

package nl.procura.gba.web.modules.hoofdmenu.aanmelding;

import nl.procura.gba.web.common.annotations.ModuleAnnotation;
import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.modules.hoofdmenu.aanmelding.page1.Page1Aanmelding;
import nl.procura.gba.web.services.beheer.fileimport.FileImportService;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

@ModuleAnnotation(url = "#aanmeldingen",
    caption = "E-aanmeldingen",
    service = FileImportService.class,
    profielActie = ProfielActie.SELECT_HOOFD_AANMELDING)
public class ModuleAanmelding extends ModuleTemplate {

  public ModuleAanmelding() {
  }

  @Override
  public void event(PageEvent event) {
    super.event(event);
    if (event.isEvent(InitPage.class)) {
      getPages().getNavigation().goToPage(Page1Aanmelding.class);
    }
  }
}
