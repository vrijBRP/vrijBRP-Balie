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

package nl.procura.gba.web.modules.beheer.fileimport.types;

import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.time.LocalDate;
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

import nl.procura.gba.web.common.csv.CsvConfig;
import nl.procura.gba.web.common.csv.CsvParser;
import nl.procura.gba.web.common.csv.CsvParser.Csv;
import nl.procura.gba.web.common.csv.CsvParser.CsvLine;
import nl.procura.gba.web.common.csv.CsvParser.CsvValue;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.fileimport.FileImportRecord;
import nl.procura.gba.web.services.beheer.fileimport.FileImportResult;
import nl.procura.gba.web.services.beheer.fileimport.FileImportValue;
import nl.procura.standard.exceptions.ProException;

public class RegistrantImporter extends FileImporter {

  public static final String ACHTERNAAM     = "Achternaam";
  public static final String VOORVOEGSEL    = "Voorvoegsel";
  public static final String VOORNAMEN      = "Voornamen";
  public static final String GESLACHT       = "Geslacht";
  public static final String GEBOORTEDATUM  = "Geboortedatum";
  public static final String GEBOORTEPLAATS = "Geboorteplaats";
  public static final String GEBOORTELAND   = "Geboorteland";
  public static final String NATIONALITEIT  = "Nationaliteit";
  public static final String STRAATNAAM     = "Straatnaam";
  public static final String HUISNUMMER     = "Huisnummer";
  public static final String HUISLETTER     = "Huisletter";
  public static final String TOEVOEGING     = "Toevoeging";
  public static final String POSTCODE       = "Postcode";
  public static final String WOONPLAATS     = "Woonplaats";
  public static final String EMAIL          = "E-mail";

  private FileImportResult result;

  @Override
  public FileImportResult convert(byte[] content, Services services) {
    Csv csv = CsvParser.parse(CsvConfig.builder()
        .content(content)
        .converter(new OnbekendConverter())
        .header(ACHTERNAAM).and()
        .header(VOORVOEGSEL).and()
        .header(VOORNAMEN).and()
        .header(GESLACHT).converter(new GeslachtConverter()).and()
        .header(GEBOORTEDATUM).converter(new GeboortedatumConverter()).and()
        .header(GEBOORTEPLAATS).and()
        .header(GEBOORTELAND).and()
        .header(STRAATNAAM).and()
        .header(HUISNUMMER).and()
        .header(HUISLETTER).and()
        .header(TOEVOEGING).and()
        .header(POSTCODE).and()
        .header(WOONPLAATS).and()
        .header(NATIONALITEIT).and()
        .header(EMAIL)
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
    for (CsvLine csvLine : csv.getLines()) {
      FileImportRecord fileImportRecord = new FileImportRecord();
      Map<String, FileImportValue> values = new LinkedHashMap<>();
      for (CsvValue csvValue : csvLine.getValues()) {
        FileImportValue value = new FileImportValue(csvValue.getValue(), csvValue.getRemark());
        values.put(csvValue.getHeader().getName(), value);
      }
      fileImportRecord.setValues(values);
      list.add(fileImportRecord);
    }
    return list;
  }

  @Override
  public FileImportResult getResult() {
    return result;
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
      return "";
    }
  }

  /**
   * Converts various date formats to the accepted one
   */
  private static class GeboortedatumConverter implements Function<String, String> {

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
