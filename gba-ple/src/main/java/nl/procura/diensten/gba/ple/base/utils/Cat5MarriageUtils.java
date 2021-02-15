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

package nl.procura.diensten.gba.ple.base.utils;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import nl.procura.diensten.gba.ple.base.BasePL;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;

/**
 * Do some post-PL marriage actions
 */
public class Cat5MarriageUtils {

  public static void updatePl(BasePL pl) {
    List<BasePLSet> marriageSets = pl.getCat(HUW_GPS).getSets();
    updateMarriageValues(marriageSets);
    sortMarriageSets(marriageSets);
    addMostRecentIndication(marriageSets);
  }

  /**
   * Add the marriage elements (date, place, country) to the divorce records
   */
  private static void updateMarriageValues(List<BasePLSet> marriageSets) {
    for (BasePLSet set : marriageSets) {
      for (BasePLRec rec : set.getRecs()) {
        String divorceDate = rec.getElemVal(DATUM_ONTBINDING).getVal();
        String marriageDate = rec.getElemVal(DATUM_VERBINTENIS).getVal();
        // If reason for divorce is set and marriage datum is empty
        if (StringUtils.isNotBlank(divorceDate) && StringUtils.isBlank(marriageDate)) {
          Optional<BasePLRec> marriageRecord = getMarriageRecord(set);
          if (marriageRecord.isPresent()) {
            BasePLRec mrec = marriageRecord.get();
            BasePLElem dHuw = mrec.getElem(DATUM_VERBINTENIS);
            BasePLElem pHuw = mrec.getElem(PLAATS_VERBINTENIS);
            BasePLElem lHuw = mrec.getElem(LAND_VERBINTENIS);
            rec.getElems().removeIf(e -> e.getElemCode() == dHuw.getElemCode());
            rec.getElems().removeIf(e -> e.getElemCode() == pHuw.getElemCode());
            rec.getElems().removeIf(e -> e.getElemCode() == lHuw.getElemCode());
            rec.getElems().add(dHuw);
            rec.getElems().add(pHuw);
            rec.getElems().add(lHuw);
          }
        }
      }
    }
  }

  private static void sortMarriageSets(List<BasePLSet> marriageSets) {
    Collections.sort(marriageSets, new Cat5MarriageSorter());
  }

  /**
   * Set 'most recent' value to a set
   */
  private static void addMostRecentIndication(List<BasePLSet> sets) {
    if (!sets.isEmpty()) {
      // If name is empty that means the marriage record is not valid
      BasePLSet currentSet = sets.get(0); // The first record is always the most recent
      BasePLRec currentRec = currentSet.getLatestRec();
      String name = currentRec.getElemVal(GESLACHTSNAAM).getVal();
      if (StringUtils.isNotBlank(name)) {
        currentSet.setMostRecentSet(true);
      }
    }
  }

  /**
   * Returns the related marriage record
   */
  private static Optional<BasePLRec> getMarriageRecord(BasePLSet set) {
    for (BasePLRec rec : set.getRecs()) {
      String dHuw = rec.getElemVal(DATUM_VERBINTENIS).getVal();
      if (StringUtils.isNotBlank(dHuw)) {
        return Optional.of(rec);
      }
    }

    return Optional.empty();
  }
}
