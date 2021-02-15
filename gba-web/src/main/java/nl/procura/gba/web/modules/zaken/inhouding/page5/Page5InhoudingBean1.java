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

package nl.procura.gba.web.modules.zaken.inhouding.page5;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page5InhoudingBean1 implements Serializable {

  public static final String NUMMER     = "nummer";
  public static final String SOORT      = "soort";
  public static final String VANTM      = "vanTm";
  public static final String AUTORITEIT = "autoriteit";

  @Field(type = FieldType.LABEL,
      caption = "Nummer")
  private String nummer     = "";
  @Field(type = FieldType.LABEL,
      caption = "Soort")
  private String soort      = "";
  @Field(type = FieldType.LABEL,
      caption = "Van / tm")
  private String vanTm      = "";
  @Field(type = FieldType.LABEL,
      caption = "Autoriteit")
  private String autoriteit = "";

  public String getAutoriteit() {
    return autoriteit;
  }

  public void setAutoriteit(String autoriteit) {
    this.autoriteit = autoriteit;
  }

  public String getNummer() {
    return nummer;
  }

  public void setNummer(String nummer) {
    this.nummer = nummer;
  }

  public String getSoort() {
    return soort;
  }

  public void setSoort(String soort) {
    this.soort = soort;
  }

  public String getVanTm() {
    return vanTm;
  }

  public void setVanTm(String vanTm) {
    this.vanTm = vanTm;
  }
}
