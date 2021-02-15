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
 * Strafmaatregelgegevens
 */
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page4bRijbewijsBean1 implements Serializable {

  public static final String NUMMER      = "nummer";
  public static final String INVORDERING = "invordering";
  public static final String EINDE       = "einde";
  public static final String RKKCODE     = "rkkcode";
  public static final String KORPSCODE   = "korpscode";
  public static final String VERBALISANT = "verbalisant";
  public static final String INHTERMIJN  = "inhtermijn";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Nummer")
  private String nummer      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Invordering")
  private String invordering = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Einde")
  private String einde       = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Rkkcode")
  private String rkkcode     = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Korpscode")
  private String korpscode   = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Verbalisant")
  private String verbalisant = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Inh. termijn")
  private String inhtermijn  = "";

  public String getEinde() {
    return einde;
  }

  public void setEinde(String einde) {
    this.einde = einde;
  }

  public String getInhtermijn() {
    return inhtermijn;
  }

  public void setInhtermijn(String inhtermijn) {
    this.inhtermijn = inhtermijn;
  }

  public String getInvordering() {
    return invordering;
  }

  public void setInvordering(String invordering) {
    this.invordering = invordering;
  }

  public String getKorpscode() {
    return korpscode;
  }

  public void setKorpscode(String korpscode) {
    this.korpscode = korpscode;
  }

  public String getNummer() {
    return nummer;
  }

  public void setNummer(String nummer) {
    this.nummer = nummer;
  }

  public String getRkkcode() {
    return rkkcode;
  }

  public void setRkkcode(String rkkcode) {
    this.rkkcode = rkkcode;
  }

  public String getVerbalisant() {
    return verbalisant;
  }

  public void setVerbalisant(String verbalisant) {
    this.verbalisant = verbalisant;
  }

}
