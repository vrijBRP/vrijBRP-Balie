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

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.VogProfTab;

public class VogProfiel extends VogProfTab implements Comparable<VogProfiel> {

  private static final long serialVersionUID = -4133030165975217305L;

  @Override
  public int compareTo(VogProfiel o) {
    return (aval(getVogProfTab()) > aval(o.getVogProfTab())) ? 1 : -1;
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
    VogProfTab other = (VogProfTab) obj;
    return getCVogProfTab().equals(other.getCVogProfTab());
  }

  @Override
  public String getVogProfTab() {
    return astr(super.getVogProfTab());
  }

  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (getCVogProfTab() ^ (getCVogProfTab() >>> 32));

    return result;
  }

  public boolean isActueel() {
    return MiscUtils.isDatumActueel(along(getDIn()), along(getDEnd()));
  }

  public String toString() {
    return getVogProfTab() + " - " + getOms();
  }
}
