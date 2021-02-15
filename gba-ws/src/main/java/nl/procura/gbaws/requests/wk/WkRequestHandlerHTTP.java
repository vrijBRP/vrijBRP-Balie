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

package nl.procura.gbaws.requests.wk;

import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gba.wk.baseWK.BaseWKBuilder;
import nl.procura.diensten.gba.wk.baseWK.BaseWKMessage;
import nl.procura.diensten.gba.wk.utils.CommandLine2Arguments;
import nl.procura.gbaws.requests.RequestHandlerHTTP;
import nl.procura.gbaws.web.servlets.RequestException;

public class WkRequestHandlerHTTP extends RequestHandlerHTTP {

  private final static Logger LOGGER = LoggerFactory.getLogger(WkRequestHandlerHTTP.class);

  private BaseWKBuilder builder = new BaseWKBuilder();

  public WkRequestHandlerHTTP(String username, String password, OutputStream outputStream, String clientCommand) {
    super(username, password, null, outputStream, clientCommand);
  }

  @Override
  public void find() {

    final CommandLine2Arguments cla = new CommandLine2Arguments(getClientCommand());
    builder = new BaseWKBuilder();

    final WkRequestHandler wk = new WkRequestHandler();
    wk.find(builder, cla.getArgumenten());
    getLogger().getLoglines().addAll(wk.getLogger().getLoglines());
  }

  @Override
  protected void handleException(Throwable t) {

    int code = 1200;
    String message = t.getMessage();

    if (t instanceof RequestException) {
      final RequestException r = (RequestException) t;

      code = r.getCode();
      message = r.getMessage();
    }

    builder.getZoekResultaat().getMessages().add(new BaseWKMessage(code, message));

    super.handleException(t);
  }

  @Override
  public void sendBack() {
    try {
      builder.serialize(getOutputStream());
    } catch (final RuntimeException e) {
      LOGGER.debug(e.toString());
    }
  }
}
