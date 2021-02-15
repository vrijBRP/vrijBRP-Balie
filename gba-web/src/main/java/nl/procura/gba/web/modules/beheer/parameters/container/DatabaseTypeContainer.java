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

package nl.procura.gba.web.modules.beheer.parameters.container;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import nl.procura.vaadin.component.container.ProcuraContainer;

public class DatabaseTypeContainer extends IndexedContainer implements ProcuraContainer {

  private static final String KEY_POSTGRES = "postgres";
  private static final String ORACLE       = "Oracle";
  private static final String POSTGRESQL   = "PostgreSQL";
  private static final String WAARDE       = "Waarde";

  private static final Map<String, Map<String, String>> WAARDEN    = new HashMap<>();
  private static final String                           KEY_ORACLE = ORACLE.toLowerCase();

  static {
    Map<String, String> omschrijving = new HashMap<>();

    omschrijving.put(KEY_POSTGRES, POSTGRESQL);
    omschrijving.put(KEY_ORACLE, ORACLE);
    WAARDEN.put(OMSCHRIJVING, omschrijving);
  }

  public DatabaseTypeContainer() {

    addContainerProperty(WAARDE, String.class, "");
    addContainerProperty(OMSCHRIJVING, String.class, "");
    removeAllItems();
    addItem(KEY_POSTGRES);
    addItem(KEY_ORACLE);
  }

  @Override
  public Item addItem(Object itemId) {

    Item item = null;

    if (itemId instanceof String) {
      String waarde = (String) itemId;

      item = super.addItem(itemId);

      if (item != null) {
        item.getItemProperty(WAARDE).setValue(waarde);
        item.getItemProperty(OMSCHRIJVING).setValue(WAARDEN.get(OMSCHRIJVING).get(waarde));
      }
    }

    return item;
  }
}
