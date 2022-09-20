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

package nl.procura.gba.web.modules.beheer.fileimport.page3;

import static nl.procura.gba.common.MiscUtils.setClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.vaadin.ui.Button;
import com.vaadin.ui.Upload;

import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.jpa.personen.db.FileRecord;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.beheer.fileimport.FileImportProcess;
import nl.procura.gba.web.modules.beheer.fileimport.FileImportType;
import nl.procura.gba.web.modules.beheer.fileimport.fileselection.FileImportHandler;
import nl.procura.gba.web.modules.beheer.fileimport.page2.Page2FileImportWindow;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTable;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImporter;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImporterDataWindow;
import nl.procura.gba.web.modules.zaken.document.page4.DocUploader;
import nl.procura.gba.web.services.beheer.fileimport.FileImportRecord;
import nl.procura.gba.web.services.beheer.fileimport.FileImportResult;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page3BestandImportWindow extends GbaModalWindow {

  private final Page3FileImportForm form = new Page3FileImportForm();
  private final FileImport          fileImport;

  public Page3BestandImportWindow(FileImport fileImport) {
    super("Bestanden importeren (Escape om te sluiten)", "500px");
    this.fileImport = fileImport;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page()));
  }

  public class Page extends NormalPageTemplate {

    private final Button     buttonImport  = new Button("Opslaan gegevens");
    private final Button     buttonContent = new Button("Toon gegevens");
    private FileImportResult result;

    public Page() {
    }

    @Override
    public void event(PageEvent event) {
      if (event.isEvent(InitPage.class)) {
        addButton(buttonImport, buttonContent);
        buttonImport.setEnabled(false);
        buttonContent.setEnabled(false);
        addComponent(new Fieldset("Bestand importeren"));
        addComponent(new Uploader());
        addComponent(form);
      }
      super.event(event);
    }

    @Override
    public void handleEvent(Button button, int keyCode) {
      if (button == buttonImport) {
        onImport();
      }

      if (button == buttonContent) {
        onShowContent();
      }
      super.handleEvent(button, keyCode);
    }

    private void onImport() {
      List<FileRecord> fileRecords = new ArrayList<>();
      for (FileImportRecord record : result.getRecords()) {
        FileRecord fileRecord = new FileRecord();
        fileRecord.setcFileImport(fileImport.getcCFileImport());
        fileRecord.setContent(new Gson().toJson(record).getBytes());
        fileRecords.add(fileRecord);
      }
      getServices().getFileImportService().save(fileImport, fileRecords);
      closeWindow();
    }

    private void onShowContent() {
      getApplication().getParentWindow().addWindow(new Page2FileImportWindow(new FileImportHandler() {

        @Override
        public FileImportProcess getFileImportProcess() {
          return null;
        }

        @Override
        public GbaApplication getApplication() {
          return Page.this.getApplication();
        }

        @Override
        public GbaTable getTable(FileImport fileImport) {
          return FileImportType.getById(fileImport.getTemplate())
              .map(type -> {
                FileImportTable table = type.getTable().apply(this::selectFileImportRecord);
                table.update(result.getRecords());
                return table;
              }).orElseThrow(() -> new ProException("Dit bestand is verouderd"));
        }

        private void selectFileImportRecord(FileImportRecord record) {
          getParentWindow().addWindow(new FileImporterDataWindow(record));
        }
      }, fileImport));
    }

    public class Uploader extends DocUploader {

      @Override
      public void uploadSucceeded(Upload.SucceededEvent event) {
        Page3FileImportBean bean = new Page3FileImportBean();
        bean.setFilename(getFileName());
        bean.setSize(FileUtils.byteCountToDisplaySize(getFile().length()));
        buttonImport.setEnabled(false);
        buttonContent.setEnabled(false);

        FileImportType.getById(fileImport.getTemplate())
            .ifPresent(type -> {
              FileImporter converter = type.getConverter();
              try {
                result = converter.convert(toByteArray(getFile()), getApplication().getServices());
                buttonImport.setEnabled(result.isValid() && !result.getRecords().isEmpty());
                buttonContent.setEnabled(!result.getRecords().isEmpty());
                bean.setValidation(getValidation(result.isValid(), result.isValid()
                    ? "Bestand is correct"
                    : remarksToHtml(result.getRemarks())));
              } catch (RuntimeException ex) {
                bean.setValidation(getValidation(false, ex.getMessage()));
              }
            });

        form.setBean(bean);
      }
    }

    private byte[] toByteArray(File file) {
      try {
        return IOUtils.toByteArray(Files.newInputStream(file.toPath()));
      } catch (IOException e) {
        throw new ProException("Kan bestand niet inlezen: " + e.getMessage());
      }
    }

    private String remarksToHtml(List<String> remarks) {
      return String.join("<br/>", remarks);
    }

    private String getValidation(boolean isValid, String isValid1) {
      return setClass(isValid, isValid1);
    }
  }
}
