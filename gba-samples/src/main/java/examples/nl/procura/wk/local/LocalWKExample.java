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

package examples.nl.procura.wk.local;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gba.wk.baseWK.BaseWKMessage;
import nl.procura.diensten.gba.wk.procura.WK;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.diensten.gba.wk.utils.Arguments2CommandLine;
import nl.procura.diensten.gba.wk.utils.CommandLine2Arguments;
import nl.procura.diensten.gba.wk.utils.ObjectViewer;

import examples.nl.procura.ple.local.AbstractLocalExample;

public class LocalWKExample extends AbstractLocalExample {

  private final static Logger LOGGER = LoggerFactory.getLogger(LocalWKExample.class.getName());

  public LocalWKExample() {

    try {
      long st = System.currentTimeMillis();

      ZoekArgumenten zoekArgumenten = new ZoekArgumenten();
      zoekArgumenten.setHuisnummer("1");
      zoekArgumenten.setPostcode("1703BA");
      zoekArgumenten.setGeen_bewoners(false);
      zoekArgumenten.setExtra_pl_gegevens(false);

      Arguments2CommandLine c = new Arguments2CommandLine(zoekArgumenten);
      LOGGER.info("Command: " + c.getCommandLine());
      ZoekArgumenten z = new CommandLine2Arguments(c.getCommandLine()).getArgumenten();

      WK wk = new WK(createManager(), z);
      try {
        wk.find();
      } catch (Exception e) {
        e.printStackTrace();
      }

      long et = System.currentTimeMillis();

      LOGGER.info("Convert");

      // serialize (wk);
      toAdressen(wk);
      ObjectViewer.view(wk);

      LOGGER.info("Resultaat");
      LOGGER.info("----------------:");
      LOGGER.info("Aantal..........: " + wk.getBuilder().getZoekResultaat().getBaseWKs().size());
      LOGGER.info("Tijd............: " + (et - st) + "ms.");
      LOGGER.info("Aantal meldingen: " + wk.getBuilder().getZoekResultaat().getMessages().size());

      for (BaseWKMessage melding : wk.getBuilder().getZoekResultaat().getMessages()) {
        LOGGER.info(" - " + melding.getCode() + " = " + melding.getWaarde());
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  } // Test_local_pl

  public static void main(String[] args) {

    new LocalWKExample();
  } // main

  public void serialize(WK wk) {
    wk.getBuilder().serialize(opslaan("tmp/wk.ser"));
  } // serialize

  private void toAdressen(WK wk) {
    wk.getBuilder().toAdressen();
  } // toPersoonslijsten

  private OutputStream opslaan(String filename) {

    try {
      return new FileOutputStream(filename);
    } catch (Exception e1) {
      // Hoeft niet te worden afgevangen
    }

    return null;
  } // opslaan
}
