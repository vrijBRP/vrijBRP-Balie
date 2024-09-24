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

package nl.procura.gba.web.modules.beheer.fileimport.types.registrant;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.validator.EmailValidator;

import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.web.common.csv.Csv;
import nl.procura.gba.web.common.csv.CsvConfig;
import nl.procura.gba.web.common.csv.CsvParser;
import nl.procura.gba.web.common.csv.CsvRow;
import nl.procura.gba.web.common.csv.CsvValue;
import nl.procura.gba.web.modules.beheer.fileimport.types.AbstractFileImport;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportLayout;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTable;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTableFilter;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTableListener;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.fileimport.FileImportRecord;
import nl.procura.gba.web.services.beheer.fileimport.FileImportResult;
import nl.procura.gba.web.services.beheer.fileimport.FileImportValue;
import nl.procura.standard.ProcuraDate;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.validator.PostcodeValidator;

public class RegistrantImport extends AbstractFileImport {

  public static final String DATUMTIJD_INVOER      = "Datum/tijd invoer";
  public static final String ACHTERNAAM            = "Achternaam";
  public static final String VOORVOEGSEL           = "Voorvoegsel";
  public static final String VOORNAMEN             = "Voornamen";
  public static final String GESLACHT              = "Geslacht";
  public static final String GEBOORTEDATUM         = "Geboortedatum";
  public static final String GEBOORTEPLAATS        = "Geboorteplaats";
  public static final String GEBOORTELAND          = "Geboorteland";
  public static final String NATIONALITEIT         = "Nationaliteit";
  public static final String STRAATNAAM            = "Straatnaam";
  public static final String HUISNUMMER            = "Huisnummer";
  public static final String HUISLETTER            = "Huisletter";
  public static final String TOEVOEGING            = "Toevoeging";
  public static final String POSTCODE              = "Postcode";
  public static final String WOONPLAATS            = "Woonplaats";
  public static final String EMAIL                 = "E-mail";
  public static final String TELEFOON              = "Telefoon";
  public static final String REFERENTIENUMMER      = "Referentienummer";
  public static final String LAND_VAN_VORIG_ADRES  = "Land van vorig adres";
  public static final String DATUM_INGANG_BEWONING = "Datum ingang bewoning";
  public static final String OPMERKINGEN           = "Opmerkingen";

  private FileImportResult result;

  @Override
  public FileImportResult convert(byte[] content, Services services) {
    Csv csv = CsvParser.parse(CsvConfig.builder()
        .content(content)
        .converter(new OnbekendConverter())
        .header(ACHTERNAAM).and()
        .header(ACHTERNAAM).and()
        .header(VOORVOEGSEL).and()
        .header(VOORNAMEN).and()
        .header(GESLACHT).converter(new GeslachtConverter()).and()
        .header(GEBOORTEDATUM).converter(new DatumConverter()).and()
        .header(GEBOORTEPLAATS).and()
        .header(GEBOORTELAND).and()
        .header(STRAATNAAM).and()
        .header(HUISNUMMER).and()
        .header(HUISLETTER).and()
        .header(TOEVOEGING).and()
        .header(POSTCODE).converter(new PostcodeConverter()).and()
        .header(WOONPLAATS).and()
        .header(NATIONALITEIT).and()
        .header(EMAIL).converter(new EmailConverter()).and()
        .header(TELEFOON).and()
        .header(REFERENTIENUMMER).and()
        .header(LAND_VAN_VORIG_ADRES).and()
        .header(DATUM_INGANG_BEWONING).converter(new DatumConverter()).and()
        .header(OPMERKINGEN)
        .and()
        .build());

    result = new FileImportResult();
    result.setRecords(toRecords(csv));
    result.setValid(csv.isValid());
    result.setRemarks(getRemarks(csv));
    return result;
  }

  private List<FileImportRecord> toRecords(Csv csv) {
    final List<FileImportRecord> list = new ArrayList<>();
    for (CsvRow csvLine : csv.getLines()) {
      FileImportRecord fileImportRecord = new FileImportRecord();
      Map<String, FileImportValue> values = new LinkedHashMap<>();
      values.put(DATUMTIJD_INVOER, new FileImportValue(LocalDateTime.now().format(ISO_LOCAL_DATE_TIME), null));
      for (CsvValue csvValue : csvLine.getValues()) {
        FileImportValue value = new FileImportValue(csvValue.getValue(), csvValue.getRemark());
        values.put(csvValue.getHeader().getName(), value);
      }
      fileImportRecord.setValues(values);
      fileImportRecord.setRemarks(csvLine.getRemarks());
      list.add(fileImportRecord);
    }
    return list;
  }

  @Override
  public FileImportResult getResult() {
    return result;
  }

  @Override
  public FileImportLayout createLayout(FileImportRecord record, FileImportTableListener importHandler) {
    return new RegistrantImportLayout(record, importHandler);
  }

  @Override
  public FileImportTable createTable(FileImportTableListener importHandler) {
    return new RegistrantImportTable(importHandler);
  }

  @Override
  public FileImportTableFilter createFilter(FileImport fileImport, FileImportTable table) {
    return new RegistrantImportTableFilter(fileImport, table);
  }

  private List<String> getRemarks(Csv csv) {
    List<String> remarks = csv.getRemarks();
    long lineRemarks = csv.getLines().stream().filter(csvLine -> !csvLine.isValid()).count();
    if (lineRemarks > 0) {
      remarks.add(String.format("%d van de %d regels bevatten fouten", lineRemarks, csv.getLines().size()));
    }
    return remarks;
  }

  /**
   * Converts 'Onbekend' to empty
   */
  private static class OnbekendConverter implements Function<String, String> {

    @Override
    public String apply(String value) {
      return "Onbekend".equalsIgnoreCase(value) ? "" : value;
    }
  }

  private static class EmailConverter implements Function<String, String> {

    @Override
    public String apply(String value) {
      if (StringUtils.isNotBlank(value)) {
        if (!new EmailValidator("").isValid(value)) {
          throw new ProException(ERROR, "E-mailadres is niet geldig");
        }
      }
      return value;
    }
  }

  private static class PostcodeConverter implements Function<String, String> {

    @Override
    public String apply(String value) {
      if (StringUtils.isNotBlank(value)) {
        if (!new PostcodeValidator().isValid(value)) {
          throw new ProException(ERROR, "Postcode is niet geldig");
        }
      }
      return value;
    }
  }

  /**
   * Converts various gender formats
   */
  private static class GeslachtConverter implements Function<String, String> {

    private static final Map<String, String> values = new HashMap<>();
    static {
      values.put("Man", "M");
      values.put("M", "M");
      values.put("Vrouw", "V");
      values.put("V", "V");
      values.put("Onbekend", "O");
      values.put("O", "O");
    }

    @Override
    public String apply(String value) {
      if (StringUtils.isNotBlank(value)) {
        return values.entrySet().stream()
            .filter(entry -> entry.getKey().equalsIgnoreCase(value))
            .map(Entry::getValue)
            .findFirst().orElseThrow(() -> new ProException(ERROR, "De geslachtaanduiding is niet geldig"));
      }
      return value;
    }
  }

  private static class DatumConverter implements Function<String, String> {

    public static final String[] patterns = { "dd-MM-yyyy", "d-MM-yyyy", "dd-M-yyyy", "d-M-yyyy", "yyyyMMdd" };

    @Override
    public String apply(String value) {
      if (StringUtils.isNotBlank(value)) {
        for (String pattern : patterns) {
          try {
            String replaceNonDigits = value.replaceAll("\\D", "-");
            LocalDate date = LocalDate.parse(replaceNonDigits, DateTimeFormatter.ofPattern(pattern));
            return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
          } catch (DateTimeParseException e) {
            // Ignore
          }
        }
        throw new ProException(ERROR, "De geboortedatum heeft geen geldig formaat");
      }
      return value;
    }
  }
}
