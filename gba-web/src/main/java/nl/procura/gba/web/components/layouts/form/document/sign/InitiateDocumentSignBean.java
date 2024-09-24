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

package nl.procura.gba.web.components.layouts.form.document.sign;

import lombok.Data;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.ProComboBox;

import java.io.Serializable;
import java.lang.annotation.ElementType;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class InitiateDocumentSignBean implements Serializable {

  public static final String PERSON_NAME = "personName";
  public static final String DOCUMENT = "document";
  public static final String EMAIL = "email";
  public static final String LANGUAGE = "language";

  @Field(type = FieldType.TEXT_FIELD,
          caption = "Naam",
          width = "300px",
          readOnly = true)
  private String personName = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Document",
      width = "300px",
      readOnly = true)
  private String document = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "E-mail",
      width = "300px",
      required = true)
  @TextField(maxLength = 240)
  private String email = "";

  @Field(customTypeClass = ProComboBox.class,
      caption = "Voorkeurstaal ondertekenaar",
      width = "300px",
      required = true)
  @Select(containerDataSource = DocumentSignLanguageContainer.class, nullSelectionAllowed = false)
  private SignInterfaceLanguage language;

  public InitiateDocumentSignBean() {
  }
}
