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

import static nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource.GBAV;
import static nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource.PROCURA;
import static nl.procura.gbaws.testdata.Testdata.*;

import java.io.*;

import nl.procura.burgerzaken.gba.numbers.Anr;
import nl.procura.burgerzaken.gba.numbers.Bsn;
import nl.procura.diensten.gba.ple.base.PLEResult;
import nl.procura.diensten.gba.ple.base.converters.json.BasePLToJsonConverter;
import nl.procura.diensten.gba.ple.client.PLEHTTPClient;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.arguments.PLELoginArgs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeneratePLJsonFiles {

  public static void main(String[] args) {
    generateDemoFiles();
    generateRvigTestFiles();
    generateGbavTestFiles();
  }

  private static void generateDemoFiles() {
    Credentials credentials = new Credentials("<username>", "<pw>", "<url>");
    generate(TEST_BSN_101, TEST_ANR_101, credentials, GBAV, "demo");
    generate(TEST_BSN_102, TEST_ANR_102, credentials, GBAV, "demo");
    generate(TEST_BSN_103, TEST_ANR_103, credentials, GBAV, "demo");
    generate(TEST_BSN_104, TEST_ANR_104, credentials, GBAV, "demo");
    generate(TEST_BSN_1, TEST_ANR_1, credentials, PROCURA, "demo");
    generate(TEST_BSN_2, TEST_ANR_2, credentials, PROCURA, "demo");
    generate(TEST_BSN_3, TEST_ANR_3, credentials, PROCURA, "demo");
    generate(TEST_BSN_4, TEST_ANR_4, credentials, PROCURA, "demo");
    generate(TEST_BSN_5, TEST_ANR_5, credentials, PROCURA, "demo");
    generate(TEST_BSN_10, TEST_ANR_10, credentials, PROCURA, "demo");
  }

  private static void generateRvigTestFiles() {
    Credentials credentials = new Credentials("<username>", "<pw>", "<url>");
    getRvIGBsns().forEach(bsn -> generate(bsn, -1L, credentials, PROCURA, "rvig"));
  }

  private static void generateGbavTestFiles() {
    Credentials credentials = new Credentials("<username>", "<pw>", "<url>");
    getGbaVBsns().forEach(bsn -> generate(bsn, -1L, credentials, GBAV, "gbav"));
  }

  private static void generate(Long bsn,
      Long anr,
      Credentials credentials,
      PLEDatasource datasource,
      String folderPath) {

    File folder = new File(folderPath);
    folder.mkdirs();

    Bsn bsnObj = new Bsn(bsn);
    if (bsnObj.isCorrect()) {
      File bsnfile = new File(folder, bsnObj + ".json");
      saveData(bsnfile, credentials, bsn, datasource);
    }

    Anr anrObj = new Anr(anr);
    if (anrObj.isCorrect()) {
      File anrfile = new File(folder, anrObj + ".json");
      saveData(anrfile, credentials, anr, datasource);
    }
  }

  /**
   * Save personal data (persoonslijst) of the given person in the given file.
   */
  private static void saveData(File file,
      Credentials credentials,
      Long id,
      PLEDatasource datasource) {

    PLEArgs args = new PLEArgs();
    args.addNummer(id.toString());
    args.setDatasource(datasource);

    PLEHTTPClient client = getPLEClient(credentials);
    PLEResult result = client.find(args, new PLELoginArgs(credentials.getUsername(), credentials.getPw()));
    assert result.getBasePLs().size() == 1;

    try (OutputStream outputStream = new FileOutputStream(file)) {
      BasePLToJsonConverter.toStream(outputStream, result);
      log.info("Writing file to " + file);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Data
  @AllArgsConstructor
  private static class Credentials {

    private String username, pw, url;
  }

  private static PLEHTTPClient getPLEClient(Credentials credentials) {
    return new PLEHTTPClient(credentials.getUrl() + "/gba");
  }
}
