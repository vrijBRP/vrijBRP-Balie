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

package nl.procura.gba.web.components.layouts.tablefilter;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.NativeSelect;

import nl.procura.gba.web.common.misc.spreadsheets.Spreadsheet;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.column.ColumnButton;
import nl.procura.gba.web.components.layouts.tablefilter.export.ExportButton;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Column;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilterLayout;

public class GbaIndexedTableFilterLayout extends IndexedTableFilterLayout {

  public GbaIndexedTableFilterLayout(GbaTable table) {
    this(table, new ArrayList<>());
  }

  public GbaIndexedTableFilterLayout(GbaTable table, NativeSelect sortField) {
    this(table, new ArrayList<>(), sortField);
  }

  public GbaIndexedTableFilterLayout(GbaTable table, List<? extends Spreadsheet> spreadsheets) {
    this(table, spreadsheets, null);
  }

  public GbaIndexedTableFilterLayout(GbaTable table,
      List<? extends Spreadsheet> spreadsheets,
      NativeSelect sortField) {
    super(table);
    if (sortField != null) {
      addComponent(sortField);
    }

    ExportButton exportButton = new ExportButton(table, spreadsheets);
    addComponent(exportButton);

    if (hasCollapsibleColumns(table)) {
      ColumnButton columnButton = new ColumnButton(table);
      addComponent(columnButton);
    }
  }

  private boolean hasCollapsibleColumns(GbaTable table) {
    for (Column column : table.getColumns()) {
      if (column.isCollapsible()) {
        return true;
      }
    }
    return false;
  }
}
