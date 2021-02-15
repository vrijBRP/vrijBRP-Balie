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

package nl.procura.gba.web.services.zaken.documenten.stempel;

public enum PositieType {

  LINKSBOVEN(0, "Linksboven"),
  RECHTSBOVEN(1, "Rechtsboven"),
  LINKSONDER(2, "Linksonder"),
  RECHTSONDER(3, "Rechtsonder"),
  WOORD(4, "Een bepaald woord"),
  ONBEKEND(-1, "Onbekend");

  private long   code = -1;
  private String oms;

  PositieType(int code, String oms) {
    this.setCode(code);
    this.oms = oms;
  }

  public static PositieType get(long code) {
    for (PositieType type : PositieType.values()) {
      if (code == type.getCode()) {
        return type;
      }
    }

    return ONBEKEND;
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

  public boolean is(PositieType... types) {
    for (PositieType type : types) {
      if (type.getCode() == code) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
