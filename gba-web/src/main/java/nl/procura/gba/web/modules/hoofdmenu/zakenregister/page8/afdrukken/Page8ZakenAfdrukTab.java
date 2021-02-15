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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.afdrukken;

import static java.util.Arrays.asList;
import static nl.procura.diensten.gba.ple.openoffice.DocumentPLConverter.convert;
import static nl.procura.diensten.gba.ple.openoffice.DocumentPLConverter.removeStillborns;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.PL_BEGELEIDENDE_BRIEF;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.PL_UITTREKSEL;

import java.util.List;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.components.layouts.form.document.DocumentContainer;
import nl.procura.gba.web.components.layouts.form.document.DocumentUitvoerContainer;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord.Status;
import nl.procura.gba.web.components.layouts.form.document.PrintSummaryWindow;
import nl.procura.gba.web.components.layouts.form.document.preview.PrintPreviewWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.Page8ZakenTab;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.Page8ZakenTabTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.tab3.Page8ZakenTab3Table;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.*;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerkType;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintActie;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptieType;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.functies.downloading.DownloadHandler;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public abstract class Page8ZakenAfdrukTab extends Page8ZakenTabTemplate<Zaak> {

  protected final Button             buttonPreview = new Button("Voorbeeld / e-mailen");
  protected final Button             buttonPrint   = new Button("Afdrukken (F3)");
  private final Page8ZakenAfdrukForm form          = new Page8ZakenAfdrukForm();

  public Page8ZakenAfdrukTab(String caption) {
    super(caption);
  }

  public Page8ZakenAfdrukForm getForm() {
    return form;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonPrint || keyCode == KeyCode.F3) {
      doPrint(false);
    } else if (button == buttonPreview) {
      doPrint(true);
    }

    super.handleEvent(button, keyCode);
  }

  /**
   * Print of preview acties
   */
  protected void doPrint(boolean isPreview) {

    List<PrintRecord> records = getPrintRecords(isPreview);

    for (PrintRecord record : records) {
      printRecord(record, isPreview, new DownloadHandlerImpl(getWindow()));
    }

    if (isPreview) {
      getWindow().addWindow(new PrintPreviewWindow(records));
    } else {
      getWindow().addWindow(new PrintSummaryWindow(records));
    }

    // Gooi eerste exceptie op
    for (PrintRecord dr : records) {
      if (dr.getException() != null) {
        throw dr.getException();
      }
    }

    // Update status
    if (!isPreview) {
      for (PrintRecord dr : records) {
        if (form.getStatus()) {
          DocumentZaak uitt = (DocumentZaak) dr.getZaak();
          getServices().getDocumentZakenService()
              .updateStatus(uitt, dr.getZaak().getStatus(), ZaakStatusType.VERWERKT, "");
        }
      }
    }

    getTable().init();
  }

  protected PrintActie getBegeleidingsBrief(PrintRecord printRecord) {

    form.commit();

    DocumentRecord document = form.getDocument();

    if (document != null) {

      Zaak zaak = printRecord.getZaak();

      if (zaak.getBasisPersoon() != null) {

        PrintOptie printOptie = new PrintOptie();
        printOptie.setUitvoerformaatType(UitvoerformaatType.PDF); // Altijd als PDF

        getServices().getPrintOptieService().getPrintOpties();

        PrintActie printActie = new PrintActie();
        printActie.setPrintOptie(printOptie);
        printActie.setDocument(document);
        printActie.setZaak(printRecord.getZaak());

        List<DocumentPL> dps = convert(asList(zaak.getBasisPersoon()), null);
        dps.stream().filter(dp -> !document.isStillbornAllowed()).forEach(dpl -> removeStillborns(dpl));

        ConditionalMap modelMap = new ConditionalMap();
        modelMap.put(DocumentType.PL_BEGELEIDENDE_BRIEF.getDoc(), dps);
        printActie.setModel(modelMap);
        return printActie;
      }
    }

    return null;
  }

  protected List<DocumentRecord> getByKenmerk(List<DocumentRecord> documenten, DocumentKenmerkType... types) {
    return getServices().getDocumentService().getDocumentenByKenmerk(documenten, types);
  }

  protected List<DocumentSoort> getBySoort(List<DocumentRecord> documenten) {
    return getServices().getDocumentService().getDocumentenBySoort(documenten);
  }

  protected List<DocumentRecord> getDocumenten(DocumentType... types) {
    return getServices().getDocumentService().getDocumenten(getApplication().getServices().getGebruiker(), types);
  }

  protected void toevoegenBegeleidendeBrieven(DocumentKenmerkType... types) {

    ProNativeSelect documentField = form.getDocumentVeld();
    List<DocumentSoort> documenten = getBySoort(getByKenmerk(getDocumenten(PL_BEGELEIDENDE_BRIEF), types));

    if (documenten.isEmpty()) {
      setInfo("Er zijn geen documenten gekoppeld van het type: " + DocumentType.PL_BEGELEIDENDE_BRIEF,
          "").setIcon(ProcuraTheme.ICOON_24.WARNING);
    }

    documentField.setDataSource(new DocumentContainer(getApplication(), documenten));
    setStandaardBegeleidendeBrief(documentField, documenten);
  }

  protected void toevoegenUitvoer(PrintOptieType type) {
    ProNativeSelect uitvoerField = form.getUitvoerVeld();
    uitvoerField.setContainerDataSource(new DocumentUitvoerContainer(getApplication(), type));
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

      printActie.addListener(() -> {

        PrintActie begPrintActie = getBegeleidingsBrief(record);

        if (begPrintActie != null) {

          PrintOptie begUitvoer = (PrintOptie) form.getUitvoerVeld().getValue();

          if (isPreview || begUitvoer == null) {
            return begPrintActie; // Toevoegen aan hoofddocument
          }

          begPrintActie.setPrintOptie(begUitvoer);

          // Als mijn overheid dan niet printen, maar doorgeven
          if (begPrintActie.getPrintOptie().getPrintType() == PrintOptieType.MIJN_OVERHEID) {
            return begPrintActie; // Toevoegen aan hoofddocument
          }

          // Afdrukken van begeleidende brief
          service.print(begPrintActie, false, downloadHandler);
        }

        return null;
      });

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

  private void setStandaardBegeleidendeBrief(ProNativeSelect documentField, List<DocumentSoort> brieven) {
    for (DocumentSoort soort : brieven) {
      for (DocumentRecord document : soort.getDocumenten()) {
        if (document.isStandaardDocument()) {
          documentField.setValue(document);
        }
      }
    }
  }

  class Table1 extends Page8ZakenTab3Table {

    @Override
    public void setRecords() {

      try {
        Page8ZakenTab parent = getParentTab();
        for (Zaak zaak : parent.getWel()) {
          addUittreksel(getDocumenten(PL_UITTREKSEL), zaak);
        }
      } catch (Exception e) {
        getApplication().handleException(e);
      }

      super.setRecords();
    }
  }
}
