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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab9.page1;

import java.util.List;

import nl.procura.gba.jpa.personen.db.Translation;
import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab9.page2.Tab9DocumentenPage2;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Tab9DocumentenPage1 extends DocumentenTabPage {

  private final GbaTable table;

  public Tab9DocumentenPage1() {
    super("Overzicht van de vertalingen");

    setMargin(true);
    addButton(buttonNew, buttonDel);

    table = new GbaTable() {

      @Override
      public void onDoubleClick(Record record) {
        try {
          getNavigation().goToPage(new Tab9DocumentenPage2(record.getObject(Translation.class)));
        } catch (Exception e) {
          getApplication().handleException(getWindow(), e);
        }
      }

      @Override
      public void setColumns() {
        setSelectable(true);
        setMultiSelect(true);

        addColumn("Id", 50);
        addColumn("Omschrijving");
      }

      @Override
      public void setRecords() {
        List<Translation> list = getServices().getDocumentService().getTranslations();
        for (Translation translation : list) {
          Record r = addRecord(translation);
          r.addValue(translation.getCTranslation());
          r.addValue(translation.getName());
        }
      }
    };

    addExpandComponent(table);
    getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(table));
  }

  @Override
  public void onDelete() {
    new DeleteRecordsFromTable(table) {

      @Override
      public void deleteRecord(Record r) {
        Tab9DocumentenPage1.this.deleteRecord(r);
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Tab9DocumentenPage2(new Translation()));
  }

  protected void deleteRecord(Record r) {
    getServices().getDocumentService().delete((Translation) r.getObject());
  }
}
