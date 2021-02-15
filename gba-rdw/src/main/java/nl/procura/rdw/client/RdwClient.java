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

package nl.procura.rdw.client;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import nl.procura.rdw.functions.Proces;
import nl.procura.rdw.functions.RdwUtils;

public class RdwClient {

  public RdwClient() {
  }

  public static Proces send(String targetURL, InputStream is) {

    return send(targetURL, RdwUtils.fromStream(is, false));
  }

  public static Proces send(String targetURL, Proces proces) {

    URL url;

    HttpURLConnection connection = null;

    try {
      url = new URL(targetURL);
      connection = (HttpURLConnection) url.openConnection();

      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

      connection.setUseCaches(false);
      connection.setDoInput(true);
      connection.setDoOutput(true);
      connection.setReadTimeout(30000);
      connection.setConnectTimeout(30000);

      RdwUtils.toStream(proces, connection.getOutputStream(), false, true);

      connection.getOutputStream().flush();

      return RdwUtils.fromStream(connection.getInputStream(), true);
    } catch (Exception e) {
      throw new RuntimeException("Fout aanroepen RDW webservice.", e);
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }
}
