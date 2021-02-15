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

package nl.procura.gba.web.modules.bs.overlijden.buitenland.overzicht;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class OverlijdenBuitenlandOverzichtBean1 implements Serializable {

  public static final String DATUM            = "datum";
  public static final String PLAATS           = "plaats";
  public static final String LAND             = "land";
  public static final String ONTVANGEN_OP     = "ontvangenOp";
  public static final String VAN              = "van";
  public static final String TYPE             = "type";
  public static final String LAND_AFGIFTE     = "landAfgifte";
  public static final String VOLDOET_AANEISEN = "voldoetAanEisen";

  @Field(type = FieldType.LABEL,
      caption = "Datum / plaats / land")
  private String datum = "";

  @Field(type = FieldType.LABEL,
      caption = "Plaats")
  private String plaats = "";

  @Field(type = FieldType.LABEL,
      caption = "Land")
  private String land = "";

  @Field(type = FieldType.LABEL,
      caption = "Ontvangen op")
  private String ontvangenOp     = "";
  @Field(type = FieldType.LABEL,
      caption = "Van")
  private String van             = "";
  @Field(type = FieldType.LABEL,
      caption = "Type")
  private String type            = "";
  @Field(type = FieldType.LABEL,
      caption = "Land afgifte")
  private String landAfgifte     = "";
  @Field(type = FieldType.LABEL,
      caption = "Voldoet aan eisen")
  private String voldoetAanEisen = "";

  public String getDatum() {
    return datum;
  }

  public void setDatum(String datum) {
    this.datum = datum;
  }

  public String getLand() {
    return land;
  }

  public void setLand(String land) {
    this.land = land;
  }

  public String getLandAfgifte() {
    return landAfgifte;
  }

  public void setLandAfgifte(String landAfgifte) {
    this.landAfgifte = landAfgifte;
  }

  public String getOntvangenOp() {
    return ontvangenOp;
  }

  public void setOntvangenOp(String ontvangenOp) {
    this.ontvangenOp = ontvangenOp;
  }

  public String getPlaats() {
    return plaats;
  }

  public void setPlaats(String plaats) {
    this.plaats = plaats;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getVan() {
    return van;
  }

  public void setVan(String van) {
    this.van = van;
  }

  public String getVoldoetAanEisen() {
    return voldoetAanEisen;
  }

  public void setVoldoetAanEisen(String voldoetAanEisen) {
    this.voldoetAanEisen = voldoetAanEisen;
  }

}
