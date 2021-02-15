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

package nl.procura.gba.web.modules.bs.geboorte.page84;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page84GeboorteBean1 implements Serializable {

  public static final String NAAM_AANGEVER     = "naamAangever";
  public static final String GEBOREN_AANGEVER  = "geborenAangever";
  public static final String REDENVERPLICHT    = "redenVerplicht";
  public static final String TARDIEVE_AANGIFTE = "tardieveAangifte";
  public static final String NAAM_MOEDER       = "naamMoeder";
  public static final String GEBOREN_MOEDER    = "geborenMoeder";
  public static final String GEMEENTE_MOEDER   = "gemeenteMoeder";
  public static final String NAAM_VADER        = "naamVader";
  public static final String GEBOREN_VADER     = "geborenVader";
  public static final String GEMEENTE_VADER    = "gemeenteVader";
  public static final String GEZIN             = "gezin";
  public static final String AFSTAMMINGSRECHT  = "afstammingsRecht";
  public static final String NAAMSRECHT        = "naamsRecht";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naam")
  private String naamAangever     = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geboren")
  private String geborenAangever  = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Reden verplicht / bevoegd")
  private String redenVerplicht   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Tardieve aangifte")
  private String tardieveAangifte = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naam")

  private String naamMoeder       = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geboren")
  private String geborenMoeder    = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gemeente")
  private String gemeenteMoeder   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naam")
  private String naamVader        = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Geboren")
  private String geborenVader     = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gemeente")
  private String gemeenteVader    = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gezinssituatie")
  private String gezin            = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Afstammingsrecht")
  private String afstammingsRecht = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Namenrecht")
  private String naamsRecht       = "";

  public String getAfstammingsRecht() {
    return afstammingsRecht;
  }

  public void setAfstammingsRecht(String afstammingsRecht) {
    this.afstammingsRecht = afstammingsRecht;
  }

  public String getGeborenAangever() {
    return geborenAangever;
  }

  public void setGeborenAangever(String geborenAangever) {
    this.geborenAangever = geborenAangever;
  }

  public String getGeborenMoeder() {
    return geborenMoeder;
  }

  public void setGeborenMoeder(String geborenMoeder) {
    this.geborenMoeder = geborenMoeder;
  }

  public String getGeborenVader() {
    return geborenVader;
  }

  public void setGeborenVader(String geborenVader) {
    this.geborenVader = geborenVader;
  }

  public String getGemeenteMoeder() {
    return gemeenteMoeder;
  }

  public void setGemeenteMoeder(String gemeenteMoeder) {
    this.gemeenteMoeder = gemeenteMoeder;
  }

  public String getGemeenteVader() {
    return gemeenteVader;
  }

  public void setGemeenteVader(String gemeenteVader) {
    this.gemeenteVader = gemeenteVader;
  }

  public String getGezin() {
    return gezin;
  }

  public void setGezin(String gezin) {
    this.gezin = gezin;
  }

  public String getNaamAangever() {
    return naamAangever;
  }

  public void setNaamAangever(String naamAangever) {
    this.naamAangever = naamAangever;
  }

  public String getNaamMoeder() {
    return naamMoeder;
  }

  public void setNaamMoeder(String naamMoeder) {
    this.naamMoeder = naamMoeder;
  }

  public String getNaamsRecht() {
    return naamsRecht;
  }

  public void setNaamsRecht(String naamsRecht) {
    this.naamsRecht = naamsRecht;
  }

  public String getNaamVader() {
    return naamVader;
  }

  public void setNaamVader(String naamVader) {
    this.naamVader = naamVader;
  }

  public String getRedenVerplicht() {
    return redenVerplicht;
  }

  public void setRedenVerplicht(String redenVerplicht) {
    this.redenVerplicht = redenVerplicht;
  }

  public String getTardieveAangifte() {
    return tardieveAangifte;
  }

  public void setTardieveAangifte(String tardieveAangifte) {
    this.tardieveAangifte = tardieveAangifte;
  }
}
