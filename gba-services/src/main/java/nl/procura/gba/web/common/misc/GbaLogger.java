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

package nl.procura.gba.web.common.misc;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.pad_right;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import nl.procura.gba.config.GbaConfig;
import nl.procura.soap.SoapHandler;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.commons.core.exceptions.ProExceptionType;
import nl.procura.standard.security.ProwebPath;

public class GbaLogger {

  public synchronized static void log(String path, SoapHandler soapHandler) {

    path = "webservices/" + path;
    StringBuilder message = new StringBuilder();
    message.append(getHeader("verstuurd bericht"));
    message.append(soapHandler.getRequest().getXmlMessage());
    message.append("\n\n");
    message.append(getHeader("ontvangen bericht"));
    message.append(soapHandler.getResponse().getXmlMessage());
    message.append("\n\n");

    write(getLogDir(), path, message);
  }

  private static StringBuilder getHeader(String message) {
    String date = format("dd-MMM-yyyy HH:mm:ss", new Date());
    String header = date + " - " + message;
    StringBuilder m = new StringBuilder(header);
    m.append("\n");
    m.append(pad_right("", "=", header.length()));
    m.append("\n");

    return m;
  }

  private static String format(String pattern, Date date) {
    return new SimpleDateFormat(pattern).format(date);
  }

  private static File getLogDir() {
    File logDir = new File(getPath().getServerDir() + "/logs/");
    if (!logDir.exists()) {
      throw new ProException(ProExceptionSeverity.ERROR, "De directorie 'logs' bestaat niet!");
    }

    return logDir;
  }

  private static ProwebPath getPath() {
    return GbaConfig.getPath();
  }

  private static void write(File logDir, String subDirs, StringBuilder message) {
    try {
      if (emp(subDirs)) {
        throw new ProException(ProExceptionSeverity.ERROR, "Geen pad waaronder log moet worden opgeslagen!");
      }

      File subDir = new File(logDir, subDirs.toLowerCase());
      FileUtils.forceMkdir(subDir);

      String date = format("yyyy-MM-dd", new Date());
      File file = new File(subDir, date + ".log");
      try (FileOutputStream fos = new FileOutputStream(file, true)) {
        IOUtils.write(message, fos);
        fos.flush();
      }
    } catch (IOException e) {
      throw new ProException(ProExceptionType.UNKNOWN, "Probleem met wegschrijven", e);
    }
  }
}
