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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.overige.tree;

import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;

import nl.procura.vaadin.component.container.ProcuraContainer;

public class ZaakOverigeContainer extends HierarchicalContainer implements ProcuraContainer {

  public static final String ID             = "Id";
  public static final String IDENTIFICATIES = "Identificaties";
  public static final String ATTRIBUTEN     = "Attributen";
  public static final String RELATIES       = "Relaties";
  public static final String BEHANDELAARS   = "Behandelaars";
  public static final String AANTAL         = "Aantal";

  public ZaakOverigeContainer() {

    addContainerProperty(ID, String.class, "");
    addContainerProperty(OMSCHRIJVING, String.class, "");
    addContainerProperty(AANTAL, String.class, "");
    removeAllItems();

    // Items toevoegen
    addTreeItem(IDENTIFICATIES);
    addTreeItem(ATTRIBUTEN);
    addTreeItem(RELATIES);
    addTreeItem(BEHANDELAARS);
  }

  private Item addTreeItem(Object itemId) {

    Item item = addItem(itemId);
    String oms = astr(itemId);

    setChildrenAllowed(itemId, false);

    item.getItemProperty(ID).setValue(oms);
    item.getItemProperty(OMSCHRIJVING).setValue(oms);

    return item;
  }
}
