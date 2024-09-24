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

package nl.procura.gba.web.modules.zaken.reisdocument.page26;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ProTextArea;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page26ReisdocumentBean1 implements Serializable {

  static final String BEZORGING_GEWENST = "bezorgingGewenst";
  static final String OPMERKINGEN       = "opmerkingen";
  static final String ADRES             = "adres";
  static final String BUNDELING         = "bundeling";

  @Field(customTypeClass = GbaNativeSelect.class,
      required = true,
      caption = "Thuisbezorging gewenst")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean bezorgingGewenst;

  @TextArea(rows = 3, maxLength = 1900)
  @Field(customTypeClass = ProTextArea.class,
      required = true,
      caption = "Opmerkingen",
      width = "400px")
  private String opmerkingen = "";

  @Field(type = FieldType.LABEL,
      caption = "Adres",
      width = "400px")
  private String adres = "";

  @Field(customTypeClass = ProNativeSelect.class,
      required = true,
      width = "400px",
      caption = "Bezorging bundelen")
  private BezorgingBundeling bundeling;
}
