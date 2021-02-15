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

package nl.procura.gba.web.components.layouts.table;

import static nl.procura.standard.Globalfunctions.fil;

import java.util.Arrays;
import java.util.List;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.beheer.parameter.ParameterType;
import nl.procura.vaadin.component.table.indexed.IndexTableDefaultContextMenu;
import nl.procura.vaadin.component.table.indexed.IndexedTable;

public class GbaTable extends IndexedTable {

  private String id = null;

  public GbaTable() {
  }

  public GbaTable(String id) {
    this.id = id;
  }

  @Override
  public GbaApplication getApplication() {
    return (GbaApplication) super.getApplication();
  }

  public String getId() {
    return id;
  }

  @Override
  public void refreshVisibility() {

    if (fil(id)) {

      List<String> unCollapsedColumns = getUnCollapsedColumnsFromDatabase();
      for (Column column : getColumns()) {
        if (column.isCollapsible()) {
          setColumnCollapsingAllowed(true);
          boolean collapsed = column.isCollapsed();

          if (unCollapsedColumns.size() > 0) {
            collapsed = !unCollapsedColumns.contains(column.getCollapseId());
          }

          setColumnCollapsed(column.getId(), collapsed);
          column.setCollapsed(collapsed);
        }
      }
    } else {
      super.refreshVisibility();
    }
  }

  @Override
  public void setMultiSelect(boolean multiSelect) {

    if (multiSelect) {
      removeAllActionHandlers();
      addActionHandler(new IndexTableDefaultContextMenu(this));
    }

    super.setMultiSelect(multiSelect);
  }

  private List<String> getUnCollapsedColumnsFromDatabase() {
    String value = getApplication().getParmValue(ParameterType.getByKey("table-" + id));
    return Arrays.asList(value.split("\\s*,\\s*"));
  }
}
