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

import java.util.Arrays;

import lombok.Getter;

@Getter
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
  GERECHTSHOF_ARNHEM_LEEUWARDEN("gerechtshof Arnhem-Leeuwarden"),
  GERECHTSHOF_DEN_HAAG("gerechtshof Den Haag"),
  GERECHTSHOF_S_HERTOGENBOSCH("gerechtshof 's-Hertogenbosch"),
  HOGE_RAAD("Hoge Raad der Nederlanden"),
  ANDERS("anders, namelijk"),
  ONBEKEND("", "Onbekend");

  private final String code;
  private final String oms;

  RechtbankLocatie(String code) {
    this(code, code);
  }

  RechtbankLocatie(String code, String oms) {
    this.code = code;
    this.oms = oms;
  }

  public static RechtbankLocatie get(String code) {
    return Arrays.stream(values())
        .filter(locatie -> equalsIgnoreCase(locatie.getCode(), code) || equalsIgnoreCase(locatie.getOms(), code))
        .findFirst()
        .orElse(ONBEKEND);

  }

  public boolean is(RechtbankLocatie... types) {
    return Arrays.stream(types).anyMatch(type -> type == this);
  }

  @Override
  public String toString() {
    return getOms();
  }
}
