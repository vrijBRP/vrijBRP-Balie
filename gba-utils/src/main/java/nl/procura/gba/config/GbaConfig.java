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

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.CONFIG;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.security.ProwebPath;

/**
 * Configuratie class van GBA web
 */
public class GbaConfig {

  private static final String PROPERTIES_FILE      = "/applicatie.properties";
  private static final String FOUT_OPSLAAN_BESTAND = "Kan bestand niet opslaan: ";
  private static final String FOUT_GEEN_PROPERTY   = "Kan parameter niet vinden: ";

  private static ProwebPath path;
  private static Properties properties;

  private static void init() {

    if (properties == null) {
      try {
        loadProperties();
      } finally {
        addMissingProperties();
        checkProperties();
      }
    }
  }

  public static String getRequired(GbaConfigProperty property) {
    if (!isFilled(property)) {
      throw new ProException(CONFIG, ERROR, (FOUT_GEEN_PROPERTY + property));
    }
    return astr(properties.getProperty(property.getProperty()));
  }

  public static String get(GbaConfigProperty property) {
    return astr(properties.getProperty(property.getProperty()));
  }

  public static Properties getProperties() {
    if (properties == null) {
      init();
    }
    return properties;
  }

  private static void loadProperties() {
    properties = new GbaProperties(new File(path.getConfigDir(), PROPERTIES_FILE));
  }

  private static void addMissingProperties() {
    for (GbaConfigProperty p : GbaConfigProperty.values()) {
      if (p.isRequired() && isNull(p)) {
        properties.put(p.getProperty(), "");
      }
    }
  }

  private static void checkProperties() {
    for (GbaConfigProperty p : GbaConfigProperty.values()) {
      if (p.isRequired() && !isFilled(p)) {
        throw new ProException(CONFIG, ERROR, (FOUT_GEEN_PROPERTY + p));
      }
    }
  }

  public static void storeProperties() {
    FileOutputStream fos = null;
    try {
      if (path != null) {
        try {
          fos = new FileOutputStream(new File(path.getConfigDir(), PROPERTIES_FILE));
          properties.store(fos, null);
        } finally {
          try {
            if (fos != null) {
              fos.flush();
            }
          } catch (Exception e) {
            // ignore
          }

          IOUtils.closeQuietly(fos);
        }
      }
    } catch (IOException e) {
      throw new ProException(CONFIG, ERROR, (FOUT_OPSLAAN_BESTAND + PROPERTIES_FILE), e);
    }
  }

  private static boolean isNull(GbaConfigProperty p) {
    return properties.get(p.getProperty()) == null;
  }

  private static boolean isFilled(GbaConfigProperty p) {
    return fil(astr(properties.get(p.getProperty())));
  }

  public static ProwebPath getPath() {
    return path;
  }

  public static void setPath(ProwebPath prowebPath) {
    path = prowebPath;
  }

}
