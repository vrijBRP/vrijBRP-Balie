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

package nl.procura.gba.web.services.zaken.correspondentie;

public enum CorrespondentieType {

  ANDERS(1, "Anders"),
  VERLOPEN_REISDOCUMENT(2, "Verlopen reisdocument"),
  VERLOPEN_TERMIJN_TOESTEMMING(3, "Verlopen termijn voor toestemming"),
  ONBEKEND(-1, "Onbekend");

  private long   code;
  private String oms;

  CorrespondentieType(long code, String oms) {

    this.code = code;
    this.oms = oms;
  }

  public static CorrespondentieType get(long code) {

    for (CorrespondentieType a : values()) {
      if (a.getCode() == code) {
        return a;
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

  @Override
  public String toString() {
    return getOms();
  }
}
