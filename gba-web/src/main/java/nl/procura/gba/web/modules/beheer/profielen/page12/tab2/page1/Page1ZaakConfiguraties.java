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

package nl.procura.gba.web.modules.beheer.profielen.page12.tab2.page1;

import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.profielen.page12.ZaakConfiguratiesTab;
import nl.procura.gba.web.modules.beheer.profielen.page12.tab2.page2.Page2ZaakConfiguraties;
import nl.procura.gba.web.services.zaken.algemeen.zaakconfiguraties.ZaakConfiguratie;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page1ZaakConfiguraties extends NormalPageTemplate {

  private Table1 table1 = null;

  public Page1ZaakConfiguraties() {
    super("Overzicht van mogelijke configuraties");
    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonDel);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table1 = new Table1();
      addExpandComponent(table1);

      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table1));
    } else if (event.isEvent(AfterReturn.class)) {
      table1.init();
    }

    super.event(event);
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<ZaakConfiguratie>(table1) {

      @Override
      public void deleteValue(ZaakConfiguratie indicatie) {
        getServices().getZaakConfiguratieService().delete(indicatie);
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Page2ZaakConfiguraties(new ZaakConfiguratie()));
    super.onNew();
  }

  @Override
  public void onPreviousPage() {
    VaadinUtils.getParent(this, ZaakConfiguratiesTab.class).getNavigation().goBackToPreviousPage();
  }

  public class Table1 extends GbaTable {

    @Override
    public void onDoubleClick(Record record) {
      ZaakConfiguratie configuratie = (ZaakConfiguratie) record.getObject();
      getNavigation().goToPage(new Page2ZaakConfiguraties(configuratie));
      super.onDoubleClick(record);
    }

    @Override
    public void setColumns() {
      setSelectable(true);
      setMultiSelect(true);

      addColumn("Omschrijving");
      addColumn("Bron / leverancier", 350);
    }

    @Override
    public void setRecords() {
      for (ZaakConfiguratie a : getServices().getZaakConfiguratieService().getConfiguraties()) {
        if (pos(a.getCZaakConf())) {
          Record r = addRecord(a);
          r.addValue(a.getZaakConf());
          r.addValue(a.getBron() + " / " + a.getLeverancier());
        }
      }

      super.setRecords();
    }
  }
}
