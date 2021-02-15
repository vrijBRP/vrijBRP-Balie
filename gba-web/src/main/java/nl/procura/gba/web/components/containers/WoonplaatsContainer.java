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

import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.pad_left;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;

public class WoonplaatsContainer extends TabelContainer {

  public WoonplaatsContainer() {
    this(false);
  }

  public WoonplaatsContainer(boolean isCurrent) {
    super(GBATable.WOONPLAATS, isCurrent);
  }

  @Override
  public TabelFieldValue get(String waarde) {
    return aval(waarde) >= 0 ? super.get(pad_left(waarde, "0", 4)) : new TabelFieldValue();
  }
}
