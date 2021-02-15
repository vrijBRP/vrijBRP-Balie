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

import com.vaadin.ui.Component;

import nl.procura.gba.web.components.buttons.KennisbankButton;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.erkenning.page20.Page20ErkenningForm2.Page20ErkenningBean2;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankBron;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankDoel;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page20ErkenningForm2 extends GbaForm<Page20ErkenningBean2> {

  public static final String STAP2      = "stap2";
  public static final String VBT_MOEDER = "vbtMoeder";
  public static final String VBT_KIND   = "vbtKind";

  public Page20ErkenningForm2() {
    setCaptionAndOrder();
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(VBT_MOEDER)) {
      Component kb = new KennisbankButton(KennisBankBron.LAND, KennisBankDoel.AFSTAMMING, getVerblijfplaats());
      column.addComponent(kb);
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public Page20ErkenningBean2 getNewBean() {
    return new Page20ErkenningBean2();
  }

  public void setCaptionAndOrder() {
  }

  protected FieldValue getVerblijfplaats() {
    return null;
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class Page20ErkenningBean2 implements Serializable {

    @Field(customTypeClass = GbaNativeSelect.class,
        caption = "Mogelijkheid naar recht verblijfplaats (land) kind?",
        required = true)
    @Select(containerDataSource = NLBooleanContainer.class,
        itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
    @Immediate
    private Boolean stap2 = null;

    @Field(type = FieldType.LABEL,
        caption = "Verblijfplaats moeder in")
    private String vbtMoeder = "";

    @Field(type = FieldType.LABEL,
        caption = "Verblijfplaats kind in")
    private String vbtKind = "";

    public Boolean getStap2() {
      return stap2;
    }

    public void setStap2(Boolean stap2) {
      this.stap2 = stap2;
    }

    public String getVbtKind() {
      return vbtKind;
    }

    public void setVbtKind(String vbtKind) {
      this.vbtKind = vbtKind;
    }

    public String getVbtMoeder() {
      return vbtMoeder;
    }

    public void setVbtMoeder(String vbtMoeder) {
      this.vbtMoeder = vbtMoeder;
    }
  }
}
