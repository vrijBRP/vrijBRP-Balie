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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.attribuut.page1;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.attribuut.page2.Page2ZaakAttribuut;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakAttribuut;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public abstract class Page1ZaakAttribuut extends NormalPageTemplate {

  private final Zaak              zaak;
  private Page1ZaakAttribuutTable table = null;

  public Page1ZaakAttribuut(Zaak zaak) {
    this.zaak = zaak;

    setSpacing(true);
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonNew);
      addButton(buttonDel);

      setInfo("Zaakattributen", "Extra gegevens die aan de zaak kunnen worden toegekend");

      table = new Page1ZaakAttribuutTable(zaak);

      addComponent(table);
    } else if (event.isEvent(AfterBackwardReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<ZaakAttribuut>(table) {

      @Override
      public void deleteValue(ZaakAttribuut attribuut) {
        getServices().getZaakAttribuutService().delete(attribuut);
      }

      @Override
      protected void afterDelete() {
        onReload();
      }
    };
  }

  @Override
  public void onNew() {

    ZaakAttribuut attribuut = new ZaakAttribuut();
    attribuut.setZaakId(zaak.getZaakId());

    getNavigation().goToPage(new Page2ZaakAttribuut(attribuut) {

      @Override
      public void onReload() {
        Page1ZaakAttribuut.this.onReload();
      }
    });

    super.onNew();
  }

  public abstract void onReload();
}
