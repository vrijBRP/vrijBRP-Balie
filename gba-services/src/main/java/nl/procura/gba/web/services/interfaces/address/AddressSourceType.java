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

package nl.procura.gba.web.services.interfaces.address;

public enum AddressSourceType {

  BRP("brp", "Lokale verblijfsobjectentabel"),
  PERSONLIST("personlist", "BRP persoonslijst"),
  BAG("bag", "BAG landelijke voorziening"),
  UNKNOWN("", "Onbekend");

  private String code  = "";
  private String descr = "";

  AddressSourceType(String code, String descr) {
    this.code = code;
    this.descr = descr;
  }

  public static AddressSourceType valueOfCode(String code) {
    for (final AddressSourceType value : AddressSourceType.values()) {
      if (value.code.equals(code)) {
        return value;
      }
    }
    return UNKNOWN;
  }

  public String getCode() {
    return code;
  }

  public String getDescr() {
    return descr;
  }

  @Override
  public String toString() {
    return descr;
  }

  public boolean is(AddressSourceType type) {
    return this == type;
  }
}
