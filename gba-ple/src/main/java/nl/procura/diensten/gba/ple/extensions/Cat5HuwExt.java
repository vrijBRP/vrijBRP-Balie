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

package nl.procura.diensten.gba.ple.extensions;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.burgerzaken.gba.core.enums.GBARecStatus.UNKNOWN;
import static nl.procura.standard.Globalfunctions.*;

import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.BasePLValue;

public class Cat5HuwExt extends BasePLCatExt {

  public Cat5HuwExt(BasePLExt ext) {
    super(ext);
  }

  public boolean isActueelOfMutatieRecord() {
    return getActueelOfMutatieRecord().hasElems();
  }

  public BasePLRec getActueelOfMutatieRecord() {
    return getHuwelijkSet().getLatestRec();
  }

  /**
   * Returns the most current marriage based on a field which is set in the webservice
   */
  public BasePLSet getHuwelijkSet() {
    List<BasePLSet> sets = getExt().getCat(HUW_GPS).getSets();
    return sets.stream().filter(set -> set.isMostRecentSet())
        .findFirst().orElse(new BasePLSet(HUW_GPS, 1));
  }

  /**
   * Geeft het Bsn en bij ontbreken daarvan het A-nummer terug.
   */
  public BasePLValue getNummer() {
    if (pos(getBsn().getCode())) {
      return getBsn();
    }
    return getAnr();
  }

  public boolean isOmzetting(BasePLSet set) {
    return getOmzetting(set).hasElems();
  }

  public BasePLRec getOmzetting(BasePLSet set) {
    String lastSoort = "";
    BasePLRec omzetting = new BasePLRec(HUW_GPS, UNKNOWN);
    for (BasePLRec i : set.getRecs()) {
      String ncSoort = i.getElemVal(GBAElem.SOORT_VERBINTENIS).getCode();
      if (!lastSoort.equals(ncSoort)) {
        if (emp(lastSoort)) {
          lastSoort = ncSoort;
        } else {
          lastSoort = ncSoort;
          omzetting = i;
        }
      }
    }

    return omzetting;
  }

  @Deprecated
  public BasePLRec getSluiting(BasePLSet set, String d_opn) {
    d_opn = pos(d_opn) ? d_opn : getSystemDate();
    for (BasePLRec i : set.getRecs()) {
      String d_geld = i.getElemVal(GBAElem.INGANGSDAT_GELDIG).getCode();
      String d_huw = i.getElemVal(
          GBAElem.DATUM_VERBINTENIS).getCode();

      if (i.isIncorrect()) {
        continue;
      }
      // Huidige datum (d_opn) ingevuld en datum geldigheid (d_geld) <= d_opn
      if (pos(d_opn) && (aval(d_geld) <= aval(d_opn)) && aval(d_huw) >= 0) {
        return i;
      }
    }

    return new BasePLRec(HUW_GPS, UNKNOWN);
  }

  @Deprecated
  public BasePLRec getOntbinding(BasePLSet set, String d_opn) {
    d_opn = pos(d_opn) ? d_opn : getSystemDate();
    for (BasePLRec i : set.getRecs()) {
      String d_geld = i.getElemVal(GBAElem.INGANGSDAT_GELDIG).getCode();
      String r_ontb = i.getElemVal(GBAElem.REDEN_ONTBINDING).getCode();

      if (i.isIncorrect()) {
        continue;
      }

      // Huidige datum (d_opn) ingevuld en datum geldigheid (d_geld) <= d_opn
      if (pos(d_opn) && (aval(d_geld) <= aval(d_opn)) && fil(r_ontb)) {
        return i;
      }
    }

    return new BasePLRec(HUW_GPS, UNKNOWN);
  }

  public BasePLValue getAnr() {
    return getActueelOfMutatieRecord().getElem(GBAElem.ANR).getValue();
  }

  public BasePLValue getBsn() {
    return getActueelOfMutatieRecord().getElem(GBAElem.BSN).getValue();
  }
}
