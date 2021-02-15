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

import java.io.InputStream;

import nl.procura.diensten.gba.ple.base.PLEResult;
import nl.procura.diensten.gba.ple.base.converters.json.BasePLToJsonConverter;
import nl.procura.validation.Anummer;
import nl.procura.validation.Bsn;

/**
 * This is test data. The ID's used are provided by the RVIG
 */
public class Testdata {

  // BSN's
  public static Long TEST_BSN_1 = 1351L;      // Lögius, Marciano
  public static Long TEST_BSN_2 = 999994335L; // van der Kaart-Logius, Jonkvrouw Madonna
  public static Long TEST_BSN_3 = 2045L;      // Baron van Logius, Josephus
  public static Long TEST_BSN_4 = 3761L;      // Lögius, Helena
  public static Long TEST_BSN_5 = 3116L;      // Logius, Luciano

  public static Long TEST_BSN_10 = 999991346L; // Rodney, Plager

  public static Long TEST_BSN_101 = 9830L;      // Graaf van der Ven-Van Roedelrode tot Broekzucht, Theodoor-Christiaan R.Ph
  public static Long TEST_BSN_102 = 999990068L; // Groeman, Rozen
  public static Long TEST_BSN_103 = 999993781L; // Groeman, Vanessa
  public static Long TEST_BSN_104 = 999990093L; // Dewansingh, Rajesh

  // A-numbers
  public static Long TEST_ANR_1 = 5463824651L; // Lögius, Marciano
  public static Long TEST_ANR_2 = 8134128629L; // van der Kaart-Logius, Jonkvrouw Madonna
  public static Long TEST_ANR_3 = 2571707121L; // Baron van Logius, Josephus
  public static Long TEST_ANR_4 = 4097263409L; // Lögius, Helena
  public static Long TEST_ANR_5 = 9581378012L; // Logius, Luciano

  public static Long TEST_ANR_10 = 9471808205L; // Rodney, Plager

  public static Long TEST_ANR_101 = 5348068307L; // Graaf van der Ven-Van Roedelrode tot Broekzucht, Theodoor-Christiaan R.Ph
  public static Long TEST_ANR_102 = 1904190875L; // Groeman, Rozen
  public static Long TEST_ANR_103 = 4859782797L; // Groeman, Vanessa
  public static Long TEST_ANR_104 = 6412328378L; // Dewansingh, Rajesh

  /**
   * Get personal data (persoonslijst) for given BSN or / A-nr.
   */
  public static PLEResult getPersonalData(long nr) {
    String bsn = new Bsn(nr).getDefaultBsn();
    String anr = new Anummer(nr).getAnummer();
    InputStream bsnStream = Testdata.class.getClassLoader().getResourceAsStream(bsn + ".json");
    if (bsnStream == null) {
      InputStream anrStream = Testdata.class.getClassLoader().getResourceAsStream(anr + ".json");
      if (anrStream == null) {
        throw new IllegalStateException("Personal data of " + nr + " has not been found");
      }
    }
    return BasePLToJsonConverter.fromStream(bsnStream);
  }
}
