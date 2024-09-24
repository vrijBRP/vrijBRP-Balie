/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.bs.lv.page30;

import static java.util.Comparator.comparing;

import java.util.Arrays;

import nl.procura.gba.web.components.containers.GbaContainer;
import nl.procura.gba.web.services.bs.lv.LvType;

public class SoortLvContainer extends GbaContainer {

  public SoortLvContainer() {
    Arrays.stream(LvType.values())
        .sorted(comparing(LvType::getOms))
        .filter(type -> type != LvType.ONBEKEND)
        .forEach(this::addItem);
  }
}
