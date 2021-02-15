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

package nl.procura.gba.web.modules.zaken.tmv.page9.componenten;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;

public class CategorieContainer extends IndexedContainer {

  public static final String OMSCHRIJVING = "omschrijving";

  public CategorieContainer() {

    addContainerProperty(OMSCHRIJVING, String.class, "");
    removeAllItems();

    for (GBACat c : GBACat.values()) {
      if (hasAuthValues(c) && c.isAuthentic()) {
        addItem(c);
      }
    }
  }

  @Override
  public Item addItem(Object itemId) {

    Item item = null;

    if (itemId instanceof GBACat) {
      GBACat waarde = (GBACat) itemId;
      item = super.addItem(itemId);
      if (item != null) {
        item.getItemProperty(OMSCHRIJVING).setValue(waarde.getCode() + ": " + waarde.getDescr());
      }
    }

    return item;
  }

  private boolean hasAuthValues(GBACat c) {
    return GBAGroupElements.getByCat(c.getCode()).stream().anyMatch(GBAGroupElements.GBAGroupElem::isAuthentic);
  }
}
