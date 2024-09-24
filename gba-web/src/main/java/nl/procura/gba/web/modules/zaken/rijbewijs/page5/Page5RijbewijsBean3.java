/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.rijbewijs.page5;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.InputPrompt;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.component.container.NLBooleanContainer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page5RijbewijsBean3 implements Serializable {

  public static final String IND_BEZORGEN = "indBezorgen";
  public static final String OPM_BEZORGEN = "opmBezorgen";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Thuisbezorgen?",
      required = true)
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE,
      nullSelectionAllowed = false)
  @Immediate()
  private Boolean indBezorgen;

  @Field(type = FieldType.TEXT_AREA,
      caption = "Opmerkingen",
      width = "400px")
  @TextArea(rows = 3)
  @InputPrompt(text = "Opmerkingen voor de bezorger")
  private String opmBezorgen = "";
}
