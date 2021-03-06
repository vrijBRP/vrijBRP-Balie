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

package nl.procura.gba.web.common.misc;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.date2str;

import java.text.MessageFormat;

public class ZaakPeriode {

  private long   dFrom = 0;
  private long   dTo   = 0;
  private String descr = "";

  public ZaakPeriode() {
  }

  public ZaakPeriode(String descr, long dFrom, long dTo) {

    setDescr(descr);
    setdFrom(dFrom);
    setdTo(dTo);

    setDescr(MessageFormat.format("{0} ({1} / {2})", descr, date2str(astr(dFrom)), date2str(astr(dTo))));
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
    ZaakPeriode other = (ZaakPeriode) obj;
    if (descr == null) {
      return other.descr == null;
    } else {
      return descr.equals(other.descr);
    }
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public long getdFrom() {
    return dFrom;
  }

  public void setdFrom(long dFrom) {
    this.dFrom = dFrom;
  }

  public long getdTo() {
    return dTo;
  }

  public void setdTo(long dTo) {
    this.dTo = dTo;
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((descr == null) ? 0 : descr.hashCode());

    return result;
  }

  public boolean isTussen(long d) {
    return d >= dFrom && d <= dTo;
  }

  @Override
  public String toString() {
    return getDescr();
  }
}
