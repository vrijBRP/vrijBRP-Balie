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

package nl.procura.rdw.functions;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneratedFilesCleaner {

  private static final String DIR = "C:/eclipse4.2/workspace/gba/gba-rdw/src/main/resources/examples/generated/";

  private final static Logger LOGGER = LoggerFactory.getLogger(GeneratedFilesCleaner.class.getName());

  public GeneratedFilesCleaner() {

    for (File file : FileUtils.listFiles(new File(DIR), null, true)) {

      if (file.length() == 0) {

        LOGGER.info("Removing file: " + file);

        file.delete();
      }
    }
  }

  public static void main(String[] args) {

    new GeneratedFilesCleaner();
  }
}
