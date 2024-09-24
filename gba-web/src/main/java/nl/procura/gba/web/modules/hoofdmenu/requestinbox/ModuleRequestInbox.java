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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox;

import static nl.procura.burgerzaken.requestinbox.api.model.InboxItemStatus.RECEIVED;

import nl.procura.gba.web.common.annotations.ModuleAnnotation;
import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.page1.Page1RequestInbox;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.page2.Page2RequestInbox;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.validation.Bsn;

@ModuleAnnotation(url = "#verzoeken",
    caption = "Verzoeken",
    profielActie = ProfielActie.SELECT_HOOFD_VERZOEK)
public class ModuleRequestInbox extends ModuleTemplate {

  private final ModuleRequestInboxParams params;

  public ModuleRequestInbox() {
    this(ModuleRequestInboxParams.builder()
        .title("Verzoeken")
        .status(RECEIVED)
        .bsn(new Bsn())
        .updateListener(() -> {})
        .build());
  }

  public ModuleRequestInbox(ModuleRequestInboxParams params) {
    this.params = params;
  }

  @Override
  public void event(PageEvent event) {
    super.event(event);
    if (event.isEvent(InitPage.class)) {
      if (params.getItem() != null) {
        getPages().getNavigation().goToPage(new Page2RequestInbox(params.getTitle(), params.getItem()));
      } else {
        getPages().getNavigation().goToPage(new Page1RequestInbox(params));
      }
    }
  }
}
