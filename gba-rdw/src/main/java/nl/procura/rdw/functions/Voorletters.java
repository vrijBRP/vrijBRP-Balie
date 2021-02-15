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

package nl.procura.rdw.functions;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.trim;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.standard.diacrieten.ProcuraDiacrieten;

public class Voorletters {

  public static String getVoorletters(String diacvoornamen, boolean aangeleverd) {

    String voornamen = ProcuraDiacrieten.parseUT8String(diacvoornamen).getNormalString();

    // Aangeleverd wordt gebruikt in 1652 en niet in 1651 en 1653
    // Aangeleverd = true then ij = y

    if (emp(voornamen)) {
      return "";
    }

    String[] splits = voornamen.split("\\s+");
    String voorletters = "";

    List<String> voorl = new ArrayList<>();
    for (String naam : splits) {
      String voorletter = naam.substring(0, 1) + " ";

      if (naam.startsWith("ij")) {
        if (aangeleverd) {
          voorletter = "y";
        } else {
          voorletter = "ij";
        }
      } else if (naam.startsWith("IJ")) {
        if (aangeleverd) {
          voorletter = "Y";
        } else {
          voorletter = "IJ";
        }
      }

      voorl.add(voorletter.trim());
    }

    if (voorl.size() > 6) {
      voorletters = StringUtils.join(voorl.subList(0, 5), " ") + " -";
    } else if (!voorl.isEmpty()) {
      voorletters = StringUtils.join(voorl, " ");
    }

    if (aangeleverd) {
      // Bij 1652 mag maximaal 2 voorletters, geen spaties
      voorletters = voorletters.replaceAll("\\s+", "");

      if (voorletters.length() > 1) {
        // Maximaal 2 bij aangeleverd
        voorletters = voorletters.substring(0, 2);
      }
    }

    return trim(voorletters);
  }
}
