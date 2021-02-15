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

package nl.procura.gba.config;

import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.CONFIG;

import java.io.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.IOUtils;

import nl.procura.standard.exceptions.ProException;

public class GbaProperties extends Properties {

  public GbaProperties(String bestand) {
    this(new File(bestand));
  }

  public GbaProperties(File bestand) {

    try {
      loadStream(new FileInputStream(bestand));
    } catch (FileNotFoundException e) {
      throw new ProException(CONFIG, ERROR,
          "Fout bij inlezen properties bestand. Kan bestand niet vinden: " + bestand, e);
    }
  }

  public GbaProperties(InputStream stream) {
    loadStream(stream);
  }

  public static byte[] toByteArray(Properties properties) {

    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      properties.store(bos, null);
      return bos.toByteArray();
    } catch (IOException e) {
      throw new ProException(CONFIG, ERROR, "Fout bij converteren properties.", e);
    }
  }

  public static Properties getProperties(byte[] props) {
    return new GbaProperties(new ByteArrayInputStream(props));
  }

  public void loadStream(InputStream stream) {

    try {
      try {
        load(stream);
      } finally {
        IOUtils.closeQuietly(stream);
      }
    } catch (IOException e) {
      throw new ProException(CONFIG, ERROR, "Fout bij inlezen properties bestand", e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public synchronized Enumeration keys() {

    Enumeration keysEnum = super.keys();
    Vector keyList = new Vector();

    while (keysEnum.hasMoreElements()) {
      keyList.add(keysEnum.nextElement());
    }

    Collections.sort(keyList);
    return keyList.elements();
  }
}
