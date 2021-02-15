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

package nl.procura.gba.web.services.bs.onderzoek.enums;

public enum VermoedAdresType {

  ONBEKEND(0, "Onbekend", BetrokkeneType.ONBEKEND),
  IN_GEMEENTE(1, "In deze gemeente", BetrokkeneType.BINNEN),
  ANDERE_GEMEENTE(2, "In andere gemeente", BetrokkeneType.NAAR_ANDERE),
  BUITENLAND(3, "In buitenland", BetrokkeneType.EMIGRATIE);

  private int            code = 0;
  private String         oms  = "";
  private BetrokkeneType betrokkeneType;

  VermoedAdresType(int code, String oms, BetrokkeneType betrokkeneType) {
    this.code = code;
    this.oms = oms;
    this.betrokkeneType = betrokkeneType;
  }

  public static VermoedAdresType get(int code) {
    for (VermoedAdresType a : values()) {
      if (a.getCode() == code) {
        return a;
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

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public BetrokkeneType getBetrokkeneType() {
    return betrokkeneType;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
