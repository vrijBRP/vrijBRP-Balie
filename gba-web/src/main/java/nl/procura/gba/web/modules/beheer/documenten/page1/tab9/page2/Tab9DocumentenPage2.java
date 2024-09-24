/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab9.page2;

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Upload;

import nl.procura.gba.jpa.personen.db.Translation;
import nl.procura.gba.jpa.personen.db.TranslationRec;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab9.page1.Tab9DocumentenPage1;
import nl.procura.gba.web.modules.zaken.document.page4.DocUploader;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.documenten.DocumentTranslation;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;

import lombok.Data;
import lombok.RequiredArgsConstructor;

public class Tab9DocumentenPage2 extends DocumentenTabPage {

  private DocumentTranslation     documentTranslation;
  private Tab9DocumentenPage2Form form  = null;
  private Table                   table = null;
  private HLayout                 recordButtonLayout;

  public Tab9DocumentenPage2(Translation translation) {
    super("Toevoegen / muteren vertaling");
    this.documentTranslation = new DocumentTranslation(translation);
    addButton(buttonPrev, buttonNew, buttonSave);
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      form = new Tab9DocumentenPage2Form(documentTranslation.getEntity());
      table = new Table();
      addComponent(form);
      addComponent(new Fieldset("Vertalingen"));
      Button addRecordButton = new Button("Toevoegen", (ClickListener) clickEvent -> addRecord());
      Button deleteRecordButton = new Button("Verwijderen", (ClickListener) clickEvent -> deleteRecords());
      Button exportButton = new Button("Exporteren", (ClickListener) clickEvent -> exportRecords());
      Button importButton = new Button("Importeren", (ClickListener) clickEvent -> importRecords());
      recordButtonLayout = new HLayout(addRecordButton, deleteRecordButton, exportButton, importButton);
      recordButtonLayout.setEnabled(documentTranslation.getEntity().isStored());
      addComponent(recordButtonLayout);
      addExpandComponent(table);
    }

    super.event(event);
  }

  public void exportRecords() {
    TranslationDownload download = new TranslationDownload();
    documentTranslation.getSortedRecords()
        .forEach(rec -> download.add(rec.getNl(), rec.getFl()));

    String json = new Gson().toJson(download);
    ByteArrayInputStream stream = new ByteArrayInputStream(json.getBytes());
    new DownloadHandlerImpl(getParentWindow()).download(stream, "vrijbrp-document-vertalingen.json", true);
    successMessage("Vertalingen geëxporteerd");
  }

  public void importRecords() {
    getParentWindow().addWindow(new ImportWindow(documentTranslation.getEntity(), getServices(), this::reload));
  }

  private void addRecord() {
    TranslationRec record = new TranslationRec(documentTranslation.getEntity());
    TranslationRecordWindow window = new TranslationRecordWindow(record, rec -> {
      getServices().getDocumentService().save(rec);
      documentTranslation.getEntity().getTranslations().add(rec);
      onSave();
    });
    getApplication().getParentWindow().addWindow(window);
  }

  private void deleteRecords() {
    for (TranslationRec rec : table.getSelectedValues(TranslationRec.class)) {
      getServices().getDocumentService().delete(rec);
      documentTranslation.getEntity().getTranslations().remove(rec);
    }
    onSave();
    reload();
  }

  @Override
  public void onNew() {
    form.reset();
    this.documentTranslation = new DocumentTranslation(new Translation());
    reload();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goToPage(new Tab9DocumentenPage1());
    getNavigation().removeOtherPages();
  }

  @Override
  public void onSave() {
    form.commit();
    Tab9DocumentenPage2Bean bean = form.getBean();
    documentTranslation.getEntity().setName(bean.getOmschrijving());
    getServices().getDocumentService().save(documentTranslation.getEntity());
    successMessage("Gegevens zijn opgeslagen.");
    recordButtonLayout.setEnabled(documentTranslation.getEntity().isStored());
    reload();
  }

  private void reload() {
    recordButtonLayout.setEnabled(documentTranslation.getEntity().isStored());
    table.init();
  }

  private class Table extends GbaTable {

    @Override
    public void onDoubleClick(Record record) {
      TranslationRec recordObject = record.getObject(TranslationRec.class);
      TranslationRecordWindow window = new TranslationRecordWindow(recordObject, savedRecord -> {
        getServices().getDocumentService().save(savedRecord);
        onSave();
      });
      getApplication().getParentWindow().addWindow(window);
    }

    @Override
    public void setColumns() {
      setSelectable(true);
      setMultiSelect(true);

      addColumn("Id", 50);
      addColumn("Nederlands", 400);
      addColumn("Vertaling");
    }

    @Override
    public void setRecords() {
      int nr = 1;
      List<TranslationRec> records = documentTranslation.getSortedRecords();
      for (TranslationRec record : records) {
        Record r = addRecord(record);
        r.addValue(nr++);
        r.addValue(record.getNl());
        r.addValue(record.getFl());
      }
    }
  }

  @Data
  @RequiredArgsConstructor
  public static class TranslationDownloadRecord {

    private final String nl;

    private final String fl;
  }

  @Data
  public static class TranslationDownload {

    private List<TranslationDownloadRecord> records = new ArrayList<>();

    public void add(String nl, String fl) {
      records.add(new TranslationDownloadRecord(nl, fl));
    }
  }

  public static class ImportWindow extends GbaModalWindow {

    private final Translation translation;
    private final Services    services;
    private final Runnable    doneListener;

    public ImportWindow(Translation translation, Services services, Runnable doneListener) {
      super("Importeren vertalingen", "500px");
      this.translation = translation;
      this.services = services;
      this.doneListener = doneListener;
      setContent(new VLayout().margin(true).add(new Uploader()));
    }

    @Override
    public void closeWindow() {
      super.closeWindow();
    }

    public class Uploader extends DocUploader {

      @Override
      public void uploadSucceeded(Upload.SucceededEvent event) {

        try {
          TranslationDownload translations = new Gson().fromJson(new FileReader(getFile()), TranslationDownload.class);
          for (TranslationDownloadRecord record : translations.getRecords()) {
            TranslationRec translationRec = new TranslationRec(translation, record.getNl(), record.getFl());
            services.getDocumentService().save(translationRec);
            translation.getTranslations().add(translationRec);
            doneListener.run();
            closeWindow();
          }
          super.uploadSucceeded(event);
        } catch (Exception e) {
          getApplication().handleException(getWindow(), e);
        }
      }
    }
  }
}
