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

import static nl.procura.standard.Globalfunctions.pos;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

public class Page60GeboorteStap3 extends GbaForm<Page60GeboorteStap3.Page60GeboorteBean4> {

  public static final String VERBLIJFSLAND = "verblijfsLand";

  public Page60GeboorteStap3(DossierGeboorte geboorte) {

    setCaption("Stap 3");
    setReadThrough(true);
    setColumnWidths("200px", "");
    setOrder(VERBLIJFSLAND);

    Page60GeboorteBean4 bean = new Page60GeboorteBean4();

    if (pos(geboorte.getVerblijfsLandAfstamming().getValue())) {
      bean.setVerblijfsLand(geboorte.getVerblijfsLandAfstamming());
    }

    setBean(bean);
  }

  @Override
  public void setBean(Object bean) {

    super.setBean(bean);

    getField(VERBLIJFSLAND).addListener(new FieldChangeListener<FieldValue>() {

      @Override
      public void onChange(FieldValue value) {
        onWijzigingVerblijfsland(value);
      }
    });
  }

  @Override
  public Page60GeboorteBean4 getNewBean() {
    return new Page60GeboorteBean4();
  }

  public FieldValue getVerblijfsLand() {
    return getBean().getVerblijfsLand();
  }

  public void setVerblijfsLand(FieldValue land) {

    getBean().setVerblijfsLand(land);

    if (getField(VERBLIJFSLAND) != null) {
      getField(VERBLIJFSLAND).setValue(land);
    }
  }

  @SuppressWarnings("unused")
  protected void onWijzigingVerblijfsland(FieldValue land) {
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class Page60GeboorteBean4 implements Serializable {

    @Field(customTypeClass = GbaComboBox.class,
        caption = "Verblijfplaats (land) kind in?",
        required = true,
        width = "300px")
    @Select(containerDataSource = LandContainer.class)
    @Immediate
    private FieldValue verblijfsLand = new FieldValue();
  }
}
