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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page12;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.collection.LambdaCollections.with;
import static nl.procura.gba.common.ZaakStatusType.INBEHANDELING;
import static nl.procura.gba.common.ZaakStatusType.OPGENOMEN;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.VERHUIZING_AANGIFTE;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord.Status;
import nl.procura.gba.web.components.layouts.form.document.PrintSummaryWindow;
import nl.procura.gba.web.components.layouts.form.document.preview.PrintPreviewWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterPage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.status.ZaakStatusUpdater;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.gba.web.services.zaken.documenten.DocumentSoort;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaakArgumenten;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintActie;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.gba.web.windows.home.navigatie.ZakenregisterAccordionTab;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;
import nl.procura.vaadin.functies.downloading.DownloadHandler;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;
import nl.procura.vaadin.functies.task.VaadinTaskOptions;

import ch.lambdaj.collection.LambdaList;

public class Page12Zaken extends ZakenregisterPage<Zaak> {

  private final Button buttonPreview = new Button("Voorbeeld / e-mailen");
  private final Button buttonPrint   = new Button("Afdrukken (F3)");
  private final Button buttonStatus  = new Button("Status wijzigen");
  private final Button buttonRefresh = new Button("Herladen (F5)");

  private final Page12ZakenForm     form    = new Page12ZakenForm();
  private final List<DocumentSoort> soorten = new ArrayList<>();
  private Table1                    table   = null;

  public Page12Zaken() {
    super(null, "Zakenregister: correspondentie afdrukken");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPreview);
      addButton(buttonPrint);
      addButton(buttonStatus);
      addButton(buttonRefresh);

      addComponent(form);

      soorten.addAll(getBrieven(VERHUIZING_AANGIFTE));

      setInfo("Alle correspondentie die in de applicatie op <b>opgenomen</b> staat.");

      table = new Table1();
      addExpandComponent(table);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonPrint || keyCode == KeyCode.F3) {
      doPrePrint(false);
    } else if (button == buttonPreview) {
      doPrePrint(true);
    } else if (button == buttonStatus) {
      wijzigStatus();
    } else if (button == buttonRefresh || keyCode == KeyCode.F5) {
      table.reset();
    }

    super.handleEvent(button, keyCode);
  }

  /**
   * Print of preview acties
   */
  protected void doPrePrint(final boolean isPreview) {

    final List<CorrespondentiePrintRecord> records = getSelectedRecords(isPreview);
    final List<Zaak> zaken = new ArrayList<>();
    for (CorrespondentiePrintRecord record : records) {
      if (record.getPrintRecord() != null) {
        zaken.add(record.getGerelateerdeZaak());
      }
    }

    ZaakLadenTask task = new ZaakLadenTask(getApplication(), zaken) {

      @Override
      public void onDone() {
        doPrint(isPreview);
      }
    };

    VaadinTaskOptions options = new VaadinTaskOptions();
    options.setWidth("300px");
    options.setShowLog(false);
    options.setAutoClose(true);

    getWindow().addWindow(new ZaakLadenWindow(task, options));
  }

  /**
   * Print of preview acties
   */
  protected synchronized void doPrint(boolean isPreview) {

    form.commit();

    List<CorrespondentiePrintRecord> records = getSelectedRecords(isPreview);
    for (CorrespondentiePrintRecord record : records) {
      if (record.getPrintRecord() != null) {
        printRecord(record.getPrintRecord(), isPreview, new DownloadHandlerImpl(getWindow()));
      }
    }

    List<PrintRecord> printRecords = toPrintRecords(records);

    if (printRecords.size() > 0) {

      if (isPreview) {
        getWindow().addWindow(new PrintPreviewWindow(printRecords));
      } else {
        getWindow().addWindow(new PrintSummaryWindow(printRecords));
      }

      // Gooi eerste exceptie op
      for (PrintRecord dr : printRecords) {
        if (dr.getException() != null) {
          throw dr.getException();
        }
      }

      // Update status
      if (!isPreview) {
        for (CorrespondentiePrintRecord dr : records) {
          if (form.getBean().getStatus()) {
            DocumentZaak uitt = dr.getUittrekselZaak();
            getServices().getDocumentZakenService().updateStatus(uitt, uitt.getStatus(),
                ZaakStatusType.VERWERKT, "");
          }
        }
      }

      table.init();
    }
  }

  @SuppressWarnings("unused")
  protected List<CorrespondentiePrintRecord> getSelectedRecords(boolean isPreview) {

    List<CorrespondentiePrintRecord> records = new ArrayList<>();
    records.addAll(table.getSelectedValues(CorrespondentiePrintRecord.class));

    if (records.isEmpty()) {
      throw new ProException(ProExceptionSeverity.WARNING, "Geen documenten geselecteerd.");
    }

    return records;
  }

  protected List<PrintRecord> toPrintRecords(List<CorrespondentiePrintRecord> objects) {

    List<PrintRecord> list = new ArrayList<>();

    for (CorrespondentiePrintRecord printRecord : objects) {
      if (printRecord.getPrintRecord() != null) {
        list.add(printRecord.getPrintRecord());
      }
    }

    return list;
  }

  private List<DocumentSoort> getBrieven(DocumentType... types) {
    DocumentService documenten = getServices().getDocumentService();
    return documenten.getDocumentSoorten(getApplication().getServices().getGebruiker(), types);
  }

  private void printRecord(final PrintRecord record, final boolean isPreview, final DownloadHandler downloadHandler) {

    final DocumentService service = getApplication().getServices().getDocumentService();
    record.setStatus(Status.DEFAULT);
    record.setException(null);

    try {

      ConditionalMap modelMap = new ConditionalMap();
      modelMap.put(record.getSoort().getType().getDoc(), record.getModel());

      PrintActie printActie = new PrintActie();
      printActie.setModel(modelMap);
      printActie.setDocument(record.getDocument());
      printActie.setZaak(record.getZaak());

      record.setPrintActie(printActie);

      if (isPreview) {

        PrintOptie printOptie = new PrintOptie();
        printOptie.setUitvoerformaatType(UitvoerformaatType.PDF); // Altijd als PDF
        printActie.setPrintOptie(printOptie);

        record.setPreviewArray(service.preview(printActie));
      } else {

        printActie.setPrintOptie((PrintOptie) record.getUitvoer().getValue());
        printActie.setZaak(record.getZaak());

        service.print(printActie, false, downloadHandler);
      }

      record.setStatus(Status.PRINTED);
    } catch (RuntimeException e) {

      record.setStatus(Status.ERROR);
      record.setException(e);
    }
  }

  private void reloadTree() {
    VaadinUtils.getChild(getWindow(), ZakenregisterAccordionTab.class).reloadTree();
  }

  private void wijzigStatus() {

    List<CorrespondentiePrintRecord> selectedValues = table.getSelectedValues(CorrespondentiePrintRecord.class);
    Zaak zaak = on(CorrespondentiePrintRecord.class).getUittrekselZaak();
    LambdaList<Zaak> zaken = with(selectedValues).extract(zaak);

    new ZaakStatusUpdater(getWindow(), zaken) {

      @Override
      protected void reload() {
        table.init();
        reloadTree();
      }
    };
  }

  class Table1 extends Page12ZakenTable {

    private List<DocumentZaak> zaken = new ArrayList<>();

    @Override
    public void onDoubleClick(Record record) {

      if (record.getObject() instanceof CorrespondentiePrintRecord) {

        CorrespondentiePrintRecord printrecord = (CorrespondentiePrintRecord) record.getObject();
        ZaakregisterNavigator.navigatoTo(printrecord.getUittrekselZaak(), Page12Zaken.this, false);
      }
    }

    public void reset() {
      zaken.clear();
      init();
    }

    @Override
    public void setRecords() {

      try {

        if (zaken.isEmpty()) {

          DocumentZaakArgumenten args = new DocumentZaakArgumenten();
          args.setStatussen(OPGENOMEN, INBEHANDELING);
          args.setDocumentTypes(VERHUIZING_AANGIFTE);

          ZakenService dbZaken = getServices().getZakenService();
          zaken = dbZaken.getStandaardZaken(getServices().getDocumentZakenService().getMinimalZaken(args));
        }

        for (Zaak zaak : zaken) {
          addPrintRecord(soorten, zaak);
        }
      } catch (Exception e) {
        getApplication().handleException(e);
      }

      super.setRecords();
    }
  }
}
