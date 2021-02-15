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

package nl.procura.diensten.gba.ple.base.converters.ple;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pad_left;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import nl.procura.diensten.gba.ple.base.*;

/**
 * Used in ZoekPersoon.pl for PROBEL
 *
 * DO NOT DELETE
 */
public class BasePLToPleConverter {

  public static void toStream(OutputStream os, PLEResult result) {
    StringBuilder buffer = new StringBuilder();
    List<BasePL> basePLs = result.getBasePLs();
    List<PLEMessage> messages = result.getMessages();

    for (PLEMessage melding : messages) {
      add(buffer, "MELD|" + melding.getCode() + "|" + melding.getDescr());
    }

    for (BasePL pl : basePLs) {
      for (BasePLCat cat : pl.getCats()) {
        for (BasePLSet set : cat.getSets()) {
          for (BasePLRec record : set.getRecs()) {
            String hist = "A";
            switch (record.getStatus()) {
              case MUTATION:
                hist = "<";
                break;
              case HIST:
                hist = "Z";
                break;
              default:
                break;
            }

            add(buffer, cat.getCatType().getCode(), 4, hist);
            add(buffer, cat.getCatType().getCode(), 5, astr(set.getExtIndex()));

            for (BasePLElem element : record.getElems()) {
              if (element.isEmpty()) {
                continue;
              }

              add(buffer, record.getCatType().getCode(), element.getElemCode(), element.getValue());
            }
            add(buffer, record.getCatType().getCode(), 9999, "@");
          }
          add(buffer, 99, 9999, "0");
        }
      }
    }

    String s = buffer.toString();

    try {
      BufferedOutputStream bos = new BufferedOutputStream(os);
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(bos, StandardCharsets.UTF_8));
      out.write(s);
      out.flush();
    } catch (Exception e) {
      // Don't catch this exception
    }
  }

  private static void add(StringBuilder sb, int catCode, int elemCode, String value) {
    add(sb, catCode, elemCode, new BasePLValue(value));
  }

  private static void add(StringBuilder sb, int catCode, int elemCode, BasePLValue value) {
    String cat = pad_left(astr(catCode), "0", 2);
    String elem = pad_left(astr(elemCode), "0", 4);
    sb.append(cat)
        .append("|")
        .append(elem)
        .append("|")
        .append(value.getCode())
        .append("|")
        .append(value.getVal())
        .append("|")
        .append(value.getDescr())
        .append("\n");
  }

  private static void add(StringBuilder sb, String s) {
    sb.append(s).append("\n");
  }
}
