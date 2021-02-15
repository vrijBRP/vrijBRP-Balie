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

import com.vaadin.data.Item;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;

public class VerblijfstitelContainer extends TabelContainer {

  public VerblijfstitelContainer() {
    this(false);
  }

  private VerblijfstitelContainer(boolean isCurrent) {
    super(GBATable.VERBLIJFSTITEL, isCurrent);
  }

  @Override
  public Item addItem(Object itemId) {

    if (itemId instanceof FieldValue) {
      TabelFieldValue fd = (TabelFieldValue) itemId;
      return super.addItem(new TabelFieldValue(fd.getValue(), fd.getValue() + ": " + fd.getDescription()));
    }

    return super.addItem(itemId);

  }
}
