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

package nl.procura.gba.web.services.bs.algemeen.functies;

import static nl.procura.standard.Globalfunctions.fil;

public class BsAkteUtils {

  /**
   * Dit is het BRP aktenummer wat wordt opgeslagen in de BRP.
   * Deze is met een extra letter.
   */
  public static String getBrpAktenummer(String nummer, String letter) {
    if (fil(nummer) && nummer.length() == 7) {
      if (fil(letter)) {
        return nummer.substring(0, 2) + letter + nummer.substring(3);
      }
    }
    return nummer;
  }

  /**
   * Dit is het aktenummer wat op de akte komt te staan.
   * Hierbij wordt de letter verwijderd als het aktenummer 7 posities is.
   */
  public static String getBsAktenummer(String nummer) {
    // Haal het derde teken er tussenuit.
    if (fil(nummer) && nummer.length() == 7) {
      nummer = nummer.substring(0, 2) + nummer.substring(3);
    }
    return nummer;
  }
}
