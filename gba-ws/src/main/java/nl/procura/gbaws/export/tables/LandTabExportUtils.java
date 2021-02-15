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

package nl.procura.gbaws.export.tables;

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.fil;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.config.GbaConfigProperty;
import nl.procura.gbaws.db.enums.LandTabType;
import nl.procura.standard.exceptions.ProException;

public class LandTabExportUtils {

  private static final Logger LOGGER      = LoggerFactory.getLogger(LandTabExportUtils.class);
  private static final String EXPORT_FILE = "target/proweb-tabellen.json";

  public LandTabExportUtils() throws Exception {

    exportFile();
    importFile();
  }

  public static void main(String[] args) throws Exception {
    new LandTabExportUtils();
  }

  public static String getEndpoint() {
    String endpoint = GbaConfig.get(GbaConfigProperty.PROCURA_ENDPOINT);
    String endpoint1 = (endpoint + "/gba");
    return fil(endpoint) ? endpoint1 : "https://burgerzaken.procura.nl/gba";
  }

  public static List<LandTabJsonRecord> getRecords(List<LandTabJsonRecord> records, LandTabType landTabType) {

    List<LandTabJsonRecord> jsonRecords = new ArrayList<>();

    for (LandTabJsonRecord jsonRecord : records) {
      if (landTabType.getCode() == aval(jsonRecord.getTabel())) {
        jsonRecords.add(jsonRecord);
      }
    }

    return jsonRecords;
  }

  public static List<LandTabJsonRecord> getRecords(File file) {

    List<LandTabJsonRecord> jsonRecords = new ArrayList<>();

    try {
      final InputStream is = new FileInputStream(file);

      try {
        JsonParser parser = getJsonParser(is);
        jsonRecords.addAll(parser.readValueAs(LandTabJsonExport.class).getRecords());
      } finally {
        IOUtils.closeQuietly(is);
      }

      return jsonRecords;
    } catch (Exception e) {
      throw new ProException("Fout bij laden tabellen", e);
    }
  }

  public static List<LandTabJsonRecord> getRecords(String url, LandTabType landTabType) {

    List<LandTabJsonRecord> jsonRecords = new ArrayList<>();

    try {
      String u = url + "/tabel?tabel=" + landTabType.getCode();
      final InputStream is = new URL(u).openConnection().getInputStream();

      try {
        JsonParser parser = getJsonParser(is);

        while (parser.nextToken() != JsonToken.END_ARRAY) {
          parser.nextToken();
          jsonRecords.add(parser.readValueAs(LandTabJsonRecord.class));
        }
      } finally {
        IOUtils.closeQuietly(is);
      }

      return jsonRecords;
    } catch (Exception e) {
      throw new ProException("Fout bij ophalen tabellen", e);
    }
  }

  private static JsonParser getJsonParser(final InputStream is) throws IOException {
    final InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
    JsonFactory factory = new MappingJsonFactory();
    return factory.createJsonParser(reader);
  }

  private void exportFile() throws IOException {
    LandTabType[] tables = LandTabType.values();
    LandTabJsonExport export = new LandTabJsonExport();

    for (LandTabType landTabType : tables) {
      if (landTabType.getCode() > 0) {
        LOGGER.info(landTabType.getGbaTabel().getDescr());
        export.getRecords().addAll(getRecords(getEndpoint(), landTabType));
      }
    }

    JsonFactory f = new MappingJsonFactory();
    JsonGenerator g = f.createJsonGenerator(new FileOutputStream(EXPORT_FILE));
    g.writeObject(export);
    g.flush();
    g.close();

    LOGGER.info("\nKlaar met exporten!!");
  }

  private void importFile() {
    System.out.println("Records: " + getRecords(new File(EXPORT_FILE)).size());
    LOGGER.info("\nKlaar met importeren!!");
  }
}
