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

package nl.procura.gba.web.services.beheer.connect;

import static java.nio.charset.StandardCharsets.UTF_8;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.*;
import static nl.procura.standard.Globalfunctions.isTru;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import nl.procura.bzconnector.app.client.VrijBRPConnectRestClient;
import nl.procura.bzconnector.app.client.actions.listprinters.Printer;
import nl.procura.bzconnector.app.client.actions.print.PrintRequest;
import nl.procura.bzconnector.app.client.actions.print.SaveFileRequest;
import nl.procura.gba.web.services.beheer.kassa.KassaFile;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.standard.exceptions.ProException;

public class VrijBrpConnectClient {

  private VrijBRPConnectRestClient restClient;

  private VrijBrpConnectClient(VrijBRPConnectRestClient restClient) {
    this.restClient = restClient;
  }

  public static Optional<VrijBrpConnectClient> of(ParameterService parameterService) {
    if (isTru(getParm(parameterService, BZ_CONNECT_ENABLED))) {
      return Optional.of(new VrijBrpConnectClient(VrijBRPConnectRestClient.builder()
          .username(getParm(parameterService, BZ_CONNECT_USERNAME))
          .password(getParm(parameterService, BZ_CONNECT_PW))
          .url(getParm(parameterService, BZ_CONNECT_URL))
          .timeoutInSeconds(10)
          .build()));
    }
    return Optional.empty();
  }

  /**
   * Returns all the printers
   */
  public List<Printer> getPrinters() {
    return restClient.listPrinters().getPrinters();
  }

  /**
   * Returns the printer based on the name
   */
  public Printer getPrinter(String name) {
    return getPrinters().stream()
        .filter(printer -> Objects.equals(printer.getName(), name))
        .findFirst()
        .orElseThrow(() -> new ProException(WARNING, "De printer {0} kan niet worden gevonden", name));
  }

  public void sendToKassa(KassaFile bestand) {
    File file = new File(bestand.getFilename());
    restClient.saveFile(new SaveFileRequest(file.getParent(), file.getName(),
        bestand.getContent().getBytes(UTF_8)));
  }

  public void print(PrintRequest request) {
    restClient.print(request);
  }

  private static String getParm(ParameterService parameterService, ParameterConstant bzConnectPkk) {
    return parameterService.getSysteemParm(bzConnectPkk, true);
  }
}
