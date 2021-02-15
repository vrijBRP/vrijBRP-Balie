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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.search;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.bcgba.v14.misc.BcGbaVraagbericht;
import nl.procura.gba.web.components.containers.*;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class PresentievraagZoekBean implements Serializable {

  public static final String VRAAG                          = "vraag";
  public static final String VOORNAMEN                      = "voornamen";
  public static final String VOORVOEGSEL                    = "voorvoegsel";
  public static final String GESLACHTSNAAM                  = "geslachtsnaam";
  public static final String GESLACHT                       = "geslacht";
  public static final String GEBOORTEDATUM                  = "geboortedatum";
  public static final String GEBOORTEPLAATS                 = "geboorteplaats";
  public static final String GEBOORTELAND                   = "geboorteland";
  public static final String NATIONALITEIT                  = "nationaliteit";
  public static final String BUITENLAND_NR                  = "buitenlandnr";
  public static final String GEMEENTE                       = "gemeente";
  public static final String DATUM_AANVANG_ADRES_BUITENLAND = "datumAanvangAdresBuitenland";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Vraag")
  @Select(containerDataSource = PresentievraagContainer.class,
      nullSelectionAllowed = false)
  @Immediate
  private BcGbaVraagbericht vraag = BcGbaVraagbericht.VRAAG1;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Voornamen",
      width = "200px")
  @TextField(maxLength = 200,
      nullRepresentation = "")
  private String voornamen = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Voorvoegsel",
      width = "200px")
  @Select(containerDataSource = VoorvoegselContainer.class)
  @FilteringMode(mode = FilteringMode.Mode.FILTERINGMODE_STARTSWITH)
  private String voorvoegsel;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geslachtsnaam",
      width = "200px")
  @TextField(maxLength = 200,
      nullRepresentation = "")
  private String geslachtsnaam = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Geslachtsaanduiding")
  @Select(containerDataSource = GeslachtContainer.class,
      itemCaptionPropertyId = GeslachtContainer.NORMAAL)
  private Geslacht geslacht = null;

  @Field(customTypeClass = GbaDateField.class,
      caption = "Geboortedatum",
      width = "85px")
  private GbaDateFieldValue geboortedatum = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geboorteplaats",
      width = "250px")
  @TextField(maxLength = 40,
      nullRepresentation = "")
  private String geboorteplaats = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Geboorteland",
      width = "250px")
  @Select(containerDataSource = LandContainer.class)
  private FieldValue geboorteland = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "EU-document nationaliteit",
      width = "250px")
  @Select(containerDataSource = EuDocumentNatioContainer.class)
  private FieldValue nationaliteit = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Buitenlands persoonsnummer",
      width = "200px")
  @TextField(nullRepresentation = "")
  private String buitenlandnr = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Gemeente van inschrijving",
      width = "250px")
  @Select(containerDataSource = PlaatsContainer.class)
  private FieldValue gemeente = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum aanvang adres buitenland",
      width = "85px")
  private DateFieldValue datumAanvangAdresBuitenland = null;
}
