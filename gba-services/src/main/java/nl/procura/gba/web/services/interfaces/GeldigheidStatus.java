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

package nl.procura.gba.web.services.interfaces;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.common.MiscUtils;
import nl.procura.standard.ProcuraDate;

public enum GeldigheidStatus {

  NOG_NIET_ACTUEEL("Nog niet actueel"),
  ACTUEEL("Actueel"),
  BEEINDIGD("Beëindigd"),
  ALLES("Alles");

  private String oms;

  GeldigheidStatus(String oms) {
    this.oms = oms;
  }

  public static GeldigheidStatus get(Geldigheid geldigheid) {

    long dIn = geldigheid.getDatumIngang().getLongDate();
    long dEnd = geldigheid.getDatumEinde().getLongDate();

    boolean isNaIngangsdatum = !pos(dIn) || new ProcuraDate().diffInDays(astr(dIn)) <= 0;
    boolean isNaEinddatum = pos(dEnd) && new ProcuraDate().diffInDays(astr(dEnd)) <= 0;

    GeldigheidStatus out = GeldigheidStatus.ACTUEEL;
    if (isNaEinddatum) {
      out = GeldigheidStatus.BEEINDIGD;
    } else if (!isNaIngangsdatum) {
      out = GeldigheidStatus.NOG_NIET_ACTUEEL;
    }

    return out;
  }

  public static String getHtml(String waarde, Geldigheid geldigheid) {
    StringBuilder out = new StringBuilder(waarde);
    GeldigheidStatus status = geldigheid.getGeldigheidStatus();
    switch (status) {
      case BEEINDIGD:
      case NOG_NIET_ACTUEEL:
        return waarde + MiscUtils.setClass(false, " (" + status.getOms().toLowerCase() + ")");

      case ACTUEEL:
      default:
        break;
    }
    return out.toString();
  }

  public String getOms() {
    return oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public boolean is(GeldigheidStatus... statussen) {
    if (ALLES.equals(this)) {
      return true;
    }
    if (statussen != null && statussen.length > 0) {
      for (GeldigheidStatus status : statussen) {
        if (this.equals(status) || ALLES.equals(status)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return getOms();
  }
}
