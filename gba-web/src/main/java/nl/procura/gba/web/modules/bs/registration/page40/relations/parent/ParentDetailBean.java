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

package nl.procura.gba.web.modules.bs.registration.page40.relations.parent;

import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.RelationshipDateTypeContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.bs.registration.RelationshipDateType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class ParentDetailBean {

  public static final String F_START_OF_RELATION       = "startOfRelation";
  public static final String F_START_OF_RELATION_LABEL = "startOfRelationLabel";
  public static final String F_DATE_OF_RELATION_START  = "dateOfRelationStart";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Datum ingang relatie",
      width = "150px",
      required = true)
  @Select(containerDataSource = RelationshipDateTypeContainer.class)
  @Immediate
  private RelationshipDateType startOfRelation;

  @Field(customTypeClass = GbaDateField.class,
      required = true,
      width = "100px")
  private GbaDateFieldValue dateOfRelationStart;

  @Field(type = Field.FieldType.LABEL,
      caption = "Datum ingang relatie",
      width = "150px")
  private String startOfRelationLabel = "Niet van toepassing";
}
