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

package nl.procura.gbaws.testdata;

import static java.util.Collections.singletonList;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

import nl.procura.diensten.gba.ple.base.PLEResult;
import nl.procura.diensten.gba.ple.base.converters.json.BasePLToJsonConverter;

/**
 * This is test data. The ID's used are provided by the RVIG
 */
public class Testdata {

  public enum DataSet {
    DEMO,
    RVIG,
    GBAV
  }

  // BSN's Procura
  public static Long TEST_BSN_1  = 1351L;      // Lögius, Marciano
  public static Long TEST_BSN_2  = 999994335L; // van der Kaart-Logius, Jonkvrouw Madonna
  public static Long TEST_BSN_3  = 2045L;      // Baron van Logius, Josephus
  public static Long TEST_BSN_4  = 3761L;      // Lögius, Helena
  public static Long TEST_BSN_5  = 3116L;      // Logius, Luciano
  public static Long TEST_BSN_10 = 999991346L; // Rodney, Plager

  // BSN's GBA-V
  public static Long TEST_BSN_101 = 9830L;      // Graaf van der Ven-Van Roedelrode tot Broekzucht, Theodoor-Christiaan R.Ph
  public static Long TEST_BSN_102 = 999990068L; // Groeman, Rozen
  public static Long TEST_BSN_103 = 999993781L; // Groeman, Vanessa
  public static Long TEST_BSN_104 = 999990093L; // Dewansingh, Rajesh

  // A-numbers Demo
  public static Long TEST_ANR_1  = 5463824651L; // Lögius, Marciano
  public static Long TEST_ANR_2  = 8134128629L; // van der Kaart-Logius, Jonkvrouw Madonna
  public static Long TEST_ANR_3  = 2571707121L; // Baron van Logius, Josephus
  public static Long TEST_ANR_4  = 4097263409L; // Lögius, Helena
  public static Long TEST_ANR_5  = 9581378012L; // Logius, Luciano
  public static Long TEST_ANR_10 = 9471808205L; // Rodney, Plager

  //  A-numbers GBA-V
  public static Long TEST_ANR_101 = 5348068307L; // Graaf van der Ven-Van Roedelrode tot Broekzucht, Theodoor-Christiaan R.Ph
  public static Long TEST_ANR_102 = 1904190875L; // Groeman, Rozen
  public static Long TEST_ANR_103 = 4859782797L; // Groeman, Vanessa
  public static Long TEST_ANR_104 = 6412328378L; // Dewansingh, Rajesh

  public static PLEResult getPersonData(Long number, DataSet dataSet) {
    return getPersonData(singletonList(number), dataSet);
  }

  /**
   * Get personal data (persoonslijst) for given BSN or / A-numbers.
   */
  public static PLEResult getPersonData(List<Long> numbers, DataSet dataSet) {
    PLEResult pleResult = new PLEResult();
    for (byte[] b : getPersonDataAsBytes(numbers, dataSet)) {
      PLEResult numberResult = BasePLToJsonConverter.fromStream(new ByteArrayInputStream(b));
      pleResult.getBasePLs().addAll(numberResult.getBasePLs());
      pleResult.getMessages().addAll(numberResult.getMessages());
    }
    return pleResult;
  }

  /**
   * Get personal data (persoonslijst) for given BSN or / A-numbers.
   */
  public static byte[] getPersonDataAsBytes(Long number, DataSet dataSet) {
    return getPersonDataAsBytes(singletonList(number), dataSet).stream().findFirst().orElse(null);
  }

  /**
   * Get personal data (persoonslijst) for given BSN or / A-numbers.
   */
  public static List<byte[]> getPersonDataAsBytes(List<Long> numbers, DataSet dataSet) {
    List<byte[]> list = new ArrayList<>();
    try {
      for (long number : numbers) {
        list.add(getEntry(dataSet, number));
      }
    } catch (IOException e) {
      throw new RuntimeException("Cannot read ZIP file with testdata", e);
    }
    return list;
  }

  public static Set<Long> getRvIGBsns() {
    return getBsnsFromFile("rvig-test-bsn.txt");
  }

  public static Set<Long> getGbaVBsns() {
    return getBsnsFromFile("gbav-test-bsn.txt");
  }

  private static Set<Long> getBsnsFromFile(String fileName) {
    try {
      InputStream resource = Testdata.class.getClassLoader().getResourceAsStream(fileName);
      return IOUtils.readLines(resource).stream().map(Long::valueOf).collect(Collectors.toSet());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static byte[] getEntry(DataSet dataSet, long number) throws IOException {
    String bsn = String.format("%09d", number);
    String anr = String.format("%10d", number);
    InputStream resource = Testdata.class.getClassLoader().getResourceAsStream("gba-test-people.zip");
    assert resource != null;
    ZipInputStream zipInputStream = new ZipInputStream(resource, StandardCharsets.UTF_8);
    ZipEntry nextEntry;
    while ((nextEntry = zipInputStream.getNextEntry()) != null) {
      boolean isBsn = nextEntry.getName().equals(dataSet.name().toLowerCase() + "/" + bsn + ".json");
      boolean isAnr = nextEntry.getName().equals(dataSet.name().toLowerCase() + "/" + anr + ".json");
      if (isBsn || isAnr)
        return IOUtils.toByteArray(zipInputStream);
    }
    throw new IllegalStateException("Personal data of " + number + " has not been found");
  }
}
