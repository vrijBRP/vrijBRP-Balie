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

package nl.procura.gba.web.components.layouts.form.document;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.PosNumberField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class PrintDocumentBean implements Serializable {

  public static final String SOORT        = "soort";
  public static final String SOORT_LEEG   = "soortLeeg";
  public static final String VERVOLG_BLAD = "vervolgblad";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Soort",
      required = true,
      requiredError = "{}: verplicht veld")
  @Select(nullSelectionAllowed = false)
  private FieldValue soort = null;

  @Field(type = Field.FieldType.LABEL,
      caption = "Soort",
      visible = false)
  private String soortLeeg = "Geen documenten";

  @Field(customTypeClass = PosNumberField.class,
      caption = "Vervolgblad",
      width = "30px",
      visible = false,
      required = true)
  @TextField(maxLength = 2)
  private String vervolgblad = "1";
}
