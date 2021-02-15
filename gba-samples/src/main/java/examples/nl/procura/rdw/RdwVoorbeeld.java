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

package examples.nl.procura.rdw;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class RdwVoorbeeld {

  public RdwVoorbeeld() throws FileNotFoundException {

    excecutePost("https://<ip>/start.rdw",
        new FileInputStream("src/main/resources/examples/1651-01.xml"));
  }

  public static void main(String[] args) throws FileNotFoundException {
    new RdwVoorbeeld();
  }

  public static void excecutePost(String targetURL, InputStream fis) {

    URL url;
    HttpURLConnection connection = null;

    try {
      url = new URL(targetURL);
      connection = (HttpURLConnection) url.openConnection();
      connection.setConnectTimeout(30000);
      connection.setReadTimeout(30000);

      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

      connection.setUseCaches(false);
      connection.setDoInput(true);
      connection.setDoOutput(true);

      IOUtils.copy(fis, connection.getOutputStream());

      connection.getOutputStream().flush();

      for (String line : IOUtils.readLines(connection.getInputStream())) {

        System.out.println("Line: " + line);
      }
    } catch (Exception e) {
      e.printStackTrace();

    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }
}
