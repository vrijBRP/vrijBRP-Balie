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

package nl.procura.gba.web.components.layouts.form.document.sign;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.function.Supplier;

import com.vaadin.data.validator.AbstractValidator;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.document.sign.DocSignForm.SignPerson;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProTextField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class DocSignBean implements Serializable {

  public static final String PERSON       = "person";
  public static final String PERSON_EMAIL = "email";
  public static final String DOCUMENT     = "document";
  public static final String LANGUAGE     = "language";

  @Field(customTypeClass = ProTextField.class,
      caption = "Document",
      width = "300px",
      readOnly = true)
  private String document = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Ondertekenaar",
      width = "300px",
      required = true)
  @Select(nullSelectionAllowed = false)
  private SignPerson person;

  @Field(customTypeClass = GbaNativeSelect.class,
      required = true,
      caption = "Kopie via e-mail versturen")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean email;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Voorkeurstaal",
      width = "300px",
      required = true)
  @Select(containerDataSource = DocSignLanguageContainer.class, nullSelectionAllowed = false)
  private DocSignInterfaceLanguage language;

  public DocSignBean() {
  }

  public static class PersonValidator extends AbstractValidator {

    private final Supplier<SignPerson> personSupplier;

    public PersonValidator(Supplier<SignPerson> personSupplier) {
      super("Ondertekenaar heeft geen e-mailadres");
      this.personSupplier = personSupplier;
    }

    @Override
    public boolean isValid(Object value) {
      SignPerson person = personSupplier.get();
      return Boolean.FALSE.equals(value) || isNotBlank(person.getEmail());
    }
  }
}
