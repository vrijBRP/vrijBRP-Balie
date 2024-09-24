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

package nl.procura.gba.web.components.layouts.form.document;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.vaadin.component.table.indexed.IndexedTable.LAZY_ACTION.FILTERING;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord.Status;
import nl.procura.gba.web.components.layouts.form.document.preview.PrintPreviewWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.gba.web.services.zaken.documenten.DocumentSoort;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintActie;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintModelUtils;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilter;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilterLayout;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilterListener;
import nl.procura.vaadin.functies.downloading.DownloadHandler;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;

public class PrintMultiLayout extends GbaVerticalLayout {

  private final InfoLayout infoLayout = new InfoLayout("", "Selecteer het document om af te drukken.");

  private Zaak   zaak;
  private Object model;

  private final PrintButtons           printButtons;
  private final PrintMultiLayoutConfig config;
  private final PrintSoortForm         form;
  private final PrintTable             table;

  public PrintMultiLayout(Object model, Zaak zaak,
      PrintSelectRecordFilter selectListener,
      DocumentType... types) {

    this(PrintMultiLayoutConfig.builder()
        .model(model)
        .zaak(zaak)
        .selectRecordFilter(selectListener)
        .documentTypes(types)
        .build());
  }

  public PrintMultiLayout(Object model, Zaak zaak,
      PrintSelectRecordFilter selectListener,
      List<DocumentSoort> documentSoorten,
      DocumentType... documentTypes) {

    this(PrintMultiLayoutConfig.builder()
        .model(model)
        .zaak(zaak)
        .selectRecordFilter(selectListener)
        .documentSoorten(documentSoorten)
        .documentTypes(documentTypes)
        .build());
  }

  public PrintMultiLayout(PrintMultiLayoutConfig config) {
    this.model = config.getModel();
    this.zaak = config.getZaak();
    this.config = config;

    setSizeFull();

    printButtons = new PrintButtons();
    printButtons.getButtonSign().setParameterConsumer(parameters -> {
      List<PrintRecord> records = getPrintRecords(true);
      records.forEach(record -> printRecord(record, true, null));
      parameters.setPrintRecords(records);
      parameters.setPersons(singletonList(records.get(0).getZaak().getBasisPersoon()));
    });

    table = new PrintTable(config);
    form = new PrintSoortForm(config.getDocumentSoorten(), config.getDocumentTypes(), soorten -> {
      table.updateSoort(ofNullable(config.getShowRecordFilter())
          .filter(listener -> !soorten.isEmpty())
          .map(listener -> listener.apply(soorten))
          .orElse(soorten));
    });
  }

  @Override
  public void attach() {
    if (table.getParent() == null) {
      VLayout vLayout = new VLayout().add(infoLayout).add(form).margin(false);
      vLayout.setVisible(!config.isFormHidden());

      Fieldset documentgegevens = new Fieldset("Documentgegevens");
      Filter filter = new Filter();
      HLayout h = new HLayout();
      h.setWidth("100%");
      h.addComponent(documentgegevens);
      h.addComponent(filter);
      h.setExpandRatio(documentgegevens, 1f);
      vLayout.addComponent(h);

      addComponent(vLayout);
      addExpandComponent(table);
    }

    table.updateSoort(form.getSelectedSoorten());

    super.attach();
  }

  /**
   * Retourneert de print buttons
   */
  public Button[] getButtons() {
    return printButtons.getButtons(zaak);
  }

  public PrintButtons getPrintButtons() {
    return printButtons;
  }

  public Object getModel() {
    return model;
  }

  public void setModel(Object model) {
    this.model = model;
  }

  public void setZaak(Zaak zaak) {
    this.zaak = zaak;
  }

  public void handleActions(Button button, int keyCode) {
    if (button == printButtons.getButtonPrint() || keyCode == KeyCode.F3) {
      doPrint(false);

    } else if (button == printButtons.getButtonPreview()) {
      doPrint(true);
    }
  }

  /**
   * Update het infoscherm
   */
  public void setInfo(String info) {
    if (fil(info)) {
      infoLayout.setMessage(info);
      infoLayout.attach();
    }
  }

  @SuppressWarnings("unused")
  protected void afterPrintRecord(PrintRecord record, boolean isPreview) {
  }

  /**
   * Print of preview acties
   */
  protected void doPrint(boolean isPreview) {

    form.commit();

    List<PrintRecord> records = getPrintRecords(isPreview);
    records.forEach(record -> printRecord(record, isPreview, new DownloadHandlerImpl(getParentWindow())));

    if (isPreview) {
      getParentWindow().addWindow(new PrintPreviewWindow(records));
    } else {
      getParentWindow().addWindow(new PrintSummaryWindow(records));
    }
  }

  @SuppressWarnings("unused")
  protected List<PrintRecord> getPrintRecords(boolean isPreview) {

    List<PrintRecord> records = table.getSelectedValues(PrintRecord.class);

    if (records.isEmpty()) {
      throw new ProException(ProExceptionSeverity.WARNING, "Geen documenten geselecteerd.");
    }

    for (PrintRecord record : records) {
      record.setModel(getModel());
      record.setZaak(zaak);
      record.setVervolgblad(form.getBean().getVervolgblad());
    }

    return getCopies(records);
  }

  /**
   * Aantal records op basis van aantal per document
   */
  private List<PrintRecord> getCopies(List<PrintRecord> records) {
    List<PrintRecord> copies = new ArrayList<>();
    for (PrintRecord record : records) {
      int aantal = record.getDocument().getAantal();
      for (int i = 0; i < aantal; i++) {
        copies.add(record);
      }
    }

    return copies;
  }

  /**
   * Zoek een gerelateerde zaak van een bepaald zaaktype
   */
  private Zaak getGerelateerdeZaak(Zaak zaak, DocumentRecord document) {
    Services services = getApplication().getServices();
    return services.getZaakRelatieService().getGerelateerdeDocumentZaak(zaak, document);
  }

  /**
   * Zoek een nieuw zaakId als het geen preview is
   */
  private String getZaakId(boolean isPreview, Zaak zaak) {
    return isPreview ? "" : getApplication().getServices().getZaakIdentificatieService().getDmsZaakId(zaak);
  }

  private void printRecord(PrintRecord record, boolean isPreview, DownloadHandler downloadHandler) {
    DocumentService service = getApplication().getServices().getDocumentService();
    record.setStatus(Status.DEFAULT);
    record.setException(null);

    try {
      Zaak zaak = record.getZaak();
      Object model = record.getModel();

      // Alternatief zaak-id opvragen
      if (model instanceof Zaak) {
        // Zoek gerelateerde zaak.
        // Meestal is dit de zaak zelf.
        model = getGerelateerdeZaak(record.getZaak(), record.getDocument());
      }

      model = PrintModelUtils.getModel(model, zaak, record.getDocument());

      ConditionalMap modelMap = new ConditionalMap();
      modelMap.put(record.getSoort().getType().getDoc(), model);

      // Zoek een eventueel alternatief zaak-id of gebruik het procura zaak-id
      modelMap.put("zaakid", getZaakId(isPreview, zaak));

      PrintActie printActie = new PrintActie();
      printActie.setModel(modelMap);
      printActie.setDocument(record.getDocument());
      printActie.setZaak(zaak);
      printActie.setVervolgblad(record.getVervolgblad());

      record.setPrintActie(printActie);

      if (isPreview) {
        PrintOptie printOptie = new PrintOptie();
        printOptie.setUitvoerformaatType(UitvoerformaatType.PDF); // Altijd als PDF
        printActie.setPrintOptie(printOptie);

        record.setPreviewArray(service.preview(printActie));

      } else {
        printActie.setPrintOptie((PrintOptie) record.getUitvoer().getValue());
        service.print(printActie, true, downloadHandler);
      }

      // Functie voor na het printen
      afterPrintRecord(record, isPreview);
      record.setStatus(Status.PRINTED);

    } catch (RuntimeException e) {
      record.setStatus(Status.ERROR);
      record.setException(e);
      throw e;
    }
  }

  public static class PrintTable extends PrintTableTemplate {

    public PrintTable(PrintMultiLayoutConfig config) {
      super(config);
    }
  }

  public class Filter extends HLayout {

    private final TextField filterField = new TextField();
    private final Label     filterLabel = new Label();

    public Filter() {

      this.setWidth("260px");
      this.setSpacing(true);
      this.align(Alignment.MIDDLE_RIGHT);

      IndexedTableFilterListener indexedTableFilterListener = (source, filter) -> {

        if (table.isLazyLoadable() && (source == null && filter == null)) {
          doFilter(astr(filterField.getValue()));
        }

        if ((source != IndexedTableFilterLayout.class) && (filter instanceof IndexedTableFilter)) {
          IndexedTableFilter iFilter = (IndexedTableFilter) filter;
          filterField.setValue(iFilter.getPattern());
        }

        countLabel();
      };

      table.getFilterListeners().add(indexedTableFilterListener);

      this.filterLabel.setSizeUndefined();
      this.filterField.setWidth("100%");
      this.filterField.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.LAZY);
      int halfSecond = 500;
      this.filterField.setTextChangeTimeout(halfSecond);

      FieldEvents.TextChangeListener textChangeListener = event -> {
        doFilter(event.getText());
        countLabel();
      };

      this.filterField.addListener(textChangeListener);
      this.addComponent(filterLabel);
      this.addComponent(filterField);
      this.setExpandRatio(filterField, 1f);
    }

    private void doFilter(String text) {
      if (fil(text)) {
        table.loadAllRecords(FILTERING);
      }
      table.setFilter(nl.procura.vaadin.component.table.indexed.IndexedTableFilterLayout.class,
          new IndexedTableFilter(text, false));
    }

    @Override
    public void attach() {
      super.attach();
      countLabel();
    }

    private void countLabel() {
      int count = table.getContainerDataSource().getItemIds().size();
      filterLabel.setValue(" Aantal: " + count);
    }
  }
}
