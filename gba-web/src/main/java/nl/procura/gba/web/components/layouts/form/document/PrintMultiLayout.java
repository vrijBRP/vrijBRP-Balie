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

package nl.procura.gba.web.components.layouts.form.document;

import static nl.procura.gba.web.components.layouts.form.document.PrintDocumentBean.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.vaadin.component.table.indexed.IndexedTable.LAZY_ACTION.FILTERING;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.*;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord.Status;
import nl.procura.gba.web.components.layouts.form.document.preview.PrintPreviewWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.*;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintActie;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintModelUtils;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilter;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilterLayout;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilterListener;
import nl.procura.vaadin.functies.downloading.DownloadHandler;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;

public class PrintMultiLayout extends GbaVerticalLayout {

  public final Button      buttonPreview = new Button("Voorbeeld / e-mailen");
  public final Button      buttonPrint   = new Button("Afdrukken (F3)");
  private final InfoLayout infoLayout    = new InfoLayout("", "Selecteer het document om af te drukken.");

  private Zaak                      zaak;
  private Object                    model;
  private final PrintTable          table;
  private final PrintSoortForm      soortForm;
  private final DocumentType[]      documentTypes;
  private final List<DocumentSoort> documentSoorten;

  public PrintMultiLayout(Object model, Zaak zaak, PrintSelectListener selectListener, DocumentType... types) {
    this(model, zaak, selectListener, new ArrayList<>(), types);
  }

  public PrintMultiLayout(Object model, Zaak zaak, PrintSelectListener selectListener, List<DocumentSoort> soorten) {
    this(model, zaak, selectListener, soorten, (DocumentType) null);
  }

  public PrintMultiLayout(Object model, Zaak zaak, PrintSelectListener selectListener,
      List<DocumentSoort> documentSoorten, DocumentType... documentTypes) {

    this.model = model;
    this.zaak = zaak;
    this.documentTypes = documentTypes;
    this.documentSoorten = documentSoorten;

    setSizeFull();
    addComponent(infoLayout);

    table = new PrintTable(selectListener);

    soortForm = new PrintSoortForm() {

      @Override
      protected void onChangeSoort(List<DocumentSoort> documentSoorten) {
        if (isTabelAdded()) {
          table.updateSoort(documentSoorten);
        }
      }
    };
  }

  @Override
  public void attach() {

    if (table.getParent() == null) {
      addComponent(soortForm);
      Fieldset documentgegevens = new Fieldset("Documentgegevens");
      Filter filter = new Filter();
      HLayout h = new HLayout();
      h.setWidth("100%");
      h.addComponent(documentgegevens);
      h.addComponent(filter);
      h.setExpandRatio(documentgegevens, 1f);
      addComponent(h);
      addExpandComponent(table);
    }

    table.updateSoort(soortForm.getSelectedSoorten());

    super.attach();
  }

  /**
   * Retourneert de print buttons
   */
  public Button[] getButtons() {
    return new Button[]{ buttonPreview, buttonPrint };
  }

  public Object getModel() {
    return model;
  }

  public void setModel(Object model) {
    this.model = model;
  }

  public Zaak getZaak() {
    return zaak;
  }

  public void setZaak(Zaak zaak) {
    this.zaak = zaak;
  }

  public void handleActions(Button button, int keyCode) {

    if (button == buttonPrint || keyCode == KeyCode.F3) {
      doPrint(false);
    } else if (button == buttonPreview) {
      doPrint(true);
    }
  }

  public boolean isTabelAdded() {
    return table.getParent() != null;
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

    soortForm.commit();

    List<PrintRecord> records = getPrintRecords(isPreview);

    for (PrintRecord record : records) {
      printRecord(record, isPreview, new DownloadHandlerImpl(getWindow()));
    }

    if (isPreview) {
      getParentWindow().addWindow(new PrintPreviewWindow(records));
    } else {
      getParentWindow().addWindow(new PrintSummaryWindow(records));
    }

    // Gooi eerste exceptie op

    for (PrintRecord dr : records) {
      if (dr.getException() != null) {
        throw dr.getException();
      }
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
      record.setZaak(getZaak());
      record.setVervolgblad(soortForm.getBean().getVervolgblad());
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
    }
  }

  public class PrintSoortForm extends PrintSoortFormTemplate {

    public PrintSoortForm() {
      setCaption("Soort");
      setOrder(SOORT, SOORT_LEEG, VERVOLG_BLAD);
      setColumnWidths(WIDTH_130, "");

      setDocumentSoorten(documentSoorten);
      setDocumentTypes(documentTypes);

      setBean(new PrintDocumentBean());
    }
  }

  public class PrintTable extends PrintTableTemplate {

    public PrintTable(PrintSelectListener selectListener) {
      super(selectListener);
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
