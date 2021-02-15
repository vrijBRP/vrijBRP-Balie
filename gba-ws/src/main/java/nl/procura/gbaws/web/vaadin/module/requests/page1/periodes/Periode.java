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

package nl.procura.gbaws.web.vaadin.module.requests.page1.periodes;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.date2str;

import java.text.MessageFormat;

public class Periode {

  private long   dFrom = 0;
  private long   dTo   = 0;
  private String descr = "";

  public Periode() {
  }

  public Periode(String descr, long dFrom, long dTo) {

    setDescr(descr);
    setdFrom(dFrom);
    setdTo(dTo);

    setDescr(MessageFormat.format("{0} ({1} / {2})", descr, date2str(astr(dFrom)), date2str(astr(dTo))));
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public long getdTo() {
    return dTo;
  }

  public void setdTo(long dTo) {
    this.dTo = dTo;
  }

  public long getdFrom() {
    return dFrom;
  }

  public void setdFrom(long dFrom) {
    this.dFrom = dFrom;
  }

  @Override
  public String toString() {
    return getDescr();
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((descr == null) ? 0 : descr.hashCode());

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
    Periode other = (Periode) obj;
    if (descr == null) {
      return other.descr == null;
    } else {
      return descr.equals(other.descr);
    }
  }
}
