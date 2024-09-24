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

package nl.procura.gba.web.modules.zaken.reisdocument.bezorging;

import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.reisdocument.bezorging.page1.BezorgingPage1;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class BezorgingWindow extends GbaModalWindow {

  public BezorgingWindow(ReisdocumentAanvraag zaak) {
    setWidth("800px");
    setCaption("Bezorging van het reisdocument");
    addComponent(new ModuleTemplate() {

      @Override
      public void event(PageEvent event) {
        super.event(event);
        if (event.isEvent(InitPage.class)) {
          // Check if there is a melding for the given aanvraag
          getApplication().getServices()
              .getReisdocumentBezorgingService()
              .findByAanvrNr(zaak.getAanvrNr())
              .ifPresent(melding -> zaak.getThuisbezorging().setMelding(melding));

          getPages().getNavigation().goToPage(new BezorgingPage1(zaak.getThuisbezorging()));
        }
      }
    });
  }
}
