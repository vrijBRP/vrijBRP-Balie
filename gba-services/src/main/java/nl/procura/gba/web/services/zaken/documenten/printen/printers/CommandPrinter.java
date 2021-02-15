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

package nl.procura.gba.web.services.zaken.documenten.printen.printers;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.CONFIG;
import static nl.procura.standard.exceptions.ProExceptionType.PRINT;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.martiansoftware.jsap.CommandLineTokenizer;

import nl.procura.gba.common.ProcessStreamReader;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.standard.exceptions.ProException;

/**
 * This printer issues a commandline command to the local linux system
 */
public class CommandPrinter extends AbstractPrinter {

  private final static Logger LOGGER = LoggerFactory.getLogger(CommandPrinter.class.getName());

  public CommandPrinter(PrintOptie po, Gebruiker gebruiker) throws ProException {
    super(po, gebruiker);
  }

  @Override
  public void printStream(byte[] documentBytes) throws ProException {
    ByteArrayInputStream bis = new ByteArrayInputStream(documentBytes);

    try {
      File temp = File.createTempFile("pdf-", ".pdf");
      temp.deleteOnExit();
      IOUtils.copy(bis, new FileOutputStream(temp));
      String file = FilenameUtils.separatorsToUnix(temp.getAbsolutePath());

      LOGGER.info("File created: " + file);
      String cmd = getPrintOptie().getCmd();
      if (!cmd.contains("${file}")) {
        throw new ProException(CONFIG, WARNING,
            "Configuratiefout: in het printcommando is de variabele <b>${file}</b> niet gezet.");
      }

      String newCommand = FilenameUtils.normalize(cmd.replaceAll("\\$\\{file\\}", file));
      newCommand = newCommand.replaceAll("\\$\\{user.name\\}", getGebruiker().getGebruikersnaam());
      newCommand = newCommand.replaceAll("\\$\\{user.id\\}", astr(getGebruiker().getCUsr()));

      LOGGER.info("Exec command: " + newCommand);

      Process process = Runtime.getRuntime().exec(CommandLineTokenizer.tokenize(newCommand));
      ProcessStreamReader inputStream = new ProcessStreamReader(process.getInputStream());
      ProcessStreamReader errorStream = new ProcessStreamReader(process.getErrorStream());

      inputStream.start();
      errorStream.start();

      process.waitFor();

      inputStream.join();
      errorStream.join();

    } catch (Exception e) {
      throw new ProException(PRINT, WARNING, "De printactie kon niet worden uitgevoerd: " + e.getMessage());
    } finally {
      IOUtils.closeQuietly(bis);
    }
  }
}
