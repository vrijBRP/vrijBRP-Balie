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

package nl.procura.gba.web.modules.beheer.verkiezing.page5;

import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.gba.jpa.personen.db.KiesrVerkInfo;
import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.verkiezing.page2.Page2Verkiezing;
import nl.procura.gba.web.modules.beheer.verkiezing.page6.Page6Verkiezing;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page5Verkiezing extends NormalPageTemplate {

  private final KiesrVerk verkiezing;
  private GbaTable        table = null;

  public Page5Verkiezing(KiesrVerk verkiezing) {
    super("Overzicht van tekstblokken voor verkiezing");
    this.verkiezing = verkiezing;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev, buttonNew, buttonDel);

      table = new GbaTable() {

        @Override
        public void setColumns() {
          setSelectable(true);
          addColumn("Naam", 200);
          addColumn("Inhoud");
          super.setColumns();
        }

        @Override
        public void onDoubleClick(IndexedTable.Record record) {
          getNavigation().goToPage(new Page6Verkiezing(record.getObject(KiesrVerkInfo.class)));
        }

        @Override
        public void setRecords() {
          for (KiesrVerkInfo info : getServices().getKiezersregisterService().getInfo(verkiezing)) {
            IndexedTable.Record record = addRecord(info);
            record.addValue(info.getNaam());
            record.addValue(info.getInhoud());
          }
          super.setRecords();
        }
      };

      addExpandComponent(table);
      getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));

    } else if (event.isEvent(AfterBackwardReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onDelete() {
    new DeleteRecordsFromTable(table) {

      @Override
      public void deleteRecord(Record record) {
        Page5Verkiezing.this.deleteRecord(record);
      }
    };
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goToPage(Page2Verkiezing.class);
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Page6Verkiezing(new KiesrVerkInfo(verkiezing)));
  }

  protected void deleteRecord(Record record) {
    getServices().getKiezersregisterService().delete(record.getObject(KiesrVerkInfo.class));
  }
}
