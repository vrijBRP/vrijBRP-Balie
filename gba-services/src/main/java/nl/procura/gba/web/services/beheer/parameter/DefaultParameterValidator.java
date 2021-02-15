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

package nl.procura.gba.web.services.beheer.parameter;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.*;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.ENTRY;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

import org.apache.commons.net.ftp.FTPClient;

import nl.procura.standard.exceptions.ProException;

public class DefaultParameterValidator implements ParameterValidator {

  private static final long IEDEREEN  = 0;
  private static final int  ALLES     = 4;
  private static final int  LOGGED_IN = 230;

  @Override
  public void validate(ParameterType parameterType, long cUsr, Map<ParameterType, String> map,
      ParameterService parameters) {

    // Algemeen
    if (cUsr == IEDEREEN && WACHTWOORD_VERLOOP == parameterType) {
      String waarde = map.get(WACHTWOORD_VERLOOP);
      if (emp(waarde)) {
        throw new ProException(ENTRY, WARNING, "Een lege waarde betekent dat het wachtwoord niet verloopt.");
      } else if (!pos(waarde)) {
        throw new ProException(ENTRY, WARNING,
            "De waarde " + waarde + " betekent dat het wachtwoord niet verloopt.");
      }
    }

    // Presentievraag
    checkURL(parameterType, PRESENTIE_VRAAG_ENDPOINT, map, parameters);

    // Verificatievraag
    checkURL(parameterType, VER_VRAAG_ENDPOINT, map, parameters);

    // Curateleregister
    checkURL(parameterType, CURATELE_URL, map);

    // Zoeken
    checkURL(parameterType, ZOEK_PLE_JAVA_SERVER_URL, map);

    // COVOG
    checkURL(parameterType, COVOG_URL, map, parameters);

    // TMV
    checkURL(parameterType, TMV_URL, map, parameters);

    // RDW
    checkURL(parameterType, RYB_URL, map, parameters);

    // Kassa
    if (KASSA_FTP_URL.equals(parameterType)) {
      String url = map.get(KASSA_FTP_URL);
      String username = map.get(KASSA_FTP_USERNAME);
      String password = map.get(KASSA_FTP_PW);

      checkFtp(url, username, password);
    }

    // Protocollering
    if (PROT_STORE_PL.equals(parameterType)) {
      if (aval(map.get(PROT_STORE_PL)) == ALLES) {
        throw new ProException(ENTRY, "Protocolleren van alle gegevens levert veel data op in de database");
      }
    }
  }

  /**
   * Controleer of toegang tot de FTP werkt.
   */
  private void checkFtp(String url, String username, String password) {

    if (fil(url)) {

      FTPClient ftp = new FTPClient();

      try {
        ftp.connect(url);
        ftp.login(username, password);

        if (ftp.getReplyCode() != LOGGED_IN) {
          throw new RuntimeException(ftp.getReplyString());
        }
      } catch (Exception e) {
        throw new ProException(ENTRY, "Er kan geen verbinding worden gemaakt met: " + url.trim() + ".", e);
      } finally {
        try {
          ftp.disconnect();
        } catch (IOException ignored) {}
      }
    }
  }

  /**
   * Controleer of de URL kan worden benaderd.
   */
  private void checkURL(String url) {

    if (fil(url)) {
      try {
        URLConnection conn = new URL(url).openConnection();
        conn.setConnectTimeout(2000);
        conn.setReadTimeout(2000);
        conn.connect();
      } catch (Exception e) {
        if (!(e instanceof SSLHandshakeException)) {
          e.printStackTrace();
          throw new ProException(ENTRY, "Er kan geen verbinding worden gemaakt met: " + url.trim() + ".", e);
        }
      }
    }
  }

  private void checkURL(ParameterType parameterType, ParameterType eqParameter,
      Map<ParameterType, String> map) {
    checkURL(parameterType, eqParameter, map, null);
  }

  private void checkURL(ParameterType parameterType, ParameterType eqParameter, Map<ParameterType, String> map,
      ParameterService parameters) {
    if (parameterType == eqParameter) {
      if (parameters != null) {
        if (emp(parameters.getParm(SSL_PROXY_URL))) {
          throw new ProException(ENTRY, "Vul eerst bij algemene instellingen de SSL Proxy URL in.");
        }
        checkURL(map.get(eqParameter));
      }
    }
  }
}
