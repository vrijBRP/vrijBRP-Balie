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

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Comparator;

import nl.procura.diensten.gba.ple.base.BasePLSet;

/**
 * Sorts the marriage set based on
 * - Divorce date
 * - Marriage date
 * - 'Valid' date
 * - 'Adding' date
 */
public class Cat5MarriageSorter implements Comparator<BasePLSet> {

  private static final int HIGHER = -1;
  private static final int LOWER  = 1;

  @Override
  public int compare(BasePLSet rec1, BasePLSet rec2) {

    long dOntb1 = rec1.getLatestRec()
        .getElemVal(DATUM_ONTBINDING)
        .toLong();

    long dOntb2 = rec2.getLatestRec()
        .getElemVal(DATUM_ONTBINDING)
        .toLong();

    long dHuw1 = rec1.getLatestRec()
        .getElemVal(DATUM_VERBINTENIS)
        .toLong();

    long dHuw2 = rec2.getLatestRec()
        .getElemVal(DATUM_VERBINTENIS)
        .toLong();

    long dGeld1 = rec1.getLatestRec()
        .getElemVal(INGANGSDAT_GELDIG)
        .toLong();

    long dGeld2 = rec2.getLatestRec()
        .getElemVal(INGANGSDAT_GELDIG)
        .toLong();

    String gesl1 = rec1.getLatestRec()
        .getElemVal(GESLACHTSNAAM)
        .getVal();

    String gesl2 = rec2.getLatestRec()
        .getElemVal(GESLACHTSNAAM)
        .getVal();

    long dOpn1 = rec1.getLatestRec()
        .getElemVal(DATUM_VAN_OPNEMING)
        .toLong();

    long dOpn2 = rec2.getLatestRec()
        .getElemVal(DATUM_VAN_OPNEMING)
        .toLong();

    // Marriage record before empty record
    if (isNotBlank(gesl1) && isBlank(gesl2)) {
      return HIGHER;
    } else if (isBlank(gesl1) && isNotBlank(gesl2)) {
      return LOWER;
    }

    // Marriage record before no marriage record
    if (dHuw1 >= 0 && dHuw2 < 0) {
      return HIGHER;
    } else if (dHuw1 < 0 && dHuw2 >= 0) {
      return LOWER;
    }

    // Show current first, then dissolved
    if (dOntb1 < 0 && dOntb2 >= 0) {
      return HIGHER;
    } else if (dOntb1 >= 0 && dOntb2 < 0) {
      return LOWER;
    }

    // Sort on divorce date
    if (dOntb1 > 0 && dOntb2 > 0) {
      if (dOntb1 > dOntb2) {
        return HIGHER;
      } else if (dOntb1 < dOntb2) {
        return LOWER;
      }
    }

    // Sort on validity date
    if (dGeld1 > 0 && dGeld2 > 0) {
      if (dGeld1 > dGeld2) {
        return HIGHER;
      } else if (dGeld1 < dGeld2) {
        return LOWER;
      }
    }

    // Sort on adding date
    if (dOpn1 > 0 && dOpn2 > 0) {
      if (dOpn1 > dOpn2) {
        return HIGHER;
      } else if (dOpn1 < dOpn2) {
        return LOWER;
      }
    }

    return 0;
  }
}
