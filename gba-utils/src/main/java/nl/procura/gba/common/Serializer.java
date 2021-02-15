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

package nl.procura.gba.common;

import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Serializer {

  private final static Logger LOGGER = LoggerFactory.getLogger(Serializer.class.getName());

  public static void serialize(OutputStream os, Object input) {

    ObjectOutputStream obj_out_stream = null;

    try {
      obj_out_stream = new ObjectOutputStream(new BufferedOutputStream(os));
      obj_out_stream.writeObject(input);
    } catch (NullPointerException | IOException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if (obj_out_stream != null) {
          obj_out_stream.flush();
        }
      } catch (IOException e) {
        LOGGER.debug(e.getMessage());
      }
    }
  }

  @SuppressWarnings("unused")
  public static <T> T deSerialize(InputStream is, Class<T> classType) {

    ObjectInputStream obj_in_stream;
    try {
      obj_in_stream = new ObjectInputStream(is);
      return (T) obj_in_stream.readObject();
    } catch (NullPointerException | ClassNotFoundException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
