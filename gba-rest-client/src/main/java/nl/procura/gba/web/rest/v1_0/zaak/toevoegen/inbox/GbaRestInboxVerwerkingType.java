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

package nl.procura.gba.web.rest.v1_0.zaak.toevoegen.inbox;

public enum GbaRestInboxVerwerkingType {

  HUWELIJK_RESERVERING("huwelijk.reservering", "Nieuwe reservering voor een huwelijk- of GPS"),
  HUWELIJK_VOORNEMEN("huwelijk.voornemen", "Voornemen toevoegen aan een huwelijk- of GPS"),
  HUWELIJK_GETUIGEN("huwelijk.getuigen", "Getuigen toevoegen aan een huwelijk- of GPS"),
  GEBOORTE_AANGIFTE("geboorte.aangifte", "Nieuwe aangifte van een geboorte"),
  ZAAK_BIJLAGEN("zaak.bijlage", "Nieuwe bijlage bij een zaak"),
  ONBEKEND("ONBEKEND", "Onbekend");

  private String code  = "";
  private String descr = "";

  GbaRestInboxVerwerkingType(String code, String descr) {
    setCode(code);
    setDescr(descr);
  }

  public static GbaRestInboxVerwerkingType get(String value) {
    for (GbaRestInboxVerwerkingType e : GbaRestInboxVerwerkingType.values()) {
      boolean isCode = e.getCode().equalsIgnoreCase(value);
      boolean isDescr = e.getDescr().equalsIgnoreCase(value);
      if (isCode || isDescr) {
        return e;
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

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  @Override
  public String toString() {
    return getDescr();
  }
}
