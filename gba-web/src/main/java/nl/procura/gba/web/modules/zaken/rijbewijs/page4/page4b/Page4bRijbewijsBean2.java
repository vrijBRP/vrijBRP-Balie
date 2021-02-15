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
 * Maatregelgegevens
 */
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page4bRijbewijsBean2 implements Serializable {

  public static final String SOORT1      = "soort1";
  public static final String VOLGNR      = "volgnr";
  public static final String AUTORITEIT1 = "autoriteit1";
  public static final String AUTORITEIT2 = "autoriteit2";
  public static final String AUTORITEIT3 = "autoriteit3";
  public static final String REGISTRATIE = "registratie";
  public static final String EINDDATUM   = "einddatum";
  public static final String GEVORDINL   = "gevordinl";
  public static final String SOORT2      = "soort2";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Soort")
  private String soort1      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Volgnr.")
  private String volgnr      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Autoriteit")
  private String autoriteit1 = "";
  @Field(type = FieldType.TEXT_FIELD)
  private String autoriteit2 = "";
  @Field(type = FieldType.TEXT_FIELD)
  private String autoriteit3 = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Registratie")
  private String registratie = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Einddatum")
  private String einddatum   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Gevord. inl")
  private String gevordinl   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Soort")
  private String soort2      = "";

  public String getAutoriteit1() {
    return autoriteit1;
  }

  public void setAutoriteit1(String autoriteit1) {
    this.autoriteit1 = autoriteit1;
  }

  public String getAutoriteit2() {
    return autoriteit2;
  }

  public void setAutoriteit2(String autoriteit2) {
    this.autoriteit2 = autoriteit2;
  }

  public String getAutoriteit3() {
    return autoriteit3;
  }

  public void setAutoriteit3(String autoriteit3) {
    this.autoriteit3 = autoriteit3;
  }

  public String getEinddatum() {
    return einddatum;
  }

  public void setEinddatum(String einddatum) {
    this.einddatum = einddatum;
  }

  public String getGevordinl() {
    return gevordinl;
  }

  public void setGevordinl(String gevordinl) {
    this.gevordinl = gevordinl;
  }

  public String getRegistratie() {
    return registratie;
  }

  public void setRegistratie(String registratie) {
    this.registratie = registratie;
  }

  public String getSoort1() {
    return soort1;
  }

  public void setSoort1(String soort1) {
    this.soort1 = soort1;
  }

  public String getSoort2() {
    return soort2;
  }

  public void setSoort2(String soort2) {
    this.soort2 = soort2;
  }

  public String getVolgnr() {
    return volgnr;
  }

  public void setVolgnr(String volgnr) {
    this.volgnr = volgnr;
  }

}
