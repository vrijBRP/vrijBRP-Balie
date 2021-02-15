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

import nl.procura.diensten.gba.wk.baseWK.BaseWKBuilder;
import nl.procura.diensten.gba.wk.baseWK.BaseWKMessage;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gbaws.requests.RequestHandler;
import nl.procura.gbaws.web.servlets.RequestException;

public class WkRequestHandlerWS extends RequestHandler {

  private BaseWKBuilder  basisWKHandler = new BaseWKBuilder();
  private ZoekArgumenten args;

  public WkRequestHandlerWS(String username, String password, ZoekArgumenten a) {
    super(username, password);
    setArgs(a);
  }

  @Override
  public void find() {

    final WkRequestHandler wk = new WkRequestHandler();
    wk.find(basisWKHandler, args);
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

    basisWKHandler.getZoekResultaat().getMessages().add(new BaseWKMessage(code, message));

    super.handleException(t);
  }

  public BaseWKBuilder getBasisWKHandler() {
    return basisWKHandler;
  }

  public void setBasisWKHandler(BaseWKBuilder basisWKHandler) {
    this.basisWKHandler = basisWKHandler;
  }

  public ZoekArgumenten getArgs() {
    return args;
  }

  public void setArgs(ZoekArgumenten args) {
    this.args = args;
  }
}
