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

package nl.procura.gba.web.modules.hoofdmenu.gv;

import static nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType.BG_JA;
import static nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType.BG_NEE;
import static nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType.VP_JA;
import static nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType.VP_NEE;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;

import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.web.components.layouts.form.document.PrintMultiLayoutConfig;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord.Status;
import nl.procura.gba.web.components.layouts.form.document.PrintSelectRecordFilter;
import nl.procura.gba.web.components.layouts.form.document.PrintSummaryWindow;
import nl.procura.gba.web.components.layouts.form.document.PrintTableTemplate;
import nl.procura.gba.web.components.layouts.form.document.preview.PrintPreviewWindow;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratie;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieSoortType;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.gba.web.services.zaken.documenten.DocumentSoort;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintActie;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.gba.web.services.zaken.gv.GvAanvraag;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.downloading.DownloadHandler;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;

public class PageGvTemplate extends NormalPageTemplate {

  protected final Button buttonPreview = new Button("Voorbeeld / e-mailen");
  protected final Button buttonPrint   = new Button("Afdrukken (F3)");

  private PrintTable     printTable = null;
  private DossierPersoon betrokkene = null;

  public PageGvTemplate(String caption) {
    super(caption);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPreview);
      addButton(buttonPrint);
      addButton(buttonReset);
    }

    super.event(event);
  }

  public DossierPersoon getBetrokkene() {
    return betrokkene;
  }

  public void setBetrokkene(DossierPersoon betrokkene) {
    this.betrokkene = betrokkene;
  }

  public PrintTable getPrintTable() {
    return printTable;
  }

  public void setPrintTable(PrintTable printTable) {
    this.printTable = printTable;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonPrint || button == buttonPreview || keyCode == KeyCode.F3) {
      if (!betrokkene.isVolledig()) {
        throw new ProException(WARNING, "Selecteer een persoon");
      }

      validatePrint(button == buttonPreview);
    }

    super.handleEvent(button, keyCode);
  }

  protected void checkPrintTable(KoppelEnumeratieType grondslag, KoppelEnumeratieType toekenning,
      KoppelEnumeratieType procesActie, KoppelEnumeratieType reactie, boolean binnengem,
      boolean verstrekkingsbep) {

    // Als toekenning JA is dan nu verstrekken
    if (KoppelEnumeratieType.TK_JA.is(toekenning)) {
      procesActie = KoppelEnumeratieType.PA_NU_VERSTREKKEN;
    }

    KoppelEnumeratieType bg = binnengem ? BG_JA : BG_NEE;
    KoppelEnumeratieType vp = verstrekkingsbep ? VP_JA : VP_NEE;

    //      System.out.println ("\n");
    //      System.out.println ("Grondslag  : " + grondslag);
    //      System.out.println ("Toekkening : " + toekenning);
    //      System.out.println ("ProcesActie: " + procesActie);
    //      System.out.println ("Reactie    : " + reactie);
    //      System.out.println ("Binnengemeentelijk: " + bg);
    //      System.out.println ("Verstrekkingsbep. : " + vp);

    printTable.updateSoort(
        Collections.singletonList(getDocumentSoort(grondslag, toekenning, procesActie, reactie, bg, vp)));
  }

  protected void doPrint(GvAanvraag aanvraag, boolean isPreview) {

    List<PrintRecord> records = getPrintRecords(aanvraag, isPreview);

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
  protected List<PrintRecord> getPrintRecords(GvAanvraag aanvraag, boolean isPreview) {

    List<PrintRecord> records = new ArrayList<>();

    if (isTabelMode()) {
      records.addAll(printTable.getSelectedValues(PrintRecord.class));
    }

    if (records.isEmpty()) {
      throw new ProException(ProExceptionSeverity.WARNING, "Geen documenten geselecteerd.");
    }

    for (PrintRecord record : records) {
      record.setModel(aanvraag);
      record.setZaak(aanvraag);
    }

    return records;
  }

  protected boolean isEigenGemeente() {
    if (getBetrokkene() != null && getBetrokkene().isVolledig()) {
      return getApplication().getServices().getGebruiker().isGemeente(
          getBetrokkene().getWoongemeente().getLongValue());
    }
    return false;
  }

  protected boolean isVerstrekkingsBeperking() {

    if (getBetrokkene() != null && getBetrokkene().isVolledig()) {
      return getBetrokkene().isVerstrekkingsbeperking();
    }
    return false;
  }

  @SuppressWarnings("unused")
  protected void validatePrint(boolean isPreview) {
  }

  private DocumentSoort getDocumentSoort(KoppelEnumeratieType... types) {

    DocumentService documenten = getServices().getDocumentService();
    List<DocumentSoort> soorten = documenten.getDocumentSoorten(getApplication().getServices().getGebruiker(),
        DocumentType.GV_AANVRAAG);
    DocumentSoort soort = new DocumentSoort(DocumentType.GV_AANVRAAG);

    for (DocumentSoort ds : soorten) {
      for (DocumentRecord d : ds.getDocumenten()) {
        if (isTypes(d, types)) {
          soort.getDocumenten().add(d);
        }
      }
    }
    return soort;
  }

  private boolean isTabelMode() {
    return true;
  }

  private boolean isType(DocumentRecord d, Entry<KoppelEnumeratieSoortType, KoppelEnumeratieType> entry) {

    if (entry == null) {
      return false;
    }

    for (KoppelEnumeratie gs : d.getKoppelElementen().getAlle()) {
      if (gs.getType().getSoortType() == entry.getKey()) {
        if (gs.getType().isNvt() || (gs.getType() == entry.getValue())) {
          return true;
        }
      }
    }

    return false;
  }

  private boolean isTypes(DocumentRecord d, KoppelEnumeratieType[] types) {

    Map<KoppelEnumeratieSoortType, KoppelEnumeratieType> map = new HashMap<>();
    map.put(KoppelEnumeratieSoortType.GRONDSLAG, types[0]);
    map.put(KoppelEnumeratieSoortType.TOEKENNING, types[1]);
    map.put(KoppelEnumeratieSoortType.PROCESACTIE, types[2]);
    map.put(KoppelEnumeratieSoortType.REACTIE, types[3]);
    map.put(KoppelEnumeratieSoortType.BINNENGEM, types[4]);
    map.put(KoppelEnumeratieSoortType.VERSTREK_BEP, types[5]);

    for (Entry<KoppelEnumeratieSoortType, KoppelEnumeratieType> e : map.entrySet()) {
      if (!isType(d, e)) {
        return false;
      }
    }

    return true;
  }

  private void printRecord(PrintRecord record, boolean isPreview, DownloadHandler downloadHandler) {

    DocumentService service = getApplication().getServices().getDocumentService();
    record.setStatus(Status.DEFAULT);
    record.setException(null);

    try {

      // Laad de basispersoonslijst
      GvAanvraag aanvraag = (GvAanvraag) record.getZaak();
      getServices().getZakenService().getService(aanvraag).setVolledigeZaakExtra(aanvraag);
      BsPersoonUtils.kopieDossierPersoon(aanvraag.getBasisPersoon(), getBetrokkene());

      ConditionalMap modelMap = new ConditionalMap();
      modelMap.put(record.getSoort().getType().getDoc(), record.getModel());
      modelMap.put(DocumentType.PL_UITTREKSEL.getDoc(), Collections.singletonList(aanvraag.getPersoon()));

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

        service.print(printActie, !isTabelMode(), downloadHandler);
      }

      record.setStatus(Status.PRINTED);
    } catch (RuntimeException e) {

      record.setStatus(Status.ERROR);
      record.setException(e);
    }
  }

  public class PrintTable extends PrintTableTemplate {

    public PrintTable(PrintSelectRecordFilter selectListener) {
      super(PrintMultiLayoutConfig.builder()
          .selectRecordFilter(selectListener)
          .build());
    }

    public void reset() {
      updateSoort(new ArrayList<>());
    }
  }
}
