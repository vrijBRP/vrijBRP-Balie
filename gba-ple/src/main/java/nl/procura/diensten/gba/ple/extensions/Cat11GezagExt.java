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

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLValue;

public class Cat11GezagExt extends BasePLCatExt {

  public Cat11GezagExt(BasePLExt pl) {
    super(pl);
  }

  public BasePLValue getIndicatieGezag() {
    return getElem(GBACat.GEZAG, GBAElem.IND_GEZAG_MINDERJ);
  }

  public boolean heeftGezag() {
    return fil(getIndicatieGezag().getVal());
  }

  public boolean heeftGezagDerden() {
    return getIndicatieGezag().getVal().matches("D|1D|2D");
  }

  public boolean staatOnderCuratele() {
    return pos(getElem(GBACat.GEZAG, GBAElem.IND_CURATELE_REG).getVal());
  }
}
