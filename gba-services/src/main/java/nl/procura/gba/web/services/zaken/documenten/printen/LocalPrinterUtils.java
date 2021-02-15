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

import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.PrinterName;

import nl.procura.standard.exceptions.ProException;

public class LocalPrinterUtils {

  public static Optional<Chromaticity> getColor(PrintService p, String name) throws ProException {
    Chromaticity color = null;
    if (fil(name)) {
      color = getAttribute(p, Chromaticity.class, "kleur", name);
    }
    return Optional.ofNullable(color);
  }

  public static Optional<Media> getMedia(PrintService p, String name) throws ProException {
    Media media = null;
    if (fil(name)) {
      media = getAttribute(p, Media.class, "lade", name);
    }

    return Optional.ofNullable(media);
  }

  public static List<Chromaticity> getColors(PrintService p) {
    return getAttributes(p, Chromaticity.class);
  }

  public static List<Media> getMedia(PrintService p) {
    return getAttributes(p, Media.class);
  }

  public static Optional<PrintService> getPrinter(String name) throws ProException {
    try {
      return Optional.ofNullable(getPrinters(name)[0]);
    } catch (ArrayIndexOutOfBoundsException e) {
      return Optional.empty();
    }
  }

  public static PrintService[] getPrinters() {
    return getPrinters(null, null);
  }

  private static PrintService[] getPrinters(DocFlavor docFlavor, AttributeSet attributeSet) {
    return PrintServiceLookup.lookupPrintServices(docFlavor, attributeSet);
  }

  private static PrintService[] getPrinters(String name) throws ProException {
    PrintServiceAttributeSet attributeSet = new HashPrintServiceAttributeSet();
    PrintService[] printers = new PrintService[0];

    if (fil(name)) {
      attributeSet.add(new PrinterName(name, null));
      printers = getPrinters(null, attributeSet);
    }

    return printers;
  }

  private static <T extends Attribute> T getAttribute(PrintService p, Class<T> cl, String type, String name) {
    return getAttributes(p, cl)
        .stream()
        .filter(c -> name.trim().equalsIgnoreCase(c.toString().trim()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            "Foutieve printoptie: printer heeft geen " + type + " genaamd " + name));
  }

  private static <T extends Attribute> List<T> getAttributes(PrintService p, Class<T> cl) {
    List<T> list = new ArrayList<>();
    Object values = p.getSupportedAttributeValues(cl, null, null);
    if (values instanceof Object[]) {
      for (Object obj : (Object[]) values) {
        list.add((T) obj);
      }
    }
    return list;
  }
}
