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

package nl.procura.gba.web.modules.beheer.fileimport.fileselection;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTable;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;

public class FileImportSearchWindow extends GbaModalWindow {

  private final FileImportHandler fileImportHandler;
  private final FileImportTable   table;

  public FileImportSearchWindow(FileImportHandler fileImportHandler, FileImport fileImport) {
    super("Selecteer uit bestand: " + fileImport.getName(), "1200px");
    this.fileImportHandler = fileImportHandler;
    fileImportHandler.setFinishImportListener(this::closeAllModalWindows);
    table = fileImportHandler.getTable(fileImport);
    table.setSelectable(true);
  }

  private void closeAllModalWindows() {
    getGbaApplication().closeAllModalWindows(getGbaApplication().getWindows());
  }

  @Override
  public void attach() {
    VLayout vLayout = new VLayout(true);
    Button closeButton = new Button("Sluiten (Esc)", event -> closeAllModalWindows());
    Component filter = fileImportHandler.getTableFilter();
    vLayout.add(new HLayout(filter, closeButton).expand(filter, 1F).sizeFull());
    vLayout.add(table);
    table.focus();
    setContent(vLayout);
    super.attach();
  }
}
