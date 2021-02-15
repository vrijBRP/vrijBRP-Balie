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
import nl.procura.gba.web.components.containers.VoorvoegselContainer;
import nl.procura.gba.web.components.containers.actueel.LandActueelContainer;
import nl.procura.gba.web.components.containers.actueel.PlaatsActueelContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.ProTextArea;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page35GeboorteBeanNk implements Serializable {

  public static final String AKTENR             = "aktenr";
  public static final String DATUM              = "datum";
  public static final String NAAMS_PERSOON_TYPE = "naamsPersoonType";
  public static final String GESLACHTSNAAM      = "geslachtsnaam";
  public static final String VOORV              = "voorv";
  public static final String TITEL              = "titel";
  public static final String GEMEENTE           = "gemeente";
  public static final String LAND               = "land";
  public static final String PLAATS             = "plaats";
  public static final String BIJZONDERHEDEN     = "bijzonderheden";

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
      caption = "Naam gekregen van",
      required = true,
      width = "200px")
  @Select(containerDataSource = NaamskeuzePersoonNkContainer.class,
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

  @Field(customTypeClass = ProTextArea.class,
      caption = "Bijzonderheden",
      width = "200px")
  @TextArea(nullRepresentation = "",
      maxLength = 400,
      rows = 3)
  private String bijzonderheden = "";

  public String getAktenr() {
    return aktenr;
  }

  public void setAktenr(String aktenr) {
    this.aktenr = aktenr;
  }

  public DateFieldValue getDatum() {
    return datum;
  }

  public void setDatum(DateFieldValue datum) {
    this.datum = datum;
  }

  public FieldValue getGemeente() {
    return gemeente;
  }

  public void setGemeente(FieldValue gemeente) {
    this.gemeente = gemeente;
  }

  public FieldValue getLand() {
    return land;
  }

  public void setLand(FieldValue land) {
    this.land = land;
  }

  public NaamsPersoonType getNaamsPersoonType() {
    return naamsPersoonType;
  }

  public void setNaamsPersoonType(NaamsPersoonType naamsPersoonType) {
    if (NaamsPersoonType.ONBEKEND.equals(naamsPersoonType)) {
      this.naamsPersoonType = null;
    } else {
      this.naamsPersoonType = naamsPersoonType;
    }
  }

  public String getPlaats() {
    return plaats;
  }

  public void setPlaats(String plaats) {
    this.plaats = plaats;
  }

  public String getBijzonderheden() {
    return bijzonderheden;
  }

  public void setBijzonderheden(String bijzonderheden) {
    this.bijzonderheden = bijzonderheden;
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    this.geslachtsnaam = geslachtsnaam;
  }

  public FieldValue getVoorv() {
    return voorv;
  }

  public void setVoorv(FieldValue voorv) {
    this.voorv = voorv;
  }

  public FieldValue getTitel() {
    return titel;
  }

  public void setTitel(FieldValue titel) {
    this.titel = titel;
  }
}
