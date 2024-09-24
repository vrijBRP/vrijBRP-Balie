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

package nl.procura.gba.web.modules.hoofdmenu.gv;

import nl.procura.gba.common.ZaakFragment;
import nl.procura.gba.web.common.annotations.ModuleAnnotation;
import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.modules.hoofdmenu.gv.page1.Page1Gv;
import nl.procura.gba.web.modules.hoofdmenu.gv.page2.Page2Gv;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.zaken.gv.GvAanvraag;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

@ModuleAnnotation(url = "#" + ZaakFragment.FR_GV,
    caption = "Gegevensverstrekking",
    profielActie = ProfielActie.SELECT_ZAAK_GV)
public class ModuleGv extends ModuleTemplate {

  public ModuleGv() {
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      GvAanvraag aanvraag = getApplication().getServices().getMemoryService().getAndRemoveByClass(
          GvAanvraag.class);

      if (aanvraag != null) {
        if (aanvraag.isStored()) {
          // Aanvraag al aangemaakt
          getPages().getNavigation().goToPage(new Page2Gv(aanvraag));
        } else {
          // Zaak bestaat, maar is nog niet opgeslagen.
          getPages().getNavigation().goToPage(new Page1Gv(aanvraag));
        }
      } else {
        // Zaak bestaat nog niet
        aanvraag = (GvAanvraag) getApplication().getServices().getGegevensverstrekkingService().getNewZaak();
        getPages().getNavigation().goToPage(new Page1Gv(aanvraag));
      }
    }

    super.event(event);
  }
}
