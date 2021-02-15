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

package nl.procura.gba.web.modules.zaken.vog.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2VogBean1 implements Serializable {

  public static final String NAAMAANVRAGER        = "naamAanvrager";
  public static final String GEBORENAANVRAGER     = "geborenAanvrager";
  public static final String GESLACHTAANVRAGER    = "geslachtAanvrager";
  public static final String AANSCHRNAAMAANVRAGER = "aanschrNaamAanvrager";
  public static final String ADRESAANVRAGER       = "adresAanvrager";
  public static final String PCAANVRAGER          = "pcAanvrager";
  public static final String PLAATSAANVRAGER      = "plaatsAanvrager";
  public static final String LANDAANVRAGER        = "landAanvrager";

  @Field(caption = "Naam")
  private String naamAanvrager        = "";
  @Field(caption = "Geboren")
  private String geborenAanvrager     = "";
  @Field(caption = "Geslacht")
  private String geslachtAanvrager    = "";
  @Field(caption = "Aanschrijfnaam")
  private String aanschrNaamAanvrager = "";
  @Field(caption = "Adres")
  private String adresAanvrager       = "";
  @Field(caption = "Postcode")
  private String pcAanvrager          = "";
  @Field(caption = "Plaats")
  private String plaatsAanvrager      = "";
  @Field(caption = "Land")
  private String landAanvrager        = "";

  public String getAanschrNaamAanvrager() {
    return aanschrNaamAanvrager;
  }

  public void setAanschrNaamAanvrager(String aanschrNaamAanvrager) {
    this.aanschrNaamAanvrager = aanschrNaamAanvrager;
  }

  public String getAdresAanvrager() {
    return adresAanvrager;
  }

  public void setAdresAanvrager(String adresAanvrager) {
    this.adresAanvrager = adresAanvrager;
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

  public String getLandAanvrager() {
    return landAanvrager;
  }

  public void setLandAanvrager(String landAanvrager) {
    this.landAanvrager = landAanvrager;
  }

  public String getNaamAanvrager() {
    return naamAanvrager;
  }

  public void setNaamAanvrager(String naamAanvrager) {
    this.naamAanvrager = naamAanvrager;
  }

  public String getPcAanvrager() {
    return pcAanvrager;
  }

  public void setPcAanvrager(String pcAanvrager) {
    this.pcAanvrager = pcAanvrager;
  }

  public String getPlaatsAanvrager() {
    return plaatsAanvrager;
  }

  public void setPlaatsAanvrager(String plaatsAanvrager) {
    this.plaatsAanvrager = plaatsAanvrager;
  }
}
