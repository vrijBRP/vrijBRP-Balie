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

package nl.procura.gba.web.modules.bs.omzetting.page90;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page90OmzettingBean1 implements Serializable {

  public static final String AMBTENAAR1 = "ambtenaar1";
  public static final String AMBTENAAR2 = "ambtenaar2";
  public static final String AMBTENAAR3 = "ambtenaar3";
  public static final String KEUZES     = "keuzes";

  @Field(type = FieldType.LABEL,
      caption = "1e keuze",
      width = "300px")
  private String ambtenaar1 = "";
  @Field(type = FieldType.LABEL,
      caption = "2e keuze",
      width = "300px")
  private String ambtenaar2 = "";

  @Field(type = FieldType.LABEL,
      caption = "Definitieve ambtenaar",
      width = "300px")
  private String ambtenaar3 = "";

  @Field(type = FieldType.LABEL,
      caption = "1e en 2e keuze",
      width = "500px")
  private String keuzes = "";

  public String getAmbtenaar1() {
    return ambtenaar1;
  }

  public void setAmbtenaar1(String ambtenaar1) {
    this.ambtenaar1 = ambtenaar1;
  }

  public String getAmbtenaar2() {
    return ambtenaar2;
  }

  public void setAmbtenaar2(String ambtenaar2) {
    this.ambtenaar2 = ambtenaar2;
  }

  public String getAmbtenaar3() {
    return ambtenaar3;
  }

  public void setAmbtenaar3(String ambtenaar3) {
    this.ambtenaar3 = ambtenaar3;
  }

  public String getKeuzes() {
    return keuzes;
  }

  public void setKeuzes(String keuzes) {
    this.keuzes = keuzes;
  }
}
