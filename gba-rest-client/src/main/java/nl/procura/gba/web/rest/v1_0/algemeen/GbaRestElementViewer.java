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

package nl.procura.gba.web.rest.v1_0.algemeen;

import static nl.procura.standard.Globalfunctions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.proweb.rest.v1_0.meldingen.ProRestMelding;

public class GbaRestElementViewer {

  private final static Logger LOGGER = LoggerFactory.getLogger(GbaRestElementViewer.class.getName());

  public static void info(GbaRestElementVraag vraag) {
    info(vraag.getVraagElement());
  }

  public static void info(GbaRestElementAntwoord antwoord) {

    for (ProRestMelding melding : antwoord.getMeldingen()) {
      LOGGER.info("Melding: " + melding.getOmschrijving());
    }

    info(antwoord.getAntwoordElement());
  }

  public static void info(GbaRestElement element) {
    for (String line : toString(element).split("\n")) {
      LOGGER.info(line);
    }
  }

  public static String toString(GbaRestElement element) {
    int l1 = getMaxType(0, 0, element);
    int l2 = getMaxWaarde(0, element);
    return toString(new StringBuilder(), 0, l1, l2, element);
  }

  private static String toString(StringBuilder sb, int indent, int l1, int l2, GbaRestElement element) {

    String waarde = "";

    if (element.heeftWaarden()) {
      if (element.heeftWaarden()) {
        String c = element.getCode();
        String w = element.getWaarde();
        String o = element.getOmschrijving();

        waarde = " = " + (pad_left(c, " ", 5) + " " + pad_right(w + " ", " ", l2));

        if (fil(o)) {
          waarde += " " + trim(" (" + o + ")");
        }
      }
    }

    sb.append(pad_left("", " ", indent) + pad_right(element.getType(), " ",
        (l1 - indent)) + waarde + "\n");

    if (element.heeftElementen()) {
      for (GbaRestElement subElement : element.getElementen()) {
        toString(sb, indent + 2, l1, l2, subElement);
      }
    }

    return sb.toString();
  }

  private static int getMaxType(int max, int indent, GbaRestElement element) {
    if (element.heeftWaarden()) {
      int length = (element.getType().length() + indent);
      if (length > max) {
        max = length;
      }
    }
    if (element.heeftElementen()) {
      for (GbaRestElement subElement : element.getElementen()) {
        max = getMaxType(max, indent + 2, subElement);
      }
    }
    return max;
  }

  private static int getMaxWaarde(int max, GbaRestElement element) {
    if (element.heeftWaarden()) {
      int length = (element.getWaarde().length());
      if (length > max) {
        max = length;
      }
    }
    if (element.heeftElementen()) {
      for (GbaRestElement subElement : element.getElementen()) {
        max = getMaxWaarde(max, subElement);
      }
    }
    return max;
  }
}
