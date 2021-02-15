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

package nl.procura.gbaws.web.vaadin.module.tables.page1;

import java.io.ByteArrayInputStream;
import java.io.File;

import com.vaadin.ui.Button;
import com.vaadin.ui.Upload.SucceededEvent;

import nl.procura.gbaws.db.handlers.LandTabDao.LandTable;
import nl.procura.gbaws.web.vaadin.layouts.DefaultPageLayout;
import nl.procura.gbaws.web.vaadin.module.tables.page1.upload.TabelUpdatesUploadWindow;
import nl.procura.gbaws.web.vaadin.module.tables.page2.Page2Tables;
import nl.procura.gbaws.web.vaadin.module.tables.tasks.TabelUpdatesExportTask;
import nl.procura.gbaws.web.vaadin.module.tables.tasks.TabelUpdatesFileImportTask;
import nl.procura.gbaws.web.vaadin.module.tables.tasks.TabelUpdatesWebserviceImportTask;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;
import nl.procura.vaadin.functies.task.VaadinTaskWindow;

@SuppressWarnings("serial")
public class Page1Tables extends DefaultPageLayout {

  private final Button buttonDownload = new Button("Tabellen downloaden");
  private final Button buttonImport   = new Button("Tabellen importeren");
  private final Button buttonExport   = new Button("Tabellen exporteren");

  private Page1TablesTable table = null;

  public Page1Tables() {
    super("Basisgegevens");
    setSpacing(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonDownload);
      addButton(buttonImport);
      addButton(buttonExport);

      table = new Page1TablesTable() {

        @Override
        public void onDoubleClick(Record record) {
          getNavigation().goToPage(new Page2Tables(record.getObject(LandTable.class)));
          super.onClick(record);
        }
      };

      addExpandComponent(table);

      onSearch();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonDownload) {
      onDownload();
    } else if (button == buttonImport) {
      onImport();
    } else if (button == buttonExport) {
      onExport();
    }

    super.handleEvent(button, keyCode);
  }

  private void onImport() {
    getWindow().addWindow(new TabelUpdatesUploadWindow() {

      @Override
      public void onUpload(File file, SucceededEvent event) {
        getParentWindow().addWindow(new TablesUpdateImportWindow(file));
        closeWindow();
      }
    });
  }

  private void onExport() {
    getWindow().addWindow(new TablesUpdateExportWindow());
  }

  private void onDownload() {
    getWindow().addWindow(new TablesUpdateWebserviceWindow());
  }

  public class TablesUpdateWebserviceWindow extends VaadinTaskWindow {

    public TablesUpdateWebserviceWindow() {
      super(new TabelUpdatesWebserviceImportTask());
      setCaption("Tabellen updaten");
    }

    @Override
    public void closeWindow() {
      table.init();
      super.closeWindow();
    }
  }

  public class TablesUpdateExportWindow extends VaadinTaskWindow {

    public TablesUpdateExportWindow() {
      super(new TabelUpdatesExportTask() {

        @Override
        public void onDone() {
          ByteArrayInputStream stream = new ByteArrayInputStream(getContent());
          new DownloadHandlerImpl(Page1Tables.this.getWindow()).download(stream, "proweb-tabellen.json",
              true);
        }
      });

      setCaption("Tabellen exporteren");
    }

    @Override
    public void closeWindow() {
      table.init();
      super.closeWindow();
    }
  }

  public class TablesUpdateImportWindow extends VaadinTaskWindow {

    public TablesUpdateImportWindow(File file) {
      super(new TabelUpdatesFileImportTask(file));
      setCaption("Tabellen importeren");
    }

    @Override
    public void closeWindow() {
      table.init();
      super.closeWindow();
    }
  }
}
