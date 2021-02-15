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

package nl.procura.gbaws.generator;

import static java.util.Arrays.asList;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;
import static nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource.GBAV;
import static nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource.PROCURA;
import static nl.procura.gbaws.testdata.Testdata.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

import nl.procura.burgerzaken.gba.numbers.Anr;
import nl.procura.burgerzaken.gba.numbers.Bsn;
import nl.procura.diensten.gba.ple.base.PLEResult;
import nl.procura.diensten.gba.ple.base.converters.json.BasePLToJsonConverter;
import nl.procura.diensten.gba.ple.client.PLEHTTPClient;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.arguments.PLELoginArgs;
import nl.procura.gbaws.testdata.Testdata;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeneratePLJsonFiles {

  public static void main(String[] args) {
    generate(TEST_BSN_101, TEST_ANR_101, GBAV);
    generate(TEST_BSN_102, TEST_ANR_102, GBAV);
    generate(TEST_BSN_103, TEST_ANR_103, GBAV);
    generate(TEST_BSN_104, TEST_ANR_104, GBAV);
    generate(TEST_BSN_1, TEST_ANR_1, PROCURA);
    generate(TEST_BSN_2, TEST_ANR_2, PROCURA);
    generate(TEST_BSN_3, TEST_ANR_3, PROCURA);
    generate(TEST_BSN_4, TEST_ANR_4, PROCURA);
    generate(TEST_BSN_5, TEST_ANR_5, PROCURA);
    generate(TEST_BSN_10, TEST_ANR_10, PROCURA);
  }

  private static void generate(Long bsn, Long anr, PLEDatasource datasource) {
    String un = "<username>";
    String pw = "<password>";
    String url = "http://pc16:8088/personen-ws";

    Path bsnfile = Paths.get(new Bsn(bsn) + ".json");
    Path anrfile = Paths.get(new Anr(anr) + ".json");
    savePersonalData(bsnfile, url, un, pw, bsn, datasource);
    savePersonalData(anrfile, url, un, pw, bsn, datasource);

    PLEResult bsnResult = Testdata.getPersonalData(bsn);
    PLEResult anrResult = Testdata.getPersonalData(anr);
    assert bsnResult.getBasePLs().size() > 0;
    assert anrResult.getBasePLs().size() > 0;
  }

  /**
   * Save personal data (persoonslijst) of the given person in the given file.
   * <p>   *
   *
   * @param file the destination file
   * @param personWebServiceUrl the Web Service URL e.g. https://personen-demo.procura.nl/personen-ws
   * @param username the username for the Web Service
   * @param password the password for the Web Service
   * @param nr the BSN or ANR for the person
   */
  private static void savePersonalData(Path file,
      String personWebServiceUrl,
      String username,
      String password,
      Long nr,
      PLEDatasource datasource) {
    PLEArgs arguments = new PLEArgs();
    arguments.addNummer(nr.toString());
    arguments.setDatasource(datasource);
    // only categories 1,5,7,8,10 until we need more for a test
    arguments.setCategories(new HashSet<>(asList(PERSOON, HUW_GPS, NATIO, KINDEREN, INSCHR, VB, VBTITEL)));
    PLEHTTPClient client = new PLEHTTPClient(personWebServiceUrl + "/gba");
    PLEResult result = client.find(arguments, new PLELoginArgs(username, password));
    try (OutputStream outputStream = Files.newOutputStream(file)) {
      BasePLToJsonConverter.toStream(outputStream, result);
      log.info("Writing file to " + file);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
