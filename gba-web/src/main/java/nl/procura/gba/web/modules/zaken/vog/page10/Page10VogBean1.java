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

package nl.procura.gba.web.modules.zaken.vog.page10;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.PostalcodeField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page10VogBean1 implements Serializable {

  public static final String NAAMAANVRAGER        = "naamAanvrager";
  public static final String GEBORENAANVRAGER     = "geborenAanvrager";
  public static final String GESLACHTAANVRAGER    = "geslachtAanvrager";
  public static final String AANSCHRNAAMAANVRAGER = "aanschrNaamAanvrager";
  public static final String AFWADRES             = "afwAdres";
  public static final String STRAAT               = "straat";
  public static final String HNR                  = "hnr";
  public static final String HNRL                 = "hnrL";
  public static final String HNRT                 = "hnrT";
  public static final String PCAANVRAGER          = "pcAanvrager";
  public static final String PLAATSAANVRAGER      = "plaatsAanvrager";
  public static final String LANDAANVRAGER        = "landAanvrager";

  @Field(caption = "Naam",
      readOnly = true)
  private String     naamAanvrager        = "";
  @Field(caption = "Geboren",
      readOnly = true)
  private String     geborenAanvrager     = "";
  @Field(caption = "Geslacht",
      readOnly = true)
  private String     geslachtAanvrager    = "";
  @Field(caption = "Aanschrijfnaam",
      width = "300px",
      required = true)
  @TextField(maxLength = 95)
  private String     aanschrNaamAanvrager = "";
  @Field(caption = "Afwijkend adres",
      customTypeClass = GbaNativeSelect.class)
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE,
      nullSelectionAllowed = false)
  @Immediate()
  private boolean    afwAdres             = false;
  @Field(caption = "Adres",
      required = true,
      description = "Straat",
      width = "200px")
  @InputPrompt(text = "Straat")
  @TextField(maxLength = 35)
  private String     straat               = "";
  @Field(caption = "Huisnummer",
      customTypeClass = NumberField.class,
      description = "Huisnummer",
      width = "30px")
  @InputPrompt(text = "Nr.")
  private String     hnr                  = "";
  @Field(caption = "Huisletter",
      description = "Huisletter",
      width = "30px")
  @InputPrompt(text = "L")
  private String     hnrL                 = "";
  @Field(caption = "Toevoeging",
      description = "Toevoeging",
      width = "50px")
  @InputPrompt(text = "T")
  private String     hnrT                 = "";
  @Field(customTypeClass = PostalcodeField.class,
      caption = "Postcode / Plaats",
      width = "60px")
  private FieldValue pcAanvrager          = null;
  @Field(caption = "Plaats",
      required = true,
      width = "134px")
  @TextField(maxLength = 40)
  private String     plaatsAanvrager      = "";
  @Field(customTypeClass = GbaComboBox.class,
      caption = "Land",
      required = true)
  @Select(containerDataSource = LandContainer.class)
  private FieldValue landAanvrager        = null;

  public String getAanschrNaamAanvrager() {
    return aanschrNaamAanvrager;
  }

  public void setAanschrNaamAanvrager(String aanschrNaamAanvrager) {
    this.aanschrNaamAanvrager = aanschrNaamAanvrager;
  }

  public boolean getAfwAdres() {
    return afwAdres;
  }

  public void setAfwAdres(boolean afwAdres) {
    this.afwAdres = afwAdres;
  }

  public String getGeborenAanvrager() {
    return geborenAanvrager;
  }

  public void setGeborenAanvrager(String geborenAanvrager) {
    this.geborenAanvrager = geborenAanvrager;
  }

  public String getGeslachtAanvrager() {
    return geslachtAanvrager;
  }

  public void setGeslachtAanvrager(String geslachtAanvrager) {
    this.geslachtAanvrager = geslachtAanvrager;
  }

  public String getHnr() {
    return hnr;
  }

  public void setHnr(String hnr) {
    this.hnr = hnr;
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

  public FieldValue getLandAanvrager() {
    return landAanvrager;
  }

  public void setLandAanvrager(FieldValue landAanvrager) {
    this.landAanvrager = landAanvrager;
  }

  public String getNaamAanvrager() {
    return naamAanvrager;
  }

  public void setNaamAanvrager(String naamAanvrager) {
    this.naamAanvrager = naamAanvrager;
  }

  public FieldValue getPcAanvrager() {
    return pcAanvrager;
  }

  public void setPcAanvrager(FieldValue pcAanvrager) {
    this.pcAanvrager = pcAanvrager;
  }

  public String getPlaatsAanvrager() {
    return plaatsAanvrager;
  }

  public void setPlaatsAanvrager(String plaatsAanvrager) {
    this.plaatsAanvrager = plaatsAanvrager;
  }

  public String getStraat() {
    return straat;
  }

  public void setStraat(String straat) {
    this.straat = straat;
  }
}
