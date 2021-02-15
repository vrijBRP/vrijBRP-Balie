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

package nl.procura.gba.web.modules.tabellen.overlijdenaangever.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.*;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FilteringMode.Mode;
import nl.procura.vaadin.component.field.BsnField;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.EmailField;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2OverlijdenAangeverBean implements Serializable {

  public static final String BSN               = "bsn";
  public static final String NAAM              = "naam";
  public static final String VOORV             = "voorv";
  public static final String VOORN             = "voorn";
  public static final String TITEL             = "titel";
  public static final String GESLACHT          = "geslacht";
  public static final String GEBOORTEDATUM     = "geboortedatum";
  public static final String GEBOORTEPLAATS_BL = "geboorteplaatsBuitenland";
  public static final String GEBOORTEPLAATS_NL = "geboorteplaatsNL";
  public static final String GEBOORTELAND      = "geboorteland";
  public static final String EMAIL             = "email";
  public static final String TELEFOON          = "telefoon";
  public static final String INGANG_GELD       = "ingangGeld";
  public static final String EINDE_GELD        = "eindeGeld";

  @Field(customTypeClass = BsnField.class,
      caption = "Burgerservicenummer",
      width = "150px")
  private BsnFieldValue bsn = new BsnFieldValue();

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam",
      width = "150px",
      required = true)
  @TextField(nullRepresentation = "")
  private String naam = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Voorvoegsel",
      width = "150px")
  @Select(containerDataSource = VoorvoegselContainer.class)
  @FilteringMode(mode = Mode.FILTERINGMODE_STARTSWITH)
  private FieldValue voorv = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Voornaam",
      width = "150px",
      required = true)
  @TextField(nullRepresentation = "")
  private String voorn = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Titel",
      width = "100px")
  @Select(containerDataSource = AdelijkeTitelPredikaatContainer.class)
  private FieldValue titel = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Geslacht",
      width = "100px",
      required = true)
  @Select(containerDataSource = GeslachtContainer.class)
  private Geslacht geslacht = null;

  @Field(customTypeClass = GbaDateField.class,
      caption = "Geboortedatum",
      width = "80px",
      required = true)
  private GbaDateFieldValue geboortedatum = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Geboorteland",
      width = "200px",
      required = true)
  @Select(containerDataSource = LandContainer.class)
  @Immediate
  private FieldValue geboorteland = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Geboorteplaats",
      width = "200px",
      required = true)
  @Select(containerDataSource = PlaatsContainer.class)
  private FieldValue geboorteplaatsNL = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geboorteplaats",
      width = "200px",
      required = true)
  @TextField(nullRepresentation = "")
  private String geboorteplaatsBuitenland = "";

  @Field(customTypeClass = EmailField.class,
      caption = "E-mail",
      width = "200px")
  private String email = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Telefoon",
      width = "200px")
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String telefoon = "";

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum ingang",
      width = "80px")
  private DateFieldValue ingangGeld = new DateFieldValue();

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum einde",
      width = "80px")
  private DateFieldValue eindeGeld = new DateFieldValue();
}
