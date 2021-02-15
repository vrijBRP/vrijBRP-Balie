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

package nl.procura.gba.web.services.zaken.documenten.printen;

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.*;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.isTru;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import nl.procura.bzconnector.app.client.BzAppClient;
import nl.procura.bzconnector.app.client.PKCredentials;
import nl.procura.bzconnector.app.client.actions.listprinters.ListPrintersRequest;
import nl.procura.bzconnector.app.client.actions.listprinters.Printer;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;

public class NetworkPrinterUtils {

  public static Optional<BzAppClient> getClient(ParameterService parameterService) {

    BzAppClient client = null;
    boolean enabled = isTru(getParm(parameterService, BZ_CONNECT_ENABLED));

    if (enabled) {
      String pkk = getParm(parameterService, BZ_CONNECT_PKK);
      String pkkPassphrase = getParm(parameterService, BZ_CONNECT_PASSPHRASE);
      String username = getParm(parameterService, BZ_CONNECT_USERNAME);
      String host = getParm(parameterService, BZ_CONNECT_HOST);
      int port = aval(getParm(parameterService, BZ_CONNECT_PORT));

      client = BzAppClient.builder()
          .pkCredentials(new PKCredentials(pkk, pkkPassphrase))
          .username(username)
          .host(host)
          .port(port)
          .timeout(30000)
          .build();
    }

    return Optional.ofNullable(client);
  }

  /**
   * Returns all the printers
   */
  public static List<Printer> getPrinters(BzAppClient client) {
    List<Printer> printers = new ArrayList<>();
    BzAppClient.BzAppSession session = client.newSession();
    ListPrintersRequest listRequest = new ListPrintersRequest();
    session.listPrinters(listRequest, response -> {
      printers.addAll(response.getPrinters());
    });
    return printers;
  }

  /**
   * Returns the printer based on the name
   */
  public static Optional<Printer> getPrinter(BzAppClient client, String name) {
    for (Printer printer : getPrinters(client)) {
      if (Objects.equals(printer.getName(), name)) {
        return Optional.of(printer);
      }
    }
    return Optional.empty();
  }

  private static String getParm(ParameterService parameterService, ParameterConstant bzConnectPkk) {
    return parameterService.getSysteemParm(bzConnectPkk, true);
  }
}
