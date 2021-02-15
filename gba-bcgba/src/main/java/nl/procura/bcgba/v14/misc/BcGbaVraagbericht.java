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

package nl.procura.bcgba.v14.misc;

import nl.procura.standard.exceptions.ProException;

public enum BcGbaVraagbericht {

  VRAAG1(1, "Vraag 1: Uitsluitend zoeken op identificerende gegevens"),
  VRAAG2(2, "Vraag 2: Zoeken op identificerende gegevens en buitenlands persoonsnummer"),
  VRAAG3(3, "Vraag 3: Uitsluitend zoeken op buitenlands persoonsnummer"),
  VRAAG4(4, "Vraag 4: Zoeken zonder geslachtsnaam");

  private int    code;
  private String oms;

  BcGbaVraagbericht(int code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static BcGbaVraagbericht get(int code) {
    for (BcGbaVraagbericht c : values()) {
      if (c.getCode() == code) {
        return c;
      }
    }

    throw new ProException("Onbekend vraagbericht: " + code);
  }

  public int getCode() {
    return code;
  }

  public String getOms() {
    return oms;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
