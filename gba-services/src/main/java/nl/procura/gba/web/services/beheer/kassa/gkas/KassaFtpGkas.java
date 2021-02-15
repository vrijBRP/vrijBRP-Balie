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

package nl.procura.gba.web.services.beheer.kassa.gkas;

import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.CONFIG;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.standard.exceptions.ProException;

public class KassaFtpGkas {

  private final static Logger LOGGER = LoggerFactory.getLogger(KassaFtpGkas.class.getName());
  private final FTPClient     ftp    = new FTPClient();

  public boolean verstuur(KassaParameters parameters, List<String> bestanden) {
    boolean error = false;
    try {
      int i = 1;
      while (!ftp.isConnected() && (i <= 5)) {
        try {
          ftp.connect(parameters.getFtpUrl());
        } catch (Exception e) {
          i++;
          LOGGER.info("Attempt: " + i);
          if (i > 5) {
            throw e;
          }
        }
      }

      for (String bestand : bestanden) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ftp.login(parameters.getFtpUsername(), parameters.getFtpPassword());
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(bos)));

        int reply = ftp.getReplyCode();

        boolean negative = FTPReply.isNegativePermanent(reply) || FTPReply.isNegativeTransient(reply);

        if (negative) {
          throw new ProException(CONFIG, WARNING,
              "Kan niet inloggen op de FTP server t.b.v. de kassa koppeling. <hr/><p>" + ftp.getReplyString() + "</p>");
        }

        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        checkFTPPath(parameters.getParameterFileName());
        ftp.deleteFile(parameters.getParameterFileName());

        if (!ftp.storeFile(parameters.getParameterFileName(), new ByteArrayInputStream(bestand.getBytes()))) {
          throw new ProException(CONFIG, WARNING,
              "Het kassa bestand kon niet worden verstuurd. " + getMessage(bos));
        }
      }
    } catch (ProException e) {
      throw e;
    } catch (Exception e) {

      throw new ProException(CONFIG, WARNING,
          "Het kassa bestand kon niet worden verstuurd vanwege een onbekende fout. <br/>Probeer het nogmaals.",
          e);
    } finally {

      try {
        ftp.disconnect();
      } catch (IOException e) {
        LOGGER.debug(e.getMessage());
      }

      try {
        if (ftp.isConnected()) {
          ftp.disconnect();
        }
      } catch (IOException e) {
        LOGGER.debug(e.getMessage());
      }
    }

    return !error;
  }

  private void checkFTPPath(String path) throws IOException {

    String s = path.replaceAll("\\\\", "/");
    Matcher m = Pattern.compile("/").matcher(s);

    while (m.find()) {
      String ftpDir = s.substring(0, m.end());
      if (ftp.getStatus(ftpDir) == null) {
        ftp.mkd(ftpDir);
      }
    }
  }

  private String getMessage(ByteArrayOutputStream bos) {

    try {
      bos.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      IOUtils.closeQuietly(bos);
    }

    return new String(bos.toByteArray());
  }
}
