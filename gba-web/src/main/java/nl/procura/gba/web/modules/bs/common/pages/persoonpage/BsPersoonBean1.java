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

package nl.procura.gba.web.modules.bs.common.pages.persoonpage;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType;
import nl.procura.gba.web.components.containers.*;
import nl.procura.gba.web.components.fields.*;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FilteringMode.Mode;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.ProTextArea;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class BsPersoonBean1 implements Serializable {

  public static final String TYPE                = "type";
  public static final String BSN                 = "bsn";
  public static final String NAAM                = "naam";
  public static final String AKTENAAM            = "aktenaam";
  public static final String VOORV               = "voorv";
  public static final String VOORN               = "voorn";
  public static final String TITEL               = "titel";
  public static final String GESLACHT            = "geslacht";
  public static final String GEBOORTEDATUM       = "geboortedatum";
  public static final String GEBOORTEPLAATS_BL   = "geboorteplaatsBuitenland";
  public static final String GEBOORTEPLAATS_NL   = "geboorteplaatsNL";
  public static final String GEBOORTEPLAATS_AKTE = "geboorteplaatsAkte";
  public static final String GEBOORTELAND        = "geboorteland";
  public static final String GEBOORTELAND_AKTE   = "geboortelandAkte";
  public static final String OVERLIJDENSDATUM    = "overlijdensdatum";
  public static final String IMMIGRATIEDATUM     = "immigratiedatum";
  public static final String IMMIGRATIELAND      = "immigratieland";
  public static final String WOONGEMEENTE        = "woongemeente";
  public static final String WOONPLAATS_BL       = "woonplaatsBuitenland";
  public static final String WOONPLAATS_NL       = "woonplaatsNL";
  public static final String WOONPLAATS_AKTE     = "woonplaatsAkte";
  public static final String WOONLAND            = "woonland";
  public static final String WOONLAND_AKTE       = "woonlandAkte";
  public static final String VBT                 = "vbt";
  public static final String BS                  = "bs";
  public static final String BS_VANAF            = "bsVanaf";
  public static final String NAT                 = "nat";
  public static final String VERSTREK            = "verstrek";
  public static final String CURATELE            = "curatele";
  public static final String STRAAT              = "straat";
  public static final String HNR                 = "hnr";
  public static final String HNRL                = "hnrL";
  public static final String HNRT                = "hnrT";
  public static final String HNRA                = "hnrA";
  public static final String PC                  = "pc";
  public static final String TOELICHTING         = "toelichting";
  public static final String STATUS              = "status";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Gemeente",
      width = "200px",
      readOnly = true)
  @Select(containerDataSource = PlaatsContainer.class)
  private FieldValue woongemeente = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Adres",
      readOnly = true,
      width = "360px")
  @TextField(nullRepresentation = "")
  @InputPrompt(text = "Straat")
  private String straat = "";

  @Field(customTypeClass = HuisnummerVeld.class,
      readOnly = true,
      width = "40px")
  @TextField(nullRepresentation = "")
  @InputPrompt(text = "hnr")
  private String hnr  = "";
  @Field(customTypeClass = GbaTextField.class,
      readOnly = true,
      width = "30px")
  @TextField(nullRepresentation = "",
      maxLength = 1)
  @InputPrompt(text = "l")
  private String hnrL = "";

  @Field(customTypeClass = GbaTextField.class,
      readOnly = true,
      width = "30px")
  @TextField(nullRepresentation = "")
  @InputPrompt(text = "t")
  private String hnrT = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      readOnly = true,
      description = "Aanduiding")
  @TextField(nullRepresentation = "")
  @Select(containerDataSource = AanduidingContainer.class)
  private FieldValue hnrA = new FieldValue();

  @Field(customTypeClass = PostalcodeField.class,
      caption = "Postcode",
      readOnly = true,
      width = "80px")
  @TextField(nullRepresentation = "")
  private FieldValue pc = new FieldValue();

  @Field(type = FieldType.LABEL,
      caption = "Type persoon")
  private String type = "";

  @Field(type = FieldType.LABEL,
      caption = "Status")
  private String status = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "BSN",
      readOnly = true)
  @TextField(nullRepresentation = "")
  private String bsn = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam",
      width = "150px")
  @TextField(nullRepresentation = "")
  private String naam     = "";
  @Field(customTypeClass = GbaTextField.class,
      caption = "Naam op de akte",
      width = "250px")
  @TextField(nullRepresentation = "")
  private String aktenaam = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Voorvoegsel",
      width = "150px")
  @Select(containerDataSource = VoorvoegselContainer.class)
  @FilteringMode(mode = Mode.FILTERINGMODE_STARTSWITH)
  private FieldValue voorv = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Voornaam",
      width = "150px")
  @TextField(nullRepresentation = "")
  private String voorn = "";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Naamgebruik",
      width = "150px")
  @TextField(nullRepresentation = "")
  private String ng = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Titel",
      width = "100px")
  @Select(containerDataSource = AdelijkeTitelPredikaatContainer.class)
  private FieldValue titel = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Geslacht",
      width = "100px")
  @Select(containerDataSource = GeslachtContainer.class)
  private Geslacht geslacht = null;

  @Field(customTypeClass = GbaDateField.class,
      caption = "Geboortedatum",
      width = "80px")
  private GbaDateFieldValue geboortedatum = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geboorteplaats",
      width = "200px")
  @TextField(nullRepresentation = "")
  private String geboorteplaatsBuitenland = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Geboorteplaats",
      width = "200px")
  @Select(containerDataSource = PlaatsContainer.class)
  private FieldValue geboorteplaatsNL = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geboorteplaats op akte",
      width = "200px")
  @TextField(nullRepresentation = "")
  private String geboorteplaatsAkte = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Geboorteland",
      width = "200px")
  @Select(containerDataSource = LandContainer.class)
  @Immediate
  private FieldValue geboorteland = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geboorteland op akte",
      width = "200px")
  @TextField(nullRepresentation = "")
  private String geboortelandAkte = "";

  @Field(customTypeClass = GbaDateField.class,
      caption = "Overlijdensdatum",
      width = "80px")
  private GbaDateFieldValue overlijdensdatum = null;

  @Field(customTypeClass = GbaDateField.class,
      caption = "Immigratiedatum",
      width = "80px")
  private GbaDateFieldValue immigratiedatum = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Immigratieland",
      width = "200px")
  @Select(containerDataSource = LandContainer.class)
  @Immediate
  private FieldValue immigratieland = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Woonplaats",
      width = "200px")
  @TextField(nullRepresentation = "")
  private String woonplaatsBuitenland = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Woonplaats",
      width = "200px")
  @Select(containerDataSource = WoonplaatsContainer.class)
  private FieldValue woonplaatsNL = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Woonplaats op akte",
      width = "200px")
  @TextField(nullRepresentation = "")
  private String woonplaatsAkte = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Woonland",
      width = "200px")
  @Select(containerDataSource = LandContainer.class)
  @Immediate
  private FieldValue woonland = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Woonland op akte",
      width = "200px")
  @TextField(nullRepresentation = "")
  private String woonlandAkte = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Burgerlijke staat",
      width = "200px")
  @Select(containerDataSource = BurgerlijkeStaatContainer.class)
  private BurgerlijkeStaatType bs = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Sinds",
      width = "80px")
  private DateFieldValue bsVanaf = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Verblijfstitelcode",
      width = "530px")
  @Select(containerDataSource = VerblijfstitelContainer.class)
  private FieldValue vbt = null;

  @Field(type = FieldType.LABEL,
      caption = "Nationaliteiten",
      width = "400px")
  private String nat = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Verstrekkingsbeperking")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean verstrek = false;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Curatele")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private boolean curatele = false;

  @Field(customTypeClass = ProTextArea.class,
      caption = "Toelichting",
      width = "530px")
  @TextArea(nullRepresentation = "",
      maxLength = 400,
      rows = 3)
  private String toelichting = "";

  public String getAktenaam() {
    return aktenaam;
  }

  public void setAktenaam(String aktenaam) {
    this.aktenaam = aktenaam;
  }

  public BurgerlijkeStaatType getBs() {
    return bs;
  }

  public void setBs(BurgerlijkeStaatType bs) {
    this.bs = bs;
  }

  public String getBsn() {
    return bsn;
  }

  public void setBsn(String bsn) {
    this.bsn = bsn;
  }

  public DateFieldValue getBsVanaf() {
    return bsVanaf;
  }

  public void setBsVanaf(DateFieldValue bsVanaf) {
    this.bsVanaf = bsVanaf;
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

  public String getGeboortelandAkte() {
    return geboortelandAkte;
  }

  public void setGeboortelandAkte(String geboortelandAkte) {
    this.geboortelandAkte = geboortelandAkte;
  }

  public String getGeboorteplaatsAkte() {
    return geboorteplaatsAkte;
  }

  public void setGeboorteplaatsAkte(String geboorteplaatsAkte) {
    this.geboorteplaatsAkte = geboorteplaatsAkte;
  }

  public String getGeboorteplaatsBuitenland() {
    return geboorteplaatsBuitenland;
  }

  public void setGeboorteplaatsBuitenland(String geboorteplaatsBuitenland) {
    this.geboorteplaatsBuitenland = geboorteplaatsBuitenland;
  }

  public FieldValue getGeboorteplaatsNL() {
    return geboorteplaatsNL;
  }

  public void setGeboorteplaatsNL(FieldValue geboorteplaatsNL) {
    this.geboorteplaatsNL = geboorteplaatsNL;
  }

  public Geslacht getGeslacht() {
    return geslacht;
  }

  public void setGeslacht(Geslacht geslacht) {
    this.geslacht = geslacht;
  }

  public String getHnr() {
    return hnr;
  }

  public void setHnr(String hnr) {
    this.hnr = hnr;
  }

  public FieldValue getHnrA() {
    return hnrA;
  }

  public void setHnrA(FieldValue hnrA) {
    this.hnrA = hnrA;
  }

  public String getHnrL() {
    return hnrL;
  }

  public void setHnrL(String hnrL) {
    this.hnrL = hnrL;
  }

  public String getHnrT() {
    return hnrT;
  }

  public void setHnrT(String hnrT) {
    this.hnrT = hnrT;
  }

  public GbaDateFieldValue getImmigratiedatum() {
    return immigratiedatum;
  }

  public void setImmigratiedatum(GbaDateFieldValue immigratiedatum) {
    this.immigratiedatum = immigratiedatum;
  }

  public FieldValue getImmigratieland() {
    return immigratieland;
  }

  public void setImmigratieland(FieldValue immigratieland) {
    this.immigratieland = immigratieland;
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
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

  public GbaDateFieldValue getOverlijdensdatum() {
    return overlijdensdatum;
  }

  public void setOverlijdensdatum(GbaDateFieldValue overlijdensdatum) {
    this.overlijdensdatum = overlijdensdatum;
  }

  public FieldValue getPc() {
    return pc;
  }

  public void setPc(FieldValue pc) {
    this.pc = pc;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStraat() {
    return straat;
  }

  public void setStraat(String straat) {
    this.straat = straat;
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public FieldValue getVbt() {
    return vbt;
  }

  public void setVbt(FieldValue vbt) {
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

  public FieldValue getWoongemeente() {
    return woongemeente;
  }

  public void setWoongemeente(FieldValue woongemeente) {
    this.woongemeente = woongemeente;
  }

  public FieldValue getWoonland() {
    return woonland;
  }

  public void setWoonland(FieldValue woonland) {
    this.woonland = woonland;
  }

  public String getWoonlandAkte() {
    return woonlandAkte;
  }

  public void setWoonlandAkte(String woonlandAkte) {
    this.woonlandAkte = woonlandAkte;
  }

  public String getWoonplaatsAkte() {
    return woonplaatsAkte;
  }

  public void setWoonplaatsAkte(String woonplaatsAkte) {
    this.woonplaatsAkte = woonplaatsAkte;
  }

  public String getWoonplaatsBuitenland() {
    return woonplaatsBuitenland;
  }

  public void setWoonplaatsBuitenland(String woonplaatsBuitenland) {
    this.woonplaatsBuitenland = woonplaatsBuitenland;
  }

  public FieldValue getWoonplaatsNL() {
    return woonplaatsNL;
  }

  public void setWoonplaatsNL(FieldValue woonplaatsNL) {
    this.woonplaatsNL = woonplaatsNL;
  }

  public boolean isCuratele() {
    return curatele;
  }

  public void setCuratele(boolean curatele) {
    this.curatele = curatele;
  }

  public boolean isVerstrek() {
    return verstrek;
  }

  public void setVerstrek(boolean verstrek) {
    this.verstrek = verstrek;
  }
}
