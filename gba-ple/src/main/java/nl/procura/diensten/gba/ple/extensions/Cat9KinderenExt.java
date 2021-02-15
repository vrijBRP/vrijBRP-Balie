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

import java.util.List;
import java.util.stream.Collectors;

import nl.procura.burgerzaken.gba.core.enums.GBACat;

public class Cat9KinderenExt extends BasePLCatExt {

  public Cat9KinderenExt(BasePLExt ext) {
    super(ext);
  }

  public List<Cat9KindExt> getKinderen() {
    return getExt().getCat(GBACat.KINDEREN).getSets().stream()
        .map(set -> new Cat9KindExt(set.getLatestRec(),
            getExt().getLatestRec(GBACat.OVERL)))
        .collect(Collectors.toList());
  }
}
