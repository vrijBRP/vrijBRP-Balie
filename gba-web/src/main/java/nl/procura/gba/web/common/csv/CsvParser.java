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

package nl.procura.gba.web.common.csv;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static org.apache.commons.lang3.StringUtils.capitalize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;

import nl.procura.standard.exceptions.ProException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class CsvParser {

  private static final String UNKNOWN_HEADER = "Kolom '%s' hoort in niet het bestand";
  private static final String MISSING_HEADER = "Kolom '%s' ontbreekt in het bestand";

  public static Csv parse(CsvConfig config) {
    Csv csv = new Csv();
    csv.headers.addAll(config.getHeaders());
    CSVReader reader = null;
    try {
      // Account for the byte order mark
      BOMInputStream bomInputStream = new BOMInputStream(new ByteArrayInputStream(config.getContent()));
      InputStreamReader fileReader = new InputStreamReader(bomInputStream, UTF_8);
      reader = new CSVReader(fileReader, ';');
      int lineIndex = 1;
      CsvHeaders lineHeaders = new CsvHeaders();
      for (String[] line : reader.readAll()) {
        if (lineIndex == 1) {
          toHeaders(csv, lineHeaders, line);
        } else {
          csv.lines.add(toCsvLine(csv, lineHeaders, line));
        }
        lineIndex++;
      }
    } catch (IOException | RuntimeException e) {
      e.printStackTrace();
      throw new ProException(ERROR, "Fout bij inlezen CSV: " + e.getMessage());
    } finally {
      IOUtils.closeQuietly(reader);
    }
    return csv;
  }

  public static byte[] export(Csv csv) {
    ByteArrayOutputStream csvOs = new ByteArrayOutputStream();
    try (OutputStreamWriter writer = new OutputStreamWriter(csvOs, UTF_8)) {
      CSVWriter csvWriter = new CSVWriter(writer, ';');
      try {
        writeHeaders(csvWriter, csv);
        csv.getLines().forEach(line -> writeValues(csvWriter, line));
        csvWriter.flush();
      } finally {
        IOUtils.closeQuietly(csvWriter);
      }
    } catch (IOException e) {
      throw new ProException("Fout bij maken spreadsheet", e);
    }
    return csvOs.toByteArray();
  }

  private static void writeValues(CSVWriter csvWriter, CsvLine line) {
    List<String> values = line.getValues()
        .stream()
        .map(CsvValue::getOriginalValue)
        .collect(Collectors.toList());
    values.add(StringUtils.join(line.getRemarks(), ", "));
    csvWriter.writeNext(values.toArray(new String[0]));
  }

  private static void writeHeaders(CSVWriter csvWriter, Csv csv) {
    List<String> headers = csv.getLines().stream()
        .findFirst()
        .map(line -> line.getValues().stream().map(v -> v.getHeader().getName())
            .collect(Collectors.toList()))
        .orElse(new ArrayList<>());
    headers.add("Opmerkingen");
    csvWriter.writeNext(headers.toArray(new String[0]));
  }

  @Getter
  public static class Csv {

    private final CsvHeaders    headers = new CsvHeaders();
    private final List<CsvLine> lines   = new ArrayList<>();
    private final List<String>  remarks = new ArrayList<>();

    public boolean isValid() {
      return remarks.isEmpty();
    }
  }

  public static class CsvHeaders extends ArrayList<CsvHeader> {

    public CsvHeader getByName(String header) {
      return findByName(header)
          .orElseThrow(() -> new ProException(ERROR, format(MISSING_HEADER,
              CsvParser.cleanHeader(header))));
    }

    public Optional<CsvHeader> findByName(String header) {
      return stream()
          .filter(h -> h.isName(header))
          .findFirst();
    }
  }

  @Getter
  public static class CsvLine {

    private final List<CsvValue> values  = new ArrayList<>();
    private final List<String>   remarks = new ArrayList<>();

    public CsvValue getValue(String header) {
      return values.stream()
          .filter(column -> column.header.isName(header))
          .findFirst()
          .orElseThrow(() -> new ProException(ERROR, format(MISSING_HEADER,
              CsvParser.cleanHeader(header))));
    }

    public boolean isValid() {
      return remarks.isEmpty();
    }
  }

  @Getter
  @AllArgsConstructor
  public static class CsvValue {

    private CsvHeader header;
    private String    originalValue;
    private String    value;
    private String    remark;

    public CsvValue(CsvHeader header, String value) {
      this.header = header;
      this.originalValue = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  private static void toHeaders(Csv csv, CsvHeaders lineHeaders, String[] line) {
    // Check unknown headers
    for (String header : line) {
      lineHeaders.add(CsvHeader.builder().name(header).build());
      if (!csv.headers.findByName(header).isPresent()) {
        csv.remarks.add(format(UNKNOWN_HEADER, cleanHeader(header)));
      }
    }

    // Check missing headers
    csv.headers.forEach(csvHeader -> {
      Optional<CsvHeader> header = lineHeaders.findByName(csvHeader.getName());
      if (!header.isPresent()) {
        csv.remarks.add(format(MISSING_HEADER, cleanHeader(csvHeader.getName())));
      }
    });
  }

  private static CsvLine toCsvLine(Csv csv, CsvHeaders lineHeaders, String[] line) {
    CsvLine csvLine = new CsvLine();
    int colIndex = 0;
    for (String linevalue : line) {
      String header = lineHeaders.get(colIndex).getName();
      Optional<CsvHeader> csvHeader = csv.headers.findByName(header);
      if (csvHeader.isPresent()) {
        CsvValue value = new CsvValue(csvHeader.get(), linevalue.trim());
        csvLine.values.add(value);
        checkValue(csvLine, value);
      }
      colIndex++;
    }
    return csvLine;
  }

  private static void checkValue(CsvLine csvLine, CsvValue value) {
    try {
      List<Function<String, String>> converters = value.header.getConverters();
      value.value = value.getOriginalValue();
      if (converters != null) {
        converters.forEach(converter -> value.value = converter.apply(value.getValue()));
      }
    } catch (RuntimeException e) {
      value.remark = e.getMessage();
      csvLine.remarks.add(e.getMessage());
    }
  }

  public static String cleanHeader(String header) {
    return capitalize(header.trim());
  }
}
