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

package nl.procura.gbaws.web.vaadin.layouts;

import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.TextField;

import nl.procura.vaadin.component.table.indexed.IndexedTable;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilter;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilterLayout;

public class SimpleTableFilter extends GbaWsHorizontalLayout {

  private final TextField textField = new TextField();

  public SimpleTableFilter(final IndexedTable table) {

    table.getFilterListeners().add((source, filter) -> {

      if ((source != IndexedTableFilterLayout.class) && (filter instanceof IndexedTableFilter)) {
        textField.setValue(((IndexedTableFilter) filter).getPattern());
      }
    });

    setWidth("100%");
    textField.setWidth("100%");
    textField.setInputPrompt("Geef een zoekterm in");
    textField.setTextChangeEventMode(TextChangeEventMode.LAZY);
    textField.setTextChangeTimeout(200);
    textField.addListener((TextChangeListener) event -> table.setFilter(IndexedTableFilterLayout.class,
        new IndexedTableFilter(event.getText(), false)));

    addComponent(textField);
  }
}
