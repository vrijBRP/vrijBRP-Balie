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
import java.util.List;

import com.vaadin.ui.Component;

import nl.procura.gba.web.components.buttons.KennisbankButton;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.erkenning.page20.Page20ErkenningForm1.Page20ErkenningBean1;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankBron;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankDoel;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page20ErkenningForm1 extends GbaForm<Page20ErkenningBean1> {

  public static final String STAP1 = "stap1";
  public static final String NAT1  = "nat1";
  public static final String VBT1  = "vbt1";

  public Page20ErkenningForm1() {
    setCaptionAndOrder();
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(NAT1)) {
      Component kb = new KennisbankButton(KennisBankBron.NATIONALITEIT, KennisBankDoel.AFSTAMMING,
          getNationaliteiten());
      column.addComponent(kb);
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public Page20ErkenningBean1 getNewBean() {
    return new Page20ErkenningBean1();
  }

  public void setCaptionAndOrder() {
  }

  protected List<DossierNationaliteit> getNationaliteiten() {
    return null;
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class Page20ErkenningBean1 implements Serializable {

    @Field(customTypeClass = GbaNativeSelect.class,
        caption = "Mogelijk naar recht nationaliteit erkenner of verblijfsvergunning asiel (code 26 of 27)?",
        required = true)
    @Select(containerDataSource = NLBooleanContainer.class,
        itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
    @Immediate
    private Boolean stap1 = null;

    @Field(type = FieldType.LABEL,
        caption = "Nationaliteit erkenner")
    private String nat1 = "";

    @Field(type = FieldType.LABEL,
        caption = "Verblijfstitelcode")
    private String vbt1 = "";

    public String getNat1() {
      return nat1;
    }

    public void setNat1(String nat1) {
      this.nat1 = nat1;
    }

    public Boolean getStap1() {
      return stap1;
    }

    public void setStap1(Boolean stap1) {
      this.stap1 = stap1;
    }

    public String getVbt1() {
      return vbt1;
    }

    public void setVbt1(String vbt1) {
      this.vbt1 = vbt1;
    }
  }
}
