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

package nl.procura.gba.web.modules.hoofdmenu.klapper.page3;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.GeslachtContainer;
import nl.procura.gba.web.components.containers.VoorvoegselContainer;
import nl.procura.gba.web.components.fields.GbaBsnField;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaDateField;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FilteringMode;
import nl.procura.vaadin.annotation.field.FilteringMode.Mode;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page3KlapperBean2 implements Serializable {

  public static final String DATUM_OVERLIJDEN = "datumOverlijden";
  public static final String DATUM_HUWELIJK   = "datumHuwelijk";
  public static final String DATUM_ERKENNING  = "datumErkenning";
  public static final String DATUM_GEBOORTE   = "datumGeboorte";

  public static final String BSN1           = "bsn1";
  public static final String VOORN1         = "voorn1";
  public static final String VOORV1         = "voorv1";
  public static final String NAAM1          = "naam1";
  public static final String GESLACHT1      = "geslacht1";
  public static final String GEBOORTEDATUM1 = "geboortedatum1";

  public static final String BSN2           = "bsn2";
  public static final String VOORN2         = "voorn2";
  public static final String VOORV2         = "voorv2";
  public static final String NAAM2          = "naam2";
  public static final String GESLACHT2      = "geslacht2";
  public static final String GEBOORTEDATUM2 = "geboortedatum2";

  @Field(customTypeClass = DatumVeld.class,
      required = true,
      caption = "Datum overlijden / lijkvinding",
      width = "80px")
  private DateFieldValue datumOverlijden = null;

  @Field(customTypeClass = DatumVeld.class,
      required = true,
      caption = "Datum huwelijk",
      width = "80px")
  private DateFieldValue datumHuwelijk = null;

  @Field(customTypeClass = DatumVeld.class,
      required = true,
      caption = "Datum erkenning",
      width = "80px")
  private DateFieldValue datumErkenning = null;

  @Field(customTypeClass = DatumVeld.class,
      required = true,
      caption = "Datum geboorte",
      width = "80px")
  private DateFieldValue datumGeboorte = null;

  @Field(customTypeClass = GbaBsnField.class,
      caption = "Burgerservicenummer",
      width = "150px")
  private BsnFieldValue bsn1 = null;

  @Field(type = FieldType.TEXT_FIELD,
      width = "200px",
      caption = "Voornamen",
      required = true)
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String voorn1 = "";

  @Field(customTypeClass = GbaComboBox.class,
      width = "200px",
      caption = "Voorvoegsel")
  @Select(containerDataSource = VoorvoegselContainer.class)
  @FilteringMode(mode = Mode.FILTERINGMODE_STARTSWITH)
  private FieldValue voorv1 = null;

  @Field(type = FieldType.TEXT_FIELD,
      width = "200px",
      caption = "Geslachtsnaam",
      required = true)
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String naam1 = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Geslacht",
      required = true,
      width = "150px")
  @Select(containerDataSource = GeslachtContainer.class,
      nullSelectionAllowed = false)
  private Geslacht geslacht1 = null;

  @Field(customTypeClass = GbaDateField.class,
      caption = "Geboortedatum",
      width = "150px",
      required = true)
  private GbaDateFieldValue geboortedatum1 = null;

  @Field(customTypeClass = GbaBsnField.class,
      caption = "Burgerservicenummer",
      width = "150px")
  private BsnFieldValue bsn2 = null;

  @Field(type = FieldType.TEXT_FIELD,
      width = "200px",
      caption = "Voornamen",
      required = true)
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String voorn2 = "";

  @Field(customTypeClass = GbaComboBox.class,
      width = "200px",
      caption = "Voorvoegsel")
  @Select(containerDataSource = VoorvoegselContainer.class)
  private FieldValue voorv2 = null;

  @Field(type = FieldType.TEXT_FIELD,
      width = "200px",
      caption = "geslachtsnaam",
      required = true)
  @TextField(maxLength = 250,
      nullRepresentation = "")
  private String naam2 = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Geslacht",
      required = true,
      width = "150px")
  @Select(containerDataSource = GeslachtContainer.class,
      nullSelectionAllowed = false)
  private Geslacht geslacht2 = null;

  @Field(customTypeClass = GbaDateField.class,
      caption = "Geboortedatum",
      width = "80px",
      required = true)
  private GbaDateFieldValue geboortedatum2 = null;

  public BsnFieldValue getBsn1() {
    return bsn1;
  }

  public void setBsn1(BsnFieldValue bsn1) {
    this.bsn1 = bsn1;
  }

  public BsnFieldValue getBsn2() {
    return bsn2;
  }

  public void setBsn2(BsnFieldValue bsn2) {
    this.bsn2 = bsn2;
  }

  public DateFieldValue getDatumErkenning() {
    return datumErkenning;
  }

  public void setDatumErkenning(DateFieldValue datumErkenning) {
    this.datumErkenning = datumErkenning;
  }

  public DateFieldValue getDatumGeboorte() {
    return datumGeboorte;
  }

  public void setDatumGeboorte(DateFieldValue datumGeboorte) {
    this.datumGeboorte = datumGeboorte;
  }

  public DateFieldValue getDatumHuwelijk() {
    return datumHuwelijk;
  }

  public void setDatumHuwelijk(DateFieldValue datumHuwelijk) {
    this.datumHuwelijk = datumHuwelijk;
  }

  public DateFieldValue getDatumOverlijden() {
    return datumOverlijden;
  }

  public void setDatumOverlijden(DateFieldValue datumOverlijden) {
    this.datumOverlijden = datumOverlijden;
  }

  public GbaDateFieldValue getGeboortedatum1() {
    return geboortedatum1;
  }

  public void setGeboortedatum1(GbaDateFieldValue geboortedatum1) {
    this.geboortedatum1 = geboortedatum1;
  }

  public GbaDateFieldValue getGeboortedatum2() {
    return geboortedatum2;
  }

  public void setGeboortedatum2(GbaDateFieldValue geboortedatum2) {
    this.geboortedatum2 = geboortedatum2;
  }

  public Geslacht getGeslacht1() {
    return geslacht1;
  }

  public void setGeslacht1(Geslacht geslacht1) {
    this.geslacht1 = geslacht1;
  }

  public Geslacht getGeslacht2() {
    return geslacht2;
  }

  public void setGeslacht2(Geslacht geslacht2) {
    this.geslacht2 = geslacht2;
  }

  public String getNaam1() {
    return naam1;
  }

  public void setNaam1(String naam1) {
    this.naam1 = naam1;
  }

  public String getNaam2() {
    return naam2;
  }

  public void setNaam2(String naam2) {
    this.naam2 = naam2;
  }

  public String getVoorn1() {
    return voorn1;
  }

  public void setVoorn1(String voorn1) {
    this.voorn1 = voorn1;
  }

  public String getVoorn2() {
    return voorn2;
  }

  public void setVoorn2(String voorn2) {
    this.voorn2 = voorn2;
  }

  public FieldValue getVoorv1() {
    return voorv1;
  }

  public void setVoorv1(FieldValue voorv1) {
    this.voorv1 = voorv1;
  }

  public FieldValue getVoorv2() {
    return voorv2;
  }

  public void setVoorv2(FieldValue voorv2) {
    this.voorv2 = voorv2;
  }
}
