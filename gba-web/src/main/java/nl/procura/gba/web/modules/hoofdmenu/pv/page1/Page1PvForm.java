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

package nl.procura.gba.web.modules.hoofdmenu.pv.page1;

import static nl.procura.gba.web.modules.hoofdmenu.pv.page1.Page1PvBean.*;
import static nl.procura.standard.Globalfunctions.along;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.telling.Anders;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page1PvForm extends GbaForm<Page1PvBean> {

  public Page1PvForm() {
    setColumnWidths("90px", "");
    setCaption("Zoeken");
    setOrder(PERIODE, VAN, TM, INHOUD_BERICHT);
    setBean(new Page1PvBean());
  }

  @Override
  public void setBean(Object bean) {

    super.setBean(bean);
    for (final Field field : getFields(PERIODE)) {
      if (field != null) {
        FieldChangeListener fieldChangeListener = new FieldChangeListener() {

          @Override
          public void onChange(Object value) {
            if (getField(PERIODE) == field) {
              onChangePeriode(value);
              repaint();
            }

            onReload();
          }
        };

        field.addListener(fieldChangeListener);
      }
    }
  }

  public long getDatumTm() {

    long datum = -1;
    Page1PvBean b = getBean();
    if (b.getPeriode() != null) {
      if (b.getPeriode().equals(new Anders())) {
        datum = along(b.getTm().getValue());
      } else {
        datum = b.getPeriode().getdTo();
      }
    }
    return datum;
  }

  public long getDatumVan() {

    long datum = -1;
    Page1PvBean b = getBean();
    if (b.getPeriode() != null) {
      if (b.getPeriode().equals(new Anders())) {
        datum = along(b.getVan().getValue());
      } else {
        datum = b.getPeriode().getdFrom();
      }
    }
    return datum;
  }

  public void onReload() {
    // Overriden please!!!
  }

  @Override
  public void setColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (isPeriodeAnders() && property.is(INHOUD_BERICHT)) {
      column.setColspan(5);
    }
  }

  private boolean isPeriodeAnders() {
    return getField(PERIODE) != null && new Anders().equals(getField(PERIODE).getValue());
  }

  private void onChangePeriode(Object periode) {

    if (new Anders().equals(periode)) {

      getField(VAN).setVisible(true);
      getField(TM).setVisible(true);

      setColumnWidths("90px", "250px", "30px", "100px", "30px", "");
    } else {

      getField(VAN).setVisible(false);
      getField(TM).setVisible(false);

      setColumnWidths("90px", "");
    }
  }
}
