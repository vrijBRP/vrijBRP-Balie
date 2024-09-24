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

import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KINDEREN;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_1;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_2;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.PERSOON;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VERW;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_AANVANG_ADRESH;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_ONJUIST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.OMSCHR_VAN_DE_AANGIFTE_ADRESH;
import static nl.procura.standard.Globalfunctions.aval;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.utils.Cat5MarriageUtils;
import nl.procura.diensten.gba.ple.procura.arguments.PLNumber;
import nl.procura.validation.Anr;
import nl.procura.validation.Bsn;

public class BasePLUtils {

  /**
   * Is BAG change
   */
  public static boolean isBagChange(BasePLSet set, BasePLRec rec, int index) {
    int datumAanvang = aval(rec.getElem(DATUM_AANVANG_ADRESH).getValue().getVal());
    boolean isIncorrect = "O".equalsIgnoreCase(rec.getElemVal(IND_ONJUIST).getCode());

    if (!isIncorrect) {
      int prevIndex = 0;
      for (BasePLRec prevRecord : set.getRecs()) {
        int prevStartdate = aval(prevRecord.getElemVal(DATUM_AANVANG_ADRESH).getVal());
        boolean isPrevIncorrect = "O".equalsIgnoreCase(prevRecord.getElemVal(IND_ONJUIST).getCode());
        boolean isPrevBagChange = "T".equals(prevRecord.getElemVal(OMSCHR_VAN_DE_AANGIFTE_ADRESH)
            .getVal());

        boolean isDatum = (prevStartdate == datumAanvang);
        boolean isAfterBagChange = (index > prevIndex);

        if (isPrevBagChange && !isPrevIncorrect && isAfterBagChange && isDatum) {
          return true;
        }

        prevIndex++;
      }
    }

    return false;
  }

  /**
   * Find relations
   */
  public static Set<PLNumber> getRelations(BasePLBuilder builder) {
    return getRelations(builder, new HashSet<>());
  }

  /**
   * Search relations except the numbers that are already selected
   */
  public static Set<PLNumber> getRelations(BasePLBuilder builder, Set<PLNumber> numbers) {
    Set<PLNumber> searchnumbersset = new LinkedHashSet<>();
    for (BasePL pl : builder.getResult().getBasePLs()) {
      boolean isPLRelated = false;

      // Van persoonslijsten die zelf al gerelateerde zijn niet de gerelateerden zoeken
      for (BasePLCat categorieSoort : pl.getCats(PERSOON, VERW)) {
        for (PLNumber relatieNumber : getRelationNumbers(categorieSoort)) {
          for (PLNumber number : numbers) {
            if (number.isRelatedPerson()) {
              if (number.equals(relatieNumber)) {
                isPLRelated = true;
              }
            }
          }
        }
      }

      // Zoek de personen die nog niet gerelateerd zijn
      if (!isPLRelated) {
        for (BasePLCat cat : pl.getCats(OUDER_1, OUDER_2, HUW_GPS, KINDEREN)) {
          searchnumbersset.addAll(getRelationNumbers(cat));
        }
      }
    }

    return searchnumbersset;
  }

  /**
   * Remove duplicate persons
   */
  public static void removeDuplicates(BasePLBuilder builder) {
    PLEResult result = builder.getResult();
    BasePLList<BasePL> uniqueList = new BasePLList<>();
    for (BasePL pl : result.getBasePLs()) {
      if (!exists(pl, uniqueList)) {
        uniqueList.add(pl);
      }
    }

    result.setBasePLs(uniqueList);
  }

  /**
   * Do some post PL creation actions like
   * - Reordering the marriages in the correct order
   * - Renumbering the external index in the sets and records
   */
  public static void finishPl(BasePLBuilder builder) {
    Cat5MarriageUtils.updatePl(builder.getPL());
    renumber(builder.getPL());
  }

  /**
   * Renumber the index values
   */
  private static void renumber(BasePL pl) {
    for (BasePLCat cat : pl.getCats()) {
      if (cat.getSets().isNotEmpty()) {
        int setVnr = cat.getSets().size();
        for (BasePLSet set : cat.getSets()) {
          set.setExtIndex(setVnr--);
          // GBA-V sets have no internal index, so remain the default (-2)
          // If internal index is negative then set it to the external value
          // Internal value is used for PL mutations to identify the correct set
          if (set.getIntIndex() < 0) {
            set.setIntIndex(set.getExtIndex());
          }
          int recVnr = set.getRecs().size();
          for (BasePLRec rec : set.getRecs()) {
            rec.setIndex(recVnr--);
          }
        }
      }
    }
  }

  /**
   * Zoek de relatienumbers
   */
  private static List<PLNumber> getRelationNumbers(BasePLCat cat) {
    List<PLNumber> numbersList = new ArrayList<>();

    for (BasePLSet set : cat.getSets()) {
      BasePLRec rec = set.getLatestRec();
      Bsn bsn = null;
      Anr anr = null;

      if (rec.isElem(GBAElem.BSN.getCode())) {
        bsn = new Bsn(rec.getElem(GBAElem.BSN).getValue().getVal());
      }
      if (rec.isElem(GBAElem.ANR.getCode())) {
        anr = new Anr(rec.getElem(GBAElem.ANR).getValue().getVal());
      }

      PLNumber number = new PLNumber(-1, -1, -1, -1, PLNumber.TABEL_BRON_ONBEKEND, true);
      if (anr != null && anr.isCorrect()) {
        number.setA1(aval(anr.getA1()));
        number.setA2(aval(anr.getA2()));
        number.setA3(aval(anr.getA3()));
      }

      if (bsn != null && bsn.isCorrect()) {
        number.setBsn(bsn.getLongBsn());
      }

      if (number.getNummer() > 0) {
        numbersList.add(number);
      }
    }

    return numbersList;
  }

  /**
   * Does the NewPL exist in the list
   */
  private static boolean exists(BasePL newPl, List<BasePL> existingList) {
    BasePLNumber newNumber = new BasePLNumber(newPl);
    for (BasePL existingPl : existingList) {
      PLNumber existingNumber = new BasePLNumber(existingPl);
      if (existingNumber.getNummer() > 0 && newNumber.getNummer() > 0 && existingNumber.equals(newNumber)) {
        return true;
      }
    }

    return false;
  }
}
