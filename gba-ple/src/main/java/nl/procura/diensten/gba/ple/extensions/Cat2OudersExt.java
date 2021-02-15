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

import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.java.collection.Collections;

public class Cat2OudersExt extends BasePLCatExt {

  public Cat2OudersExt(BasePLExt ext) {
    super(ext);
  }

  public Cat2OuderExt getOuder(int nr) {
    switch (nr) {
      case 1:
        return getOuder1();
      case 2:
        return getOuder2();
      default:
        return null;
    }
  }

  /**
   * Kind heeft ouder
   */
  public boolean heeftOuder(int nr) {
    return heeftOuder(getOuder(nr));
  }

  public boolean heeftOuder(Cat2OuderExt ouder) {
    return fil(ouder.getNaam().getGeslachtsnaam().getValue().getDescr());
  }

  public Cat2OuderExt getOuder1() {
    return new Cat2OuderExt(getExt().getLatestRec(GBACat.OUDER_1),
        getExt().getLatestRec(GBACat.OVERL));
  }

  public Cat2OuderExt getOuder2() {
    return new Cat2OuderExt(getExt().getLatestRec(GBACat.OUDER_2),
        getExt().getLatestRec(GBACat.OVERL));
  }

  public List<Cat2OuderExt> getOuders() {
    return Collections.list(getOuder1(), getOuder2());
  }

  public int getAantalOuders() {
    int nr = 0;
    if (heeftOuder(1)) {
      nr++;
    }
    if (heeftOuder(2)) {
      nr++;
    }
    return nr;
  }
}
