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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4b;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

/**
 * Vorderingsprocedure gegevens
 */
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page4bRijbewijsBean3 implements Serializable {

  public static final String DATUM       = "datum";
  public static final String EINDE1      = "einde1";
  public static final String AUTORITEIT4 = "autoriteit4";
  public static final String AUTORITEIT5 = "autoriteit5";
  public static final String AUTORITEIT6 = "autoriteit6";
  public static final String RKKCODE     = "rkkcode";
  public static final String KORPSCODE   = "korpscode";
  public static final String VERBALISANT = "verbalisant";
  public static final String DOSSIER     = "dossier";
  public static final String SCHORSING   = "schorsing";
  public static final String EINDE2      = "einde2";
  public static final String FEITELIJK   = "feitelijk";
  public static final String ONGELDIG1   = "ongeldig1";
  public static final String ONGELDIG2   = "ongeldig2";
  public static final String FEITELINL   = "feitelinl";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum")
  private String datum       = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Einde")
  private String einde1      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Autoriteit")
  private String autoriteit4 = "";
  @Field(type = FieldType.TEXT_FIELD)
  private String autoriteit5 = "";
  @Field(type = FieldType.TEXT_FIELD)
  private String autoriteit6 = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "RKK-code")
  private String rkkcode     = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Korpscode")
  private String korpscode   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verbalisant")
  private String verbalisant = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Dossier")
  private String dossier     = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Schorsing")
  private String schorsing   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Einde")
  private String einde2      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Feitelijk")
  private String feitelijk   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Ongeldig")
  private String ongeldig1   = "";
  @Field(type = FieldType.TEXT_FIELD)
  private String ongeldig2   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Feitel. inl")
  private String feitelinl   = "";

  public String getAutoriteit4() {
    return autoriteit4;
  }

  public void setAutoriteit4(String autoriteit4) {
    this.autoriteit4 = autoriteit4;
  }

  public String getAutoriteit5() {
    return autoriteit5;
  }

  public void setAutoriteit5(String autoriteit5) {
    this.autoriteit5 = autoriteit5;
  }

  public String getAutoriteit6() {
    return autoriteit6;
  }

  public void setAutoriteit6(String autoriteit6) {
    this.autoriteit6 = autoriteit6;
  }

  public String getDatum() {
    return datum;
  }

  public void setDatum(String datum) {
    this.datum = datum;
  }

  public String getDossier() {
    return dossier;
  }

  public void setDossier(String dossier) {
    this.dossier = dossier;
  }

  public String getEinde1() {
    return einde1;
  }

  public void setEinde1(String einde1) {
    this.einde1 = einde1;
  }

  public String getEinde2() {
    return einde2;
  }

  public void setEinde2(String einde2) {
    this.einde2 = einde2;
  }

  public String getFeitelijk() {
    return feitelijk;
  }

  public void setFeitelijk(String feitelijk) {
    this.feitelijk = feitelijk;
  }

  public String getFeitelinl() {
    return feitelinl;
  }

  public void setFeitelinl(String feitelinl) {
    this.feitelinl = feitelinl;
  }

  public String getKorpscode() {
    return korpscode;
  }

  public void setKorpscode(String korpscode) {
    this.korpscode = korpscode;
  }

  public String getOngeldig1() {
    return ongeldig1;
  }

  public void setOngeldig1(String ongeldig1) {
    this.ongeldig1 = ongeldig1;
  }

  public String getOngeldig2() {
    return ongeldig2;
  }

  public void setOngeldig2(String ongeldig2) {
    this.ongeldig2 = ongeldig2;
  }

  public String getRkkcode() {
    return rkkcode;
  }

  public void setRkkcode(String rkkcode) {
    this.rkkcode = rkkcode;
  }

  public String getSchorsing() {
    return schorsing;
  }

  public void setSchorsing(String schorsing) {
    this.schorsing = schorsing;
  }

  public String getVerbalisant() {
    return verbalisant;
  }

  public void setVerbalisant(String verbalisant) {
    this.verbalisant = verbalisant;
  }

}
