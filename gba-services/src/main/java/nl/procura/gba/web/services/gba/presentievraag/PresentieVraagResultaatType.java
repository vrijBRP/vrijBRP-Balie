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

package nl.procura.gba.web.services.gba.presentievraag;

public enum PresentieVraagResultaatType {

  GEEN_RESULTATEN(12001, "Geen resultaten gevonden"),
  RESULTAAT_GEVONDEN(12002, "Resultaat gevonden"),
  TEVEEL_RESULTATEN(12003, "Te veel resultaten gevonden"),
  VRAAG_VOLDOET_NIET(12004, "Vraag voldoet niet aan een toegestaan zoekpad"),
  ZELFDE_BSN(12005, "Uniciteitsprobleem geconstateerd"),
  OVERGESLAGEN(2, "Overgeslagen"),
  NOG_NIET_UITGEVOERD(1, "Nog niet uitgevoerd"),
  ONBEKEND(0, "Onbekend");

  private long   code;
  private String oms;

  PresentieVraagResultaatType(int code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static PresentieVraagResultaatType get(int code) {
    for (PresentieVraagResultaatType e : values()) {
      if (e.getCode() == code) {
        return e;
      }
    }

    return ONBEKEND;
  }

  public long getCode() {
    return code;
  }

  public String getOms() {
    return oms;
  }

  public boolean is(int code) {
    return this.code == code;
  }

  public boolean is(PresentieVraagResultaatType... types) {
    for (PresentieVraagResultaatType type : types) {
      if (type.getCode() == code) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return oms;
  }
}
