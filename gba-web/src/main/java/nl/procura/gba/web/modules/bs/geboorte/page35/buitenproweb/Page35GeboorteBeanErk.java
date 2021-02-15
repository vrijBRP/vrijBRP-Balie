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

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.containers.NaamskeuzeBuitenProwebTypeContainer;
import nl.procura.gba.web.components.containers.RechtbankContainer;
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
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page35GeboorteBeanErk implements Serializable {

  public static final String AKTENR                 = "aktenr";
  public static final String DATUM                  = "datum";
  public static final String TOESTEMMINGGEVER_TYPE  = "toestemminggeverType";
  public static final String RECHTBANK              = "rechtbank";
  public static final String NAAMSKEUZE_TYPE        = "naamskeuzeType";
  public static final String NAAMS_PERSOON_TYPE     = "naamsPersoonType";
  public static final String NAAMS_PERSOON_TYPE_ERK = "naamsPersoonTypeErk";
  public static final String AFSTAMMINGSRECHT       = "afstammingsrecht";
  public static final String GEMEENTE               = "gemeente";
  public static final String LAND                   = "land";
  public static final String PLAATS                 = "plaats";

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
      caption = "Geslachtsnaam",
      required = true,
      visible = false,
      width = "200px")
  @Select(containerDataSource = NaamskeuzePersoonErkContainer.class,
      nullSelectionAllowed = false)
  @Immediate
  private NaamsPersoonType naamsPersoonType = null;

  @Field(type = FieldType.LABEL,
      caption = "Geslachtsnaam",
      required = true,
      visible = false,
      width = "200px")
  private NaamsPersoonType naamsPersoonTypeErk = NaamsPersoonType.ERKENNER; // Alleen voor tonen erkenner

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

  public FieldValue getAfstammingsrecht() {
    return afstammingsrecht;
  }

  public void setAfstammingsrecht(FieldValue afstammingsrecht) {
    this.afstammingsrecht = afstammingsrecht;
  }

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

  public NaamskeuzeVanToepassingType getNaamskeuzeType() {
    return naamskeuzeType;
  }

  public void setNaamskeuzeType(NaamskeuzeVanToepassingType naamskeuzeType) {
    this.naamskeuzeType = naamskeuzeType;
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

  public NaamsPersoonType getNaamsPersoonTypeErk() {
    return naamsPersoonTypeErk;
  }

  public void setNaamsPersoonTypeErk(NaamsPersoonType naamsAanduidingTypeErk) {
    this.naamsPersoonTypeErk = naamsAanduidingTypeErk;
  }

  public String getPlaats() {
    return plaats;
  }

  public void setPlaats(String plaats) {
    this.plaats = plaats;
  }

  public RechtbankLocatie getRechtbank() {
    return rechtbank;
  }

  public void setRechtbank(RechtbankLocatie rechtbank) {
    this.rechtbank = rechtbank;
  }

  public ToestemminggeverType getToestemminggeverType() {
    return toestemminggeverType;
  }

  public void setToestemminggeverType(ToestemminggeverType toestemminggeverType) {
    this.toestemminggeverType = toestemminggeverType;
  }
}
