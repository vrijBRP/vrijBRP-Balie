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
import nl.procura.gba.web.modules.bs.erkenning.page20.Page20ErkenningForm3.Page20ErkenningBean3;
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

public class Page20ErkenningForm3 extends GbaForm<Page20ErkenningBean3> {

  public static final String STAP3      = "stap3";
  public static final String NAT_MOEDER = "natMoeder";
  public static final String NAT_KIND   = "natKind";

  public Page20ErkenningForm3() {
    setCaptionAndOrder();
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(NAT_MOEDER)) {
      Component kb = new KennisbankButton(KennisBankBron.NATIONALITEIT, KennisBankDoel.AFSTAMMING,
          getNationaliteitMoeder());
      column.addComponent(kb);
    }
    if (property.is(NAT_KIND)) {
      Component kb = new KennisbankButton(KennisBankBron.NATIONALITEIT, KennisBankDoel.AFSTAMMING,
          getNationaliteitKind());
      column.addComponent(kb);
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public Page20ErkenningBean3 getNewBean() {
    return new Page20ErkenningBean3();
  }

  public void setCaptionAndOrder() {
  }

  protected FieldValue getNationaliteitKind() { // Override
    return null;
  }

  protected FieldValue getNationaliteitMoeder() { // Override
    return null;
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class Page20ErkenningBean3 implements Serializable {

    @Field(customTypeClass = GbaNativeSelect.class,
        caption = "Mogelijkheid naar recht nationaliteit kind (zonder erkenning)?",
        required = true)
    @Select(containerDataSource = NLBooleanContainer.class,
        itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
    @Immediate
    private Boolean stap3 = null;

    @Field(type = FieldType.LABEL,
        caption = "Nationaliteit(en) moeder")
    private String natMoeder = "";

    @Field(type = FieldType.LABEL,
        caption = "Nationaliteit(en) kind")
    private String natKind = "";

    public String getNatKind() {
      return natKind;
    }

    public void setNatKind(String natKind) {
      this.natKind = natKind;
    }

    public String getNatMoeder() {
      return natMoeder;
    }

    public void setNatMoeder(String natMoeder) {
      this.natMoeder = natMoeder;
    }

    public Boolean getStap3() {
      return stap3;
    }

    public void setStap3(Boolean stap3) {
      this.stap3 = stap3;
    }
  }
}
