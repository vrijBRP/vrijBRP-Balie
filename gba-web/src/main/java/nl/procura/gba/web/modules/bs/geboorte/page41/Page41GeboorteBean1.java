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

package nl.procura.gba.web.modules.bs.geboorte.page41;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page41GeboorteBean1 implements Serializable {

  public static final String BSN            = "bsn";
  public static final String NAAM           = "naam";
  public static final String GESLACHT       = "geslacht";
  public static final String GEBOREN        = "geboren";
  public static final String VERBLIJFPLAATS = "verblijfplaats";
  public static final String NATIONALITEIT  = "nationaliteit";
  public static final String VBT            = "vbt";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "BSN")
  private String bsn            = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naam")
  private String naam           = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geslacht")
  private String geslacht       = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geboren")
  private String geboren        = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verblijfplaats")
  private String verblijfplaats = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Nationaliteit")
  private String nationaliteit  = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verblijfstitel")
  private String vbt            = "";

  public String getBsn() {
    return bsn;
  }

  public void setBsn(String bsn) {
    this.bsn = bsn;
  }

  public String getGeboren() {
    return geboren;
  }

  public void setGeboren(String geboren) {
    this.geboren = geboren;
  }

  public String getGeslacht() {
    return geslacht;
  }

  public void setGeslacht(String geslacht) {
    this.geslacht = geslacht;
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  public String getNationaliteit() {
    return nationaliteit;
  }

  public void setNationaliteit(String nationaliteit) {
    this.nationaliteit = nationaliteit;
  }

  public String getVbt() {
    return vbt;
  }

  public void setVbt(String vbt) {
    this.vbt = vbt;
  }

  public String getVerblijfplaats() {
    return verblijfplaats;
  }

  public void setVerblijfplaats(String verblijfplaats) {
    this.verblijfplaats = verblijfplaats;
  }
}
