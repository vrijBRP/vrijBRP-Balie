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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.relatie.page1;

import nl.procura.gba.web.components.dialogs.OntkoppelProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.relatie.page2.Page2ZaakRelatie;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatie;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatieService;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public abstract class Page1ZaakRelatie extends NormalPageTemplate {

  private final Zaak zaak;
  private Table      table;

  public Page1ZaakRelatie(Zaak zaak) {
    this.zaak = zaak;

    setSpacing(true);
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      buttonNew.setCaption("Koppel (F7)");
      buttonDel.setCaption("Ontkoppel (F8)");

      addButton(buttonNew);
      addButton(buttonDel);

      setInfo("Gekoppelde zaken", "Zaken die gekoppeld zijn aan deze zaak");

      table = new Table(zaak);

      addComponent(table);
    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onDelete() {

    new OntkoppelProcedure<ZaakRelatie>(table, false) {

      @Override
      public void deleteValue(ZaakRelatie zaakRelatie) {

        ZaakRelatieService db = getServices().getZaakRelatieService();
        db.delete(zaakRelatie);
      }

      @Override
      protected void afterDelete() {
        onReload();
      }
    };

    super.onDelete();
  }

  @Override
  public void onNew() {

    ZaakRelatie relatie = new ZaakRelatie();
    relatie.setZaakId(zaak.getZaakId());
    relatie.setZaakType(zaak.getType());

    getNavigation().goToPage(new Page2ZaakRelatie(zaak, relatie) {

      @Override
      public void onReload() {
        Page1ZaakRelatie.this.onReload();
      }
    });

    super.onNew();
  }

  public abstract void onReload();

  public class Table extends Page1ZaakRelatieTable {

    Table(Zaak zaak) {
      super(zaak);
    }
  }
}
