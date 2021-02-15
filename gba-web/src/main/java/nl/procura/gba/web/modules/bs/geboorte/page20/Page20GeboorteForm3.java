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

package nl.procura.gba.web.modules.bs.geboorte.page20;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.TimeField;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.geboorte.page20.Page20GeboorteForm3.Bean;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.fieldvalues.TimeFieldValue;

public class Page20GeboorteForm3 extends GbaForm<Bean> {

  public static final String TIJD = "tijd";

  public Page20GeboorteForm3() {
    setColumnWidths("60px", "");
    setOrder(TIJD);
  }

  @Override
  public Bean getNewBean() {
    return new Bean();
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Bean implements Serializable {

    @Field(customTypeClass = TimeField.class,
        caption = "Tijdstip",
        required = true,
        width = "45px")
    @TextField(maxLength = 5)
    @Immediate
    private TimeFieldValue tijd = new TimeFieldValue();

    public TimeFieldValue getTijd() {
      return tijd;
    }

    public void setTijd(TimeFieldValue tijd) {
      this.tijd = tijd;
    }
  }
}
