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

package nl.procura.gba.web.services.zaken.documenten.printen.printers;

import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import nl.procura.bzconnector.app.client.BzAppClient;
import nl.procura.bzconnector.app.client.actions.ActionResponse;
import nl.procura.bzconnector.app.client.actions.StatusType;
import nl.procura.bzconnector.app.client.actions.listprinters.Printer;
import nl.procura.bzconnector.app.client.actions.print.*;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.documenten.printen.NetworkPrinterUtils;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionType;

/**
 * This printer connects to the Burgerzaken Connect © application
 */
public class NetworkPrinter extends AbstractPrinter {

  private final BzAppClient client;
  private final Printer     printer;

  public NetworkPrinter(PrintOptie printOptie, Services services)
      throws ProException {
    super(printOptie, services.getGebruiker());

    client = NetworkPrinterUtils.getClient(services.getParameterService()).orElseThrow(
        () -> new ProException(WARNING, "Controleer de parameters voor het gebruik van de netwerkprinters"));

    printer = NetworkPrinterUtils.getPrinter(client, printOptie.getPrintoptie()).orElseThrow(
        () -> new ProException(WARNING, "De printer {0} kan niet worden gevonden", printOptie.getPrintoptie()));
  }

  public PrintChromacity getColor() throws ProException {
    String color = getPrintOptie().getKleur();
    if (StringUtils.isBlank(color)) {
      throw new ProException(ProExceptionType.CONFIG, "Geen standaarkleur ingesteld voor deze printer");
    }
    return getOrElse(printer.getChromacities(), c -> c.toString().trim().equalsIgnoreCase(color.trim()),
        "Onbekende kleur voor deze printer: " + color);
  }

  public PrintMedia getMedia() throws ProException {
    String media = getPrintOptie().getMedia();
    if (StringUtils.isBlank(media)) {
      throw new ProException(ProExceptionType.CONFIG, "Geen lade/media ingesteld voor deze printer");
    }
    return getOrElse(printer.getMedia(), c -> c.toString().trim().equalsIgnoreCase(media.trim()),
        "Onbekende lade/media voor de printer: " + media);
  }

  @Override
  public void printStream(byte[] documentBytes) throws ProException {
    PrintRequest printAction = new PrintRequest("test.pdf", documentBytes);
    printAction.setPrinter(printer.getName());
    printAction.setMedia(getMedia().toString().trim());
    printAction.setChromacity(getColor());
    printAction.setSides(PrintSide.ONE_SIDED);
    printAction.setOrientation(PrintOrientation.PORTRAIT);

    BzAppClient.BzAppSession session = client.newSession();
    session.print(printAction, response -> {
      ActionResponse actionResponse = (ActionResponse) response;
      if (StatusType.ERROR == actionResponse.getStatus()) {
        throw new ProException(actionResponse.getMsg());
      }
    });
  }

  private <T> T getOrElse(List<T> list, Predicate<T> filter, String message) {
    return list.stream().filter(filter).findFirst()
        .orElseThrow(() -> new ProException(ProExceptionType.CONFIG, message));
  }
}
