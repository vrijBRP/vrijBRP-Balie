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

package nl.procura.gbaws.web.vaadin.module.auth.profile.page3;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.HorizontalLayout;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.table.indexed.IndexedTableFilterLayout;

public class Page2AuthElementFilter extends HorizontalLayout {

  public Page2AuthElementFilter(final Page2AuthElementTable table) {

    setSpacing(true);
    setWidth("100%");

    IndexedTableFilterLayout filterLayout = new IndexedTableFilterLayout(table);

    ProNativeSelect categoryField = new ProNativeSelect();
    categoryField.setContainerDataSource(new GbaCategorieContainer());
    categoryField.setItemCaptionPropertyId(GbaCategorieContainer.DESCR);
    categoryField.setImmediate(true);

    ProNativeSelect statusField = new ProNativeSelect();
    statusField.setContainerDataSource(new GbaCategorieStatusContainer());
    statusField.setItemCaptionPropertyId(GbaCategorieStatusContainer.DESCR);
    statusField.setImmediate(true);

    ProNativeSelect koppelField = new ProNativeSelect();
    koppelField.setContainerDataSource(new GbaCategorieKoppelContainer());
    koppelField.setItemCaptionPropertyId(GbaCategorieKoppelContainer.DESCR);
    koppelField.setImmediate(true);

    categoryField.addListener((ValueChangeListener) event -> {
      table.setCategorie((GBACat) event.getProperty().getValue());
      table.init();
    });

    statusField.addListener((ValueChangeListener) event -> {
      table.setStatus((GBARecStatus) event.getProperty().getValue());
      table.init();
    });

    koppelField.addListener((ValueChangeListener) event -> {
      table.setGekoppeld((Boolean) event.getProperty().getValue());
      table.init();
    });

    categoryField.setWidth("200px");
    statusField.setWidth("200px");
    koppelField.setWidth("200px");

    addComponent(categoryField);
    addComponent(statusField);
    addComponent(koppelField);
    addComponent(filterLayout);

    setExpandRatio(filterLayout, 1f);
  }
}
