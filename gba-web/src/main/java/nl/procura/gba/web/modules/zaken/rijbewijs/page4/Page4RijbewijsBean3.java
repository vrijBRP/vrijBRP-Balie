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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page4RijbewijsBean3 implements Serializable {

  public static final String ONTVANGST   = "ontvangst";
  public static final String KORPSCODEO  = "korpscodeO";
  public static final String ONTVANGST2  = "ontvangst2";
  public static final String VERZENDING  = "verzending";
  public static final String KORPSCODEV  = "korpscodeV";
  public static final String VERZENDING2 = "verzending2";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Ontvangst")
  private String ontvangst   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Korpscode")
  private String korpscodeO  = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "&nbsp;")
  private String ontvangst2  = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verzending")
  private String verzending  = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Korpscode")
  private String korpscodeV  = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "&nbsp;")
  private String verzending2 = "";

  public String getKorpscodeO() {
    return korpscodeO;
  }

  public void setKorpscodeO(String korpscodeO) {
    this.korpscodeO = korpscodeO;
  }

  public String getKorpscodeV() {
    return korpscodeV;
  }

  public void setKorpscodeV(String korpscodeV) {
    this.korpscodeV = korpscodeV;
  }

  public String getOntvangst() {
    return ontvangst;
  }

  public void setOntvangst(String ontvangst) {
    this.ontvangst = ontvangst;
  }

  public String getOntvangst2() {
    return ontvangst2;
  }

  public void setOntvangst2(String ontvangst2) {
    this.ontvangst2 = ontvangst2;
  }

  public String getVerzending() {
    return verzending;
  }

  public void setVerzending(String verzending) {
    this.verzending = verzending;
  }

  public String getVerzending2() {
    return verzending2;
  }

  public void setVerzending2(String verzending2) {
    this.verzending2 = verzending2;
  }

}
