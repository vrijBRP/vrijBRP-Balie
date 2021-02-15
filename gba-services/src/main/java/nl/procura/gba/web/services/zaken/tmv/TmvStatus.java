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

package nl.procura.gba.web.services.zaken.tmv;

import static nl.procura.standard.Globalfunctions.pos;

public enum TmvStatus {

  AANGEMELD(1, "Aangemeld in TMV"),
  DOORMELDING(2, "Doormelding gedaan aan gemeente"),
  INONDERZOEK(3, "In onderzoek bij gemeente"),
  GESLOTEN(4, "Dossier gesloten"),
  ONBEKEND(0, "Onbekend");

  private long   code = 0;
  private String oms  = "";

  TmvStatus(long code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static TmvStatus get(long code) {

    for (TmvStatus a : values()) {
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
    return pos(getCode()) ? getCode() + ": " + getOms() : getOms();
  }
}
