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

package nl.procura.gba.web.services.gba.ple.opslag;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;

public class PersoonStatusOpslagEntry {

  private long   anr;
  private long   bsn;
  private String status;

  public PersoonStatusOpslagEntry(BasePLExt pl) {
    this(pl, "");
  }

  public PersoonStatusOpslagEntry(BasePLExt pl, String status) {

    this.anr = along(pl.getPersoon().getAnr().getCode());
    this.bsn = along(pl.getPersoon().getBsn().getCode());
    this.status = status;
  }

  @Override
  public boolean equals(Object obj) {

    if (obj instanceof PersoonStatusOpslagEntry) {

      PersoonStatusOpslagEntry entry = (PersoonStatusOpslagEntry) obj;
      boolean isBsn = (pos(getBsn()) && pos(entry.getBsn()) && along(getBsn()) == along(entry.getBsn()));
      boolean isAnr = (pos(getAnr()) && pos(entry.getAnr()) && along(getAnr()) == along(entry.getAnr()));

      return (isBsn || isAnr);
    }

    return false;
  }

  public long getAnr() {
    return anr;
  }

  public long getBsn() {
    return bsn;
  }

  public String getStatus() {
    return status;
  }

  @Override
  public String toString() {
    StringBuilder toString = new StringBuilder();
    toString.append("[");
    if (pos(anr)) {
      toString.append("anr=");
      toString.append(anr);
      toString.append(", ");
    }
    if (pos(bsn)) {
      toString.append("bsn=");
      toString.append(bsn);
    }
    toString.append("]");
    return toString.toString();
  }
}
