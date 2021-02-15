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

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.emp;

import java.io.Serializable;
import java.util.Arrays;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;

public class BasePLElem implements Serializable, Comparable<BasePLElem> {

  private static final long serialVersionUID = -8241629872761651875L;

  private int         catCode  = 0;
  private int         elemCode = 0;
  private boolean     allowed  = true;
  private BasePLValue value    = new BasePLValue();

  public BasePLElem() {
  }

  public BasePLElem(int catCode, int elementCode, String value) {
    setCatCode(catCode);
    setElemCode(elementCode);
    getValue().setVal(value);
  }

  public int getCatCode() {
    return catCode;
  }

  public void setCatCode(int catCode) {
    this.catCode = catCode;
  }

  public int getElemCode() {
    return elemCode;
  }

  public void setElemCode(int elemCode) {
    this.elemCode = elemCode;
  }

  public boolean isAllowed() {
    return allowed;
  }

  public void setAllowed(boolean allowed) {
    this.allowed = allowed;
  }

  public BasePLValue getValue() {
    return value;
  }

  public void setValue(BasePLValue value) {
    this.value = value;
  }

  public boolean isElement(GBAElem... elementen) {
    return Arrays.stream(elementen).anyMatch(e -> e.getCode() == getElemCode());
  }

  public boolean isElemCode(int... nrs) {
    return Arrays.stream(nrs).anyMatch(elem -> elem == getElemCode());
  }

  public boolean isEmpty() {
    return emp(astr(getValue().getVal()));
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;

    result = prime * result + catCode;
    result = prime * result + elemCode;

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

    BasePLElem other = (BasePLElem) obj;

    if (catCode != other.catCode) {
      return false;
    }
    return elemCode == other.elemCode;
  }

  @Override
  public int compareTo(BasePLElem o) {
    return Integer.compare(getElemCode(), o.getElemCode());
  }
}
