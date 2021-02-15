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

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

/**
 * Soorten reisdocumenten
 */
public enum ReisdocumentType {

  EERSTE_NATIONAAL_PASPOORT("PN", "Nationaal paspoort", 18),
  NEDERLANDSE_IDENTITEITSKAART("NI", "Nederlandse identiteitskaart", 12),
  EERSTE_ZAKENPASPOORT("ZN", "Nationaal paspoort met 64 bladzijden (zakenpaspoort)", 18),
  VREEMDELINGEN_PASPOORT("PB", "Reisdocument voor vreemdelingen", 18),
  VLUCHTELINGEN_PASPOORT("PV", "Reisdocument voor vluchtelingen", 18),
  TWEEDE_ZAKENPASPOORT("TE", "Tweede paspoort (zakenpaspoort)", 18),
  TWEEDE_NATIONAAL_PASPOORT("TN", "Tweede paspoort", 18),
  FACILITEITEN_PASPOORT("PF", "Faciliteitenpaspoort", 0),
  ONBEKEND("onbekend", "onbekend", 0);

  private String code                = "";
  private String oms                 = "";
  private int    leeftijdToestemming = 0;

  ReisdocumentType(String code, String oms, int leeftijdToestemming) {

    setCode(code);
    setOms(oms);
    setLeeftijdToestemming(leeftijdToestemming);
  }

  public static ReisdocumentType get(String code) {
    for (ReisdocumentType var : ReisdocumentType.values()) {
      if (equalsIgnoreCase(var.getCode(), code)) {
        return var;
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

  public int getLeeftijdToestemming() {
    return leeftijdToestemming;
  }

  public void setLeeftijdToestemming(int leeftijdToestemming) {
    this.leeftijdToestemming = leeftijdToestemming;
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public boolean isDocument(ReisdocumentType... types) {
    for (ReisdocumentType type : types) {
      if (equalsIgnoreCase(type.getCode(), code)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
