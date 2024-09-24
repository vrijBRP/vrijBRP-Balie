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

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Embedded;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.common.misc.spreadsheets.Spreadsheet;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.table.indexed.IndexedTable;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;

public class GbaIndexedTableExportWindow extends ModalWindow {

  private final IndexedTable                table;
  private final List<? extends Spreadsheet> spreadsheets;

  public GbaIndexedTableExportWindow(IndexedTable table, List<? extends Spreadsheet> spreadsheets) {

    setCaption("Exporteren (Escape om te sluiten)");

    this.table = table;
    this.spreadsheets = spreadsheets;

    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(true);

    SoortTable soortTable = new SoortTable();
    layout.addComponent(soortTable);

    setContent(layout);

    soortTable.focus();
  }

  private class SoortTable extends GbaTable {

    public SoortTable() {
    }

    @Override
    public void onClick(Record record) {

      super.onClick(record);

      Spreadsheet spreadsheet = record.getObject(Spreadsheet.class);
      UitvoerformaatType type = spreadsheet.getType();
      spreadsheet.compose();

      DocumentService documenten = getApplication().getServices().getDocumentService();
      byte[] bytes = documenten.convertSpreadsheet(spreadsheet.getData(), type);
      DownloadHandlerImpl downloadHandler = new DownloadHandlerImpl(getWindow().getParent());
      downloadHandler.download(new ByteArrayInputStream(bytes), "tabel." + type.getExt(), true);

      ((ModalWindow) getWindow()).closeWindow();
    }

    @Override
    public void setColumns() {

      setSelectable(true);

      addColumn("", 15).setClassType(Embedded.class);
      addColumn("Soort");
    }

    @Override
    public void setRecords() {

      List<Spreadsheet> spList = new ArrayList<>(spreadsheets);
      if (spList.isEmpty()) {
        spList.add(new StandaardTabelSpreadsheet(table, "Standaarduitvoer", UitvoerformaatType.CSV_SEMICOLON));
        spList.add(new StandaardTabelSpreadsheet(table, "Standaarduitvoer", UitvoerformaatType.CSV));
      }

      for (Spreadsheet spreadsheet : spList) {
        Record record = addRecord(spreadsheet);
        record.addValue(TableImage.getByBestandType(BestandType.getType(spreadsheet.getType().getExt())));
        record.addValue(spreadsheet.getName() + " (" + spreadsheet.getType().getOms() + ")");
      }

      super.setRecords();
    }

  }
}
