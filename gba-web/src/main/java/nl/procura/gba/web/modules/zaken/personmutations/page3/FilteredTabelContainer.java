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

package nl.procura.gba.web.modules.zaken.personmutations.page3;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.gba.web.components.containers.TabelContainer;
import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;

public class FilteredTabelContainer extends TabelContainer {

  public FilteredTabelContainer(GBATable table, boolean isCurrent, boolean showValue) {
    super(table, isCurrent, showValue);
  }

  @Override
  public void add(TabelFieldValue record, boolean isCurrent, boolean showValue) {
    // Ignore administratieve historie
    if (getTabel().isTable(GBATable.IND_ONJUIST)) {
      if (record.getStringValue().matches("[XY]")) {
        return;
      }
    }
    super.add(record, isCurrent, showValue);
  }
}
