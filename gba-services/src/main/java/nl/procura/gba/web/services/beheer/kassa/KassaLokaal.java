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

package nl.procura.gba.web.services.beheer.kassa;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.CONFIG;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import nl.procura.gba.web.services.beheer.kassa.gkas.KassaParameters;
import nl.procura.standard.exceptions.ProException;

public class KassaLokaal {

  public boolean verstuur(KassaLeverancierType leverancier, KassaParameters parameters, List<String> bestanden) {

    switch (leverancier) {
      case CENTRIC:
        return verstuurCentric(parameters, bestanden);

      case ONBEKEND:
      case JCC:
      default:
        return verstuurGKas(parameters, bestanden);
    }
  }

  private void opschonen(KassaParameters parameters, File parentFile) {
    for (File file : parentFile.listFiles(
        (FileFilter) new WildcardFileFilter("*" + parameters.getKassaLocatieId() + "*"))) {
      if (file.isFile()) {
        FileUtils.deleteQuietly(file);
      }
    }
  }

  private void schrijf(String bestandsNaam, String bestand) throws IOException {

    File kassaBestand = new File(bestandsNaam);
    FileUtils.forceMkdir(kassaBestand.getParentFile());
    FileWriter writer = new FileWriter(kassaBestand);

    try {
      writer.write(bestand.toCharArray());
      writer.flush();
    } finally {
      IOUtils.closeQuietly(writer);
      kassaBestand.setExecutable(true, false);
      kassaBestand.setReadable(true, false);
      kassaBestand.setWritable(true, false);
    }
  }

  private boolean verstuurCentric(KassaParameters parameters, List<String> bestanden) {

    try {
      if (emp(parameters.getKassaLocatieId())) {
        throw new ProException(CONFIG, WARNING,
            "De waarde voor Kassa-locatie-identificatie is leeg bij deze locatie (" + parameters.getLocatie() + ")");
      }

      File defaultKassaBestand = new File(parameters.getParameterFileName());
      FileUtils.forceMkdir(defaultKassaBestand.getParentFile());
      opschonen(parameters, defaultKassaBestand.getParentFile());

      int nr = 0;
      for (String bestand : bestanden) {

        nr++;

        schrijf(parameters.getParameterFileName() + "-" + parameters.getKassaLocatieId() + "-" + nr + ".001",
            bestand);
      }

      return true;
    } catch (Exception e) {
      throw new ProException(CONFIG, WARNING, "Het kassa bestand kon niet worden verstuurd.", e);
    }
  }

  private boolean verstuurGKas(KassaParameters parameters, List<String> bestanden) {

    try {
      for (String bestand : bestanden) {
        schrijf(parameters.getParameterFileName(), bestand);
      }

      return true;
    } catch (Exception e) {
      throw new ProException(CONFIG, WARNING, "Het kassa bestand kon niet worden verstuurd.", e);
    }
  }
}
