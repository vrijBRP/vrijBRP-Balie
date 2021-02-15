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

import static nl.procura.standard.Globalfunctions.*;

import java.awt.print.Book;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;

public class PrintUtils {

  public static Book getBook(byte[] documentBytes) throws IOException {
    PDDocument document = PDDocument.load(documentBytes);
    PDFPrintable printable = new PDFPrintable(document);
    Book book = new Book();
    book.append(printable, new PaperA4(), document.getNumberOfPages());
    return book;
  }

  /**
   * Vertaald een string naar nummers bijv. 1,2,5-10
   */
  public static List<Integer> getPages(String text) {
    List<Integer> nrs = new ArrayList<>();
    if (fil(text)) {
      for (String part : text.split(",")) { // split op komma
        if (pos(part)) {
          nrs.add(aval(part));
        } else { // Houd rekening met bijv. 4-10
          Matcher m = Pattern.compile("^([\\d]+)-([\\d]+)$").matcher(part);
          if (m.find()) {
            int start = aval(m.group(1));
            int end = aval(m.group(2));
            for (int nr = start; nr <= end; nr++) {
              nrs.add(nr);
            }
          }
        }
      }
    }

    return nrs;
  }
}
