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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.procura.vaadin.component.application.ApplicationRequestHandler;
import nl.procura.vaadin.theme.ProcuraApplication;

public class GbaApplicationRequestHandler extends ApplicationRequestHandler {

  public GbaApplicationRequestHandler(GbaApplication gbaApplication) {
    super(gbaApplication);
  }

  @Override
  public void handleRequest(ProcuraApplication application) {
    super.handleRequest(application);
    removeTicketParameter(application);
  }

  /**
   * Removes the ticket parameter from the url
   */
  private void removeTicketParameter(ProcuraApplication application) {
    if (!isInternalRequest()) {
      HttpServletRequest request = getApplication().getHttpRequest();
      HttpServletResponse response = getApplication().getHttpResponse();
      GbaApplication gbaApplication = (GbaApplication) application;

      String ticket = request.getParameter("ticket");
      if (ticket != null && gbaApplication.getServices().getGebruiker() != null) {
        try {
          response.sendRedirect(request.getContextPath() + "/#");
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
