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

package nl.procura.gba.web.modules.bs.geboorte.page81;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.containers.AdelijkeTitelPredikaatContainer;
import nl.procura.gba.web.components.containers.GeslachtContainer;
import nl.procura.gba.web.components.containers.VoorvoegselContainer;
import nl.procura.gba.web.components.fields.*;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FilteringMode.Mode;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProTextArea;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.field.fieldvalues.TimeFieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page81GeboorteBean1 implements Serializable {

  public static final String BSN            = "bsn";
  public static final String ANR            = "anr";
  public static final String NAAM           = "naam";
  public static final String VOORV          = "voorv";
  public static final String VOORN          = "voorn";
  public static final String TITEL          = "titel";
  public static final String GESLACHT       = "geslacht";
  public static final String NAAMSKEUZE     = "naamskeuze";
  public static final String GEBOORTEDATUM  = "geboortedatum";
  public static final String GEBOORTETIJD   = "geboortetijd";
  public static final String GEBOORTEPLAATS = "geboorteplaats";
  public static final String GEBOORTELAND   = "geboorteland";
  public static final String WOONPLAATS     = "woonplaats";
  public static final String WOONLAND       = "woonland";
  public static final String VBT            = "vbt";
  public static final String NAT            = "nat";
  public static final String VERSTREK       = "verstrek";
  public static final String TOELICHTING    = "toelichting";

  @Field(type = FieldType.LABEL,
      caption = "BSN")
  @TextField(nullRepresentation = "")
  private String bsn = "";

  @Field(type = FieldType.LABEL,
      caption = "A-nummer")
  private String anr = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam",
      required = true,
      width = "150px",
      readOnly = true)
  @TextField(nullRepresentation = "")
  private String naam = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Voorvoegsel",
      width = "150px",
      readOnly = true)
  @Select(containerDataSource = VoorvoegselContainer.class)
  @FilteringMode(mode = Mode.FILTERINGMODE_STARTSWITH)
  private FieldValue voorv = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Voornaam",
      width = "200px")
  @TextField(nullRepresentation = "")
  private String voorn = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Naamgebruik",
      width = "200px")
  @TextField(nullRepresentation = "")
  private String ng = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Titel",
      width = "150px")
  @Select(containerDataSource = AdelijkeTitelPredikaatContainer.class)
  private FieldValue titel = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Geslacht",
      required = true,
      width = "150px")
  @Select(containerDataSource = GeslachtContainer.class,
      nullSelectionAllowed = false)
  private Geslacht geslacht = null;

  @Field(customTypeClass = GbaDateField.class,
      caption = "Geboortedatum",
      required = true,
      width = "80px")
  private GbaDateFieldValue geboortedatum = null;

  @Field(customTypeClass = TimeField.class,
      caption = "Geboortetijd",
      required = true,
      width = "45px")
  @TextField(maxLength = 5)
  @Immediate
  private TimeFieldValue geboortetijd = new TimeFieldValue();

  @Field(type = FieldType.LABEL,
      caption = "Geboorteplaats",
      width = "300px")
  private String geboorteplaats = "";

  @Field(type = FieldType.LABEL,
      caption = "Naamskeuze",
      width = "300px")
  private String naamskeuze = "";

  @Field(type = FieldType.LABEL,
      caption = "Geboorteland",
      width = "300px")
  private FieldValue geboorteland = Landelijk.getNederland();

  @Field(type = FieldType.LABEL,
      caption = "Woonplaats",
      width = "300px")
  private String woonplaats = "";

  @Field(type = FieldType.LABEL,
      caption = "Woonland",
      width = "300px")
  private FieldValue woonland = Landelijk.getNederland();

  @Field(type = FieldType.LABEL,
      caption = "Verblijfstitelcode")
  private String vbt = null;

  @Field(type = FieldType.LABEL,
      caption = "Nationaliteiten",
      width = "690px")
  private String nat = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Verstrekkingsbeperking",
      readOnly = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean verstrek = false;

  @Field(customTypeClass = ProTextArea.class,
      caption = "Toelichting BRP",
      width = "690px")
  @TextArea(nullRepresentation = "",
      maxLength = 400,
      rows = 3)
  private String toelichting = "";

  public String getAnr() {
    return anr;
  }

  public void setAnr(String anr) {
    this.anr = anr;
  }

  public String getBsn() {
    return bsn;
  }

  public void setBsn(String bsn) {
    this.bsn = bsn;
  }

  public GbaDateFieldValue getGeboortedatum() {
    return geboortedatum;
  }

  public void setGeboortedatum(GbaDateFieldValue geboortedatum) {
    this.geboortedatum = geboortedatum;
  }

  public FieldValue getGeboorteland() {
    return geboorteland;
  }

  public void setGeboorteland(FieldValue geboorteland) {
    this.geboorteland = geboorteland;
  }

  public String getGeboorteplaats() {
    return geboorteplaats;
  }

  public void setGeboorteplaats(String geboorteplaats) {
    this.geboorteplaats = geboorteplaats;
  }

  public TimeFieldValue getGeboortetijd() {
    return geboortetijd;
  }

  public void setGeboortetijd(TimeFieldValue geboortetijd) {
    this.geboortetijd = geboortetijd;
  }

  public Geslacht getGeslacht() {
    return geslacht;
  }

  public void setGeslacht(Geslacht geslacht) {
    this.geslacht = geslacht;
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  public String getNaamskeuze() {
    return naamskeuze;
  }

  public void setNaamskeuze(String naamskeuze) {
    this.naamskeuze = naamskeuze;
  }

  public String getNat() {
    return nat;
  }

  public void setNat(String nat) {
    this.nat = nat;
  }

  public String getNg() {
    return ng;
  }

  public void setNg(String ng) {
    this.ng = ng;
  }

  public FieldValue getTitel() {
    return titel;
  }

  public void setTitel(FieldValue titel) {
    this.titel = titel;
  }

  public String getToelichting() {
    return toelichting;
  }

  public void setToelichting(String toelichting) {
    this.toelichting = toelichting;
  }

  public String getVbt() {
    return vbt;
  }

  public void setVbt(String vbt) {
    this.vbt = vbt;
  }

  public String getVoorn() {
    return voorn;
  }

  public void setVoorn(String voorn) {
    this.voorn = voorn;
  }

  public FieldValue getVoorv() {
    return voorv;
  }

  public void setVoorv(FieldValue voorv) {
    this.voorv = voorv;
  }

  public FieldValue getWoonland() {
    return woonland;
  }

  public void setWoonland(FieldValue woonland) {
    this.woonland = woonland;
  }

  public String getWoonplaats() {
    return woonplaats;
  }

  public void setWoonplaats(String woonplaats) {
    this.woonplaats = woonplaats;
  }

  public boolean isVerstrek() {
    return verstrek;
  }

  public void setVerstrek(boolean verstrek) {
    this.verstrek = verstrek;
  }
}
