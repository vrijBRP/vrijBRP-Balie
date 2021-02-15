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

package nl.procura.diensten.gba.ple.base.converters.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.google.gson.GsonBuilder;

import nl.procura.diensten.gba.ple.base.PLEResult;
import nl.procura.diensten.gba.ple.base.converters.ws.BasePLToGbaWsConverter;
import nl.procura.gbaws.web.rest.v2.personlists.GbaWsPersonListResponse;

public class BasePLToJsonConverter {

  public static void toStream(OutputStream outputStream, PLEResult resultaat) {
    try {
      outputStream.write(new GsonBuilder()
          .create()
          .toJson(BasePLToGbaWsConverter.toGbaWsPersonListResponse(resultaat)).getBytes());
    } catch (IOException e) {
      throw new RuntimeException("Error writing Json", e);
    }
  }

  public static PLEResult fromStream(InputStream inputStream) {
    return BasePLToGbaWsConverter.toPLEResult(new GsonBuilder()
        .create().fromJson(new InputStreamReader(inputStream),
            GbaWsPersonListResponse.class));
  }
}
