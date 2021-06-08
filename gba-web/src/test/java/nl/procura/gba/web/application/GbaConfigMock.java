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

package nl.procura.gba.web.application;

import java.lang.reflect.Field;
import java.util.Properties;

import org.apache.commons.lang3.reflect.FieldUtils;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.config.GbaConfigProperty;
import nl.procura.standard.security.ProwebPath;

public class GbaConfigMock {

  public static void setGbaConfig() {
    Properties properties = new Properties();
    properties.setProperty(GbaConfigProperty.GEMEENTE.getProperty(), "Municipality");
    Field propertiesField = FieldUtils.getDeclaredField(GbaConfig.class, "properties", true);
    try {
      propertiesField.set(null, properties);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
    ProwebPath path = new ProwebPath("TestApplication").mkDirs();
    GbaConfig.setPath(path);
  }
}
