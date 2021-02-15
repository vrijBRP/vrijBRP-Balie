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

package nl.procura.gbaws.db.enums;

public enum PersonenWsDatabaseType {

  ONBEKEND(-1, "Onbekend"),
  PROCURA(0, "Procura database"),
  GBA_V(1, "GBA-V"),
  BRP(2, "BRP");

  private int    code;
  private String descr;

  PersonenWsDatabaseType(int code, String descr) {
    this.code = code;
    this.descr = descr;
  }

  public static PersonenWsDatabaseType get(int order) {
    for (PersonenWsDatabaseType so : values()) {
      if (so.getCode() == order) {
        return so;
      }
    }

    return ONBEKEND;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  @Override
  public String toString() {
    return descr;
  }

}
