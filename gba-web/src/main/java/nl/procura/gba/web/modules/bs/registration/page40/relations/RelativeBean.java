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

import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
@Data
public final class RelativeBean {

  public static final String F_IDS    = "ids";
  public static final String F_NAME   = "name";
  public static final String F_GENDER = "gender";
  public static final String F_BIRTH  = "birth";

  @Field(type = Field.FieldType.LABEL, caption = "BSN / A-nummer")
  private String ids;

  @Field(type = Field.FieldType.LABEL, caption = "Naam")
  private String name = "";

  @Field(type = Field.FieldType.LABEL, caption = "Geslacht")
  private Geslacht gender;

  @Field(type = Field.FieldType.LABEL, caption = "Geboren")
  private String birth;
}
