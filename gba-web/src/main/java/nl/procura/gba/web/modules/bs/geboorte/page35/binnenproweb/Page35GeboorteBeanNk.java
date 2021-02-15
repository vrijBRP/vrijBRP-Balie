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

package nl.procura.gba.web.modules.bs.geboorte.page35.binnenproweb;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.bs.erkenning.page30.NaamskeuzePersoonContainer;
import nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page35GeboorteBeanNk implements Serializable {

  public static final String GEMEENTE           = "gemeente";
  public static final String DATUM              = "datum";
  public static final String AKTENR             = "aktenr";
  public static final String GESLACHTSNAAM      = "geslachtsnaam";
  public static final String VOORVOEGSEL        = "voorv";
  public static final String TITEL              = "titel";
  public static final String NAAMSKEUZE_PERSOON = "naamskeuzePersoon";

  @Field(type = FieldType.LABEL,
      caption = "Gemeente")
  private String gemeente = "";

  @Field(type = FieldType.LABEL,
      caption = "Datum")
  private String datum = "";

  @Field(type = FieldType.LABEL,
      caption = "Aktenummer")
  private String aktenr = "";

  @Field(type = FieldType.LABEL,
      caption = "Geslachtsnaam")
  private String geslachtsnaam = "";

  @Field(type = FieldType.LABEL,
      caption = "Voorvoegsel")
  private FieldValue voorv;

  @Field(type = FieldType.LABEL,
      caption = "Titel/predikaat")
  private FieldValue titel;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Naam gekregen van",
      width = "200px",
      readOnly = true)
  @Select(containerDataSource = NaamskeuzePersoonContainer.class)
  @Immediate
  private NaamsPersoonType naamskeuzePersoon = NaamsPersoonType.ONBEKEND;

  public String getAktenr() {
    return aktenr;
  }

  public void setAktenr(String aktenr) {
    this.aktenr = aktenr;
  }

  public String getDatum() {
    return datum;
  }

  public void setDatum(String datum) {
    this.datum = datum;
  }

  public String getGemeente() {
    return gemeente;
  }

  public void setGemeente(String gemeente) {
    this.gemeente = gemeente;
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

  public NaamsPersoonType getNaamskeuzePersoon() {
    return naamskeuzePersoon;
  }

  public void setNaamskeuzePersoon(NaamsPersoonType naamskeuzePersoon) {
    this.naamskeuzePersoon = naamskeuzePersoon;
  }
}
