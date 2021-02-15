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

package nl.procura.gba.web.modules.tabellen.overlijdenaangever.page1;

import com.vaadin.ui.Alignment;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.fields.GeldigheidField;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.tabellen.overlijdenaangever.page2.Page2OverlijdenAangever;
import nl.procura.gba.web.services.gba.basistabellen.overlijdenaangever.OverlijdenAangever;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1OverlijdenAangever extends NormalPageTemplate {

  private Table1          table1 = null;
  private GeldigheidField geldigheidField;

  public Page1OverlijdenAangever() {

    super("Overzicht van aangevers van overlijden");

    addButton(buttonNew);
    addButton(buttonDel);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      geldigheidField = new GeldigheidField() {

        @Override
        public void onChangeValue(GeldigheidStatus value) {
          table1.init();
        }
      };

      table1 = new Table1();
      addExpandComponent(table1);

      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table1));
      getButtonLayout().addComponent(geldigheidField);
      getButtonLayout().align(geldigheidField, Alignment.MIDDLE_RIGHT);
    } else if (event.isEvent(AfterReturn.class)) {

      table1.init();
    }

    super.event(event);
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<OverlijdenAangever>(table1) {

      @Override
      public void deleteValue(OverlijdenAangever ambtenaar) {

        getServices().getOverlijdenGemeenteService().delete(ambtenaar);
      }
    };
  }

  @Override
  public void onNew() {

    getNavigation().goToPage(new Page2OverlijdenAangever(new OverlijdenAangever()));

    super.onNew();
  }

  public class Table1 extends GbaTable {

    @Override
    public void onDoubleClick(Record record) {

      getNavigation().goToPage(new Page2OverlijdenAangever((OverlijdenAangever) record.getObject()));

      super.onDoubleClick(record);
    }

    @Override
    public void setColumns() {

      setSelectable(true);
      setMultiSelect(true);

      addColumn("Naam").setUseHTML(true);
      addColumn("Geboortedatum", 100);
    }

    @Override
    public void setRecords() {

      for (OverlijdenAangever a : getServices().getOverlijdenGemeenteService().getOverlijdenAangevers(
          geldigheidField.getValue())) {
        Record r = addRecord(a);
        r.addValue(GeldigheidStatus.getHtml(a.getNaam().getPred_adel_voorv_gesl_voorn(), a));
        r.addValue(a.getGeboorte().getDatum_leeftijd());
      }

      super.setRecords();
    }
  }
}
