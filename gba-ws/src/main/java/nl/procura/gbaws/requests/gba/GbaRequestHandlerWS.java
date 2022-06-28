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

import nl.procura.diensten.gba.ple.base.BasePLBuilder;
import nl.procura.diensten.gba.ple.base.PLEMessage;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.gbaws.requests.RequestCredentials;
import nl.procura.gbaws.requests.RequestHandler;
import nl.procura.gbaws.web.servlets.RequestException;

public class GbaRequestHandlerWS extends RequestHandler {

  private final PLEArgs args;
  private BasePLBuilder builder = new BasePLBuilder();

  public GbaRequestHandlerWS(RequestCredentials credentials, PLEArgs args) {
    super(credentials);
    this.args = args;
    execute();
  }

  public GbaRequestHandlerWS(String username, String password, PLEArgs args) {
    super(new RequestCredentials(username, password));
    this.args = args;
    execute();
  }

  @Override
  protected void find() {

    builder = new BasePLBuilder();
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
      final RequestException r = (RequestException) throwable;
      code = r.getCode();
      message = r.getMessage();
    }

    builder.getResult().getMessages().add(new PLEMessage(code, message));
    super.handleException(throwable);
  }

  public BasePLBuilder getBuilder() {
    return builder;
  }
}
