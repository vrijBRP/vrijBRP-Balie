package nl.procura.gba.web.modules.beheer.fileimport.types.registrant;

import static com.vaadin.ui.Label.CONTENT_XHTML;
import static java.util.Optional.ofNullable;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.ACHTERNAAM;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.DATUM_INGANG_BEWONING;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.EMAIL;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.GEBOORTEDATUM;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.GEBOORTELAND;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.GEBOORTEPLAATS;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.GESLACHT;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.HUISLETTER;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.HUISNUMMER;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.LAND_VAN_VORIG_ADRES;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.NATIONALITEIT;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.OPMERKINGEN;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.POSTCODE;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.REFERENTIENUMMER;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.STRAATNAAM;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.TELEFOON;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.TOEVOEGING;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.VOORNAMEN;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.VOORVOEGSEL;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.WOONPLAATS;

import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportLayout;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportListener;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTableListener;
import nl.procura.gba.web.modules.zaken.document.DocumentenTabel;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.fileimport.FileImportRecord;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSResult;
import nl.procura.gba.web.services.zaken.documenten.BestandType;
import nl.procura.gba.web.windows.home.HomeWindow;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;
import nl.procura.vaadin.theme.ProcuraWindow;
import nl.procura.vaadin.theme.twee.layout.ScrollLayout;

public class RegistrantImportLayout extends FileImportLayout {

  private final FileImportRecord importRecord;
  private FileImportListener     importListener;
  private boolean                nieuweZaak;

  public RegistrantImportLayout(FileImportRecord importRecord, FileImportTableListener selection) {
    this.importRecord = importRecord;
    if (selection != null) {
      this.nieuweZaak = selection.isNewRegistration();
      this.importListener = selection.getListener();
    }
    setHeight("600px");
  }

  @Override
  public void attach() {
    if (getComponentCount() == 0) {
      TableLayout layout = new TableLayout();
      layout.setColumnWidths("120px", "300px", "160px", "");

      add(layout, REFERENTIENUMMER).setColspan(3);
      add(layout, EMAIL);
      add(layout, TELEFOON);

      add(layout, ACHTERNAAM);
      add(layout, STRAATNAAM);

      add(layout, VOORVOEGSEL);
      add(layout, HUISNUMMER);

      add(layout, VOORNAMEN);
      add(layout, HUISLETTER);

      add(layout, GESLACHT);
      add(layout, TOEVOEGING);

      add(layout, GEBOORTEDATUM, RegistrantImportLayout::toDate);
      add(layout, POSTCODE);

      add(layout, GEBOORTEPLAATS);
      add(layout, WOONPLAATS);

      add(layout, GEBOORTELAND);
      add(layout, NATIONALITEIT);

      add(layout, LAND_VAN_VORIG_ADRES);
      add(layout, DATUM_INGANG_BEWONING, RegistrantImportLayout::toDate);

      add(layout, OPMERKINGEN).setColspan(3);

      DMSResult result = getApplication().getServices()
          .getDmsService()
          .getDocumentsById(importRecord.getUuid());

      // Button Layout
      HLayout hLayout = new HLayout();
      if (importListener != null) {
        if (this.nieuweZaak) {
          hLayout.add(new Button("Nieuwe zaak aanmaken",
              event -> check(() -> importListener.onSelect(importRecord))));

        } else {
          hLayout.add(new Button("Aanmelding selecteren",
              event -> check(() -> importListener.onSelect(importRecord))));
        }
      }

      hLayout.addExpandComponent(new Label()).add(getCloseButton());
      addComponent(hLayout);
      addExpandComponent(new ScrollLayout(layout));

      // Errors
      if (StringUtils.isNotBlank(importRecord.getRemarksAsString())) {
        TableLayout errorLayout = new TableLayout();
        errorLayout.addData(new Label(setClass(false, importRecord.getRemarksAsString()), CONTENT_XHTML));
        add(new Fieldset("Foutmeldingen", errorLayout));
      }

      // Attachments
      add(new Fieldset("Bijlages"));
      addComponent(new DocumentTable(result));
      addExpandComponent(new Fieldset("Zaken", new ZaakTable()));
    }
    super.attach();
  }

  private void check(Runnable runnable) {
    if (getApplication().getServices().getFileImportService().getZaken(importRecord.getId()).isEmpty()) {
      runnable.run();
    } else {
      getApplication().getParentWindow().addWindow(getConfirmDialog(runnable));
    }
  }

  private ConfirmDialog getConfirmDialog(Runnable runnable) {
    return new ConfirmDialog("Dubbele selectie",
        "Voor deze aanmelding is al een zaak aangemaakt."
            + "</br>Wilt u toch een nieuwe zaak aanmaken?",
        "400px", "150px") {

      @Override
      public void buttonYes() {
        runnable.run();
        super.buttonYes();
      }
    };
  }

  private Button getCloseButton() {
    ClickListener clickListener = event -> ((ModalWindow) getWindow()).closeWindow();
    Button button = new Button("Sluiten (Esc)", clickListener);
    button.focus();
    return button;
  }

  private Column add(TableLayout layout, String key, Function<String, String> markupFunction) {
    layout.addLabel(key);
    return layout.addData(new Label(ofNullable(importRecord.get(key))
        .map(r -> r.isValid()
            ? markupFunction.apply(r.getValue())
            : (r.getValue() + setClass(false, " (ongeldige waarde)")))
        .orElse(""), CONTENT_XHTML));
  }

  private Column add(TableLayout layout, String key) {
    return add(layout, key, s -> s);
  }

  private static class DocumentTable extends DocumentenTabel {

    private final DMSResult result;

    public DocumentTable(DMSResult result) {
      this.result = result;
    }

    @Override
    public void setColumns() {
      setClickable(true);
      addColumn("Nr", 50);
      addColumn("Type", 30).setClassType(Embedded.class);
      addColumn("Datum/tijd", 140);
      addColumn("Document");
      addColumn("Grootte", 120);
    }

    @Override
    public void setRecords() {
      DMSResult dmsResult = getOpgeslagenBestanden();
      List<DMSDocument> records = dmsResult.getDocuments();
      int index = records.size();

      for (DMSDocument record : records) {
        Record r = addRecord(record);
        r.addValue(index);
        r.addValue(TableImage.getByBestandType(BestandType.getType(record.getContent().getExtension())));
        r.addValue(new DateTime(record.getDate(), record.getTime()));
        r.addValue(record.getTitle());
        r.addValue(FileUtils.byteCountToDisplaySize(record.getContent().getSize()));
        index--;
      }
    }

    @Override
    public DMSResult getOpgeslagenBestanden() {
      return result;
    }
  }

  private static String toDate(String date) {
    ProcuraDate procuraDate = new ProcuraDate(date);
    procuraDate.setAllowedFormatExceptions(true);
    procuraDate.setForceFormatType(ProcuraDate.SYSTEMDATE_ONLY);
    return procuraDate.isCorrect() ? procuraDate.getFormatDate() : date;
  }

  private class ZaakTable extends GbaTable {

    public ZaakTable() {
    }

    @Override
    public void setColumns() {
      setClickable(true);
      addColumn("Nr", 50);
      addColumn("Zaaktype", 250);
      addColumn("Omschrijving");
      addColumn("Gebruiker", 170).setCollapsed(true);
      addColumn("Status", 120).setUseHTML(true);
    }

    @Override
    public void onDoubleClick(Record record) {
      getParentWindow().addWindow(new ConfirmDialog("Wilt u naar de eerste inschrijving?") {

        @Override
        public void buttonYes() {
          openInschrijving(record.getObject(Zaak.class));
          super.buttonYes();
        }
      });
      super.onDoubleClick(record);
    }

    private void openInschrijving(Zaak zaak) {
      getApplication().getServices().getMemoryService().setObject(Dossier.class, zaak);
      getApplication().openWindow(getParentWindow(), new HomeWindow(), "bs.registratie");
      ((ProcuraWindow) getWindow()).closeWindow();
    }

    @Override
    public void setRecords() {
      Services services = getApplication().getServices();
      List<String> zaakIds = services.getFileImportService().getZaken(importRecord.getId());
      int index = 0;
      if (!zaakIds.isEmpty()) {
        ZaakArgumenten zaakargs = new ZaakArgumenten();
        zaakargs.setZaakIds(new HashSet<>(zaakIds));
        for (Zaak zaak : services.getZakenService().getStandaardZaken(zaakargs)) {
          index++;
          Record r = addRecord(zaak);
          r.addValue(index);
          r.addValue(zaak.getType().getOms());
          r.addValue(ZaakUtils.getOmschrijving(zaak));
          r.addValue(ZaakUtils.getIngevoerdDoorGebruiker(zaak));
          r.addValue(zaak.getStatus().getOms());
        }
      }
    }
  }
}
