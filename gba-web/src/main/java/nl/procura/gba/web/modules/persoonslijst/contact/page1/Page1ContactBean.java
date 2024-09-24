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

package nl.procura.gba.web.modules.persoonslijst.contact.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1ContactBean implements Serializable {

  public static final String TEL              = "tel";
  public static final String TEL_VER_IND      = "telVerInd";
  public static final String TEL_GELDIG_VANAF = "telGeldigVanaf";

  public static final String EMAIL              = "email";
  public static final String EMAIL_VER_IND      = "emailVerInd";
  public static final String EMAIL_GELDIG_VANAF = "emailGeldigVanaf";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Telefoonnummer")
  private String tel = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verificatie-indicatie")
  private String telVerInd = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geldig vanaf")
  private String telGeldigVanaf = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "E-mailadres")
  private String email = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verificatie-indicatie")
  private String emailVerInd = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geldig vanaf")
  private String emailGeldigVanaf = "";

}
