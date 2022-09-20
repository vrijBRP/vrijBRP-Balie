/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.fileimport.page1;

import java.util.List;

import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.fileimport.FileImportType;
import nl.procura.gba.web.modules.beheer.fileimport.page2.Page2FileImport;
import nl.procura.gba.web.services.beheer.fileimport.FileImportService;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page1BestandImport extends NormalPageTemplate {

  private GbaTable table = null;

  public Page1BestandImport() {
    super("Overzicht van bestanden");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonNew, buttonDel);

      table = new GbaTable() {

        @Override
        public void onDoubleClick(Record record) {
          getNavigation().goToPage(new Page2FileImport(record.getObject(FileImport.class)));
        }

        @Override
        public void setColumns() {
          setSelectable(true);
          addColumn("Naam", 300);
          addColumn("Inhoud");
          super.setColumns();
        }

        @Override
        public void setRecords() {
          FileImportService service = getServices().getFileImportService();
          List<FileImport> fileImports = service.getFileImports();
          for (FileImport fileImport : fileImports) {
            Record r = addRecord(fileImport);
            r.addValue(fileImport.getName());
            r.addValue(FileImportType.getById(fileImport.getTemplate())
                .map(FileImportType::getDescr)
                .orElse("Onbekend type"));
          }
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
        Page1BestandImport.this.deleteRecord(record);
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Page2FileImport(new FileImport()));
  }

  protected void deleteRecord(Record record) {
    getServices().getFileImportService().delete(record.getObject(FileImport.class));
  }
}
