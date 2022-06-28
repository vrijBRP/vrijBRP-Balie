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
import static nl.procura.standard.exceptions.ProExceptionType.CONFIG;
import static nl.procura.standard.exceptions.ProExceptionType.ENTRY;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

import org.apache.commons.net.ftp.FTPClient;

import nl.procura.bzconnector.app.client.VrijBRPConnectRestClient;
import nl.procura.gba.web.services.beheer.kassa.KassaSendType;
import nl.procura.standard.exceptions.ProException;

public class DefaultParameterValidator implements ParameterValidator {

  private static final long IEDEREEN  = 0;
  private static final int  ALLES     = 4;
  private static final int  LOGGED_IN = 230;

  @Override
  public void validate(
      ParameterType parameterType,
      long cUsr, Map<ParameterType, String> map,
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

    // Kassa using FTP
    if (KASSA_SEND_TYPE.equals(parameterType)) {
      String type = map.get(KASSA_SEND_TYPE);
      if (KassaSendType.FTP == KassaSendType.get(type)) {
        String url = map.get(KASSA_FTP_URL);
        String username = map.get(KASSA_FTP_USERNAME);
        String password = map.get(KASSA_FTP_PW);
        checkFtp(url, username, password);
      }

      // Kassa using vrijBRP Connect
      if (KassaSendType.CONNECT == KassaSendType.get(type)) {
        boolean connectEnabled = isTru(parameters.getParm(BZ_CONNECT_ENABLED));
        String url = parameters.getParm(BZ_CONNECT_URL);
        String username = parameters.getParm(BZ_CONNECT_USERNAME);
        String password = parameters.getParm(BZ_CONNECT_PW);
        checkConnect(connectEnabled, url, username, password);
      }
    }

    // VrijBRP Connect
    if (BZ_CONNECT_ENABLED.equals(parameterType)) {
      if (isTru(map.get(BZ_CONNECT_ENABLED))) {
        String url = map.get(BZ_CONNECT_URL);
        String username = map.get(BZ_CONNECT_USERNAME);
        String password = map.get(BZ_CONNECT_PW);
        checkConnect(true, url, username, password);
      }
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

  /**
   * Controleer of toegang tot vrijBRP Connect service werkt.
   */
  private void checkConnect(boolean enabled, String url, String username, String password) {
    if (!enabled) {
      throw new ProException(CONFIG, "VrijBRP Connect is niet ingeschakeld");
    }
    try {
      VrijBRPConnectRestClient.builder()
          .username(username)
          .password(password)
          .url(url)
          .timeoutInSeconds(10)
          .build()
          .listPrinters();
    } catch (Exception e) {
      throw new ProException(ENTRY, "Er kan geen verbinding worden gemaakt met: " + url.trim() + ".", e);
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

  private void checkURL(ParameterType parameterType, ParameterType eqParameter, Map<ParameterType, String> map) {
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
