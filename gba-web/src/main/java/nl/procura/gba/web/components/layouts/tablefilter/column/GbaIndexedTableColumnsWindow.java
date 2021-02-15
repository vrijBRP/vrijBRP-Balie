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

package nl.procura.gba.web.components.layouts.tablefilter.column;

import static ch.lambdaj.Lambda.join;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.services.beheer.parameter.ParameterType;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class GbaIndexedTableColumnsWindow extends GbaModalWindow {

  private final GbaTable table;

  public GbaIndexedTableColumnsWindow(GbaTable table) {

    setCaption("Kolommen van deze tabel (Escape om te sluiten)");

    setWidth("400px");

    this.table = table;
  }

  @Override
  public void attach() {

    super.attach();

    MainModuleContainer mainModule = new MainModuleContainer();

    addComponent(mainModule);

    mainModule.getNavigation().addPage(new Module());
  }

  public class Module extends ModuleTemplate {

    public Module() {

      setMargin(false);
    }

    @Override
    public void event(PageEvent event) {

      super.event(event);

      if (event.isEvent(InitPage.class)) {

        getPages().getNavigation().goToPage(new Page1());
      }
    }
  }

  public class Page1 extends NormalPageTemplate {

    public Page1() {

      setSpacing(true);
    }

    @Override
    public void event(PageEvent event) {

      if (event.isEvent(InitPage.class)) {

        setInfo("Extra kolommen", "Zet extra kolommen aan of uit.");

        final ColumnTable soortTable = new ColumnTable();

        addComponent(soortTable);

        soortTable.selectedColumns();
        soortTable.addSelectionListener();
      }

      super.event(event);
    }
  }

  private class ColumnTable extends GbaTable {

    public ColumnTable() {
    }

    public void addSelectionListener() {

      addListener((ValueChangeListener) event -> {

        List<Column> allValues = getAllValues(Column.class);
        List<Column> selectedValues = getSelectedValues(Column.class);

        Set<String> set = new HashSet<>();

        for (Column column : allValues) {

          if (column.isCollapsible()) {

            boolean collapse = !selectedValues.contains(column);

            // Enable / disable column
            table.setColumnCollapsed(column.getId(), collapse);

            // Update column
            column.setCollapsed(collapse);

            // Add / remove set
            if (collapse) {
              set.remove(column.getCollapseId());
            } else {
              set.add(column.getCollapseId());
            }
          }
        }

        // Store
        ParameterService parameters = getApplication().getServices().getParameterService();
        parameters.setParm(ParameterType.getByKey("table-" + table.getId()), join(set),
            getApplication().getServices().getGebruiker(), null, true);
      });
    }

    public void selectedColumns() {

      for (Column column : table.getColumns()) {
        if (column.isCollapsible() && !column.isCollapsed()) {
          for (Record record : getRecords()) {
            if (record.getObject(Column.class).getId() == column.getId()) {
              select(record.getItemId());
            }
          }
        }
      }
    }

    @Override
    public void setColumns() {

      setSelectable(true);
      setMultiSelect(true);
      setMultiSelectMode(MultiSelectMode.SIMPLE);
      addColumn("Kolom");
    }

    @Override
    public void setRecords() {

      for (Column column : table.getColumns()) {
        if (fil(column.getCaption()) && column.isCollapsible()) {
          Record record = addRecord(column);
          record.addValue(column.getCaption());
        }
      }

      super.setRecords();
    }
  }
}
