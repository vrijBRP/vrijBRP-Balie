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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.windows;

import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.hoofdmenu.raas.tab2.page1.Tab2RaasPage1;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class RaasCommWindow extends GbaModalWindow {

  public RaasCommWindow(DocAanvraagDto aanvraag) {
    setWidth("80%");
    setCaption("Berichten van en naar het RAAS");
    addComponent(new ModuleTemplate() {

      @Override
      public void event(PageEvent event) {
        super.event(event);
        if (event.isEvent(InitPage.class)) {
          getPages().getNavigation().goToPage(new Tab2RaasPage1(aanvraag));
        }
      }
    });
  }
}
