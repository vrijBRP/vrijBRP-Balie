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

import static nl.procura.standard.Globalfunctions.astr;

public enum CorrespondentieAfsluitingType {

  AUTOMATISCH("", "Automatisch", "Automatisch (als gerelateerde zaken zijn afgesloten)"),
  NIET_AUTOMATISCH("N", "Handmatig", "Handmatig");

  private String code;
  private String oms;
  private String uitleg;

  CorrespondentieAfsluitingType(String code, String oms, String uitleg) {

    this.code = code;
    this.oms = oms;
    this.uitleg = uitleg;
  }

  public static CorrespondentieAfsluitingType get(String code) {
    for (CorrespondentieAfsluitingType a : values()) {
      if (a.getCode().equalsIgnoreCase(astr(code))) {
        return a;
      }
    }

    return NIET_AUTOMATISCH;
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

  public String getUitleg() {
    return uitleg;
  }

  public void setUitleg(String uitleg) {
    this.uitleg = uitleg;
  }

  @Override
  public String toString() {
    return getUitleg();
  }
}
