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

package nl.procura.diensten.gba.ple.procura.arguments;

import static nl.procura.standard.Globalfunctions.*;

import java.io.Serializable;

public class PLNumber implements Serializable {

  public static final int TABEL_BRON_ONBEKEND = -1;
  public static final int TABEL_BRON_VERW     = 0;
  public static final int TABEL_BRON_INW      = 1;

  private static final long serialVersionUID = -7298491365433705796L;

  private int     a1            = 0;
  private int     a2            = 0;
  private int     a3            = 0;
  private long    bsn           = 0;
  private long    anr           = -1;
  private boolean relatedPerson = false;
  private int     source        = TABEL_BRON_ONBEKEND;

  public PLNumber(int a1, int a2, int a3, long bsn, int source) {
    this(a1, a2, a3, bsn, source, false);
  }

  public PLNumber(int a1, int a2, int a3, long bsn, int source, boolean related) {
    setA1(a1);
    setA2(a2);
    setA3(a3);
    setBsn(bsn);
    setSource(source);
    setRelatedPerson(related);
  }

  public boolean isAnr() {
    return getA1() > 0;
  }

  public long getAnr() {
    if (anr < 0) {
      anr = along(pad_left(astr(getA1()), "0", 3) +
          pad_left(astr(getA2()), "0", 4) +
          pad_left(astr(getA3()), "0", 3));
    }

    return anr;
  }

  public long getNummer() {
    return isAnr() ? getAnr() : getBsn();
  }

  public int getA1() {
    return a1;
  }

  public void setA1(int a1) {
    anr = -1;
    this.a1 = a1;
  }

  public int getA2() {
    return a2;
  }

  public void setA2(int a2) {
    anr = -1;
    this.a2 = a2;
  }

  public int getA3() {
    return a3;
  }

  public void setA3(int a3) {
    anr = -1;
    this.a3 = a3;
  }

  public long getBsn() {
    return bsn;
  }

  public void setBsn(long bsn) {
    this.bsn = bsn;
  }

  public int getSource() {
    return source;
  }

  public void setSource(int source) {
    this.source = source;
  }

  @Override
  public String toString() {
    if (a1 > 0) {
      return pad_left(astr(a1), "0", 3) + "." + pad_left(astr(a2), "0", 4) + "." + pad_left(astr(a3), "0", 3);
    }

    return astr(bsn);
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;

    if (a1 > 0) {
      result = prime * result + a1;
      result = prime * result + a2;
      result = prime * result + a3;
    } else {
      result = prime * result + (int) (bsn ^ (bsn >>> 32));
    }

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

    PLNumber other = (PLNumber) obj;

    if ((a1 > 0) && (other.a1 > 0)) {
      if (a1 != other.a1) {
        return false;
      }

      if (a2 != other.a2) {
        return false;
      }

      if (a3 != other.a3) {
        return false;
      }
    }

    return bsn <= 0 || other.bsn <= 0 || bsn == other.bsn;
  }

  public boolean isRelatedPerson() {
    return relatedPerson;
  }

  public void setRelatedPerson(boolean relatedPerson) {
    this.relatedPerson = relatedPerson;
  }
}
