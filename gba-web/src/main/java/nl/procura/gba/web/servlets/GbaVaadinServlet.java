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

import java.io.BufferedWriter;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.procura.vaadin.component.application.ProcuraApplicationServlet;

public class GbaVaadinServlet extends ProcuraApplicationServlet {

  @Override
  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
      IOException {
    super.service(request, response);
  }

  @Override
  protected void writeAjaxPageHtmlHeader(BufferedWriter page, String title, String themeUri,
      HttpServletRequest request) throws IOException {

    // Voeg code toe aan de pagina.
    page.write("<meta name=\"format-detection\" content=\"telephone=no\"/>\n");
    page.write("<meta name=\"google\" content=\"notranslate\"/>\n");
    page.write("<meta name=\"robots\" content=\"nofollow\"/>\n");
    page.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\n");

    GbaVaadinServletUtils.writeLegacyModeTag(page, request);

    page.write("<style type=\"text/css\">html, body {height:100%;margin:0;}</style>");

    // Add favicon links
    page.write(
        "<link rel=\"shortcut icon\" type=\"image/vnd.microsoft.icon\" href=\"" + themeUri + "/favicon.ico\" />");
    page.write("<link rel=\"icon\" type=\"image/vnd.microsoft.icon\" href=\"" + themeUri + "/favicon.ico\" />");
    page.write("<title>" + safeEscapeForHtml(title) + "</title>");
  }
}
