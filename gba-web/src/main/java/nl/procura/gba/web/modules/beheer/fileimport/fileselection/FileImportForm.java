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

import static nl.procura.gba.web.modules.beheer.fileimport.fileselection.FileImportBean.F_FILE_IMPORT;

import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.beheer.fileimport.FileImportType;
import nl.procura.gba.web.services.beheer.fileimport.FileImportService.Count;
import nl.procura.vaadin.component.container.ProcuraContainer;

public class FileImportForm extends GbaForm<FileImportBean> {

  private final FileImportHandler handler;

  public FileImportForm(FileImportHandler handler) {
    this.handler = handler;
    setColumnWidths("70px", "");
    setOrder(F_FILE_IMPORT);
    FileImportBean bean = new FileImportBean();
    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    GbaNativeSelect fileImportField = getField(F_FILE_IMPORT, GbaNativeSelect.class);
    List<FileImport> fileImports = getFileImports();
    fileImportField.setContainerDataSource(new FileImportTypeContainer(fileImports));
    fileImportField.addListener((ValueChangeListener) event -> {
      FileImport fileImport = (FileImport) event.getProperty().getValue();
      if (fileImport != null) {
        selectFileImport(fileImport);
      }
    });
    // Automatically select first record if only 1
    if (fileImports.size() == 1) {
      selectFileImport(fileImports.get(0));
    }
    super.afterSetBean();
  }

  private void selectFileImport(FileImport fileImport) {
    FileImportSearchWindow searchWindow = new FileImportSearchWindow(handler, fileImport);
    handler.getApplication().getParentWindow().addWindow(searchWindow);
  }

  public List<FileImport> getFileImports() {
    return FileImportType.getByProcess(handler.getFileImportProcess())
        .stream().flatMap(type -> handler.getApplication()
            .getServices()
            .getFileImportService()
            .getFileImports(type.getId())
            .stream())
        .collect(Collectors.toList());
  }

  public class FileImportTypeContainer extends IndexedContainer implements ProcuraContainer {

    public FileImportTypeContainer(List<FileImport> fileImports) {
      addContainerProperty(OMSCHRIJVING, String.class, "");
      fileImports.forEach(fileImport -> {
        if (!fileImport.isClosed()) {
          Count count = handler.getApplication().getServices().getFileImportService().countRecords(fileImport);
          Item item = addItem(fileImport);
          item.getItemProperty(OMSCHRIJVING).setValue(String.format("%s (%d / %d)",
              fileImport.getName(),
              count.getNewRecords(),
              count.getTotalRecords()));
        }
      });
    }
  }
}
