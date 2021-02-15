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

package nl.procura.bcgba.v12.misc;

public enum BcGbaCode {

  ONBEKEND(0, "Onbekend"),
  SYSTEEMFOUT(4, "Systeemfout"),
  TEVEEL_VRAGEN(6, "Teveel vragen"),
  GEEN_VRAGEN(7, "Geen vragen"),
  GEEN_VRAAGNUMMER(11, "Geen vraagnummer"),
  VRAAG_NUMMER_NIET_1(13, "Vraagnummer_niet_1"),
  VRAAG_NUMMER_NIET_OPLOPEND(14, "Vraagnummer niet oplopend"),
  VERWERKING_SUCCESS(12000, "Verwerking bericht succesvol"),
  GEEN_RESULTATEN(12001, "Geen resultaten"),
  RESULTAAT_GEVONDEN(12002, "Resultaat gevonden"),
  TE_VEEL_ZOEKRESULTATEN(12003, "Teveel zoekresultaten"),
  INVALIDE_ID_GEGEVENS(12004, "Invalide idgegevens"),
  TWEE_PERSONEN_ZELFDE_BSN(12005, "Twee personen zelfde BSN");

  private int    code;
  private String oms;

  BcGbaCode(int code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static BcGbaCode get(int code) {
    for (BcGbaCode c : values()) {
      if (c.getCode() == code) {
        return c;
      }
    }

    return ONBEKEND;
  }

  public int getCode() {
    return code;
  }

  public String getOms() {
    return oms;
  }
}
