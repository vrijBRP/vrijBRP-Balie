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

package nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie;

public enum KoppelEnumeratieSoortType {

  GRONDSLAG("grondslag", "Grondslagen"),
  PROCESACTIE("procesactie", "Procesacties"),
  TOEKENNING("toekenning", "Toekenningen"),
  REACTIE("reactie", "Reacties"),
  BINNENGEM("binnengemeentelijk", "Binnengemeentelijk"),
  VERSTREK_BEP("verstrekkingsbeperking", "Verstrekkingsbeperking");

  private String code = "";
  private String oms  = "";

  KoppelEnumeratieSoortType(String code, String oms) {

    setCode(code);
    setOms(oms);
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

  public boolean is(KoppelEnumeratieSoortType type) {
    return (type != null && type.getCode().equals(code));
  }

  @Override
  public String toString() {
    return getOms();
  }
}
