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

package nl.procura.gba.web.services.zaken.algemeen.identificatie;

public enum ZaakIdType {

  ZAAKSYSTEEM(1, "ZAAKSYSTEEM", "Zaaksysteem"),
  PROWEB_PERSONEN(3, "PROWEB-PERSONEN", "Proweb Personen"),
  ONBEKEND(4, "", "Onbekend");

  private final String oms;
  private final int    order;
  private String       code;

  ZaakIdType(int prioriteit, String code, String oms) {
    this.code = code;
    this.oms = oms;
    this.order = prioriteit;
  }

  public static ZaakIdType get(String code) {
    for (ZaakIdType type : ZaakIdType.values()) {
      boolean isName = type.name().equalsIgnoreCase(code);
      boolean isCode = type.getCode().equalsIgnoreCase(code);
      boolean isOms = type.getOms().equalsIgnoreCase(code);
      if (isName || isCode || isOms) {
        return type;
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

  public int getOrder() {
    return order;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
