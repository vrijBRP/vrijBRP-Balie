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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4a;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page4aRijbewijsBean1 implements Serializable {

  public static final String CATEGORIE     = "categorie";
  public static final String AUTOMAAT      = "automaat";
  public static final String EERSTEAFGIFTE = "eersteAfgifte";
  public static final String MEDVERKLARING = "medVerklaring";
  public static final String OMGEWISSELD   = "omgewisseld";
  public static final String LAND          = "land";
  public static final String VOERTUIG      = "voertuig";
  public static final String BEPERKING     = "beperking";
  public static final String GESCHIKTHEID  = "geschiktheid";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Categorie")
  private String categorie     = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Automaat")
  private String automaat      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Eerste afgifte")
  private String eersteAfgifte = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Med. verklaring")
  private String medVerklaring = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Omgewisseld")
  private String omgewisseld   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Land")
  private String land          = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Voertuig")
  private String voertuig      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Beperking")
  private String beperking     = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Indicatie m.b.t. geschiktheid")
  private String geschiktheid  = "";

  public String getAutomaat() {
    return automaat;
  }

  public void setAutomaat(String automaat) {
    this.automaat = automaat;
  }

  public String getBeperking() {
    return beperking;
  }

  public void setBeperking(String beperking) {
    this.beperking = beperking;
  }

  public String getCategorie() {
    return categorie;
  }

  public void setCategorie(String categorie) {
    this.categorie = categorie;
  }

  public String getEersteAfgifte() {
    return eersteAfgifte;
  }

  public void setEersteAfgifte(String eersteAfgifte) {
    this.eersteAfgifte = eersteAfgifte;
  }

  public String getLand() {
    return land;
  }

  public void setLand(String land) {
    this.land = land;
  }

  public String getMedVerklaring() {
    return medVerklaring;
  }

  public void setMedVerklaring(String medVerklaring) {
    this.medVerklaring = medVerklaring;
  }

  public String getOmgewisseld() {
    return omgewisseld;
  }

  public void setOmgewisseld(String omgewisseld) {
    this.omgewisseld = omgewisseld;
  }

  public String getVoertuig() {
    return voertuig;
  }

  public void setVoertuig(String voertuig) {
    this.voertuig = voertuig;
  }

  public String getGeschiktheid() {
    return geschiktheid;
  }

  public void setGeschiktheid(String geschiktheid) {
    this.geschiktheid = geschiktheid;
  }
}
