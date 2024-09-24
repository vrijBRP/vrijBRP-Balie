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

package nl.procura.gba.web.modules.zaken.common;

import nl.procura.gba.web.components.layouts.page.GbaPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.zaken.ZakenModuleTemplate;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class ZaakWindow extends GbaModalWindow {

  private final Zaak zaak;

  public ZaakWindow(Zaak zaak) {
    super("Zaak (Escape om te sluiten)", "1400px");
    this.zaak = zaak;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new ZaakModule()));
  }

  public class ZaakModule extends ZakenModuleTemplate {

    @Override
    public void event(PageEvent event) {
      super.event(event);
      if (event.isEvent(InitPage.class)) {
        GbaPageTemplate selectPage = ZaakPageIndex.selectPage(zaak);
        getPages().getNavigation().goToPage(selectPage);
      }
    }
  }
}
