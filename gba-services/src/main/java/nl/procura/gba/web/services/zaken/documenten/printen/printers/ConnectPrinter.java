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

import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import nl.procura.bzconnector.app.client.actions.listprinters.Printer;
import nl.procura.bzconnector.app.client.actions.print.*;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.connect.VrijBrpConnectClient;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionType;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

/**
 * This printer connects to the VrijBRP Connect application
 */
public class ConnectPrinter extends AbstractPrinter {

  private final VrijBrpConnectClient client;
  private final Printer              printer;

  public ConnectPrinter(PrintOptie printOptie, Services services) throws ProException {
    super(printOptie, services.getGebruiker());
    client = VrijBrpConnectClient.of(services.getParameterService())
        .orElseThrow(() -> new ProException(WARNING, "VrijBRP Connect is niet ingeschakeld"));
    printer = client.getPrinter(printOptie.getPrintoptie());
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
    PrintRequest request = new PrintRequest("test.pdf", documentBytes);
    request.setPrinter(printer.getName());
    request.setMedia(getMedia().toString().trim());
    request.setChromacity(getColor());
    request.setSides(PrintSide.ONE_SIDED);
    request.setOrientation(PrintOrientation.PORTRAIT);

    try {
      client.print(request);
    } catch (RuntimeException e) {
      throw new ProException(e.getMessage());
    }
  }

  private <T> T getOrElse(List<T> list, Predicate<T> filter, String message) {
    return list.stream().filter(filter).findFirst()
        .orElseThrow(() -> new ProException(ProExceptionType.CONFIG, message));
  }
}
