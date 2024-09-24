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

import static java.util.Arrays.asList;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_GEG_IN_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_EINDE_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_ONJUIST;
import static nl.procura.standard.Globalfunctions.fil;

import java.io.Serializable;
import java.util.Arrays;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;

public class BasePLRec implements Serializable {

  private static final long serialVersionUID = -8777827670983531163L;

  private GBACat                 catType = GBACat.UNKNOWN;
  private GBARecStatus           status  = GBARecStatus.UNKNOWN;
  private BasePLSet                    set     = new BasePLSet(GBACat.UNKNOWN, 1);
  private final BasePLList<BasePLElem> elems   = new BasePLList<>();
  private int                          index   = 0;

  public BasePLRec() {
  }

  public BasePLRec(GBACat catType, GBARecStatus status) {
    setCatType(catType);
    setStatus(status);
  }

  public BasePLRec(GBACat catType, BasePLSet set, GBARecStatus status) {
    this(catType, status);
    setSet(set);
  }

  public GBACat getCatType() {
    return catType;
  }

  public void setCatType(GBACat catType) {
    this.catType = catType;
  }

  public GBARecStatus getStatus() {
    return status;
  }

  public void setStatus(GBARecStatus status) {
    this.status = status;
  }

  public BasePLList<BasePLElem> getElems() {
    return elems;
  }

  /**
   * Is dit record komen te vervallen na een BAG wijziging?
   */
  public boolean isBagChange() {
    boolean returnValue = false;
    try {
      if (catType == GBACat.VB) {
        int nr = set.getRecs().indexOf(this);
        returnValue = BasePLUtils.isBagChange(set, set.getRecs().get(nr), nr);
      }
    } catch (UnknownGBAElementException e) {
      returnValue = false;
    }
    return returnValue;
  }

  public boolean isCorrect() {
    return !isIncorrect();
  }

  public boolean isIncorrect() {
    try {
      return fil(getElemVal(IND_ONJUIST).getCode());
    } catch (UnknownGBAElementException e) {
      return false;
    }
  }

  public boolean isAdmHistory() {
    try {
      return asList("X", "Y").contains(getElemVal(IND_ONJUIST).getCode());
    } catch (UnknownGBAElementException e) {
      return false;
    }
  }

  public boolean isConflicting() {
    try {
      return "S".equals(getElemVal(IND_ONJUIST).getCode());
    } catch (UnknownGBAElementException e) {
      return false;
    }
  }

  public boolean isInOnderzoek() {
    try {
      return getElemVal(AAND_GEG_IN_ONDERZ).isNotBlank() && !getElemVal(DATUM_EINDE_ONDERZ).isNotBlank();
    } catch (UnknownGBAElementException e) {
      return false;
    }
  }

  public boolean hasElems() {
    return getElems().isNotEmpty();
  }

  public boolean isElem(int elemNr) {
    return GBAGroupElements.getByCat(getCatType().getCode(), elemNr) != null;
  }

  public BasePLElem getElem(GBAElem type) {
    return getElem(type.getCode());
  }

  public BasePLValue getElemVal(GBAElem type) {
    return getElem(type.getCode()).getValue();
  }

  public boolean isElem(GBAElem type) {
    return isElem(type.getCode());
  }

  public BasePLElem getElem(int elemNr) {
    if (getCatType() != GBACat.UNKNOWN) {
      if (!isElem(elemNr)) {
        throw new UnknownGBAElementException(getCatType().getCode(), elemNr);
      }
      for (BasePLElem elem : getElems()) {
        if (elem.getElemCode() == elemNr) {
          return elem;
        }
      }
    }

    return new BasePLElem(getCatType().getCode(), elemNr, "");
  }

  public boolean isStatus(GBARecStatus... statussen) {
    return Arrays.stream(statussen).anyMatch(stat -> stat == getStatus());
  }

  public boolean isCat(GBACat... cats) {
    return Arrays.stream(cats).anyMatch(cat -> getCatType() == cat);
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public BasePLSet getSet() {
    return set;
  }

  public void setSet(BasePLSet set) {
    this.set = set;
  }
}
