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

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page4bRijbewijsBean4 implements Serializable {

  public static final String INLEVERDATUM = "inleverdatum";
  public static final String AUTORITEIT1  = "autoriteit1";
  public static final String AUTORITEIT2  = "autoriteit2";
  public static final String AUTORITEIT3  = "autoriteit3";
  public static final String CODEONGELDIG = "codeOngeldig";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Inleverdatum")
  private String inleverdatum = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Autoriteit")
  private String autoriteit1  = "";
  @Field(type = FieldType.TEXT_FIELD)
  private String autoriteit2  = "";
  @Field(type = FieldType.TEXT_FIELD)
  private String autoriteit3  = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Code ongeldig")
  private String codeOngeldig = "";

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

  public String getCodeOngeldig() {
    return codeOngeldig;
  }

  public void setCodeOngeldig(String codeOngeldig) {
    this.codeOngeldig = codeOngeldig;
  }

  public String getInleverdatum() {
    return inleverdatum;
  }

  public void setInleverdatum(String inleverdatum) {
    this.inleverdatum = inleverdatum;
  }
}
