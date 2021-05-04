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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab8.page1;

import java.util.List;
import java.util.stream.Collectors;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.dialogs.DeleteRecordsFromTable;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab8.page2.Tab8DocumentenPage2;
import nl.procura.gba.web.services.zaken.documenten.dmstypes.DmsDocumentType;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Tab8DocumentenPage1 extends DocumentenTabPage {

  private final GbaTable table;

  public Tab8DocumentenPage1() {
    super("Overzicht van de DMS documenttypes");

    setMargin(true);
    addButton(buttonNew, buttonDel);

    table = new GbaTable() {

      @Override
      public void onDoubleClick(Record record) {
        try {
          getNavigation().goToPage(new Tab8DocumentenPage2(record.getObject(DmsDocumentType.class)));
        } catch (Exception e) {
          getApplication().handleException(getWindow(), e);
        }
      }

      @Override
      public void setColumns() {

        setSelectable(true);
        setMultiSelect(true);

        addColumn("Id", 50);
        addColumn("Omschrijving", 250);
        addColumn("Zaaktypes");
      }

      @Override
      public void setRecords() {
        List<DmsDocumentType> list = getServices().getDocumentService().getDmsDocumentTypes();
        for (DmsDocumentType type : list) {
          Record r = addRecord(type);
          r.addValue(type.getCode());
          r.addValue(type.getDocumentDmsType());
          r.addValue(type.getZaakTypesAsList().stream()
              .map(ZaakType::toString)
              .collect(Collectors.joining(", ")));
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
        Tab8DocumentenPage1.this.deleteRecord(r);
      }
    };
  }

  @Override
  public void onNew() {
    getNavigation().goToPage(new Tab8DocumentenPage2(new DmsDocumentType()));
  }

  protected void deleteRecord(Record r) {
    getServices().getDocumentService().delete((DmsDocumentType) r.getObject());
  }
}
