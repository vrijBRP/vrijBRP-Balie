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

package nl.procura.gba.web.modules.beheer.onderhoud.licenses;

import static com.vaadin.ui.AbstractTextField.TextChangeEventMode.LAZY;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.TextField;

import nl.procura.burgerzaken.dependencies.models.Dependency;
import nl.procura.burgerzaken.dependencies.models.DependencyLicenses;
import nl.procura.burgerzaken.dependencies.models.License;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.standard.Resource;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilter;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilterLayout;

public class LicensesListWindow extends GbaModalWindow {

  public LicensesListWindow() {
    super("Open source licenties (Escape om te sluiten)", "800px");
    setHeight("600px");

    GbaTable table = new GbaTable() {

      @Override
      public void setColumns() {

        setSelectable(true);
        setMultiSelect(true);
        addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

        addColumn("Nr.", 50);
        addColumn("Naam open source component", 300);
        addColumn("Licentie(s)");
      }

      @Override
      public void setRecords() {
        InputStream asInputStream = Resource.getAsInputStream("burgerzaken-licenses.json");
        InputStreamReader inputStreamReader = new InputStreamReader(asInputStream);
        DependencyLicenses dependencies = new Gson().fromJson(inputStreamReader, DependencyLicenses.class);
        dependencies.getDependencies().sort(Comparator.comparing(Dependency::getName));

        int nr = 0;
        for (Dependency dependency : dependencies.getDependencies()) {
          Record record = addRecord(dependency);
          record.addValue(++nr);
          record.addValue(dependency.getName());
          record.addValue(getLicenses(dependency.getLicenses()));
        }

        super.setRecords();
      }

      @Override
      public void onClick(Record record) {
        LicenseDetailWindow licenseDetailWindow = new LicenseDetailWindow(record.getObject(Dependency.class));
        getGbaApplication().getParentWindow().addWindow(licenseDetailWindow);
        super.onClick(record);
      }

      private String getLicenses(List<License> licenses) {
        if (licenses.isEmpty()) {
          return "-";
        }
        return licenses
            .stream()
            .map(License::getOfficialName)
            .collect(Collectors.joining(", "));
      }
    };

    TextField textField = new TextField();
    textField.setInputPrompt("Filter");
    textField.setWidth("100%");
    textField.setTextChangeTimeout(200);
    textField.setTextChangeEventMode(LAZY);
    textField.addListener((FieldEvents.TextChangeListener) e -> {
      table.setFilter(IndexedTableFilterLayout.class,
          new IndexedTableFilter(e.getText(),
              false));
    });

    setContent(new VLayout()
        .margin(true)
        .add(textField)
        .add(new InfoLayout("", "Klik op de regel voor meer informatie."))
        .addExpandComponent(table));
  }
}
