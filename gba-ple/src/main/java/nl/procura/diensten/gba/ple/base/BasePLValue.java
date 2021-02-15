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

package nl.procura.diensten.gba.ple.base;

import static nl.procura.standard.Globalfunctions.along;

import java.io.Serializable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BasePLValue implements Serializable {

  private static final long serialVersionUID = 9192097570058070597L;

  private String  code       = "";
  private String  val        = "";
  private String  descr      = "";
  private boolean isNotBlank = false;

  public BasePLValue() {
  }

  public BasePLValue(String val) {
    this(val, "");
  }

  public BasePLValue(String val, String descr) {
    this(val, val, descr);
  }

  public BasePLValue(String code, String val, String descr) {
    setCode(code);
    setVal(val);
    setDescr(descr);
  }

  public String getVal() {
    return val;
  }

  public void setVal(String val) {
    this.val = (val != null) ? val.trim() : "";
  }

  public long toLong() {
    return along(val);
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = (descr != null) ? descr.trim() : "";
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    try {
      throw new RuntimeException("Waarschuwing: gebruik van BasisPlElementWaarde.toString wordt afgeraden.");
    } catch (RuntimeException e) {
      log.error("Code: " + getCode() + ", waarde: " + getVal() + ", oms: " + getDescr(), e);
    }
    return getVal();
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    result = prime * result + ((descr == null) ? 0 : descr.hashCode());
    result = prime * result + ((val == null) ? 0 : val.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    BasePLValue other = (BasePLValue) obj;
    if (code == null) {
      if (other.code != null) {
        return false;
      }
    } else if (!code.equals(other.code)) {
      return false;
    }

    if (descr == null) {
      if (other.descr != null) {
        return false;
      }
    } else if (!descr.equals(other.descr)) {
      return false;
    }

    if (val == null) {
      return other.val == null;
    } else
      return val.equals(other.val);

  }

  public boolean isNotBlank() {
    return isNotBlank;
  }

  public void setNotBlank(boolean isNotBlank) {
    this.isNotBlank = isNotBlank;
  }
}
