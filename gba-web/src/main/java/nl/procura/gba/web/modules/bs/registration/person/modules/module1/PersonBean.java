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

package nl.procura.gba.web.modules.bs.registration.person.modules.module1;

import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.AdelijkeTitelPredikaatContainer;
import nl.procura.gba.web.components.containers.GeslachtContainer;
import nl.procura.gba.web.components.containers.PlaatsContainer;
import nl.procura.gba.web.components.containers.VoorvoegselContainer;
import nl.procura.gba.web.components.fields.CountryBox;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.AnrField;
import nl.procura.vaadin.component.field.ProComboBox;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class PersonBean {

  public static final String F_BSN         = "bsn";
  public static final String F_ANR         = "anr";
  public static final String F_PREFIX      = "prefix";
  public static final String F_FAMILY_NAME = "familyName";
  public static final String F_TITLE       = "title";
  public static final String F_FIRSTNAMES  = "firstNames";
  public static final String F_GENDER      = "gender";

  public static final String F_DATE_OF_BIRTH = "dateOfBirth";
  public static final String F_NL_MUN        = "municipality";
  public static final String F_FOREIGN_MUN   = "foreignMunicipality";
  public static final String F_COUNTRY       = "country";

  @Field(customTypeClass = GbaTextField.class,
      caption = "BSN",
      required = true,
      readOnly = true,
      width = "210px")
  private String bsn;

  @Field(customTypeClass = AnrField.class,
      caption = "A-nummer",
      required = true,
      readOnly = false,
      width = "210px")
  private AnrFieldValue anr;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Voorvoegsel",
      width = "210px")
  @Select(containerDataSource = VoorvoegselContainer.class)
  private FieldValue prefix;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam",
      width = "210px",
      required = true)
  private String familyName = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Titel",
      width = "210px")
  @Select(containerDataSource = AdelijkeTitelPredikaatContainer.class)
  private FieldValue title;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Voornamen",
      width = "210px")
  private String firstNames = "";

  @Field(customTypeClass = ProComboBox.class,
      caption = "Geslacht",
      width = "210px",
      required = true)
  @Select(containerDataSource = GeslachtContainer.class)
  private Geslacht gender;

  // dateOfBirth is not a real date. I can support dates like 00-00-2001 or 00-01-2001
  @Field(customTypeClass = GbaDateField.class,
      caption = "Geboortedatum",
      width = "80px",
      required = true)
  private GbaDateFieldValue dateOfBirth = new GbaDateFieldValue("");

  @Field(customTypeClass = CountryBox.class,
      caption = "Geboorteland",
      width = "410px",
      required = true)
  private FieldValue country;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Geboorteplaats",
      width = "410px",
      required = true)
  @Select(containerDataSource = PlaatsContainer.class)
  private FieldValue municipality;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geboorteplaats",
      width = "410px",
      required = true)
  @TextField(nullRepresentation = "")
  private String foreignMunicipality = "";

}
