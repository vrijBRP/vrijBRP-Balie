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

package nl.procura.gba.web.services.zaken.reisdocumenten;

public enum SluitingType {

  AANVRAAG_NIET_AFGESLOTEN(0, "Aanvraag niet afgesloten"),
  DOCUMENT_UITGEREIKT(1, "Document uitgereikt"),
  DOCUMENT_NIET_UITGEREIKT_ONJUIST(2, "Document niet uitgereikt (onjuist)"),
  DOCUMENT_UITGEREIKT_DOOR_ANDERE_INSTANTIE(3, "Document uitgereikt door andere instantie"),
  DOCUMENT_NIET_UITGEREIKT_OVERIGE_REDEN(4, "Document niet uitgereikt, overige reden"),
  DOCUMENT_NIET_OPGEHAALD(5, "Document niet opgehaald"),
  ONBEKEND(-1, "Onbekend");

  private int    code = 0;
  private String oms  = "";

  SluitingType(int code, String oms) {

    setCode(code);
    setOms(oms);
  }

  public static SluitingType get(int code) {
    for (SluitingType var : values()) {
      if (var.getCode() == code) {
        return var;
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

  @Override
  public String toString() {
    return getCode() >= 0 ? (getCode() + ": " + getOms()) : getOms();
  }

}
