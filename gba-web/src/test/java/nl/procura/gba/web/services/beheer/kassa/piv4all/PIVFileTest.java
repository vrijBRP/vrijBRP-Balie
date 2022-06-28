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

package nl.procura.gba.web.services.beheer.kassa.piv4all;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import lombok.SneakyThrows;

public class PIVFileTest {

  @Test
  public void linesMustMatch() {
    String originalLine = "123456789 Duck, Donald             Key2Bz         1134021091913220000000000301+000001O+00000000";
    PIVLine originalPIVLine = PIVLine.builder()
        .line(originalLine)
        .build();

    PIVLine originalLineElements = PIVLine.builder()
        .klantId(originalPIVLine.getKlantId())
        .klantnaam(originalPIVLine.getKlantnaam())
        .zendendSysteem(originalPIVLine.getZendendSysteem())
        .gebruikerID(originalPIVLine.getGebruikerID())
        .kassaID(originalPIVLine.getKassaID())
        .productCode(originalPIVLine.getProductCode())
        .geleverdAantal(originalPIVLine.getGeleverdAantal())
        .datum(originalPIVLine.getDatum())
        .tijd(originalPIVLine.getTijd())
        .prijs(originalPIVLine.getPrijs())
        .build();

    PIVLine newPIVLine = PIVLine.builder()
        .klantId("123456789")
        .klantnaam("Duck, Donald")
        .zendendSysteem("Key2Bz")
        .gebruikerID("113")
        .kassaID(40)
        .productCode(301)
        .geleverdAantal(1)
        .datum(210919)
        .tijd(1322)
        .prijs(0)
        .build();

    System.out.println(originalPIVLine.toLineWithElements());
    Assert.assertEquals(originalLine, originalPIVLine.toLine());
    Assert.assertEquals(originalLine, originalLineElements.toLine());
    Assert.assertEquals(originalPIVLine.toLineWithElements(), originalLineElements.toLineWithElements());
    Assert.assertEquals(originalPIVLine.toLine(), newPIVLine.toLine());
    Assert.assertEquals(originalPIVLine.toLineWithElements(), newPIVLine.toLineWithElements());
  }

  @Test
  @SneakyThrows
  public void fileMustMatch() {

    PIVLine pivLine = PIVLine.builder()
        .klantId("123456789")
        .klantnaam("Duck, Donald")
        .zendendSysteem("Key2Bz")
        .gebruikerID("113")
        .kassaID(40)
        .productCode(301)
        .geleverdAantal(1)
        .datum(210919)
        .tijd(1322)
        .prijs(0)
        .build();

    compareFile(pivLine, "kas0043.piv");
  }

  @Test
  @SneakyThrows
  public void fileMustSupportDiacritics() {

    PIVLine pivLine = PIVLine.builder()
        .klantId("123456789")
        .klantnaam("Dúck, Donáld")
        .zendendSysteem("Key2Bz")
        .gebruikerID("")
        .kassaID(0)
        .productCode(301)
        .geleverdAantal(1)
        .prijs(0)
        .build();

    System.out.println(pivLine.toLine());
    System.out.println(pivLine.toLineWithElements());
  }

  private void compareFile(PIVLine pivLine, String file) {
    byte[] lineBytes = pivLine.toLine().getBytes();
    byte[] fileBytes = getFile(file);
    for (int i = 0; i < lineBytes.length; i++) {
      System.out.println(((char) fileBytes[i]) + " = " + ((char) lineBytes[i]));
      Assert.assertEquals(((char) fileBytes[i]), ((char) lineBytes[i]));
      Assert.assertEquals(((int) fileBytes[i]), ((int) lineBytes[i]));
    }
  }

  @Test
  public void filesMustMatch() {
    for (String file : getAllFiles()) {
      Assert.assertEquals(95, getFile(file).length);
    }
  }

  private List<String> getAllFiles() {
    return Arrays.asList("kas0043.piv", "kas0677.piv",
        "kas0685.piv", "kas0686.piv", "kas0824.piv",
        "kas0964.piv");
  }

  @SneakyThrows
  private byte[] getFile(String file) {
    return IOUtils
        .readLines(Objects.requireNonNull(PIVFileTest.class.getClassLoader().getResourceAsStream("piv/" + file))).get(0)
        .getBytes(UTF_8);
  }
}
