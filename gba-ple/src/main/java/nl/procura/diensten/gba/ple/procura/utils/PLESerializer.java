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

package nl.procura.diensten.gba.ple.procura.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PLESerializer {

  private final static Logger LOGGER = LoggerFactory.getLogger(PLESerializer.class.getName());

  public static boolean serialize(OutputStream os, Serializable obj) {

    try {
      SerializationUtils.serialize(obj, os);
    } catch (SerializationException e) {
      e.printStackTrace();
      LOGGER.debug(e.getMessage());
      return false;
    } catch (NullPointerException e) {
      LOGGER.debug(e.getMessage());
      return false;
    }

    return true;
  }

  public static <T> T deSerialize(InputStream is, Class<T> classType) {

    try {
      return (T) SerializationUtils.deserialize(is);
    } catch (SerializationException e) {
      LOGGER.debug(e.getMessage());
    } catch (NullPointerException e) {
      LOGGER.debug(e.getMessage());
      throw new RuntimeException(e);
    }

    // Maak nieuwe class
    try {
      return classType.newInstance();
    } catch (Exception e) {
      return null;
    }
  }
}
