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

package nl.procura.gba.web.components.containers;

import static nl.procura.gba.common.ZaakType.LEGE_PERSOONLIJST;
import static nl.procura.gba.common.ZaakType.ONBEKEND;

import java.util.Arrays;

import nl.procura.gba.common.ZaakType;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class ZaakTypeContainer extends ArrayListContainer {

  public ZaakTypeContainer() {
    Arrays.stream(ZaakType.values())
        .filter(zt -> !zt.is(ONBEKEND, LEGE_PERSOONLIJST))
        .map(type -> new FieldValue(type.getCode(), type.getOms()))
        .forEach(this::addItem);
  }
}