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

package nl.procura.gba.web.modules.bs.geboorte.page35.buitenproweb;

import static nl.procura.vaadin.annotation.field.FilteringMode.Mode.FILTERINGMODE_STARTSWITH;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.containers.AdelijkeTitelContainer;
import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.containers.NaamskeuzeBuitenProwebTypeContainer;
import nl.procura.gba.web.components.containers.RechtbankContainer;
import nl.procura.gba.web.components.containers.VoorvoegselContainer;
import nl.procura.gba.web.components.containers.actueel.LandActueelContainer;
import nl.procura.gba.web.components.containers.actueel.PlaatsActueelContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.modules.bs.geboorte.page35.ToestemminggeverTypeContainer;
import nl.procura.gba.web.services.bs.algemeen.enums.RechtbankLocatie;
import nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType;
import nl.procura.gba.web.services.bs.erkenning.NaamskeuzeVanToepassingType;
import nl.procura.gba.web.services.bs.geboorte.erkenningbuitenproweb.ToestemminggeverType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FilteringMode;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page35GeboorteBeanBuitenErk implements Serializable {

  public static final String AKTENR                = "aktenr";
  public static final String DATUM                 = "datum";
  public static final String TOESTEMMINGGEVER_TYPE = "toestemminggeverType";
  public static final String VERKLARING_GEZAG      = "verklaringGezag";
  public static final String RECHTBANK             = "rechtbank";
  public static final String NAAMSKEUZE_TYPE       = "naamskeuzeType";
  public static final String NAAMS_PERSOON_TYPE    = "naamsPersoonType";
  public static final String AFSTAMMINGSRECHT      = "afstammingsrecht";
  public static final String GEMEENTE              = "gemeente";
  public static final String LAND                  = "land";
  public static final String PLAATS                = "plaats";

  public static final String TITEL         = "titel";
  public static final String VOORV         = "voorv";
  public static final String GESLACHTSNAAM = "geslachtsnaam";
  public static final String DUBBELE_NAAM  = "dubbeleNaam";

  @Field(customTypeClass = NumberField.class,
      caption = "Aktenummer",
      required = true,
      width = "40px")
  @TextField(maxLength = 4)
  private String aktenr = "";

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum",
      required = true,
      width = "90px")
  private DateFieldValue datum = new DateFieldValue();

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Toestemminggever",
      required = true,
      width = "200px")
  @Select(containerDataSource = ToestemminggeverTypeContainer.class,
      nullSelectionAllowed = false)
  @Immediate
  private ToestemminggeverType toestemminggeverType = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Verklaring moeder+erkenner m.b.t gezag bij moeder?")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean verklaringGezag = false;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Rechtbank",
      required = true,
      visible = false,
      width = "200px")
  @Select(containerDataSource = RechtbankContainer.class,
      nullSelectionAllowed = false)
  private RechtbankLocatie rechtbank = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Naamskeuze",
      width = "200px",
      required = true)
  @Select(containerDataSource = NaamskeuzeBuitenProwebTypeContainer.class,
      nullSelectionAllowed = false)
  @Immediate
  private NaamskeuzeVanToepassingType naamskeuzeType = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Naam van",
      required = true,
      width = "200px")
  @Select(containerDataSource = NaamskeuzePersoonErkContainer.class,
      nullSelectionAllowed = false)
  @Immediate
  private NaamsPersoonType naamsPersoonType = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam",
      required = true,
      width = "200px")
  private String geslachtsnaam = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Voorvoegsel",
      width = "200px")
  @Select(containerDataSource = VoorvoegselContainer.class)
  @Immediate
  @TextField(nullRepresentation = "")
  @FilteringMode(mode = FILTERINGMODE_STARTSWITH)
  private FieldValue voorv = new FieldValue();

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Titel",
      width = "200px")
  @Select(containerDataSource = AdelijkeTitelContainer.class)
  @Immediate
  @TextField(nullRepresentation = "")
  @FilteringMode(mode = FILTERINGMODE_STARTSWITH)
  private FieldValue titel = new FieldValue();

  @Field(customTypeClass = GbaTextField.class,
      width = "200px",
      caption = "2e gekozen naam")
  private String dubbeleNaam = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Toegepast recht van",
      required = true,
      width = "200px")
  @Select(containerDataSource = LandContainer.class)
  private FieldValue afstammingsrecht = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Gemeente",
      width = "200px",
      required = true)
  @Select(containerDataSource = PlaatsActueelContainer.class)
  private FieldValue gemeente = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Plaats",
      width = "200px",
      required = true)
  @TextField(maxLength = 250)
  private String plaats = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Land",
      width = "200px",
      required = true)
  @Select(containerDataSource = LandActueelContainer.class)
  private FieldValue land = Landelijk.getNederland();

  public void setNaamsPersoonType(NaamsPersoonType naamsPersoonType) {
    if (NaamsPersoonType.ONBEKEND.equals(naamsPersoonType)) {
      this.naamsPersoonType = null;
    } else {
      this.naamsPersoonType = naamsPersoonType;
    }
  }
}
