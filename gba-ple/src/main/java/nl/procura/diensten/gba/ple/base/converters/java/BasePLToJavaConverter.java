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

package nl.procura.diensten.gba.ple.base.converters.java;

import java.io.InputStream;
import java.io.OutputStream;

import nl.procura.diensten.gba.ple.base.PLEResult;
import nl.procura.diensten.gba.ple.procura.utils.PLESerializer;

public class BasePLToJavaConverter {

  public static void toStream(OutputStream outputStream, PLEResult resultaat) {
    PLESerializer.serialize(outputStream, resultaat);
  }

  public static PLEResult fromStream(InputStream inputStream) {
    return PLESerializer.deSerialize(inputStream, PLEResult.class);
  }
}
