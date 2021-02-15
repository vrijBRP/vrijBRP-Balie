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

package examples.nl.procura.ple.local;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nl.procura.diensten.gba.ple.base.BasePLViewer;
import nl.procura.diensten.gba.ple.base.PLEMessage;
import nl.procura.diensten.gba.ple.base.PLEResult;
import nl.procura.diensten.gba.ple.base.converters.java.BasePLToJavaConverter;
import nl.procura.diensten.gba.ple.base.converters.json.BasePLToJsonConverter;
import nl.procura.diensten.gba.ple.base.converters.persoonlijst.BasePLToPersoonsLijstConverter;
import nl.procura.diensten.gba.ple.procura.PLE;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.utils.PLECommandLineUtils;
import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpaManager;
import nl.procura.diensten.zoekpersoon.objecten.Persoonslijst;
import nl.procura.gbaws.testdata.Testdata;

public class LocalPLExample extends AbstractLocalExample {

  private final static Logger LOGGER = LoggerFactory.getLogger(LocalPLExample.class.getName());

  public LocalPLExample() {

    try {
      PLEArgs args = new PLEArgs();
      args.addNummer(Testdata.TEST_BSN_1.toString());
      args.addNummer(Testdata.TEST_BSN_2.toString());

      args.setDatasource(PLEDatasource.PROCURA);
      args.setShowHistory(true);
      args.setShowArchives(true);
      args.setShowMutations(true);
      args.setMaxFindCount(100);

      String cl = PLECommandLineUtils.convert(args);
      args = PLECommandLineUtils.convert(cl);
      LOGGER.info("Command: " + cl);

      PLEJpaManager plEm = createManager();
      PLE ple = new PLE(plEm, args);

      try {
        ple.find();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        IOUtils.closeQuietly(plEm);
      }

      LOGGER.info("Convert");

      toPersonsList(ple);
      toJava(ple);
      toBasic(ple);
      toJson(ple);
      showResult(ple);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new LocalPLExample();
  }

  public void toBasic(PLE ple) {
    BasePLViewer.view(ple.getBuilder().getResult().getBasePLs());
  }

  public void toJava(PLE ple) {
    BasePLToJavaConverter.toStream(System.out, ple.getBuilder().getResult());
  }

  public void toJson(PLE ple) {
    BasePLToJsonConverter.toStream(System.out, ple.getBuilder().getResult());
  }

  public void toPersonsList(PLE ple) {
    PLEResult resultaat = ple.getBuilder().getResult();
    BasePLToPersoonsLijstConverter converter = new BasePLToPersoonsLijstConverter();
    List<Persoonslijst> pls = converter.convert(resultaat.getBasePLs(), true, true, false);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    System.out.println(gson.toJson(pls));
  }

  public void showResult(PLE ple) {
    LOGGER.info("Result");
    LOGGER.info("----------------:");
    LOGGER.info("Count ..........: " + ple.getBuilder().getResult().getBasePLs().size());
    LOGGER.info("Messages .......: " + ple.getBuilder().getResult().getMessages().size());
    for (PLEMessage message : ple.getBuilder().getResult().getMessages()) {
      LOGGER.info(" - " + message.getCode() + " = " + message.getDescr());
    }
  }
}
