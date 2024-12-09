/*
 * Copyright 2024 - 2025 Procura B.V.
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

import static nl.procura.rdw.functions.RdwProces.P0177_F1;
import static nl.procura.rdw.functions.RdwProces.P0178_F1;
import static nl.procura.rdw.functions.RdwProces.P0179_F1;
import static nl.procura.rdw.functions.RdwProces.P0181_F1;
import static nl.procura.rdw.functions.RdwProces.P0182_F1;
import static nl.procura.rdw.functions.RdwProces.P0183_F1;
import static nl.procura.rdw.functions.RdwProces.P0252_F1;
import static nl.procura.rdw.functions.RdwProces.P0252_F2;
import static nl.procura.rdw.functions.RdwProces.P1303_F1;
import static nl.procura.rdw.functions.RdwProces.P13_F1;
import static nl.procura.rdw.functions.RdwProces.P1651_F1;
import static nl.procura.rdw.functions.RdwProces.P1651_F2;
import static nl.procura.rdw.functions.RdwProces.P1652_F1;
import static nl.procura.rdw.functions.RdwProces.P1653_F1;
import static nl.procura.rdw.functions.RdwProces.P1654_F1;
import static nl.procura.rdw.functions.RdwProces.P1656_F1;
import static nl.procura.rdw.functions.RdwProces.P1658_F1;
import static nl.procura.rdw.functions.RdwProces.P1659_F1;
import static nl.procura.rdw.functions.RdwProces.P1660_F1;
import static nl.procura.rdw.functions.RdwProces.P1722_F1;
import static nl.procura.rdw.functions.RdwProces.P1908_F1;
import static nl.procura.rdw.functions.RdwProces.P1914_F1;
import static nl.procura.rdw.functions.RdwProces.get;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RdwTester {

  private static final String NORMAAL           = "normaal";
  private static final String MEERDERE_PERSONEN = "meedererPersonen";
  private static final String GEEN_PERSONEN     = "geenPersonen";
  private static final Logger LOGGER            = LoggerFactory.getLogger(RdwTester.class);

  public static Test getNormaal() {

    return new Test(NORMAAL) {

      @Override
      protected void init() {

        // 1651_8_30401

        getProcessen().add(new TestProces(P1651_F1, "1651_8/1651_8_101.xml"));
        getProcessen().add(new TestProces(P1653_F1, "1653_2/1653_2_1663.xml"));
      }
    };
  }

  @SuppressWarnings("unused")
  public static Test getGeenPersonen() {

    return new Test(GEEN_PERSONEN) {

      @Override
      protected void init() {

        getProcessen().add(new TestProces(P1651_F1, "1651_9/1651_9_1012.xml"));
        getProcessen().add(new TestProces(P1653_F1, "1653_2/1653_2_1663.xml"));
        getProcessen().add(new TestProces(P1654_F1, "1654_2/1654_2_1340.xml"));
        getProcessen().add(new TestProces(P1656_F1, "1656_2/1656_2_1178.xml")); // Geannuleerd
      }
    };
  }

  public static Test getMeerderePersonen() {

    return new Test(MEERDERE_PERSONEN) {

      @Override
      protected void init() {

        getProcessen().add(new TestProces(P13_F1, "13_8/13_8_1.xml"));
        getProcessen().add(new TestProces(P1303_F1, "1303_2/1303_2_1.xml"));

        //        getProcessen ().add (new TestProces (P1651_F1, "1651_9/1651_9_547.xml"));   // Geen personen
        getProcessen().add(new TestProces(P1651_F1, "1651_8/1651_8_30401.xml")); // Geen personen
        getProcessen().add(new TestProces(P1651_F2, "1651_8/1651_8_101.xml"));
        getProcessen().add(new TestProces(P1652_F1, "1652_2/1652_2_1018.xml"));
        getProcessen().add(new TestProces(P1653_F1, "1653_2/1653_2_16633.xml"));
        //        getProcessen().add(new TestProces(P1653_F1, "1653_2/1653_2_3703.xml"));  // Verlies / diefstal
        getProcessen().add(new TestProces(P1654_F1, "1654_2/1654_2_2859.xml"));

        getProcessen().add(new TestProces(P1656_F1, "1656_2/1656_2_103.xml")); // Geannuleerd
        //        getProcessen ().add (new TestProces (P1656_F1, "1656_2/1656_2_97.xml"));    // Niet uitgereikt
        //        getProcessen().add(new TestProces(P1656_F1, "1656_2/1656_2_91.xml")); // Uitgereikt
        //        getProcessen ().add (new TestProces (P1656_F1, "1656_2/1656_2_85.xml"));    // Geaccordeerd

        //        getProcessen().add(new TestProces(P0252_F1, "252_8/252_8_221b.xml"));
        getProcessen().add(new TestProces(P0252_F1, "252_8/252_8_40000.xml"));
        //        getProcessen().add(new TestProces(P0252_F1, "252_9/252_9_2.xml")); // Wachtwoord is fout
        getProcessen().add(new TestProces(P0252_F2, "252_7/252_7_1943.xml"));

        //        getProcessen().add(new TestProces(P1658_F1, "1658_2/1658_2_99.xml"));
        getProcessen().add(new TestProces(P1658_F1, "1658_2/1658_2_40000.xml"));

        //        getProcessen().add(new TestProces(P1659_F1, "1659_2/1659_2_403.xml")); // Geen nieuw rijbewijsnummer
        getProcessen().add(new TestProces(P1659_F1, "1659_2/1659_2_16895.xml")); // Geen nieuw rijbewijsnummer
        getProcessen().add(new TestProces(P1660_F1, "1660_2/1660_2_1.xml"));

        getProcessen().add(new TestProces(P1908_F1, "1908_2/1908_2_101.xml"));
        //        getProcessen().add(new TestProces(P1908_F1, "1908_2/1908_2_40000.xml"));
        //        getProcessen().add(new TestProces(P1908_F1, "1908_9/1908_9_603.xml"));

        getProcessen().add(new TestProces(P1722_F1, "1722_2/1722_2_40.xml"));
        getProcessen().add(new TestProces(P1722_F1, "1722_2/1722_2_48.xml"));

        getProcessen().add(new TestProces(P1914_F1, "1914_2/1914_2_22835.xml"));
        getProcessen().add(new TestProces(P1914_F1, "1914_2/1914_2_30264.xml"));
        getProcessen().add(new TestProces(P1914_F1, "1914_2/1914_2_5826.xml"));

        getProcessen().add(new TestProces(P0177_F1, "177_8/177_8_12613.xml"));
        getProcessen().add(new TestProces(P0178_F1, "178_8/178_8_3743.xml"));
        getProcessen().add(new TestProces(P0179_F1, "179_8/179_8_3743.xml"));
        getProcessen().add(new TestProces(P0181_F1, "181_8/181_8_1.xml"));
        getProcessen().add(new TestProces(P0182_F1, "182_8/182_8_1.xml"));
        getProcessen().add(new TestProces(P0183_F1, "183_8/183_8_1.xml"));
      }
    };
  }

  public Proces getResponse(Test test, Proces request) {

    try {
      List<TestProces> processList = new ArrayList<>();
      for (TestProces tp : test.getProcessen()) {
        if (tp.getProcesIn() == get(request.getProces(), request.getFunctie())) {
          processList.add(tp);
        }
      }

      if (!processList.isEmpty()) {
        TestProces tp = processList.get(new Random().nextInt(processList.size()));
        String dir = "C:/dev/tools/rijbewijzen/generated/";
        String name = dir + tp.getFileName();
        LOGGER.info("Testbestand: " + name);
        return RdwUtils.fromStream(new FileInputStream(name), true);
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    throw new IllegalArgumentException("Geen file voor " + request.getProces() + " - " + request.getFunctie());
  }

  public static class Test {

    private String name;
    private List<TestProces> processen = new ArrayList<>();

    public Test(String name) {
      this.name = name;
      init();
    }

    protected void init() {
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public List<TestProces> getProcessen() {
      return processen;
    }

    public void setProcessen(List<TestProces> processen) {
      this.processen = processen;
    }
  }

  public static class TestProces {

    private RdwProces procesIn = null;
    private String    fileName = "";

    TestProces(RdwProces p, String f) {
      setProcesIn(p);
      setFileName(f);
    }

    RdwProces getProcesIn() {
      return procesIn;
    }

    void setProcesIn(RdwProces procesIn) {
      this.procesIn = procesIn;
    }

    public String getFileName() {
      return fileName;
    }

    public void setFileName(String fileName) {
      this.fileName = fileName;
    }
  }
}
