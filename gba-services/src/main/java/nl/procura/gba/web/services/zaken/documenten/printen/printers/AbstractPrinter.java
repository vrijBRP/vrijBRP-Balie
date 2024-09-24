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

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.PRINT;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.print.PrintException;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;

public abstract class AbstractPrinter {

  private PrintOptie printOptie;
  private Gebruiker  gebruiker;

  public AbstractPrinter(PrintOptie po, Gebruiker gebruiker) {
    this.printOptie = po;
    this.gebruiker = gebruiker;
  }

  public void print(byte[] documentBytes) throws ProException {
    final ExecutorService service = Executors.newSingleThreadExecutor();
    Future<Boolean> printAction = service.submit(new PrintCallable(documentBytes));

    try {
      printAction.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new ProException(PRINT, WARNING, e.getMessage());
    }

    try {
      service.shutdown();
      service.awaitTermination(30, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new ProException(PRINT, WARNING, "De printactie is onderbroken. Het duurde meer dan 30 seconden.");
    } finally {
      service.shutdownNow();
    }
  }

  public abstract void printStream(byte[] documentBytes) throws ProException, IOException, PrintException;

  public PrintOptie getPrintOptie() {
    return printOptie;
  }

  public Gebruiker getGebruiker() {
    return gebruiker;
  }

  private class PrintCallable implements Callable<Boolean> {

    private final byte[] documentBytes;

    private PrintCallable(byte[] documentBytes) {
      this.documentBytes = documentBytes;
    }

    @Override
    public Boolean call() throws IOException, PrintException {
      printStream(documentBytes);
      return true;
    }
  }
}
