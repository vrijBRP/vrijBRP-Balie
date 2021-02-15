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

package nl.procura.gba.web.modules.zaken.personmutations.overview;

import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class PersonMutationOverviewBean {

  public static final String EXPLANATION = "explanation";
  public static final String CAT         = "category";
  public static final String OPERATION   = "operation";
  public static final String SET         = "set";
  public static final String RECORD      = "record";

  @Field(type = Field.FieldType.LABEL,
      caption = "Redenen",
      width = "350px")
  private Object explanation;

  @Field(type = Field.FieldType.LABEL,
      caption = "Categorie",
      width = "350px")
  private Object category;

  @Field(type = Field.FieldType.LABEL,
      caption = "Actie",
      width = "350px")
  private Object operation;

  @Field(type = Field.FieldType.LABEL,
      caption = "Gegevensset",
      width = "350px")
  private Object set;

  @Field(type = Field.FieldType.LABEL,
      caption = "Record",
      width = "350px")
  private Object record;
}
