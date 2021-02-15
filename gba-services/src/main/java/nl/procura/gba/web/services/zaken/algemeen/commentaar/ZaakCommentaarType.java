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

package nl.procura.gba.web.services.zaken.algemeen.commentaar;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

public enum ZaakCommentaarType {

  INFO(1, "I", "Info"),
  WARN(2, "W", "Waarschuwing"),
  ERROR(3, "E", "Fout"),
  FAV(0, "F", "Favouriet"),
  ONBEKEND(0, "O", "Onbekend");

  private int    vnr;
  private String code = "";
  private String oms  = "";

  ZaakCommentaarType(int vnr, String code, String oms) {
    this.vnr = vnr;
    setCode(code);
    setOms(oms);
  }

  public static ZaakCommentaarType get(String code) {
    for (ZaakCommentaarType c : values()) {
      if (equalsIgnoreCase(c.getCode(), code)) {
        return c;
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

  public int getVnr() {
    return vnr;
  }

  public void setVnr(int vnr) {
    this.vnr = vnr;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
