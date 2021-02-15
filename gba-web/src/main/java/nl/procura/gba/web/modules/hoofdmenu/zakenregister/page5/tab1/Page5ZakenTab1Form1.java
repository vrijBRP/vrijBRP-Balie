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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page5.tab1;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.page5.tab1.Page5ZakenTab1Bean1.*;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.telling.Anders;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page5ZakenTab1Form1 extends GbaForm<Page5ZakenTab1Bean1> {

  Page5ZakenTab1Form1() {
    setCaption("Zoekargumenten");
    setOrder(PERIODE, VAN, TM, STATUS, MAX, AANGEVER);
    setColumnWidths("140px", "");
    setBean(new Page5ZakenTab1Bean1());
  }

  @Override
  public void setBean(Object bean) {
    super.setBean(bean);

    if (getField(PERIODE) != null) {
      getField(PERIODE).addListener((ValueChangeListener) event -> {

        Object v = event.getProperty().getValue();
        if ((v != null) && v.equals(new Anders())) {
          getField(VAN).setVisible(true);
          getField(TM).setVisible(true);
          setColumnWidths("120px", "300px", "80px", "100px", "30px", "");
        } else {
          getField(VAN).setVisible(false);
          getField(TM).setVisible(false);
          setColumnWidths("120px", "");

          onReload();
        }

        repaint();
      });
    }
  }

  @Override
  public void setColumn(Column column, com.vaadin.ui.Field field, Property property) {

    boolean isAnders = getField(PERIODE) != null && getField(PERIODE).getValue() != null &&
        getField(PERIODE).getValue().equals(new Anders());

    if (isAnders && property.is(STATUS)) {
      column.setColspan(5);
    }

    super.setColumn(column, field, property);
  }

  private void onReload() {
    // Overriden please!!!
  }
}
