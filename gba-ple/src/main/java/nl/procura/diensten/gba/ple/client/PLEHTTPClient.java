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

package nl.procura.diensten.gba.ple.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.*;

import org.apache.commons.io.IOUtils;

import nl.procura.diensten.gba.ple.base.BasePLBuilder;
import nl.procura.diensten.gba.ple.base.PLEMessage;
import nl.procura.diensten.gba.ple.base.PLEResult;
import nl.procura.diensten.gba.ple.base.converters.java.BasePLToJavaConverter;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLECatArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLELoginArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLESearchArgs;
import nl.procura.diensten.gba.ple.procura.utils.PLECommandLineUtils;
import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEArgsConverter;
import nl.procura.standard.security.Base64;

public class PLEHTTPClient {

  private String endpoint;

  public PLEHTTPClient(String endpoint) {
    this.endpoint = endpoint;
  }

  public PLEResult find(PLESearchArgs searchArgs, PLECatArgs catArgs, PLELoginArgs loginArgs) {
    return find(PLEArgsConverter.convert(searchArgs, catArgs), loginArgs);
  }

  public PLEResult find(PLEArgs pleArgs, PLELoginArgs loginArgs) {

    try {
      URL url = new URL(null, endpoint, new URLStreamHandler() {

        @Override
        protected URLConnection openConnection(URL url) throws IOException {
          URL clone_url = new URL(url.toString());
          HttpURLConnection clone_urlconnection = (HttpURLConnection) clone_url.openConnection();
          clone_urlconnection.setConnectTimeout(35000);
          clone_urlconnection.setReadTimeout(35000);
          return (clone_urlconnection);
        }
      });

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);

      if (loginArgs != null) {
        String encoding = Base64.encodeBytes(String.format("%s:%s",
            loginArgs.getUname(),
            loginArgs.getPasswd()).getBytes());
        connection.setRequestProperty("Authorization", "Basic " + encoding);
      }

      BasePLBuilder builder = new BasePLBuilder();
      findSerialize(connection, builder, getCommandLine(pleArgs));
      return builder.getResult();

    } catch (MalformedURLException e) {
      throw new RuntimeException("Foutieve URL: " + endpoint, e);
    } catch (IOException e) {
      throw new RuntimeException("Fout bij openen verbinding", e);
    }
  }

  private String getCommandLine(PLEArgs argumenten) {
    return PLECommandLineUtils.convert(argumenten);
  }

  private void findSerialize(URLConnection connection, BasePLBuilder plBuilder, String commandLine) {
    try {
      OutputStreamWriter out = null;
      try {
        String data = URLEncoder.encode(commandLine, "UTF-8");
        out = new OutputStreamWriter(connection.getOutputStream());
        IOUtils.write(data, out);
        out.flush();
      } finally {
        IOUtils.closeQuietly(out);
      }

      plBuilder.setResult(BasePLToJavaConverter.fromStream(connection.getInputStream()));
    } catch (SocketTimeoutException e) {
      String message = "Het zoeken duurde te lang en is voortijdig afgebroken. De maximale zoektijd is 30 seconden.";
      plBuilder.getResult().getMessages().add(new PLEMessage(1200, message));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Onbekende URL.", e);
    } catch (ConnectException e) {
      String message = "Er kan geen verbindingen worden gemaakt met de Proweb Personen webservice.";
      throw new RuntimeException(message, e);
    } catch (IOException e) {
      String message = "Er is een fout opgetreden bij aanspreken van de Proweb Personen webservice.";
      throw new RuntimeException(message, e);
    }
  }
}
