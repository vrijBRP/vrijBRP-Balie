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
import com.vaadin.data.util.IndexedContainer;

import nl.procura.gba.web.services.gba.functies.Geslacht;

public class GeslachtContainer extends IndexedContainer {

  public static final String  NORMAAL   = "Normaal";
  private static final String AFKORTING = "Afkorting";
  private static final String VOLLEDIG  = "Volledig";
  private static final String WAARDE    = "Waarde";

  public GeslachtContainer() {

    addContainerProperty(WAARDE, Boolean.class, "");
    addContainerProperty(AFKORTING, String.class, "");
    addContainerProperty(NORMAAL, String.class, "");
    addContainerProperty(VOLLEDIG, String.class, "");
    removeAllItems();
    addItem(Geslacht.MAN);
    addItem(Geslacht.VROUW);
    addItem(Geslacht.ONBEKEND);
  }

  @Override
  public Item addItem(Object itemId) {

    Item item = null;

    if (itemId instanceof Geslacht) {
      Geslacht waarde = (Geslacht) itemId;

      item = super.addItem(itemId);

      if (item != null) {
        item.getItemProperty(WAARDE).setValue(waarde);
        item.getItemProperty(AFKORTING).setValue(waarde.getAfkorting());
        item.getItemProperty(NORMAAL).setValue(waarde.getNormaal());
        item.getItemProperty(VOLLEDIG).setValue(waarde.getVolledig());
      }
    }

    return item;
  }
}
