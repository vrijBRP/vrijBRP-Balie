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

package nl.procura.gba.web.modules.bs.lv.page40;

import java.util.Arrays;

import nl.procura.gba.web.services.bs.lv.afstamming.LvToestemmingType;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class LvToestemmingTypeContainer extends ArrayListContainer {

  public LvToestemmingTypeContainer() {
    Arrays.stream(LvToestemmingType.values())
        .filter(type -> type != LvToestemmingType.ONBEKEND)
        .forEach(this::addItem);
  }
}
