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

package nl.procura.gba.web.modules.tabellen.belanghebbende.page1;

import com.vaadin.ui.Alignment;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.fields.GeldigheidField;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.tabellen.belanghebbende.page2.Page2Belanghebbende;
import nl.procura.gba.web.services.gba.basistabellen.belanghebbende.Belanghebbende;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1Belanghebbende extends NormalPageTemplate {

  private Table           table = null;
  private GeldigheidField geldigheidField;

  public Page1Belanghebbende() {

    super("Overzicht van belanghebbenden");

    addButton(buttonNew);
    addButton(buttonDel);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      geldigheidField = new GeldigheidField() {

        @Override
        public void onChangeValue(GeldigheidStatus value) {
          table.init();
        }
      };

      table = new Table();
      addExpandComponent(table);

      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));
      getButtonLayout().addComponent(geldigheidField);
      getButtonLayout().align(geldigheidField, Alignment.MIDDLE_RIGHT);
    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<Belanghebbende>(table) {

      @Override
      public void deleteValue(Belanghebbende belanghebbende) {
        getServices().getBelanghebbendeService().delete(belanghebbende);
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Page2Belanghebbende());
  }

  public class Table extends GbaTable {

    @Override
    public int getPageLength() {
      return 10;
    }

    @Override
    public void onDoubleClick(Record record) {
      getNavigation().goToPage(new Page2Belanghebbende(record.getObject(Belanghebbende.class)));
    }

    @Override
    public void setColumns() {

      setSelectable(true);
      setMultiSelect(true);

      addColumn("Naam", 300).setUseHTML(true);
      addColumn("Type");
      addColumn("Tav");
      addColumn("Adres");
      addColumn("Postcode / plaats");
    }

    @Override
    public void setRecords() {

      for (Belanghebbende l : getServices().getBelanghebbendeService().getBelanghebbenden(
          geldigheidField.getValue())) {

        Record r = addRecord(l);
        r.addValue(GeldigheidStatus.getHtml(l.getNaam(), l));
        r.addValue(l.getBelanghebbendeType());
        r.addValue(l.getTav());
        r.addValue(l.getAdres());
        r.addValue((l.getPostcode() + " " + l.getPlaats()).trim());
      }

      super.setRecords();
    }
  }
}
