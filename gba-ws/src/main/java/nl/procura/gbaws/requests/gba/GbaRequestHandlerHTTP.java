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

package nl.procura.gbaws.requests.gba;

import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gba.ple.base.BasePLBuilder;
import nl.procura.diensten.gba.ple.base.PLEMessage;
import nl.procura.diensten.gba.ple.base.converters.java.BasePLToJavaConverter;
import nl.procura.diensten.gba.ple.base.converters.json.BasePLToJsonConverter;
import nl.procura.diensten.gba.ple.base.converters.ple.BasePLToPleConverter;
import nl.procura.diensten.gba.ple.base.converters.yaml.BasisPLToYamlConverter;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.utils.PLECommandLineUtils;
import nl.procura.gbaws.requests.RequestCredentials;
import nl.procura.gbaws.requests.RequestHandlerHTTP;
import nl.procura.gbaws.web.servlets.RequestException;

public class GbaRequestHandlerHTTP extends RequestHandlerHTTP {

  private final static Logger LOGGER = LoggerFactory.getLogger(GbaRequestHandlerHTTP.class.getName());

  private PleReturnType returnType = PleReturnType.JAVA;
  private BasePLBuilder builder    = new BasePLBuilder();

  public GbaRequestHandlerHTTP(RequestCredentials credentials, OutputStream outputStream, String clientCommand) {
    super(credentials, null, outputStream, clientCommand);
  }

  @Override
  public void sendBack() {
    try {
      switch (returnType) {
        case PLE:
          BasePLToPleConverter.toStream(getOutputStream(), builder.getResult());
          break;

        case JSON:
          BasePLToJsonConverter.toStream(getOutputStream(), builder.getResult());
          break;

        case YAML: // Used by PROBEL (ZoekPersoon.pl)
          BasisPLToYamlConverter.toStream(getOutputStream(), builder.getResult());
          break;

        default:
          BasePLToJavaConverter.toStream(getOutputStream(), builder.getResult());
      }
    } catch (final RuntimeException e) {
      LOGGER.debug(e.toString());
    }
  }

  @Override
  protected void find() {
    builder = new BasePLBuilder();
    PLEArgs args = PLECommandLineUtils.convert(getClientCommand());
    returnType = PleReturnType.get(PLECommandLineUtils.getReturnTypeCode(getClientCommand()));
    GbaRequestHandler gba = new GbaRequestHandler();

    try {
      gba.setBuilder(builder);
      gba.setGebruiker(getGebruiker());
      gba.setArgs(args);
      gba.find();
    } finally {
      getLogger().getLoglines().addAll(gba.getLogger().getLoglines());
    }
  }

  @Override
  protected void handleException(Throwable throwable) {

    int code = 1200;
    String message = throwable.getMessage();

    if (throwable instanceof RequestException) {
      final RequestException exception = (RequestException) throwable;
      code = exception.getCode();
      message = exception.getMessage();
    }

    builder.getResult().getMessages().add(new PLEMessage(code, message));
    super.handleException(throwable);
  }
}
