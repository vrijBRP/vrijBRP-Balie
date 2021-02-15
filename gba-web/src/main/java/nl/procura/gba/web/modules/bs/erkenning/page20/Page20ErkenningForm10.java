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

package nl.procura.gba.web.modules.bs.erkenning.page20;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import com.vaadin.data.Container;

import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.erkenning.page20.Page20ErkenningForm10.Page20ErkenningBean10;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Page20ErkenningForm10 extends GbaForm<Page20ErkenningBean10> {

  public static final String RECHT = "recht";

  public Page20ErkenningForm10() {

    setColumnWidths("140px", "");

    setCaptionAndOrder();
  }

  @Override
  public void afterSetBean() {

    if (getField(RECHT) != null) {

      Container container = getContainer();

      if (container != null) {
        getField(RECHT, GbaNativeSelect.class).setDataSource(container);
      }
    }

    super.afterSetBean();
  }

  @Override
  public Page20ErkenningBean10 getNewBean() {
    return new Page20ErkenningBean10();
  }

  public void setCaptionAndOrder() {
  }

  protected Container getContainer() {
    return null;
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class Page20ErkenningBean10 implements Serializable {

    @Field(customTypeClass = GbaNativeSelect.class,
        caption = "Toegepast recht van",
        required = true)
    @Select(containerDataSource = LandContainer.class)
    private FieldValue recht = null;

    public FieldValue getRecht() {
      return recht;
    }

    public void setRecht(FieldValue recht) {
      this.recht = recht;
    }
  }
}
