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

import java.io.Serializable;
import java.util.Arrays;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;

public class BasePLCat implements Serializable {

  private static final long serialVersionUID = -7491790423963238199L;

  private GBACat                catType = GBACat.UNKNOWN;
  private BasePLList<BasePLSet> sets    = new BasePLList<>();

  public BasePLCat(GBACat catType) {
    setCatType(catType);
  }

  public GBACat getCatType() {
    return catType;
  }

  public void setCatType(GBACat catType) {
    this.catType = catType;
  }

  public BasePLList<BasePLSet> getSets() {
    return sets;
  }

  /**
   * This is either the mutation or current record.
   */
  public BasePLRec getLatestRec() {
    return sets.getFirst()
        .map(BasePLSet::getLatestRec)
        .orElse(new BasePLRec(catType, GBARecStatus.UNKNOWN));
  }

  public BasePLRec getCurrentRec() {
    return sets.getFirst()
        .map(BasePLSet::getCurrentRec)
        .orElse(new BasePLRec(catType, GBARecStatus.UNKNOWN));
  }

  public boolean hasSets() {
    return getSets().isNotEmpty();
  }

  public boolean hasMultipleSets() {
    return getSets().size() > 1;
  }

  public boolean isCategoryType(GBACat... categorieen) {
    return Arrays.stream(categorieen).anyMatch(c -> c == getCatType());
  }
}
