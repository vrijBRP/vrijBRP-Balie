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

package nl.procura.gba.web.components.layouts.form.document.email.page1;

import static java.util.Arrays.asList;

import java.util.List;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Embedded;

import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.components.layouts.form.document.preview.PrintPreviewWindow;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.documenten.BestandType;

public class Page1EmailBijlageTable extends GbaTable {

  private final List<PrintRecord> records;

  public Page1EmailBijlageTable(List<PrintRecord> records) {
    this.records = records;
    setWidth("100%");
    setHeight("100%");
  }

  @Override
  public void onDoubleClick(Record record) {
    getWindow().getParent().addWindow(new PrintPreviewWindow(asList(record.getObject(PrintRecord.class)), false));
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("&nbsp;", 15).setClassType(Embedded.class);
    addColumn("Bijlage");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (PrintRecord printRecord : records) {
      Record record = addRecord(printRecord);
      record.addValue(new Embedded(null, new ThemeResource(BestandType.PDF.getPath())));
      record.addValue(printRecord.getDocument().getEmailDocument(BestandType.PDF));
    }

    super.setRecords();
  }
}
