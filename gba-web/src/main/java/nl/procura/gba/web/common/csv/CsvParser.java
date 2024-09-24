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
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
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

import nl.procura.commons.core.exceptions.ProException;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import lombok.Data;

@Data
public class CsvParser {

  public static final String UNKNOWN_HEADER = "Veld '%s' is onbekend";
  public static final String MISSING_HEADER = "Veld '%s' ontbreekt";

  public static Csv parse(CsvConfig config) {
    Csv csv = new Csv();
    csv.getHeaders().addAll(config.getHeaders());
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
          csv.getLines().add(toCsvLine(csv, lineHeaders, line));
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
      // Specify the BOM (Byte order Mask) for UTF-8 so that Excel can identify the encoding
      // and open it in the correct format
      csvOs.write(new byte[]{ (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
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

  private static void writeValues(CSVWriter csvWriter, CsvRow line) {
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

  private static void toHeaders(Csv csv, CsvHeaders lineHeaders, String[] line) {
    // Check unknown headers
    for (String header : line) {
      lineHeaders.add(CsvHeader.builder().name(header).build());
      if (!csv.getHeaders().findByName(header).isPresent()) {
        csv.getRemarks().add(format(UNKNOWN_HEADER, cleanHeader(header)));
      }
    }

    // Check missing headers
    csv.getHeaders().forEach(csvHeader -> {
      Optional<CsvHeader> header = lineHeaders.findByName(csvHeader.getName());
      if (!header.isPresent()) {
        csv.getRemarks().add(format(MISSING_HEADER, cleanHeader(csvHeader.getName())));
      }
    });
  }

  private static CsvRow toCsvLine(Csv csv, CsvHeaders lineHeaders, String[] line) {
    CsvRow csvLine = new CsvRow();
    int colIndex = 0;
    for (String linevalue : line) {
      String header = lineHeaders.get(colIndex).getName();
      Optional<CsvHeader> csvHeader = csv.getHeaders().findByName(header);
      if (csvHeader.isPresent()) {
        CsvValue value = new CsvValue(csvHeader.get(), linevalue.trim());
        csvLine.getValues().add(value);
        checkValue(csvLine, value);
      }
      colIndex++;
    }
    return csvLine;
  }

  private static void checkValue(CsvRow csvLine, CsvValue value) {
    try {
      List<Function<String, String>> converters = value.getHeader().getConverters();
      value.setValue(value.getOriginalValue());
      if (converters != null) {
        converters.forEach(converter -> value.setValue(converter.apply(value.getValue())));
      }
    } catch (RuntimeException e) {
      value.setRemark(e.getMessage());
      csvLine.getRemarks().add(e.getMessage());
    }
  }

  public static String cleanHeader(String header) {
    return capitalize(header.trim());
  }
}
