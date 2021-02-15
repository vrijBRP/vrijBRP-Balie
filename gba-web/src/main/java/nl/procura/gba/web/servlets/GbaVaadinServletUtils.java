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

package nl.procura.gba.web.servlets;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import java.io.BufferedWriter;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;

import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;

public class GbaVaadinServletUtils {

  /**
   * Sets the X-UA-Compatibility value
   */
  public static void writeLegacyModeTag(BufferedWriter page, HttpServletRequest request) throws IOException {

    HttpSession session = request.getSession();
    WebApplicationContext applicationContext = WebApplicationContext.getApplicationContext(session);

    if (isIE11(applicationContext)) {
      String legacyMode = astr(session.getAttribute(ParameterConstant.X_UA_COMPATIBLE.getKey()));
      if (fil(legacyMode)) {
        page.write("<meta http-equiv=\"X-UA-Compatible\" content=\"" + legacyMode + "\"/>\n");
      }
    }
  }

  private static boolean isIE11(WebApplicationContext applicationContext) {
    WebBrowser browser = applicationContext.getBrowser();
    return browser.isIE() && browser.getBrowserMajorVersion() >= 10;
  }
}
