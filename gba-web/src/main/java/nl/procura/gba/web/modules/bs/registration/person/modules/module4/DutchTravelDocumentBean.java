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

package nl.procura.gba.web.modules.bs.registration.person.modules.module4;

import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.containers.DutchTravelDocumentContainer;
import nl.procura.gba.web.components.containers.TabelContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.UpperCaseField;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.validator.DatumInVerledenValidator;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class DutchTravelDocumentBean {

  public static final String F_TYPE                   = "type";
  public static final String F_NUMBER                 = "number";
  public static final String F_ISSUE_DATE             = "issueDate";
  public static final String F_END_DATE               = "endDate";
  public static final String F_AUTHORITY              = "authority";
  public static final String F_AUTHORITY_MUNICIPALITY = "authorityMunicipality";
  public static final String F_AUTHORITY_COUNTRY      = "authorityCountry";
  public static final String F_DOSSIER_MUNICIPALITY   = "dossierMunicipality";
  public static final String F_DOSSIER_START_DATE     = "dossierStartDate";
  public static final String F_DOSSIER_DESCRIPTION    = "dossierDescription";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Soort",
      width = "420px",
      required = true)
  @Select(containerDataSource = DutchTravelDocumentContainer.class)
  private FieldValue type;

  @Field(customTypeClass = UpperCaseField.class,
      caption = "Nummer",
      width = "100px",
      required = true)
  @TextField(maxLength = 9)
  private String number;

  @Field(customTypeClass = ProDateField.class,
      width = "100px",
      required = true,
      caption = "Datum uitgifte",
      validators = DatumInVerledenValidator.class)
  private Date issueDate;

  @Field(customTypeClass = ProDateField.class,
      width = "100px",
      required = true,
      caption = "Datum einde")
  private Date endDate;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Autoriteit",
      width = "420px",
      required = true)
  @Select(itemCaptionPropertyId = TabelContainer.OMSCHRIJVING)
  private FieldValue authority;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Gemeente",
      visible = false,
      width = "420px",
      required = true)
  @Select
  private FieldValue authorityMunicipality;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Land",
      width = "420px",
      visible = false,
      required = true)
  @Select
  private FieldValue authorityCountry;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Gemeente",
      width = "400px",
      readOnly = true)
  @TextField(nullRepresentation = "Onbekend (standaardwaarde)")
  private String dossierMunicipality;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Datum",
      width = "400px",
      readOnly = true)
  @TextField(nullRepresentation = "Onbekend (standaardwaarde)")
  private String dossierStartDate;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Beschrijving",
      required = true,
      width = "420px")
  @TextField(maxLength = 40, nullRepresentation = ". (standaardwaarde)")
  private String dossierDescription;

}
