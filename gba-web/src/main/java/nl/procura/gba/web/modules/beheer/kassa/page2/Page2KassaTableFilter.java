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

package nl.procura.gba.web.modules.beheer.kassa.page2;

import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.TextField;

import nl.procura.gba.web.components.layouts.GbaHorizontalLayout;
import nl.procura.vaadin.component.table.indexed.IndexedTable;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilter;

@SuppressWarnings("serial")
public class Page2KassaTableFilter extends GbaHorizontalLayout {

  private final TextField textField = new TextField();
  private IndexedTable    table;

  public Page2KassaTableFilter(IndexedTable table) {

    setSizeFull();

    this.table = table;

    table.getFilterListeners().add((source, filter) -> {

      if ((source != Page2KassaTableFilter.class) && (filter instanceof IndexedTableFilter)) {

        IndexedTableFilter iFilter = (IndexedTableFilter) filter;

        textField.setValue(iFilter.getPattern());
      }
    });

    textField.setTextChangeEventMode(TextChangeEventMode.LAZY);
    textField.setWidth("100%");
    textField.setTextChangeTimeout(200);
    textField.addListener((TextChangeListener) event -> getTable().setFilter(Page2KassaTableFilter.class,
        new IndexedTableFilter(event.getText(), false)));

    addComponent(textField);
  }

  public IndexedTable getTable() {
    return table;
  }

  public void setTable(IndexedTable table) {
    this.table = table;
  }
}
