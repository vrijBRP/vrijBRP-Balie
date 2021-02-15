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

package nl.procura.gba.web.modules.zaken.protocol.page1;

import static nl.procura.gba.web.modules.zaken.protocol.page1.Page1ProtocolleringBean.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.web.components.containers.GebruikerContainer;
import nl.procura.gba.web.components.fields.BsnAnrVeld;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.telling.Anders;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page1ProtocolleringForm extends GbaForm<Page1ProtocolleringBean> {

  final long anr;

  public Page1ProtocolleringForm(long anr) {

    this.anr = anr;
    setCaption("Zoekargumenten");
    setReadThrough(true);
    setOrder(ANR, GEBRUIKER, PERIODE, VAN, TM, GROEP);
    setColumnWidths("100px", "");
  }

  @Override
  public void setBean(Object bean) {

    super.setBean(bean);

    if (pos(anr)) { // Als bij PL dan A-nummer prefillen

      getBean().setAnr(new FieldValue(astr(anr)));
      getField(ANR, BsnAnrVeld.class).setValue(new FieldValue(astr(anr)));
      getField(ANR, BsnAnrVeld.class).setReadOnly(true);
    }

    GbaComboBox select = getField(GEBRUIKER, GbaComboBox.class);

    select.setContainerDataSource(
        new GebruikerContainer(getApplication().getServices().getGebruikerService().getGebruikers(false)));

    if (getField(PERIODE) != null) {
      getField(PERIODE).addListener((ValueChangeListener) event -> {

        Object v = event.getProperty().getValue();

        if ((v != null) && v.equals(new Anders())) {

          getField(VAN).setVisible(true);
          getField(TM).setVisible(true);

          setColumnWidths("100px", "230px", "30px", "100px", "30px", "");
        } else {

          getField(VAN).setVisible(false);
          getField(TM).setVisible(false);

          setColumnWidths("100px", "");
        }

        repaint();
      });
    }
  }

  @Override
  public Page1ProtocolleringBean getNewBean() {
    return new Page1ProtocolleringBean();
  }

  @Override
  public void setColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (getField(PERIODE) != null) {

      Object p = getField(PERIODE).getValue();
      if (property.is(GEBRUIKER, ANR, GROEP) && (p != null) && p.equals(new Anders())) {
        column.setColspan(5);
      }
    }

    super.setColumn(column, field, property);
  }
}
