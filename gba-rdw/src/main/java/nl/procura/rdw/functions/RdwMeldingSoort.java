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

package nl.procura.rdw.functions;

public enum RdwMeldingSoort {

  FOUT("F", "Foutmelding, gebruikersactie noodzakelijk"),
  INFORMATIE("I", "Informatie"),
  WAARSCHUWING("W", "Waarschuwing"),
  SYSTEEMFOUT("S", "Systeemfout, gebruikersactie niet mogelijk"),
  ONBEKEND("O", "Onbekend");

  private String code = "";
  private String oms  = "";

  RdwMeldingSoort(String code, String oms) {
    setCode(code);
    setOms(oms);
  }

  public static RdwMeldingSoort get(String code) {

    for (RdwMeldingSoort v : values()) {
      if (v.getCode().equalsIgnoreCase(code)) {
        return v;
      }
    }

    return ONBEKEND;
  }

  public boolean is(RdwMeldingSoort... soorten) {
    for (RdwMeldingSoort soort : soorten) {
      if (this == soort) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return getCode() + ": " + getOms();
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
}
