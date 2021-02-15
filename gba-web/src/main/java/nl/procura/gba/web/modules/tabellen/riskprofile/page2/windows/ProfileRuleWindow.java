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

package nl.procura.gba.web.modules.tabellen.riskprofile.page2.windows;

import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.components.listeners.ChangeListener;
import nl.procura.gba.web.modules.tabellen.riskprofile.page2.windows.page1.Page1ProfileRule;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class ProfileRuleWindow extends GbaModalWindow {

  public ProfileRuleWindow(RiskProfileRule riskProfileRule, ChangeListener changeListener) {
    setWidth("900px");

    if (riskProfileRule.isStored()) {
      setCaption("Profielregel aanpassen");
    } else {
      setCaption("Nieuwe profielregel");
    }

    addListener((CloseListener) e -> changeListener.onChange());
    addComponent(new ModuleTemplate() {

      @Override
      public void event(PageEvent event) {
        super.event(event);
        if (event.isEvent(InitPage.class)) {
          getPages().getNavigation().goToPage(new Page1ProfileRule(riskProfileRule));
        }
      }
    });
  }
}
