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

package nl.procura.diensten.gba.ple.extensions.formats;

public enum GeheimType {

  GEEN(0, "Geen"),
  GEDEELTELIJK(3, "Gedeeltelijk"),
  MAXIMALE(7, "Maximaal"),
  ONBEKEND(99, "Onbekend");

  private long   code = 0;
  private String oms  = "";

  GeheimType(long code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static GeheimType get(int code) {
    switch (code) {
      case 0:
        return GEEN;
      case 7:
        return MAXIMALE;
      default:
        return GEDEELTELIJK;
    }
  }

  @Override
  public String toString() {
    return getCode() + ": " + getOms();
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public long getCode() {
    return code;
  }

  public void setCode(long code) {
    this.code = code;
  }
}
