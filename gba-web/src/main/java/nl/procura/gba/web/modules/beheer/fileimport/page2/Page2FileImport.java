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

package nl.procura.gba.web.modules.beheer.fileimport.page2;

import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.fileimport.FileImportType;
import nl.procura.gba.web.modules.beheer.fileimport.page3.Page3BestandImportWindow;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTable;
import nl.procura.gba.web.services.beheer.fileimport.FileImportRecord;
import nl.procura.gba.web.services.beheer.fileimport.FileImportService;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2FileImport extends NormalPageTemplate {

  private final Button        buttonImport = new Button("Importeren");
  private final FileImport    fileImport;
  private Page2FileImportForm form;
  private FileImportTable     table;

  public Page2FileImport(FileImport fileImport) {
    super("Toevoegen / muteren bestanden");
    this.fileImport = fileImport;
    addButton(buttonPrev, buttonSave, buttonImport, buttonDel);
    buttonImport.setEnabled(false);
    buttonDel.setEnabled(false);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      form = new Page2FileImportForm(fileImport);
      addComponent(form);
      updateTableLayout();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonImport) {
      onImport();
    }
    super.handleEvent(button, keyCode);
  }

  private void onImport() {
    getApplication().getParentWindow().addWindow(new Page3BestandImportWindow(fileImport) {

      @Override
      public void closeWindow() {
        updateTableLayout();
        super.closeWindow();
      }
    });
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {
    doSave();
    successMessage("De gegevens zijn opgeslagen.");
    form.update();
    updateTableLayout();
  }

  @Override
  public void onDelete() {
    new DeleteProcedure<FileImportRecord>(table, true) {

      @Override
      protected void deleteValues(List<FileImportRecord> records) {
        getServices().getFileImportService().delete(records);
      }

      @Override
      protected void afterDelete() {
        updateTableLayout();
      }
    };
    super.onDelete();
  }

  private void updateTableLayout() {
    if (fileImport.isStored()) {
      FileImportType.getById(fileImport.getTemplate())
          .ifPresent(type -> {
            FileImportService service = getApplication().getServices().getFileImportService();
            List<FileImportRecord> fileImportRecords = service.getFileImportRecords(fileImport);
            if (table == null) {
              table = type.getImporter().createTable(null);
              table.setSelectable(true);
              table.setMultiSelect(true);
              addComponent(new Fieldset("Geselecteerde gegevens"));
              addComponent(type.getImporter().createFilter(fileImport, table));
              addExpandComponent(table);
            }
            table.update(fileImportRecords);
            buttonImport.setEnabled(true);
            buttonDel.setEnabled(true);
          });
    }
  }

  private void doSave() {
    form.commit();
    Page2FileImportBean bean = form.getBean();
    fileImport.setName(bean.getName());
    fileImport.setTemplate(bean.getFileImportType().getId());
    fileImport.setClosed(bean.isClosed());
    getServices().getFileImportService().save(fileImport);
  }
}
