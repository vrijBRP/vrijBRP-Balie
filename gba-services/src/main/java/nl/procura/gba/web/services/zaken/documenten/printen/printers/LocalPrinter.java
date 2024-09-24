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

import static javax.print.DocFlavor.SERVICE_FORMATTED.PAGEABLE;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.awt.print.Book;
import java.io.IOException;

import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.*;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Media;

import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.zaken.documenten.printen.LocalPrinterUtils;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintUtils;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.commons.core.exceptions.ProException;

/**
 * This printer uses the internal JAVA print library
 */
public class LocalPrinter extends AbstractPrinter {

  private final PrintService printService;

  public LocalPrinter(PrintOptie printOptie, Gebruiker gebruiker) throws ProException {
    super(printOptie, gebruiker);
    printService = LocalPrinterUtils.getPrinter(printOptie.getPrintoptie())
        .orElseThrow(
            () -> new ProException(WARNING, "De printer {0} kan niet worden gevonden", printOptie.getPrintoptie()));
  }

  @Override
  public void printStream(byte[] documentBytes) throws ProException, IOException, PrintException {
    DocAttributeSet docAttributen = new HashDocAttributeSet();
    Book book = PrintUtils.getBook(documentBytes);
    SimpleDoc document = new SimpleDoc(book, PAGEABLE, docAttributen);
    DocPrintJob printJob = printService.createPrintJob();
    printJob.print(document, getPrintAttributes());
  }

  private PrintRequestAttributeSet getPrintAttributes() throws ProException {
    PrintRequestAttributeSet resultaat = new HashPrintRequestAttributeSet();
    for (Attribute attr : new Attribute[]{ getMedia(), getColor() }) {
      if (attr != null) {
        resultaat.add(attr);
      }
    }

    return resultaat;
  }

  private Chromaticity getColor() throws ProException {
    return LocalPrinterUtils.getColor(printService, getPrintOptie().getKleur()).orElse(null);
  }

  private Media getMedia() throws ProException {
    return LocalPrinterUtils.getMedia(printService, getPrintOptie().getMedia()).orElse(null);
  }
}
