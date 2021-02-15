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

package nl.procura.gba.web.modules.beheer.profielen.container;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.vaadin.component.container.ProcuraContainer;

public class GbaElementFormContainer extends HierarchicalContainer implements ProcuraContainer {

  public static final String ALLE                = "Alle";
  public static final String PROFIELGBACATEGORIE = "profielGbaCategorie";

  public GbaElementFormContainer() {

    addContainerProperty(OMSCHRIJVING, String.class, "");
    addContainerProperty(PROFIELGBACATEGORIE, Object.class, "");
    removeAllItems();
    addTreeItem(ALLE, true);

    GBACat[] gbaCats = GBACat.values();

    for (GBACat gbaCat : gbaCats) {
      if (gbaCat == GBACat.UNKNOWN) {
        continue;
      }

      addTreeItem(gbaCat, false);
      setParent(gbaCat, ALLE);
    }
  }

  private void addTreeItem(Object itemId, boolean childrenAllowed) {

    Item item = addItem(itemId);
    setChildrenAllowed(itemId, childrenAllowed);
    item.getItemProperty(OMSCHRIJVING).setValue(itemId);
    item.getItemProperty(PROFIELGBACATEGORIE).setValue(itemId);
  }
}
