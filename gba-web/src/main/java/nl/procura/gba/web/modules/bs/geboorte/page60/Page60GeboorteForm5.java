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

package nl.procura.gba.web.modules.bs.geboorte.page60;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import com.vaadin.data.Container;

import nl.procura.gba.web.components.buttons.KennisbankButton;
import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankBron;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankDoel;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

import lombok.Data;

public class Page60GeboorteForm5 extends GbaForm<Page60GeboorteForm5.Page60GeboorteBean5> {

  public static final String RECHT = "recht";

  public Page60GeboorteForm5() {

    setColumnWidths("200px", "");

    setCaptionAndOrder();
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(RECHT)) {

      column.addComponent(new KennisbankButton(KennisBankBron.LAND, KennisBankDoel.AFSTAMMING, 0) {

        @Override
        public long getCode() {
          return getFieldValueCode(RECHT);
        }
      });
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public Page60GeboorteBean5 getNewBean() {
    return new Page60GeboorteBean5();
  }

  public void setCaptionAndOrder() {
  }

  public void setRecht(FieldValue land) {
    getField(RECHT).setValue(land);
  }

  public void updateContainer(FieldValue land) {

    Container container = getContainer(land);

    if (container != null) {

      getField(RECHT, GbaNativeSelect.class).setDataSource(container);
    }
  }

  /**
   * Haal container op plus de verblijfplaats
   */
  protected Container getContainer(FieldValue land) {
    return null;
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Page60GeboorteBean5 implements Serializable {

    @Field(customTypeClass = GbaNativeSelect.class,
        caption = "Toegepast recht van",
        required = true,
        width = "300px")
    @Select(containerDataSource = LandContainer.class)
    private FieldValue recht = null;
  }
}
