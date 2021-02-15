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

package nl.procura.gbaws.export.elements;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;

import nl.procura.gbaws.db.wrappers.ProfileWrapper.ProfielElement;
import nl.procura.standard.exceptions.ProException;

public class ElementsExportUtils {

  public ElementsExportUtils() {
  }

  public static void main(String[] args) throws Exception {
    new ElementsExportUtils();
  }

  public static ByteArrayOutputStream export(List<ProfielElement> elements) {

    ElementsJsonExport export = new ElementsJsonExport();

    try {

      for (ProfielElement element : elements) {
        export.getRecords().add(new ElementsJsonRecord(element.getCode_cat(), element.getCode_element()));
      }

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      JsonFactory f = new MappingJsonFactory();
      JsonGenerator g = f.createJsonGenerator(bos);
      g.writeObject(export);
      g.flush();
      g.close();

      return bos;
    } catch (Exception e) {
      throw new ProException("Fout bij exporteren elementen", e);
    }
  }

  public static List<ElementsJsonRecord> getRecords(File file) {

    List<ElementsJsonRecord> jsonRecords = new ArrayList<>();

    try {
      final InputStream is = new FileInputStream(file);

      try {
        JsonParser parser = getJsonParser(is);
        jsonRecords.addAll(parser.readValueAs(ElementsJsonExport.class).getRecords());
      } finally {
        IOUtils.closeQuietly(is);
      }

      return jsonRecords;
    } catch (Exception e) {
      throw new ProException("Fout bij laden elementen", e);
    }
  }

  private static JsonParser getJsonParser(final InputStream is) throws IOException {
    final InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
    JsonFactory factory = new MappingJsonFactory();
    return factory.createJsonParser(reader);
  }
}
