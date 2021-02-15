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

package nl.procura.gba.web.modules.zaken.common;

import static nl.procura.gba.web.services.bs.registration.SourceDocumentType.NOT_SET;

import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.PlaatsContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.bs.registration.SourceDocumentType;
import nl.procura.gba.web.services.bs.registration.ValidityDateType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class SourceDocumentBean {

  public static final String F_DATE_TYPE       = "dateType";
  public static final String F_DATE_TYPE_LABEL = "dateTypeLabel";
  public static final String F_DATE            = "date";
  public static final String F_TYPE            = "type";
  public static final String F_TYPE_LABEL      = "typeLabel";
  public static final String F_CERTIFICATE_NO  = "certificateNo";
  public static final String F_MUNICIPALITY    = "municipality";
  public static final String F_DESCRIPTION     = "description";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Gegevens geldig vanaf",
      width = "150px",
      required = true)
  @Select
  @Immediate
  private ValidityDateType dateType;

  @Field(customTypeClass = GbaDateField.class,
      caption = "Datum",
      required = true,
      width = "100px")
  private GbaDateFieldValue date;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Brondocument",
      width = "150px",
      required = true)
  @Select
  @Immediate
  private SourceDocumentType type = NOT_SET;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Nummer",
      width = "100px",
      required = true)
  @TextField(nullRepresentation = "", maxLength = 7)
  @InputPrompt(text = "Nummer")
  private String certificateNo;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Gemeente",
      width = "300px",
      required = true)
  @Immediate
  @Select(containerDataSource = PlaatsContainer.class)
  private FieldValue municipality = new FieldValue();

  @Field(customTypeClass = GbaTextField.class,
      width = "250px",
      required = true,
      caption = "Omschrijving")
  @TextField(nullRepresentation = "", maxLength = 40)
  @InputPrompt(text = "Omschrijving")
  private String description;

  @Field(type = Field.FieldType.LABEL,
      caption = "Gegevens geldig vanaf",
      width = "150px")
  private String dateTypeLabel = "Niet van toepassing";

  @Field(type = Field.FieldType.LABEL,
      caption = "Brondocument",
      width = "150px")
  private String typeLabel = "Niet van toepassing";
}
