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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab6.page1;

import java.util.List;

import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab6.page2.Tab6DocumentenPage2;
import nl.procura.gba.web.services.zaken.documenten.stempel.DocumentStempel;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Tab6DocumentenPage1 extends DocumentenTabPage {

  private final GbaTable table;

  public Tab6DocumentenPage1() {

    super("Overzicht van de stempels");
    setMargin(true);

    addButton(buttonNew, buttonDel);

    table = new GbaTable() {

      @Override
      public void onDoubleClick(Record record) {

        try {

          getNavigation().goToPage(new Tab6DocumentenPage2(record.getObject(DocumentStempel.class)));
        } catch (Exception e) {
          getApplication().handleException(getWindow(), e);
        }
      }

      @Override
      public void setColumns() {

        setSelectable(true);
        setMultiSelect(true);

        addColumn("Id", 50);
        addColumn("Volgorde", 70);
        addColumn("Naam");
        addColumn("Type", 200);
        addColumn("Vanaf", 130);
        addColumn("X", 25);
        addColumn("Y", 25);
        addColumn("Breedte", 50);
        addColumn("Hoogte", 50);
        addColumn("Volgorde", 70);
        addColumn("Pagina's", 70);
      }

      @Override
      public void setRecords() {

        List<DocumentStempel> list = getServices().getStempelService().getStempels();

        for (DocumentStempel documentStempel : list) {
          Record r = addRecord(documentStempel);
          r.addValue(documentStempel.getCode());
          r.addValue(documentStempel.getVolgorde());
          r.addValue(documentStempel.getDocumentStempel());
          r.addValue(documentStempel.getStempelType());
          r.addValue(documentStempel.getPositie());
          r.addValue(documentStempel.getXcoordinaat());
          r.addValue(documentStempel.getYcoordinaat());
          r.addValue(documentStempel.getBreedte());
          r.addValue(documentStempel.getHoogte());
          r.addValue(documentStempel.getVolgorde());
          r.addValue(documentStempel.getPaginas());
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
        Tab6DocumentenPage1.this.deleteRecord(r);
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Tab6DocumentenPage2(new DocumentStempel()));
  }

  protected void deleteRecord(Record r) {
    getServices().getDocumentService().delete((DocumentStempel) r.getObject());
  }

}
