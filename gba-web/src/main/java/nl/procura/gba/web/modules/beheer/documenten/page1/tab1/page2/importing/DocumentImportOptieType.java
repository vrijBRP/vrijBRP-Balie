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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.importing;

public enum DocumentImportOptieType {

  OVERSCHRIJVEN(0, "Altijd overschrijven"),
  AAN(1, "Altijd aanzetten"),
  UIT(2, "Altijd uitzetten"),
  NIET_WIJZIGEN(3, "Niet wijzigen"),
  INITIEEL_OVERNEMEN(4, "Initieel overnemen");

  private long   code = 0;
  private String oms  = "";

  DocumentImportOptieType(long code, String oms) {
    setCode(code);
    setOms(oms);
  }

  public static DocumentImportOptieType get(long code) {
    for (DocumentImportOptieType var : values()) {
      if (var.getCode() == code) {
        return var;
      }
    }

    return OVERSCHRIJVEN;
  }

  public long getCode() {
    return code;
  }

  public void setCode(long code) {
    this.code = code;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
