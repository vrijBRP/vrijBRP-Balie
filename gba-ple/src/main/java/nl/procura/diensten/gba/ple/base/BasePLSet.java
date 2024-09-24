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

import static nl.procura.burgerzaken.gba.StringUtils.isBlank;
import static nl.procura.burgerzaken.gba.core.enums.GBARecStatus.CURRENT;
import static nl.procura.burgerzaken.gba.core.enums.GBARecStatus.MUTATION;
import static nl.procura.standard.Globalfunctions.astr;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;

public class BasePLSet implements Serializable {

  private static final long serialVersionUID = 4170097904066447514L;

  private GBACat                catType       = GBACat.UNKNOWN;
  private int                   extIndex      = 1;                 // External index
  private int                   intIndex      = 1;                 // Internal index in Procura DB
  private boolean                     mostRecentSet = false;             // Used for marriage category
  private final BasePLList<BasePLRec> recs          = new BasePLList<>();

  public BasePLSet(GBACat catType, int index) {
    setCatType(catType);
    setExtIndex(index);
    setIntIndex(index);
  }

  public GBACat getCatType() {
    return catType;
  }

  public void setCatType(GBACat catType) {
    this.catType = catType;
  }

  public int getExtIndex() {
    return extIndex;
  }

  public void setExtIndex(int extIndex) {
    this.extIndex = extIndex;
  }

  public int getIntIndex() {
    return intIndex;
  }

  public void setIntIndex(int intIndex) {
    this.intIndex = intIndex;
  }

  public BasePLList<BasePLRec> getRecs() {
    return recs;
  }

  public boolean isMutation() {
    return getRecs().getFirst()
        .orElse(new BasePLRec()).isStatus(MUTATION);
  }

  public boolean hasRecs() {
    return getRecs().isNotEmpty();
  }

  public BasePLRec getCurrentRec() {
    return getRec(CURRENT);
  }

  public BasePLRec getMutationRec() {
    return getRec(MUTATION);
  }

  /**
   * This is either the mutation or current record.
   */
  public BasePLRec getLatestRec() {
    return getRec(MUTATION, CURRENT);
  }

  public List<BasePLRec> getOfficialHistRecs() {
    return getHistRecs().stream()
        .filter(rec -> isBlank(rec.getElemVal(GBAElem.IND_ONJUIST).getVal()))
        .collect(Collectors.toList());
  }

  public List<BasePLRec> getHistRecs() {
    return getRecs().stream()
        .filter(rec -> rec.isStatus(GBARecStatus.HIST))
        .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return astr(getExtIndex());
  }

  public boolean isMostRecentSet() {
    return mostRecentSet;
  }

  public void setMostRecentSet(boolean mostRecentSet) {
    this.mostRecentSet = mostRecentSet;
  }

  /**
   * Always returns an empty record if no records match
   * Combine this method with the hasRecs method.
   */
  private BasePLRec getRec(GBARecStatus... statusses) {
    return getRecs().stream()
        .filter(rec -> rec.isStatus(statusses))
        .findFirst()
        .orElse(new BasePLRec(catType, GBARecStatus.UNKNOWN));
  }
}
