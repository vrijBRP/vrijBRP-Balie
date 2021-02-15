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

package nl.procura.gba.jpa.personenws.db;

public enum GbavProfileType {

  BEPERKT(0, "Beperkt GBA-V profiel"),
  VOLLEDIG(1, "Volledig GBA-V profiel");

  private final int    code;
  private final String oms;

  GbavProfileType(int code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static GbavProfileType get(int code) {
    for (final GbavProfileType type : values()) {
      if (type.getCode() == code) {
        return type;
      }
    }

    return BEPERKT;
  }

  @Override
  public String toString() {
    return getOms();
  }

  public int getCode() {
    return code;
  }

  public String getOms() {
    return oms;
  }
}
