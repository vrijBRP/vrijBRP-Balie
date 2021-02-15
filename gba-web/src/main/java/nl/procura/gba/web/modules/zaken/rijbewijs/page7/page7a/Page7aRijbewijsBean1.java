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

package nl.procura.gba.web.modules.zaken.rijbewijs.page7.page7a;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page7aRijbewijsBean1 implements Serializable {

  public static final String CAT          = "cat";
  public static final String TOELICHTING1 = "toelichting1";
  public static final String STATUS1      = "status1";
  public static final String AFGIFTE1     = "afgifte1";
  public static final String EINDE        = "einde";
  public static final String BEPERKINGEN  = "beperkingen";
  public static final String TOELICHTING2 = "toelichting2";
  public static final String STATUS2      = "status2";
  public static final String AFGIFTE2     = "afgifte2";
  public static final String AUTOMAAT     = "automaat";
  public static final String BEPERKING    = "beperking";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Categorie")
  private String cat          = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Melding")
  private String melding      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Toelichting")
  private String toelichting1 = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Status")
  private String status1      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Afgifte")
  private String afgifte1     = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Einde")
  private String einde        = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Beperkingen")
  private String beperkingen  = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Toelichting")
  private String toelichting2 = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Status")
  private String status2      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Afgifte")
  private String afgifte2     = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "automaat")
  private String automaat     = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Beperking")
  private String beperking    = "";

  public String getAfgifte1() {
    return afgifte1;
  }

  public void setAfgifte1(String afgifte1) {
    this.afgifte1 = afgifte1;
  }

  public String getAfgifte2() {
    return afgifte2;
  }

  public void setAfgifte2(String afgifte2) {
    this.afgifte2 = afgifte2;
  }

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

  public String getBeperkingen() {
    return beperkingen;
  }

  public void setBeperkingen(String beperkingen) {
    this.beperkingen = beperkingen;
  }

  public String getCat() {
    return cat;
  }

  public void setCat(String cat) {
    this.cat = cat;
  }

  public String getEinde() {
    return einde;
  }

  public void setEinde(String einde) {
    this.einde = einde;
  }

  public String getMelding() {
    return melding;
  }

  public void setMelding(String melding) {
    this.melding = melding;
  }

  public String getStatus1() {
    return status1;
  }

  public void setStatus1(String status1) {
    this.status1 = status1;
  }

  public String getStatus2() {
    return status2;
  }

  public void setStatus2(String status2) {
    this.status2 = status2;
  }

  public String getToelichting1() {
    return toelichting1;
  }

  public void setToelichting1(String toelichting1) {
    this.toelichting1 = toelichting1;
  }

  public String getToelichting2() {
    return toelichting2;
  }

  public void setToelichting2(String toelichting2) {
    this.toelichting2 = toelichting2;
  }

}
