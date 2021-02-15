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

import static nl.procura.standard.Globalfunctions.aval;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLValue;

public class Cat10VbtExt extends BasePLCatExt {

  public Cat10VbtExt(BasePLExt ext) {
    super(ext);
  }

  public BasePLValue getVerblijfstitel() {
    BasePLRec r = getExt().getLatestRec(GBACat.VBTITEL);
    if (r.hasElems()) {
      return r.getElemVal(GBAElem.AAND_VBT);
    }

    return new BasePLValue();
  }

  public boolean isVerblijfstitelCode(int... codes) {
    int vCode = aval(getVerblijfstitel().getVal());
    for (int i : codes) {
      if (i == vCode) {
        return true;
      }
    }

    return false;
  }
}
