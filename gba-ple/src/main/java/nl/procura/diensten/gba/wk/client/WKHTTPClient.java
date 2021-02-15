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

package nl.procura.diensten.gba.wk.client;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.*;

import org.apache.commons.io.IOUtils;

import nl.procura.diensten.gba.ple.procura.arguments.PLELoginArgs;
import nl.procura.diensten.gba.wk.baseWK.BaseWKBuilder;
import nl.procura.diensten.gba.wk.baseWK.BaseWKMessage;
import nl.procura.diensten.gba.wk.baseWK.WkSearchResult;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.diensten.gba.wk.utils.Arguments2CommandLine;
import nl.procura.standard.security.Base64;

public class WKHTTPClient {

  public static final int OBJECT         = 0;
  private ZoekArgumenten  zoekArgumenten = new ZoekArgumenten();
  private String          url            = "";
  private int             type           = OBJECT;

  public WKHTTPClient(String url) {
    setUrl(url);
  }

  public WkSearchResult find(ZoekArgumenten zoekArgumenten) {
    return find(zoekArgumenten, null);
  }

  public WkSearchResult find(ZoekArgumenten zoekArgumenten, PLELoginArgs loginArgumenten) {

    setZoekArgumenten(zoekArgumenten);

    try {
      URL url = new URL(getUrl());
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setConnectTimeout(30000);
      connection.setReadTimeout(30000);
      connection.setDoOutput(true);

      if (loginArgumenten != null) {
        String encoding = Base64.encodeBytes(
            (loginArgumenten.getUname() + ":" + loginArgumenten.getPasswd()).getBytes());
        connection.setRequestProperty("Authorization", "Basic " + encoding);
      }

      BaseWKBuilder basisWkHandler = new BaseWKBuilder();

      findSerialize(connection, basisWkHandler);

      return basisWkHandler.getZoekResultaat();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new WkSearchResult();
  }

  public ZoekArgumenten getZoekArgumenten() {
    return zoekArgumenten;
  }

  public void setZoekArgumenten(ZoekArgumenten zoekArgumenten) {
    this.zoekArgumenten = zoekArgumenten;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  private String getCommandLine() {

    return new Arguments2CommandLine(getZoekArgumenten()).getCommandLine();
  }

  private void findSerialize(URLConnection connection, BaseWKBuilder basisWkHandler) {

    try {
      OutputStreamWriter out = null;

      try {
        String data = URLEncoder.encode(getCommandLine(), "UTF-8");
        out = new OutputStreamWriter(connection.getOutputStream());

        out.write(data);
        out.close();
      } finally {
        try {
          if (out != null) {
            out.flush();
          }
        } catch (Exception e) {
          // Hoeft niet te worden afgevangen.}
        }

        IOUtils.closeQuietly(out);
      }

      basisWkHandler.deserialize(connection.getInputStream());
    } catch (SocketTimeoutException e) {
      basisWkHandler.getZoekResultaat().getMessages().add(
          new BaseWKMessage(1200, "Het zoeken duurde te lang en is afgebroken."));
    } catch (IOException e) {
      throw new RuntimeException("Er kan geen verbindingen worden gemaakt met de GBA webservice.");
    }
  }
}
