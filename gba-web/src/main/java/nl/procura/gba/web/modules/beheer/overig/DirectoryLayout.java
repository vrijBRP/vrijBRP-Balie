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

package nl.procura.gba.web.modules.beheer.overig;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;

import nl.procura.gba.web.common.misc.spreadsheets.Spreadsheet;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GeldigheidField;
import nl.procura.gba.web.components.layouts.GbaHorizontalLayout;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilter;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilterListener;

/**

 * <p>
 * 2012
 * <p>
 * <p>
 * Deze klasse wordt gebruikt voor tabellen met een mapstructuur.
 * Boven de tabel wordt de directory aangegeven waar je je op dat moment bevindt,
 * er wordt een zoekveldje getoond en er wordt een keuzeveldje getoond voor het tonen
 * van de records in mappen of in een lijst.
 */
public class DirectoryLayout extends GbaHorizontalLayout {

  private GbaIndexedTableFilterLayout indexedTableFilterLayout;
  private MapListField                mapListField;
  private StatusField                 statusField;
  private Label                       pathLabel;

  public DirectoryLayout(GbaTable table) {
    this(table, new ArrayList<>());
  }

  public DirectoryLayout(GbaTable table, List<? extends Spreadsheet> spreadsheets) {

    align(Alignment.MIDDLE_CENTER);
    setMapListField(new MapListField());
    setStatusField(new StatusField());
    setIndexedTableFilterLayout(new GbaIndexedTableFilterLayout(table, spreadsheets));

    setPathLabel(new Label());
    getPathLabel().setContentMode(Label.CONTENT_XHTML);

    addComponent(getPathLabel());
    addComponent(getIndexedTableFilterLayout());
    addComponent(getMapListField());
    addComponent(getStatusField());

    IndexedTableFilterListener indexedTableFilterListener = (source, filter) -> {

      IndexedTableFilter iFilter = (IndexedTableFilter) filter;

      if (iFilter != null) {
        if (fil(iFilter.getPattern())) {
          getMapListField().setValue(TabelToonType.LIJST);
        } else {
          getMapListField().setValue(TabelToonType.MAPPEN);
        }
      }
    };

    table.getFilterListeners().add(indexedTableFilterListener);
    setExpandRatio(getPathLabel(), 1f);
  }

  public GbaIndexedTableFilterLayout getIndexedTableFilterLayout() {
    return indexedTableFilterLayout;
  }

  public void setIndexedTableFilterLayout(GbaIndexedTableFilterLayout indexedTableFilterLayout) {
    this.indexedTableFilterLayout = indexedTableFilterLayout;
  }

  public TabelToonType getMapList() {
    return (TabelToonType) mapListField.getValue();
  }

  public MapListField getMapListField() {
    return mapListField;
  }

  public void setMapListField(MapListField select) {
    this.mapListField = select;
  }

  public Label getPathLabel() {
    return pathLabel;
  }

  public void setPathLabel(Label pathLabel) {
    this.pathLabel = pathLabel;
  }

  public GeldigheidStatus getRecordStatus() {
    return statusField.getValue();
  }

  public StatusField getStatusField() {
    return statusField;
  }

  public void setStatusField(StatusField statusField) {
    this.statusField = statusField;
  }

  public void setPath(String path) {
    path = path.replaceAll("/", " > "); // vervang slashes door >
    getPathLabel().setValue("<b>" + (emp(path) ? "Home" : "Home > " + path) + "</b>");
  }

  @SuppressWarnings("unused")
  protected void changeValue(GeldigheidStatus status) {
  } // Override

  @SuppressWarnings("unused")
  protected void changeValue(TabelToonType toonType) {
  } // Override

  public class MapListContainer extends ArrayListContainer {

    public MapListContainer() {
      addItems(TabelToonType.values(), false);
    }
  }

  public class MapListField extends GbaNativeSelect {

    public MapListField() {

      setNullSelectionAllowed(false);
      setContainerDataSource(new MapListContainer());
      setImmediate(true);
      ValueChangeListener changeListener = (ValueChangeListener) event -> changeValue(
          (TabelToonType) event.getProperty().getValue());

      addListener(changeListener);
    }
  }

  public class StatusField extends GeldigheidField {

    @Override
    public void onChangeValue(GeldigheidStatus value) {
      changeValue(value);
    }
  }
}
