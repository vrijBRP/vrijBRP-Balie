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

package nl.procura.gba.web.services.beheer.kassa.transports;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.CONFIG;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import nl.procura.gba.web.services.beheer.kassa.KassaFile;
import nl.procura.gba.web.services.beheer.kassa.KassaParameters;
import nl.procura.commons.core.exceptions.ProException;

public class KassaLocal {

  public boolean send(KassaParameters parameters, List<KassaFile> bestanden) {
    for (KassaFile bestand : bestanden) {
      switch (bestand.getKassaApplicationType()) {
        case KEY2BETALEN:
          sendToKey2betalen(parameters, bestand);
          break;
        case GKAS:
          sendToGKas(parameters, bestand);
          break;
        default:
          throw new ProException(WARNING, "Onbekend kassatype");
      }
    }
    return true;
  }

  private void sendToKey2betalen(KassaParameters parameters, KassaFile bestand) {
    try {
      // Cleanup directory
      if (bestand.getNr() == 1) {
        File file = new File(bestand.getFilename());
        FileUtils.forceMkdir(file.getParentFile());
        cleanup(parameters, file.getParentFile());
      }

      write(bestand.getFilename(), bestand.getContent());
    } catch (Exception e) {
      throw new ProException(CONFIG, WARNING, "Het kassa bestand kon niet worden verstuurd.", e);
    }
  }

  private void sendToGKas(KassaParameters parameters, KassaFile bestand) {
    try {
      write(parameters.getFilename(), bestand.getContent());
    } catch (Exception e) {
      throw new ProException(CONFIG, WARNING, "Het kassa bestand kon niet worden verstuurd.", e);
    }
  }

  private void cleanup(KassaParameters parameters, File parentFile) {
    FileFilter filter = new WildcardFileFilter("*" + parameters.getKassaLocatieId() + "*");
    for (File file : Objects.requireNonNull(parentFile.listFiles(filter))) {
      if (file.isFile()) {
        FileUtils.deleteQuietly(file);
      }
    }
  }

  private void write(String bestandsnaam, String inhoud) throws IOException {
    File file = new File(bestandsnaam);
    FileUtils.forceMkdir(file.getParentFile());
    FileWriter writer = new FileWriter(file);

    try {
      writer.write(inhoud.toCharArray());
      writer.flush();
    } finally {
      IOUtils.closeQuietly(writer);
      file.setExecutable(true, false);
      file.setReadable(true, false);
      file.setWritable(true, false);
    }
  }
}
