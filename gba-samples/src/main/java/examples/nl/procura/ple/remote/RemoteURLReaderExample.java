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

package examples.nl.procura.ple.remote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gbaws.testdata.Testdata;
import nl.procura.standard.security.Base64;

public class RemoteURLReaderExample extends AbstractTestRemote {

  private final static Logger LOGGER = LoggerFactory.getLogger(RemoteURLReaderExample.class.getName());

  public RemoteURLReaderExample() {
    test("http://localhost:8088/personen-ws/gba");
  }

  public static void main(String[] args) {
    new RemoteURLReaderExample();
  }

  public void test(String url) {

    try {
      String s = "-c 1,2,3,4,5,6,7,8,9,10,11,12,13,14,21,31,32,33,34 -ple -actueel -b \"" + Testdata.TEST_BSN_1
          + "\" -e \"0\" -mf \"100\" -type 2";
      URLConnection conn = new URL(url).openConnection();
      String encoding = Base64.encodeBytes((USER + ":" + PW).getBytes());
      conn.setRequestProperty("Authorization", "Basic " + encoding);
      conn.setDoOutput(true);
      conn.setConnectTimeout(30000);
      conn.setReadTimeout(30000);

      OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
      wr.write(s);
      wr.flush();

      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

      String line;
      while ((line = rd.readLine()) != null) {
        LOGGER.info(line);
      }

      wr.close();
      rd.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
