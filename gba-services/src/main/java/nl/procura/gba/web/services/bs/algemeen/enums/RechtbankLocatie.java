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

package nl.procura.gba.web.services.bs.algemeen.enums;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum RechtbankLocatie {

  AMSTERDAM("Amsterdam", "rechtbank Amsterdam"),
  DEN_HAAG("Den Haag", "rechtbank Den Haag"),
  GELDERLAND("Gelderland", "rechtbank Gelderland"),
  LIMBURG("Limburg", "rechtbank Limburg"),
  MIDDEN_NEDERLAND("Midden-Nederland", "rechtbank Midden-Nederland"),
  NOORD_HOLLAND("Noord-Holland", "rechtbank Noord-Holland"),
  NOORD_NEDERLAND("Noord-Nederland", "rechtbank Noord-Nederland"),
  OOST_BRABANT("Oost-Brabant", "rechtbank Oost-Brabant"),
  OVERIJSSEL("Overijssel", "rechtbank Overijssel"),
  ROTTERDAM("Rotterdam", "rechtbank Rotterdam"),
  ZEELAND_WEST_BRABANT("Zeeland-West-Brabant", "rechtbank Zeeland-West-Brabant"),
  gerechtshof_ARNHEM_LEEUWARDEN("gerechtshof Arnhem-Leeuwarden"),
  gerechtshof_DEN_HAAG("gerechtshof Den Haag"),
  gerechtshof_S_HERTOGENBOSCH("gerechtshof 's-Hertogenbosch"),
  HOGE_RAAD("Hoge Raad der Nederlanden"),
  ONBEKEND("", "Onbekend");

  private String code = null;
  private String oms  = "";

  RechtbankLocatie(String code) {
    this(code, code);
  }

  RechtbankLocatie(String code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static RechtbankLocatie get(String code) {

    for (RechtbankLocatie a : values()) {
      if (equalsIgnoreCase(a.getCode(), code)) {
        return a;
      }
    }

    return ONBEKEND;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
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
