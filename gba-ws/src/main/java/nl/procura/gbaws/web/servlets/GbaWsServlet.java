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

package nl.procura.gbaws.web.servlets;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpaStorageWrapper;
import nl.procura.gbaws.db.jpa.PleJpaUtils;
import nl.procura.gbaws.requests.RequestHeaderHandler;
import nl.procura.gbaws.requests.gba.GbaRequestHandlerHTTP;
import nl.procura.gbaws.requests.wk.WkRequestHandlerHTTP;
import nl.procura.standard.threadlocal.ThreadLocalStorage;

public class GbaWsServlet extends HttpServlet {

  private static final long   serialVersionUID = -1581939581489992499L;
  private static final Logger LOGGER           = LoggerFactory.getLogger(GbaWsServlet.class);

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    try {

      final RequestHeaderHandler headerhandler = new RequestHeaderHandler(request);
      final String u = headerhandler.getCredentials().getUsername();
      final String p = headerhandler.getCredentials().getPassword();
      final String c = getLine(request);
      final ServletOutputStream os = response.getOutputStream();

      if (c.contains("-ple")) {
        new GbaRequestHandlerHTTP(u, p, os, c);
      } else if (c.contains("-wk")) {
        new WkRequestHandlerHTTP(u, p, os, c);
      }
    } catch (final RuntimeException e) {
      LOGGER.debug(e.toString());
    }
  }

  @Override
  public synchronized void init(ServletConfig config) throws ServletException {
    LOGGER.info("Initiating GBA servlet");
    initControleJPA();
    initPleJPA();
    super.init(config);
  }

  public void initControleJPA() {
    LOGGER.info("INIT JPA verbinding 1");
    ThreadLocalStorage.init(new GbaWsJpaStorageWrapper(GbaConfig.getProperties()));
  }

  public void initPleJPA() {
    LOGGER.info("INIT JPA verbinding 2");
    PleJpaUtils.init();
  }

  private String getLine(HttpServletRequest request) throws IOException {

    String line = "";

    for (final String l : IOUtils.readLines(request.getInputStream(), "UTF-8")) {
      line = URLDecoder.decode(l, "UTF-8");
      break;
    }

    final String[] cl = line.split("command=");
    return cl.length > 1 ? cl[1] : line;
  } // getLine
}
