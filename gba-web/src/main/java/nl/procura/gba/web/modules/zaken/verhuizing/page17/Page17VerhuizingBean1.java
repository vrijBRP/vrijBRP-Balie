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

package nl.procura.gba.web.modules.zaken.verhuizing.page17;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page17VerhuizingBean1 implements Serializable {

  public static final String SOORTVERHUIZING  = "soortVerhuizing";
  public static final String HUIDIGADRES      = "huidigAdres";
  public static final String NIEUWADRES       = "nieuwAdres";
  public static final String AANGEVER         = "aangever";
  public static final String TOELICHTING      = "toelichting";
  public static final String TOESTEMMINGGEVER = "toestemminggever";
  public static final String DUUR             = "duur";
  public static final String LANDHERVESTIGING = "landHervestiging";
  public static final String RECHTSFEITEN     = "rechtsfeiten";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Soort verhuizing")
  private String soortVerhuizing  = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Huidig adres",
      width = "400px")
  private String huidigAdres      = "";
  @Field(type = FieldType.LABEL,
      caption = "Nieuw adres",
      width = "400px")
  private String nieuwAdres       = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Aangever",
      width = "400px")
  private String aangever         = "";
  @Field(type = FieldType.TEXT_AREA,
      caption = "Toelichting aangifte",
      width = "500px")
  private String toelichting      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Toestemminggever")
  private String toestemminggever = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Vermoedelijke duur")
  private String duur             = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Land van herkomst")
  private String landHervestiging = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Rechtsfeiten")
  private String rechtsfeiten     = "";

  public String getAangever() {
    return aangever;
  }

  public void setAangever(String aangever) {
    this.aangever = aangever;
  }

  public String getDuur() {
    return duur;
  }

  public void setDuur(String duur) {
    this.duur = duur;
  }

  public String getHuidigAdres() {
    return huidigAdres;
  }

  public void setHuidigAdres(String huidigAdres) {
    this.huidigAdres = huidigAdres;
  }

  public String getLandHervestiging() {
    return landHervestiging;
  }

  public void setLandHervestiging(String landHervestiging) {
    this.landHervestiging = landHervestiging;
  }

  public String getNieuwAdres() {
    return nieuwAdres;
  }

  public void setNieuwAdres(String nieuwAdres) {
    this.nieuwAdres = nieuwAdres;
  }

  public String getRechtsfeiten() {
    return rechtsfeiten;
  }

  public void setRechtsfeiten(String rechtsfeiten) {
    this.rechtsfeiten = rechtsfeiten;
  }

  public String getSoortVerhuizing() {
    return soortVerhuizing;
  }

  public void setSoortVerhuizing(String soortVerhuizing) {
    this.soortVerhuizing = soortVerhuizing;
  }

  public String getToelichting() {
    return toelichting;
  }

  public void setToelichting(String toelichting) {
    this.toelichting = toelichting;
  }

  public String getToestemminggever() {
    return toestemminggever;
  }

  public void setToestemminggever(String toestemminggever) {
    this.toestemminggever = toestemminggever;
  }

}
