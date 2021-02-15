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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig;

import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page2.Page2Zaken;
import nl.procura.gba.web.services.zaken.zakenregister.ZaakItem;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class SubModuleZaken extends ModuleTemplate {

  private Object itemId = null;

  public SubModuleZaken() {
  }

  @Override
  public void event(PageEvent event) {

    super.event(event);

    if (event.isEvent(InitPage.class)) {

      if (getItemId() instanceof ZaakItem) {

        ZaakItem zaakItem = (ZaakItem) getItemId();

        getPages().getNavigation().goToPage(new Page2Zaken(zaakItem));
      }
    }
  }

  public Object getItemId() {
    return itemId;
  }

  public void setItemId(Object itemId) {
    this.itemId = itemId;
  }

  public void init(Object itemId) {
    setItemId(itemId);
  }
}
