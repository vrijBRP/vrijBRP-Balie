/*
 * Copyright 2023 - 2024 Procura B.V.
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

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionType.CONFIG;
import static nl.procura.commons.core.exceptions.ProExceptionType.DATABASE;

import java.io.File;
import java.net.URL;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.gba.web.common.database.DatabaseUpdate;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.standard.security.ProwebPath;

public class ConfigServlet extends HttpServlet {

  private static final long serialVersionUID = 7472586523544674254L;

  private static final String PROPERTIES_FILE = "/applicatie.properties";

  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigServlet.class.getName());

  @Override
  public void init() throws ServletException {

    try {
      final ProwebPath path = new ProwebPath(getServletContext().getContextPath()).mkDirs();
      checkPaths(path);
      GbaConfig.setPath(path);
      checkConnection();
      super.init();
    } catch (RuntimeException e) {
      LOGGER.error("Error", e);
      System.exit(1); // NOSONAR
    }
  }

  private void checkConnection() {

    long st = System.currentTimeMillis();

    EntityManager em = null;

    try {
      try {

        LOGGER.info("");
        LOGGER.info("JPA Initialisatie");
        LOGGER.info("==============================");

        em = GbaJpa.createManager(GbaConfig.getProperties());
      } catch (Exception e) {
        throw new ProException(DATABASE, ERROR, "Verbinding met database NIET gelukt: " + e.getMessage(), e);
      }

      long et = System.currentTimeMillis();

      LOGGER.debug("Opstarten JPA: " + (et - st) + " ms.");

      LOGGER.info("");
      LOGGER.info("Database update");
      LOGGER.info("==============================");

      new DatabaseUpdate(em);
    } finally {
      if (em != null) {
        em.close();
      }
    }
  }

  private ProwebPath checkPaths(ProwebPath pad) {
    pad.checkFile(pad.getConfigDir(), PROPERTIES_FILE, "Configuratie bestand");
    controleerConfigBestand(pad);
    return pad;
  }

  private void controleerConfigBestand(ProwebPath path) {

    if (!new File(path.getConfigDir(), PROPERTIES_FILE).exists()) {
      URL resource = GbaConfig.class.getResource(PROPERTIES_FILE);
      if (resource != null) {
        LOGGER.warn(
            "Configuratie bestand alleen in classpath gevonden. Dat wordt gebruikt (" + resource.getFile() + ")");
      } else {
        throw new ProException(CONFIG, ERROR, "Configuratie bestand is niet gevonden.");
      }
    }
  }
}
