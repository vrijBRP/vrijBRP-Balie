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

package nl.procura.gba.web.services.zaken.vog;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.aval;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.VogFuncTab;

public class VogFunctie extends VogFuncTab implements Comparable<VogFunctie> {

  private static final long serialVersionUID = -358235485038258868L;

  @Override
  public int compareTo(VogFunctie a) {
    return (aval(getVogFuncTab()) > aval(a.getVogFuncTab())) ? 1 : -1;
  }

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
    VogFuncTab other = (VogFuncTab) obj;
    return getCVogFuncTab().equals(other.getCVogFuncTab());
  }

  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (getCVogFuncTab() ^ (getCVogFuncTab() >>> 32));
    return result;
  }

  public boolean isActueel() {
    return MiscUtils.isDatumActueel(along(getDIn()), along(getDEnd()));
  }

  public String toString() {
    return getVogFuncTab() + " - " + getOms();
  }
}
