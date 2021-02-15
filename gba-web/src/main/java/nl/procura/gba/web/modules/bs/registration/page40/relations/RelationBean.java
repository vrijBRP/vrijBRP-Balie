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

package nl.procura.gba.web.modules.bs.registration.page40.relations;

import java.lang.annotation.ElementType;

import nl.procura.gba.web.modules.bs.registration.page40.relations.matching.RelationsMatchInfo;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class RelationBean {

  public static final String F_PERSON            = "person";
  public static final String F_RELATIONSHIP_TYPE = "relationshipType";
  public static final String F_RELATED_PERSON    = "relatedPerson";
  public static final String F_NO_RELATED_PERSON = "noRelatedPerson";
  public static final String F_MATCHING_RELATIVE = "matchingRelative";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Te vestigen persoon",
      width = "570px",
      required = true)
  @Immediate
  private FieldValue person;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Soort relatie",
      width = "570px",
      required = true)
  @Immediate
  private FieldValue relationshipType;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Gerelateerde persoon",
      width = "570px",
      required = true)
  @Immediate
  private FieldValue relatedPerson;

  @Field(type = Field.FieldType.LABEL,
      caption = "Gerelateerde persoon",
      width = "500px",
      visible = false)
  private String noRelatedPerson = "Niet van toepassing";

  @Field(customTypeClass = ProNativeSelect.class,
      width = "500px",
      required = true)
  @Immediate
  private RelationsMatchInfo matchingRelative;
}
