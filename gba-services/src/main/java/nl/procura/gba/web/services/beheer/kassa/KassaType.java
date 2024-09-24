/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.services.beheer.kassa;

public enum KassaType {

  ANDERS(12, "Anders"),
  COVOG(4, "Covog aanvraag"),
  GPK(5, "GPK aanvraag"),
  JEUGDTARIEF_REISDOC(8, "Jeugdtarief reisdocument"),
  ONBEKEND(0, "Onbekend"),
  REISDOCUMENT(1, "Reisdocument"),
  RIJBEWIJS(2, "Rijbewijs"),
  SPOED_REISDOC(9, "Spoed reisdocument"),
  SPOED_RIJBEWIJS(10, "Spoed rijbewijs"),
  UITTREKSEL(3, "Uittreksel"),
  //  VERMISS_REISDOC(6, "Vermissing reisdocument"), // Removed
  //  VERMISS_RIJBEWIJS(7, "Vermissing rijbewijs"), // Removed
  BEZORGING_REISDOC(11, "Thuisbezorging reisdocument");

  private String oms = "";
  private int    nr  = 0;

  KassaType(int nr, String descr) {

    setNr(nr);
    setOms(descr);
  }

  public static KassaType getType(int nr) {

    for (KassaType t : values()) {
      if (t.getNr() == nr) {
        return t;
      }
    }

    return ONBEKEND;
  }

  public int getNr() {
    return nr;
  }

  public void setNr(int nr) {
    this.nr = nr;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public boolean is(KassaType... types) {
    if (types != null) {
      for (KassaType type : types) {
        if (type != null) {
          if (type.getNr() == getNr()) {
            return true;
          }
        }
      }
    }

    return false;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
