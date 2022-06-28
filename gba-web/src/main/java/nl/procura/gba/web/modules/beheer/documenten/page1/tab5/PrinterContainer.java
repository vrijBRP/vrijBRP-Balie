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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab5;

import static nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptieType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.print.PrintService;

import nl.procura.bzconnector.app.client.actions.listprinters.Printer;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.connect.VrijBrpConnectClient;
import nl.procura.gba.web.services.zaken.documenten.printen.LocalPrinterUtils;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptieType;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class PrinterContainer extends ArrayListContainer {

  private final List<PrintOptieValue> printers = new ArrayList<>();

  public PrinterContainer() {
    printers.add(new PrintOptieValue(COMMAND, COMMAND.getCode(), COMMAND.getDescr()));
    printers.add(new PrintOptieValue(MIJN_OVERHEID, MIJN_OVERHEID.getCode(), MIJN_OVERHEID.getDescr()));
    addLocalPrinters();
  }

  public PrinterContainer(Services services) {
    this();
    addNetworkPrinters(services);
    addItems(printers, false);
  }

  public List<PrintOptieValue> getPrinters() {
    return printers;
  }

  public Optional<PrintOptieValue> get(PrintOptieType type, String name) {
    for (PrintOptieValue value : printers) {
      if (value.getType() == type && value.getStringValue().equals(name)) {
        return Optional.of(value);
      }
    }
    return Optional.empty();
  }

  private void addLocalPrinters() {
    for (PrintService pf : LocalPrinterUtils.getPrinters()) {
      String label = LOCAL_PRINTER.getDescr() + ": " + pf.getName();
      printers.add(new PrintOptieValue(LOCAL_PRINTER, pf.getName(), label));
    }
  }

  private void addNetworkPrinters(Services services) {
    VrijBrpConnectClient.of(services.getParameterService())
        .ifPresent(client -> {
          for (Printer printer : client.getPrinters()) {
            String label = NETWORK_PRINTER.getDescr() + ": " + printer.getName();
            PrintOptieValue value = new PrintOptieValue(NETWORK_PRINTER, printer.getName(), label);
            value.setNetworkPrinter(printer);
            printers.add(value);
          }
        });
  }
}
