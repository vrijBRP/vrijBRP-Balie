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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.AanduidingContainer;
import nl.procura.gba.web.components.containers.AdelijkeTitelPredikaatContainer;
import nl.procura.gba.web.components.containers.GeslachtContainer;
import nl.procura.gba.web.components.containers.VoorvoegselContainer;
import nl.procura.gba.web.components.containers.actueel.GemdeelActueelContainer;
import nl.procura.gba.web.components.containers.actueel.LocatieActueelContainer;
import nl.procura.gba.web.components.containers.actueel.PlaatsActueelContainer;
import nl.procura.gba.web.components.containers.actueel.StraatActueelContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FilteringMode.Mode;
import nl.procura.vaadin.component.field.AnrField;
import nl.procura.vaadin.component.field.BsnField;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class ZoekBean implements Serializable {

  public static final String BSN              = "bsn";
  public static final String ANR              = "anr";
  public static final String GEBOORTEDATUM    = "geboortedatum";
  public static final String GESLACHT         = "geslacht";
  public static final String GESLACHTSNAAM    = "geslachtsnaam";
  public static final String VOORVOEGSEL      = "voorvoegsel";
  public static final String VOORNAAM         = "voornaam";
  public static final String TITEL            = "titel";
  public static final String STRAAT           = "straat";
  public static final String STRAATLAND       = "straatLand";
  public static final String HNR              = "hnr";
  public static final String HNRL             = "hnrl";
  public static final String HNRT             = "hnrt";
  public static final String HNRA             = "hnra";
  public static final String LOCATIE          = "locatie";
  public static final String POSTCODE         = "postcode";
  public static final String GEMEENTE         = "gemeente";
  public static final String GEMEENTEDEEL     = "gemeentedeel";
  public static final String ADRESIND         = "adresind";
  public static final String REISDOC          = "reisdoc";
  public static final String RIJBEWIJS        = "rijbewijs";
  public static final String VREEMDELINGENDOC = "vreemdelingendoc";

  @Field(customTypeClass = BsnField.class,
      caption = "Burgerservicenummer")
  @Immediate
  private BsnFieldValue bsn = null;

  @Field(customTypeClass = AnrField.class,
      caption = "A-nummer")
  private AnrFieldValue anr = null;

  @Field(customTypeClass = GbaDateField.class,
      caption = "Geboortedatum")
  private GbaDateFieldValue geboortedatum = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Geslacht")
  @Select(containerDataSource = GeslachtContainer.class,
      itemCaptionPropertyId = GeslachtContainer.NORMAAL)
  private Geslacht geslacht = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam")
  private String geslachtsnaam = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Voorvoegsel")
  @Select(containerDataSource = VoorvoegselContainer.class)
  @FilteringMode(mode = Mode.FILTERINGMODE_STARTSWITH)
  private FieldValue voorvoegsel = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Titel/predikaat")
  @Select(containerDataSource = AdelijkeTitelPredikaatContainer.class)
  private String titel = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Voornaam")
  private String voornaam = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Straat",
      width = "245px")
  @Select(containerDataSource = StraatActueelContainer.class)
  private FieldValue straat = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Straat")
  private String straatLand = "";

  @Field(customTypeClass = NumberField.class,
      caption = "Huisnummer",
      description = "Huisnummer",
      width = "50px")
  @InputPrompt(text = "")
  private String hnr = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Huisletter",
      description = "Huisletter",
      width = "50px")
  @InputPrompt(text = "")
  @TextField(maxLength = 1)
  private String hnrl = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Toevoeging",
      description = "Toevoeging",
      width = "50px")
  @InputPrompt(text = "")
  private String hnrt = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      description = "Aanduiding",
      width = "80px")
  @TextField(nullRepresentation = "")
  @Select(containerDataSource = AanduidingContainer.class)
  private FieldValue hnra = new FieldValue();

  @Field(customTypeClass = PostalcodeField.class,
      caption = "Postcode",
      width = "150px")
  private String postcode = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Lokatie",
      width = "150px")
  @Select(containerDataSource = LocatieActueelContainer.class)
  private FieldValue locatie = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Gemeente",
      width = "150px")
  @Select(containerDataSource = PlaatsActueelContainer.class)
  private FieldValue gemeente = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Gemeentedeel",
      width = "245px")
  @Select(containerDataSource = GemdeelActueelContainer.class)
  private FieldValue gemeentedeel = null;

  @Field(type = FieldType.CHECK_BOX,
      caption = "Adresidentificatie",
      description = "Zoek alle personen die op hetzelfde adres wonen")
  private boolean adresind = false;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Reisdocument")
  private String reisdoc = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Rijbewijs")
  private String rijbewijs = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Vreemdelingendoc.")
  private String vreemdelingendoc = "";

  public ZoekBean() {
  }
}
