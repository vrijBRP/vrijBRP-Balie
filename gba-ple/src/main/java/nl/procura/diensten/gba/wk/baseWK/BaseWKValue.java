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

package nl.procura.diensten.gba.wk.baseWK;

import java.io.Serializable;

public class BaseWKValue implements Serializable {

  private static final long serialVersionUID = 6723473193080687041L;

  private String code  = "";
  private String value = "";
  private String descr = "";

  public BaseWKValue() {
  }

  public BaseWKValue(String code, String value, String descr) {
    setCode(code);
    setValue(value);
    setDescr(descr);
  }

  @Override
  public String toString() {
    return "BasisWKObjectWaarde [code=" + code + ", waarde=" + value + ", omschrijving=" + descr + "]";
  }

  public String getCode() {
    return code;
  }

  public BaseWKValue setCode(String code) {

    this.code = code;
    this.value = code;
    this.descr = code;
    return this;
  }

  public String getValue() {
    return value;
  }

  public BaseWKValue setValue(String value) {
    this.value = value;
    this.descr = value;
    return this;
  }

  public String getDescr() {
    return descr;
  }

  public BaseWKValue setDescr(String descr) {
    this.descr = descr;
    return this;
  }
}
