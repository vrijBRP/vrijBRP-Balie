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

package nl.procura.diensten.gbav.utils.vraag;

import static nl.procura.standard.Globalfunctions.aval;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.diensten.gba.ple.base.BasePLBuilder;
import nl.procura.diensten.gba.ple.base.BasePLValue;

public class GbavAntwoordConverter {

  protected static String parseResultaatOmschrijving(String s) {
    if (s.toLowerCase().startsWith("geen autorisatie voor rubriek")) {
      Matcher m = Pattern.compile("\\d+.$").matcher(s);
      if (m.find()) {
        String rubriek = m.group();
        if (rubriek.length() == 7) {
          String cat = rubriek.substring(0, 2);
          String elem = rubriek.substring(2, 6);
          GBAGroupElements.GBAGroupElem e = GBAGroupElements.getByCat(aval(cat), aval(elem));
          GBACat c = GBACat.getByCode(aval(cat));
          if ((c != null) && (e != null)) {
            return "Geen autorisatie voor rubriek: " + c.getDescr() + " - "
                + e.getElem().getDescr() + " (" + cat + "." + elem + ")";
          }
        }
        return rubriek;
      }
    }

    return s;
  }

  /**
   * If element is 910 also add 911 (Code of municipality)
   */
  protected static void checkNummer(BasePLBuilder plBuilder, int elementCode, String elementWaarde) {
    if (elementCode == GBAElem.GEM_INSCHR.getCode()) {
      plBuilder.setElem(GBAElem.GEM_INSCHR_CODE, new BasePLValue(elementWaarde));
    }
  }
}
