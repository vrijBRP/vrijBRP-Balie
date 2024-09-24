package nl.procura.gba.web.modules.beheer.fileimport.types.registrant;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.ACHTERNAAM;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.DATUM_INGANG_BEWONING;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.GEBOORTEDATUM;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.HUISLETTER;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.HUISNUMMER;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.POSTCODE;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.STRAATNAAM;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.TOEVOEGING;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.VOORNAMEN;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.VOORVOEGSEL;
import static nl.procura.gba.web.modules.beheer.fileimport.types.registrant.RegistrantImport.WOONPLAATS;
import static org.apache.commons.lang3.builder.CompareToBuilder.reflectionCompare;
import static org.apache.commons.lang3.math.NumberUtils.toLong;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import nl.procura.commons.misc.formats.adres.Adres;
import nl.procura.commons.misc.formats.naam.NaamBuilder;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportDataWindow;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTable;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTableListener;
import nl.procura.gba.web.services.beheer.fileimport.FileImportRecord;
import nl.procura.standard.ProcuraDate;

public class RegistrantImportTable extends FileImportTable {

  private final List<FileImportRecord>  records = new ArrayList<>();
  private final FileImportTableListener importHandler;

  public RegistrantImportTable(FileImportTableListener importHandler) {
    this.importHandler = importHandler;
    setClickable(true);
  }

  @Override
  public void setColumns() {
    addColumn("Zaak aangemaakt", 120).setUseHTML(true);
    addColumn("Datum bewoning", 120);
    addColumn("Naam", 250);
    addColumn("Geboortedatum", 120);
    addColumn("Adres");
    super.setColumns();
  }

  @Override
  public void onDoubleClick(Record record) {
    FileImportRecord importRecord = record.getObject(FileImportRecord.class);
    RegistrantImportLayout layout = new RegistrantImportLayout(importRecord, importHandler);
    getApplication().getParentWindow().addWindow(new FileImportDataWindow(layout));
  }

  @Override
  public void setRecords() {
    records.sort(getFileImportRecordComparator());
    for (FileImportRecord fir : records) {
      Record record = addRecord(fir);
      record.addValue(fir.isReference() ? setClass(true, "Ja") : setClass(false, "Nee"));
      record.addValue(toDate(fir.getValue(DATUM_INGANG_BEWONING)));
      record.addValue(fir.getValue(VOORNAMEN) + " " + new NaamBuilder()
          .setVoorvoegsel(fir.getValue(VOORVOEGSEL))
          .setGeslachtsnaam(fir.getValue(ACHTERNAAM))
          .createNaam()
          .getNaamNaarNaamgebruik(false));
      record.addValue(toDate(fir.getValue(GEBOORTEDATUM)));
      record.addValue(new Adres()
          .setStraat(fir.getValue(STRAATNAAM))
          .setHuisnummer(toLong(fir.getValue(HUISNUMMER), -1L))
          .setHuisletter(fir.getValue(HUISLETTER))
          .setToevoeging(fir.getValue(TOEVOEGING))
          .setPostcode(fir.getValue(POSTCODE))
          .setWoonplaats(fir.getValue(WOONPLAATS))
          .getAdresPcWpl());
    }

    super.setRecords();
  }

  @Override
  public void update(List<FileImportRecord> records) {
    this.records.clear();
    this.records.addAll(records);
    init();
  }

  private Comparator<FileImportRecord> getFileImportRecordComparator() {
    return (o1, o2) -> {
      int sort1 = NumberUtils.toInt(o1.getValue(DATUM_INGANG_BEWONING), 0);
      int sort2 = NumberUtils.toInt(o2.getValue(DATUM_INGANG_BEWONING), 0);
      return reflectionCompare(sort1, sort2);
    };
  }

  private String toDate(String date) {
    ProcuraDate procuraDate = new ProcuraDate(date);
    procuraDate.setAllowedFormatExceptions(true);
    procuraDate.setForceFormatType(ProcuraDate.SYSTEMDATE_ONLY);
    return procuraDate.isCorrect() ? procuraDate.getFormatDate() : date;
  }
}
