/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.bs.lv.page40;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.containers.GeslachtContainer;
import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.services.bs.algemeen.enums.LvDocumentType;
import nl.procura.gba.web.services.bs.algemeen.enums.RechtbankLocatie;
import nl.procura.gba.web.services.bs.lv.afstamming.KeuzeVaststellingGeslachtsnaam;
import nl.procura.gba.web.services.bs.lv.afstamming.LvDocumentDoorType;
import nl.procura.gba.web.services.bs.lv.afstamming.LvGezagType;
import nl.procura.gba.web.services.bs.lv.afstamming.LvOuderType;
import nl.procura.gba.web.services.bs.lv.afstamming.LvSoortVerbintenisType;
import nl.procura.gba.web.services.bs.lv.afstamming.LvToegepastRechtType;
import nl.procura.gba.web.services.bs.lv.afstamming.LvToestemmingType;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page40LvBean1 implements Serializable {

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Uitspraak door",
      required = true,
      width = "250px")
  @Select(containerDataSource = LvRechtbankContainer.class)
  private RechtbankLocatie uitspraak = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Uitspraak anders, namelijk",
      required = true,
      width = "250px")
  @TextField(nullRepresentation = "")
  private String uitspraakAnders = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum uitspraak",
      width = "97px")
  private Date datumUitspraak = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum kracht van gewijsde",
      width = "97px")
  private Date datumGewijsde = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Soort verbintenis",
      width = "250px")
  @Select(containerDataSource = LvSoortVerbintenisContainer.class)
  private LvSoortVerbintenisType soortVerbintenis = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      required = true,
      caption = "Document")
  @Select(containerDataSource = LvDocumentContainer.class)
  private LvDocumentType document = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Document anders, namelijk",
      required = true,
      width = "250px")
  @TextField(nullRepresentation = "")
  private String documentAnders = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Nummer",
      width = "250px")
  private String nummer = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum",
      width = "97px")
  private Date datum = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Plaats",
      width = "250px")
  private String plaats = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Door",
      width = "250px")
  @Select(containerDataSource = LvDocumentDoorContainer.class)
  private LvDocumentDoorType door = null;

  @Field(customTypeClass = ProNativeSelect.class,
      required = true,
      caption = "Tweede document")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean tweedeDocument = false;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Omschrijving document")
  @Select(containerDataSource = LvDocumentContainer.class)
  private LvDocumentType tweedeDocumentOms = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Omschrijving document anders, namelijk",
      required = true,
      width = "250px")
  @TextField(nullRepresentation = "")
  private String tweedeDocumentOmsAnders = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum document",
      width = "97px")
  private Date tweedeDocumentDatum = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Plaats document",
      width = "250px")
  private String tweedeDocumentPlaats = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Betreft deze ouder",
      required = true,
      width = "250px")
  @Select(containerDataSource = LvOuderTypeContainer.class)
  private LvOuderType betreftOuder = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Familierechtelijke betrekking met deze ouder blijft in stand",
      required = true,
      width = "250px")
  @Select(containerDataSource = LvOuderTypeContainer.class)
  private LvOuderType famRecht = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Erkenning gedaan door",
      required = true,
      width = "250px")
  @Select(containerDataSource = LvOuderZonderNvtTypeContainer.class)
  private LvOuderType erkenningDoor = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Ouderschap ontkend van",
      required = true,
      width = "250px")
  @Select(containerDataSource = LvOuderZonderNvtTypeContainer.class)
  private LvOuderType ouderschapOntkend = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Betreft deze ouder (Persoon)",
      required = true)
  private KeuzeOuder betreftOuderPersoon = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Ouderschap vastgesteld van",
      required = true,
      width = "250px")
  @Select(containerDataSource = LvOuderZonderNvtTypeContainer.class)
  private LvOuderType ouderschapVastgesteld = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam ouder vastgesteld als",
      width = "250px")
  private String geslOuderVastgesteld = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam ouder gewijzigd in",
      width = "250px")
  private String geslOuderGewijzigd = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Voornamen ouder vastgesteld als",
      width = "250px")
  private String voornamenOuderVastgesteldAls = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Keuze of vaststelling geslachtnaam kind",
      width = "250px")
  @Select(containerDataSource = KeuzeVaststellingGeslContainer.class)
  private KeuzeVaststellingGeslachtsnaam keuzeGeslachtsnaam = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam kind is",
      required = true,
      width = "250px")
  private String geslachtsnaamIs = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam vastgesteld als",
      width = "250px")
  private String geslachtsnaamVastgesteldAls = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam gewijzigd in",
      width = "250px")
  private String geslachtsnaamGewijzigdIn = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam kind gewijzigd in",
      width = "250px")
  private String geslachtsnaamKindGewijzigdIn = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Voornamen vastgesteld als",
      width = "250px")
  private String voornamenVastgesteldAls = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Voornamen gewijzigd",
      width = "60px")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean voornamenGewijzigd = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Voornamen gewijzigd in",
      required = true,
      width = "185px")
  private String voornamenGewijzigdIn = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Geslacht gewijzigd in",
      required = true,
      width = "250px")
  @Select(containerDataSource = GeslachtContainer.class)
  private Geslacht geslachtsaand = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Toestemming door",
      required = true,
      width = "250px")
  @Select(containerDataSource = LvToestemmingTypeContainer.class)
  private LvToestemmingType toestemmingDoor = null;

  @Field(customTypeClass = GbaTextField.class,
      required = true,
      width = "250px")
  private String toestemmingAnders = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Toegepast recht van",
      required = true,
      width = "250px")
  @Select(containerDataSource = LvToegepastRechtTypeContainer.class)
  private LvToegepastRechtType toegepastRecht = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "",
      required = true,
      width = "250px")
  @Select(containerDataSource = LandContainer.class)
  private FieldValue toegepastRechtAnders = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Gezag",
      required = true,
      width = "250px")
  @Select(containerDataSource = LvGezagTypeContainer.class)
  private LvGezagType gezag = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Gekozen recht",
      width = "250px")
  private String gekozenRecht = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Dag van wijziging",
      width = "97px")
  private Date dagVanWijziging = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Adoptiefouders",
      readOnly = true)
  private String adoptiefouders = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Ouders",
      readOnly = true)
  private String ouders = null;
}
