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

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.container.NLBooleanContainer;

import lombok.Data;

public class Page60GeboorteFormStap1a extends GbaForm<Page60GeboorteFormStap1a.Page60GeboorteBean2> {

  public static final String STAP1 = "stap1";

  public Page60GeboorteFormStap1a() {
    setCaptionAndOrder();
  }

  @Override
  public Page60GeboorteBean2 getNewBean() {
    return new Page60GeboorteBean2();
  }

  public void setCaptionAndOrder() {
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Page60GeboorteBean2 implements Serializable {

    @Field(customTypeClass = GbaNativeSelect.class,
        caption = "Gemeenschappelijke nationaliteit ouders",
        required = true)
    @Select(containerDataSource = NLBooleanContainer.class,
        itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
    @Immediate
    private Boolean stap1 = null;
  }
}
