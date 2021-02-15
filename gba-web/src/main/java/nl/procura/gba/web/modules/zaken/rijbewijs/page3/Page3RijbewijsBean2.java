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

package nl.procura.gba.web.modules.zaken.rijbewijs.page3;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page3RijbewijsBean2 implements Serializable {

  public static final String GBABESTENDIG    = "gbaBestendig";
  public static final String DAGEN185NL      = "dagen185nl";
  public static final String NLNATIONALITEIT = "nlNationaliteit";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "BRP bestendig")
  private String gbaBestendig    = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "185 dagen in NL")
  private String dagen185nl      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "NL nationaliteit")
  private String nlNationaliteit = "";

  public String getDagen185nl() {
    return dagen185nl;
  }

  public void setDagen185nl(String dagen185nl) {
    this.dagen185nl = dagen185nl;
  }

  public String getGbaBestendig() {
    return gbaBestendig;
  }

  public void setGbaBestendig(String gbaBestendig) {
    this.gbaBestendig = gbaBestendig;
  }

  public String getNlNationaliteit() {
    return nlNationaliteit;
  }

  public void setNlNationaliteit(String nlNationaliteit) {
    this.nlNationaliteit = nlNationaliteit;
  }
}
