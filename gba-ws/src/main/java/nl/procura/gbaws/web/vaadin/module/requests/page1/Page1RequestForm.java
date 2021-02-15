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

package nl.procura.gbaws.web.vaadin.module.requests.page1;

import static nl.procura.gbaws.web.vaadin.module.requests.page1.Page1RequestBean.*;

import com.vaadin.ui.Field;

import nl.procura.gbaws.db.handlers.UsrDao;
import nl.procura.gbaws.web.vaadin.layouts.forms.DefaultForm;
import nl.procura.gbaws.web.vaadin.module.requests.page1.periodes.Anders;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public abstract class Page1RequestForm extends DefaultForm {

  public Page1RequestForm() {

    setCaption("Zoekargumenten");
    setColumnWidths("70px", "");
    setOrder(PERIODE, VAN, TM, GEBRUIKER, ZOEKEN);
    setBean(new Page1RequestBean());
  }

  @Override
  public Page1RequestBean getBean() {
    return (Page1RequestBean) super.getBean();
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

          setColumnWidths("70px", "300px", "50px", "100px", "50px", "");
        } else {

          getField(VAN).setVisible(false);
          getField(TM).setVisible(false);

          setColumnWidths("70px", "");
        }

        repaint();

        zoek();
      });
    }

    if (getField(GEBRUIKER) != null) {
      getField(GEBRUIKER, ProNativeSelect.class).setContainerDataSource(new UsrContainer(UsrDao.getUsers()));
      getField(GEBRUIKER).addListener((ValueChangeListener) event -> zoek());
    }
  }

  protected abstract void zoek();

  @Override
  public void setColumn(Column column, com.vaadin.ui.Field field, Property property) {

    Field periode = getField(PERIODE);
    boolean isAnders = periode != null && periode.getValue() != null && periode.getValue().equals(new Anders());

    if (isAnders && property.is(ZOEKEN)) {
      column.setColspan(5);
    }

    if (isAnders && property.is(GEBRUIKER)) {
      column.setColspan(5);
    }

    super.setColumn(column, field, property);
  }
}
