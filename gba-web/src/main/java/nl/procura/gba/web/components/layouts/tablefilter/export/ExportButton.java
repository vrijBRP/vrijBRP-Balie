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

package nl.procura.gba.web.components.layouts.tablefilter.export;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.common.misc.spreadsheets.Spreadsheet;
import nl.procura.vaadin.component.table.indexed.IndexedTable;

public class ExportButton extends Button {

  public ExportButton(IndexedTable table) {
    this(table, new ArrayList<>());
  }

  public ExportButton(final IndexedTable table, final List<? extends Spreadsheet> spreadsheets) {

    super("Exporteer");
    addListener((ClickListener) event -> ((GbaApplication) getApplication()).getParentWindow().addWindow(
        new GbaIndexedTableExportWindow(table, spreadsheets)));
  }
}
